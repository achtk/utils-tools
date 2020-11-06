package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.constant.NumberConstant;
import com.chua.utils.tools.spi.common.SpiConfigs;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.chua.utils.tools.spi.options.SpiOptions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_LEFT_SLASH;

/**
 * 以自主读取接口/类文件实现扩展接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
@Slf4j
public class CustomExtensionProcessor<T> extends AbstractSimpleExtensionProcessor<T> {

    private static final Multimap<String, ExtensionClass<?>> CACHE = HashMultimap.create();

    private List<String> extensionLoadPath;

    protected String nameAndOrder = "|";

    @Override
    public void init(SpiConfig spiConfig) {
        if (null == spiConfig) {
            spiConfig = new SpiConfig();
        }

        List<String> loaderPath = spiConfig.getExtensionLoaderPath();
        if (!BooleanHelper.hasLength(loaderPath)) {
            loaderPath = SpiConfigs.newConfig().getListValue(SpiOptions.EXTENSION_LOAD_PATH);
        }

        this.extensionLoadPath = loaderPath;
    }

    @Override
    public Collection<ExtensionClass<?>> analyze(Class<T> service, ClassLoader classLoader) {
        if (null == this.extensionLoadPath) {
            log.warn("spi机制忽略，原因: 找不到配置文件: spi-config-default");
            return Collections.emptyList();
        }
        if (CACHE.containsKey(service.getName())) {
            return CACHE.get(service.getName());
        }

        setClassLoader(classLoader);
        setInterfaceClass(service);
        List<ExtensionClass<?>> result = new ArrayList<>();
        for (String path : this.extensionLoadPath) {
            List<ExtensionClass<T>> classes = loadFromFile(path);
            if (null == classes) {
                continue;
            }
            result.addAll(classes);
        }
        CACHE.putAll(service.getName(), result);
        return result;
    }

    @Override
    public void removeAll() {
        CACHE.clear();
    }

    @Override
    public void remove(Class<T> tClass) {
        CACHE.removeAll(tClass.getName());
    }

    /**
     * 加载文件配置
     *
     * @param path path必须以/结尾
     * @return List<ExtensionClass < T>>
     */
    protected synchronized List<ExtensionClass<T>> loadFromFile(String path) {
        if (log.isDebugEnabled()) {
            log.debug("Loading extension of extensible {} from path: {}", getInterfaceName(), path);
        }
        if (!path.endsWith(SYMBOL_LEFT_SLASH)) {
            path += SYMBOL_LEFT_SLASH;
        }
        // 默认如果不指定文件名字，就是接口名
        String fullFileName = path + getInterfaceName();
        try {
            return loadFromClassLoader(fullFileName);
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to load extension of extensible {} from path: {}", getInterfaceName(), fullFileName, t);
            }
        }
        return null;
    }

    /**
     * 从类加载器中加载文件
     *
     * @param fullFileName 文件全称
     * @return List<ExtensionClass < T>
     * @throws Throwable Throwable
     */
    private List<ExtensionClass<T>> loadFromClassLoader(final String fullFileName) throws Throwable {
        Enumeration<URL> urls = getClassLoader().getResources(fullFileName);
        List<ExtensionClass<T>> allExtensionClass = new ArrayList<>();
        // 可能存在多个文件
        if (urls != null) {
            while (urls.hasMoreElements()) {
                // 读取一个文件
                URL url = urls.nextElement();
                if (log.isDebugEnabled()) {
                    log.debug("Loading extension of extensible {} from classloader: {} and file: {}", getInterfaceName(), getClassLoader(), url);
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        List<ExtensionClass<T>> classList = readLine(line, url);
                        if (null == classList) {
                            continue;
                        }
                        allExtensionClass.addAll(classList);
                    }
                } catch (Throwable t) {
                    if (log.isDebugEnabled()) {
                        log.debug("Failed to load extension of extensible {} from classloader: {} and file:", getInterfaceName(), getClassLoader());
                    }
                }
            }
        }
        return allExtensionClass;
    }

    /**
     * 读取文件
     *
     * @param line 一行数据
     * @param url  链接
     * @return List<ExtensionClass < T>>
     */
    protected List<ExtensionClass<T>> readLine(final String line, URL url) {
        String[] aliasAndClassName = parseSpiNameAndClassName(line);
        if (aliasAndClassName == null || aliasAndClassName.length != NumberConstant.THREE) {
            return null;
        }
        //Spi名称
        String alias = aliasAndClassName[0];
        //类名
        String className = aliasAndClassName[1];
        //排序
        String order = aliasAndClassName[2];
        // 读取配置的实现类
        Class<? extends T> tmp;
        try {
            tmp = ClassHelper.forName(className, false);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("Extension {} of extensible {} is disabled, cause by: {}", className, getInterfaceName(), e.getMessage());
            }
            if (log.isDebugEnabled()) {
                log.debug("Extension " + className + " of extensible " + getInterfaceName() + " is disabled.");
            }
            return null;
        }
        int orderInteger = 0;
        try {
            orderInteger = Integer.parseInt(order);
        } catch (NumberFormatException ignored) {
        }
        List<ExtensionClass<T>> byClass = buildExtensionClassByClass(tmp);
        for (ExtensionClass<T> aClass : byClass) {
            aClass.setName(alias);
            aClass.setOrder(orderInteger);
            aClass.setUrl(url);
        }
        return byClass;
    }

    /**
     * 解析名字以及类名
     *
     * @param line 一行数据
     * @return String[]{名称, 类, 优先级}
     */
    protected String[] parseSpiNameAndClassName(String line) {
        if (Strings.isNullOrEmpty(line)) {
            return null;
        }
        line = line.trim();
        int index = line.indexOf('#');
        // 当前行是注释 或者 空
        if (index != -1 || line.length() == 0) {
            return null;
        }
        String name = line;
        String className = line;
        int i = line.indexOf('=');
        if (i > 0) {
            //Spi预处理名称
            name = line.substring(0, i).trim();
            //类名
            className = line.substring(i + 1).trim();
        }
        //类名无效
        if (className.length() == 0) {
            return null;
        }

        String order = "0";
        int i1 = name.indexOf(nameAndOrder);
        if (i1 > 0) {
            name = name.substring(0, i1).trim();
            order = name.substring(i1 + 1).trim();
        }
        return new String[]{name, className, order};
    }

}
