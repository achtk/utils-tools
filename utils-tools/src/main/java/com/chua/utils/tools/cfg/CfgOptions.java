package com.chua.utils.tools.cfg;

import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.PropertiesHelper;
import com.chua.utils.tools.constant.SymbolConstant;
import com.chua.utils.tools.prop.loader.*;
import com.chua.utils.tools.prop.placeholder.EnvPropertyPlaceholder;
import com.chua.utils.tools.prop.placeholder.PropertyPlaceholder;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.chua.utils.tools.constant.StringConstant.SYSTEM_PRIORITY_PROP;

/**
 * <p>配置项解析</p>
 * <p>只解析json</p>
 *
 * @author CH
 * @since 1.0
 */
@Slf4j
public class CfgOptions {

    private static final ConcurrentHashMap<String, ProfileLoader> LOADER_CACHE = new ConcurrentHashMap<>();
    private static final String KEY_NAME = "KEY#NAME";
    /**
     * 默认占位符
     */
    private static final Set<PropertyPlaceholder> DEFAULT_PLACE_HOLDER = Sets.newHashSet(new EnvPropertyPlaceholder());
    private final ConcurrentMap<String, List<Properties>> cache = new ConcurrentHashMap<>();
    /**
     * 占位符
     */
    private final Set<PropertyPlaceholder> placeholder = new HashSet<>();
    /**
     * 文件名称
     */
    private String master;
    /**
     * 文件后缀
     */
    private Set<String> suffixes;
    /**
     * 类加载器
     */
    private ClassLoader classLoader;
    private CfgConfig cfgConfig;

    public CfgOptions(CfgConfig cfgConfig) {
        this.cfgConfig = cfgConfig;
        this.analysis(cfgConfig);
    }

    /**
     * 初始化cfg信息
     *
     * @param master 文件名称
     * @return CfgOptions
     */
    public static CfgOptions analysis(String master) {
        return new CfgOptions(new CfgConfig().setMaster(master));
    }

    /**
     * 获取缓存索引
     *
     * @param cfgConfig 配置文件
     * @return 缓存索引
     */
    private static String getCacheKey(CfgConfig cfgConfig) {
        return cfgConfig.getMaster();
    }

    /**
     * 获取HashMultimap
     *
     * @return List
     */
    public List<Properties> toHashMultimap() {
        return cache.get(getCacheKey(cfgConfig));
    }

    /**
     * 添加占位符处理
     *
     * @param propertyPlaceholder 占位符
     */
    public void addPlaceholder(PropertyPlaceholder propertyPlaceholder) {
        placeholder.add(propertyPlaceholder);
    }

    /**
     * 初始化cfg信息
     *
     * @param cfgConfig 文件名称
     * @return List
     */
    public List<Properties> analysis(CfgConfig cfgConfig) {
        this.cfgConfig = cfgConfig;
        //存储数据
        List<Properties> concurrentHashMap = cache.get(getCacheKey(cfgConfig));
        if (null != concurrentHashMap) {
            return concurrentHashMap;
        }
        if (placeholder.size() == 0) {
            placeholder.addAll(DEFAULT_PLACE_HOLDER);
        }
        //文件名称
        this.master = cfgConfig.getMaster();
        //文件路径
        Set<String> slavers = cfgConfig.getSlaver();
        //文件后缀
        this.suffixes = cfgConfig.getSuffix();
        //类加载器
        this.classLoader = cfgConfig.getClassLoader();
        HashMultimap<String, Properties> allData = HashMultimap.create();
        for (String slaver : slavers) {
            allData.putAll(doAnalysisSlaver(slaver));
        }
        //占位符处理
        placeholder(allData);
        //排序
        List<Properties> properties = sortPropertiesList(allData);
        cache.put(master, properties);
        return properties;
    }

    /**
     * 排序
     *
     * @param source 数据源
     * @return List
     */
    private List<Properties> sortPropertiesList(HashMultimap<String, Properties> source) {
        List<Properties> values = new ArrayList<>();
        source.asMap().forEach((s, properties) -> {
            List<Properties> result = new ArrayList<>();
            for (Properties property : properties) {
                Properties properties1 = new Properties();
                properties1.putAll(property);
                properties1.put(KEY_NAME, s);

                result.add(properties1);
            }

            values.addAll(result);
        });

        values.sort((o1, o2) -> {
            int int1 = MapOperableHelper.getInteger(o1, SYSTEM_PRIORITY_PROP, 0);
            int int2 = MapOperableHelper.getInteger(o2, SYSTEM_PRIORITY_PROP, 0);
            return int1 > int2 ? 0 : -1;
        });


        return values;
    }

