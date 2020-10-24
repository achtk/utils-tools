package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.spi.common.SpiConfigs;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.chua.utils.tools.spi.options.SpiOptions;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import static com.chua.utils.tools.constant.StringConstant.FILE_LEFT_SLASH;

/**
 * 以自主读取接口/类文件实现扩展接口
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 15:43
 */
@Slf4j
public class ExtensionProcessor<T> implements IExtensionProcessor<T> {

    private List<String> extensionLoadPath;
    private String interfaceName;
    private Class<T> interfaceClass;

    /**
     * 全部的加载的实现类 {"alias":ExtensionClass}
     * 注解默认@Spi
     */
    private Class<? extends Annotation> extension;

    private final Multimap<String, ExtensionClass<T>> providerCache = MapHelper.newHashMultimap();
    private ClassLoader classLoader;

    @Override
    public void init(SpiConfig spiConfig) {
        if(null == spiConfig) {
            spiConfig = new SpiConfig();
        }

        this.extension = spiConfig.getExtension();
        List<String> loaderPath = spiConfig.getExtensionLoaderPath();
        if(!BooleanHelper.hasLength(loaderPath)) {
            loaderPath = SpiConfigs.newConfig().getListValue(SpiOptions.EXTENSION_LOAD_PATH);
        }
        
        this.extensionLoadPath = loaderPath;
    }

    @Override
    public Multimap<String, ExtensionClass<T>> analyze(final Class<T> service, final ClassLoader classLoader) {
        if(null == service) {
            return null;
        }
        
        this.interfaceName = service.getName();
        this.interfaceClass = service;
        this.classLoader = classLoader == null ? ClassHelper.getDefaultClassLoader() : classLoader;

        if(null == this.extensionLoadPath) {
            log.warn("spi机制忽略，原因: 找不到配置文件: spi-config-default.json");
            return providerCache;
        }
        for (String path : this.extensionLoadPath) {
            loadFromFile(path);
        }
        
        return providerCache;
    }

    @Override
    public void refresh() {
        providerCache.clear();
    }


    /**
     * 家在文件配置
     * @param path path必须以/结尾
     */
    protected synchronized void loadFromFile(String path) {
        if (log.isDebugEnabled()) {
            log.debug("Loading extension of extensible {} from path: {}", interfaceName, path);
        }
        if(!path.endsWith(FILE_LEFT_SLASH)) {
            path += FILE_LEFT_SLASH;
        }
        // 默认如果不指定文件名字，就是接口名
        String fullFileName = path + interfaceName;
        try {
           // ClassLoader classLoader = ClassHelper.getClassLoader(getClass());
            loadFromClassLoader(fullFileName);
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to load extension of extensible {} from path: {}", interfaceName, fullFileName, t);
            }
        }
    }

    /**
     * 从类加载器中加载文件
     * @param fullFileName 文件全称
     * @throws Throwable 
     */
    protected void loadFromClassLoader(final String fullFileName) throws Throwable {
        Enumeration<URL> urls = classLoader != null ? classLoader.getResources(fullFileName) : ClassLoader.getSystemResources(fullFileName);
		// 可能存在多个文件。
        if (urls != null) {
            while (urls.hasMoreElements()) {
                // 读取一个文件
                URL url = urls.nextElement();
                if (log.isDebugEnabled()) {
                    log.debug("Loading extension of extensible {} from classloader: {} and file: {}", interfaceName, classLoader, url);
                }
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        readLine(line);
                    }
                } catch (Throwable t) {
                    if (log.isDebugEnabled()) {
                        log.debug("Failed to load extension of extensible {} from classloader: {} and file:", interfaceName, classLoader, url, t);
                    }
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
        }
    }

    /**
     * 读取文件
     * @param line 一行数据
     */
    protected void readLine(final String line) {
        String[] aliasAndClassName = parseSpiNameAndClassName(line);
        if (aliasAndClassName == null || aliasAndClassName.length != 3) {
            return;
        }
        String alias = aliasAndClassName[0];
        String className = aliasAndClassName[1];
        String order = aliasAndClassName[2];
		// 读取配置的实现类
        Class tmp;
        try {
            tmp = ClassHelper.forName(className, false);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("Extension {} of extensible {} is disabled, cause by: {}", className, interfaceName, e.getMessage());
            }
            if (log.isDebugEnabled()) {
                log.debug("Extension " + className + " of extensible " + interfaceName + " is disabled.");
            }
            return;
        }

        List<ExtensionClass<T>> extensionClass = loadExtension(interfaceClass, extension, alias, tmp, order);
        for (ExtensionClass<T> tExtensionClass : extensionClass) {
            if(null != tExtensionClass && tExtensionClass.isSingle()) {
                tExtensionClass.setObj(ClassHelper.forObject(tExtensionClass.getClazz().getName(), this.classLoader));
            }
            if(null != tExtensionClass) {
                providerCache.put(tExtensionClass.getName(), tExtensionClass);
            }
        }
    }

    /**
     * 解析名字以及类名
     * @param line 一行数据
     * @return
     */
    protected String[] parseSpiNameAndClassName(String line) {
        if (StringHelper.isBlank(line)) {
            return null;
        }
        line = line.trim();
        int index = line.indexOf('#');
        if (index == 0 || line.length() == 0) {
            return null;
		    // 当前行是注释 或者 空
        }
        if (index > 0) {
            line = line.substring(0, index).trim();
        }

        String name = null;
        String className;
        int i = line.indexOf('=');
        if (i > 0) {
            name = line.substring(0, i).trim();
		    // 以代码里的为准
            className = line.substring(i + 1).trim();
        } else {
            className = line;
        }
        if (className.length() == 0) {
            return null;
        }

        String order = "";
        int i1 = name.indexOf("|");
        if(i1 > 0) {
            name = name.substring(0, i1).trim();
            order = name.substring(i1 + 1).trim();
        }
        return new String[] {name, className, order};
    }

}