    /**
     * 解析文件
     *
     * @param slaver 文件路径
     * @return 文件内容
     */
    private HashMultimap<String, Properties> doAnalysisSlaver(String slaver) {
        HashMultimap<String, Properties> result = HashMultimap.create();
        for (String suffix : suffixes) {
            String path = FileHelper.toFolder(slaver) + master + SymbolConstant.SYMBOL_DOT + suffix;
            if (FileHelper.isFile(path)) {
                result.put(path, doAnalysisLocationFile(path, suffix));
            } else {
                result.putAll(doAnalysisClasspathFile(path, classLoader, suffix));
            }
        }
        return result;
    }

    /**
     * 解析本地文件
     *
     * @param path   文件路径
     * @param suffix 后缀
     * @return 文件内容
     */
    private Properties doAnalysisLocationFile(final String path, final String suffix) {
        ProfileLoader profileLoader = ExtensionFactory.getExtensionLoader(ProfileLoader.class).getExtension(suffix);
        if (null == profileLoader) {
            return PropertiesHelper.emptyProperties();
        }
        try {
            return profileLoader.toProp(new FileInputStream(new File(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return PropertiesHelper.emptyProperties();
    }

    /**
     * 解析classpath文件
     *
     * @param path        文件路径
     * @param classLoader 类加载器
     * @param suffix      后缀
     * @return HashMultimap
     */
    private HashMultimap<String, Properties> doAnalysisClasspathFile(final String path, final ClassLoader classLoader, final String suffix) {
        HashMultimap<String, Properties> result = HashMultimap.create();
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {
            return result;
        }
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            ProfileLoader profileLoader = getExtension(suffix);
            if (null == profileLoader) {
                continue;
            }
            try {
                Properties properties = profileLoader.toProp(url.openStream());
                result.put(url.toExternalForm(), properties);
            } catch (IOException ignored) {
            }
        }

        return result;
    }

    /**
     * 占位符处理
     *
     * @param hashMultimap 原始数据
     */
    private void placeholder(HashMultimap<String, Properties> hashMultimap) {
        Collection<Properties> collection = hashMultimap.values();
        for (Properties properties1 : collection) {
            for (PropertyPlaceholder propertyPlaceholder : placeholder) {
                propertyPlaceholder.addPropertySource(properties1);
            }
        }
        for (Properties properties1 : collection) {
            placeholderProperties(properties1);
        }
    }

    /**
     * 占位符处理
     *
     * @param properties 原始数据
     */
    private void placeholderProperties(Properties properties) {
        properties.replaceAll((k, v) -> placeholderValue(v));
    }

    /**
     * 占位符处理
     *
     * @param value 原始数据
     */
    private Object placeholderValue(Object value) {
        Object placeholder = null;
        for (PropertyPlaceholder propertyPlaceholder : this.placeholder) {
            placeholder = propertyPlaceholder.placeholder(value);
        }
        return placeholder;
    }

    /**
     * 判断文件类型
     *
     * @param suffix 后缀
     * @return PropertiesLoader
     */
    private ProfileLoader getExtension(String suffix) {
        if (LOADER_CACHE.containsKey(suffix)) {
            return LOADER_CACHE.get(suffix);
        }
        ProfileLoader profileLoader = new PropertiesProfileLoader();
        switch (suffix) {
            case "json":
                profileLoader = new JsonProfileLoader();
                break;
            case "yml":
                profileLoader = new YamlProfileLoader();
                break;
            case "xml":
                profileLoader = new XmlProfileLoader();
                break;
            case "properties":
                profileLoader = new PropertiesProfileLoader();
                break;
            default:
        }
        LOADER_CACHE.put(suffix, profileLoader);
        return profileLoader;
    }

    /**
     * 获取ConcurrentHashMap
     *
     * @return ConcurrentHashMap
     */
    public ConcurrentHashMap<String, Object> toConcurrentHashMap() {
        List<Properties> multimap = cache.get(getCacheKey(cfgConfig));
        Properties properties = FinderHelper.firstElement(multimap);
        return PropertiesHelper.toConcurrentHashMap(properties);
    }

    /**
     * 获取Map
     *
     * @return Map
     */
    public Map<String, Properties> asMap() {
        List<Properties> properties = toHashMultimap();
        Map<String, Properties> result = new HashMap<>(properties.size());
        for (Properties property : properties) {
            String key = property.getProperty(KEY_NAME);
            Properties properties1 = new Properties();
            properties1.putAll(property);
            properties1.remove(key);
            result.put(key, properties1);
        }

        return result;
    }
}
