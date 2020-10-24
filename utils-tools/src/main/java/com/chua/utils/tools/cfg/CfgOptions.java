package com.chua.utils.tools.cfg;

import com.chua.utils.tools.common.*;
import com.chua.utils.tools.prop.loader.*;
import com.chua.utils.tools.prop.placeholder.AbstractPropertiesPlaceholderResolver;
import com.chua.utils.tools.prop.placeholder.PropertiesPlaceholderFactory;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.google.common.collect.HashMultimap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.chua.utils.tools.constant.StringConstant.*;

/**
 * <p>配置项解析</p>
 * <p>只解析json</p>
 *
 * @author CH
 * @since 1.0
 */
@Slf4j
public class CfgOptions {

    private static final ConcurrentMap<String, HashMultimap<String, Properties>> CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, PropertiesLoader> LOADER_CACHE = new ConcurrentHashMap<>();
    /**
     * 文件路径
     */
    private Set<String> slavers;
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
     * @return
     */
    public static CfgOptions analysis(String master) {
        return new CfgOptions(new CfgConfig().setMaster(master));
    }

    /**
     * 获取HashMultimap
     * @return
     */
    public HashMultimap<String, Properties> toHashMultimap() {
        return CACHE.get(getCacheKey(cfgConfig));
    }

    /**
     * 初始化cfg信息
     *
     * @param cfgConfig 文件名称
     * @return
     */
    public HashMultimap<String, Properties> analysis(CfgConfig cfgConfig) {
        this.cfgConfig = cfgConfig;
        //存储数据
        HashMultimap<String, Properties> concurrentHashMap = CACHE.get(getCacheKey(cfgConfig));
        if (null != concurrentHashMap) {
            return concurrentHashMap;
        }
        //文件名称
        this.master = cfgConfig.getMaster();
        //文件路径
        this.slavers = cfgConfig.getSlaver();
        //文件后缀
        this.suffixes = cfgConfig.getSuffix();
        //类加载器
        this.classLoader = cfgConfig.getClassLoader();

        HashMultimap<String, Properties> allData = HashMultimap.create();
        for (String slaver : slavers) {
            allData.putAll(doAnalysisSlaver(slaver));
        }
        HashMultimap<String, Properties> result = HashMultimap.create();
        for (String s : allData.keySet()) {
            List<Properties> properties = sortPropertiesList(allData.get(s));
            insertResult(result, s, properties);
        }
        CACHE.put(master, allData);
        return allData;
    }

    private void insertResult(HashMultimap<String, Properties> result, String s, List<Properties> properties) {
        for (Properties property : properties) {
            result.put(s, property);
        }
    }

    /**
     * 排序
     *
     * @param properties
     * @return
     */
    private List<Properties> sortPropertiesList(Set<Properties> properties) {
        List<Properties> list = new ArrayList<>(properties);
        Collections.sort(list, new Comparator<Properties>() {
            @Override
            public int compare(Properties o1, Properties o2) {
                int int1 = MapHelper.ints(SYSTEM_PRIORITY_PROP, 0, o1);
                int int2 = MapHelper.ints(SYSTEM_PRIORITY_PROP, 0, o2);
                return int1 > int2 ? 1 : -1;
            }
        });
        return list;
    }

    /**
     * 获取缓存索引
     *
     * @param cfgConfig 配置文件
     * @return
     */
    private static String getCacheKey(CfgConfig cfgConfig) {
        return cfgConfig.getMaster();
    }

    /**
     * 解析文件
     *
     * @param slaver 文件路径
     * @return
     */
    private HashMultimap<String, Properties> doAnalysisSlaver(String slaver) {
        HashMultimap<String, Properties> result = HashMultimap.create();
        for (String suffix : suffixes) {
            String path = FileHelper.toFolder(slaver) + master + EXTENSION_DOT + suffix;
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
     * @return
     */
    private Properties doAnalysisLocationFile(final String path, final String suffix) {
        PropertiesLoader propertiesLoader = ExtensionFactory.getExtensionLoader(PropertiesLoader.class).getExtension(suffix);
        if (null == propertiesLoader) {
            return PropertiesHelper.emptyProperties();
        }
        File file = new File(path);
        try {
            Properties properties = propertiesLoader.toProp(new FileInputStream(file));
            return placeholder(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return PropertiesHelper.emptyProperties();
    }

    /**
     * 占位符处理
     *
     * @param properties
     * @return
     */
    private static Properties placeholder(Properties properties) {
        String env = MapHelper.strings(SYSTEM_PLACEHOLDER_PROP, "system", properties);
        AbstractPropertiesPlaceholderResolver extension = ExtensionFactory.getExtensionLoader(AbstractPropertiesPlaceholderResolver.class).getExtension(env);
        PropertiesPlaceholderFactory propertiesPlaceholderFactory = PropertiesPlaceholderFactory.newBuilder().dataMapper(MapHelper.maps(properties)).addResolver(extension).build();
        Properties result = new Properties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof List) {
                value = doPlaceholderListAnalyze((List) value, propertiesPlaceholderFactory);
            } else if (value instanceof Map) {
                value = doPlaceholderMapAnalyze((Map) value, propertiesPlaceholderFactory);
            } else {
                if (extension.isMatcher(value + "")) {
                    value = extension.analyze(value.toString());
                }
            }
            result.put(entry.getKey(), value);
        }
        return result;
    }

    /**
     * 解析classpath文件
     *
     * @param path        文件路径
     * @param classLoader 类加载器
     * @param suffix      后缀
     * @return
     */
    private static HashMultimap<String, Properties> doAnalysisClasspathFile(final String path, final ClassLoader classLoader, final String suffix) {
        HashMultimap<String, Properties> result = HashMultimap.create();
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {
            return result;
        }
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            PropertiesLoader propertiesLoader = getExtension(suffix);
            if (null == propertiesLoader) {
                continue;
            }
            Properties properties = null;
            try {
                properties = propertiesLoader.toProp(url.openStream());
            } catch (IOException e) {
                continue;
            }
            result.put(url.toExternalForm(), properties);
        }
        return result;
    }

    /**
     * 判断文件类型
     *
     * @param suffix
     * @return
     */
    private static PropertiesLoader getExtension(String suffix) {
        if (LOADER_CACHE.containsKey(suffix)) {
            return LOADER_CACHE.get(suffix);
        }
        PropertiesLoader propertiesLoader = null;
        switch (suffix) {
            case "json":
                propertiesLoader = new JsonPropertiesLoader();
                break;
            case "yml":
                propertiesLoader = new YamlPropertiesLoader();
                break;
            case "xml":
                propertiesLoader = new XmlPropertiesLoader();
                break;
            case "properties":
                propertiesLoader = new PropertiesPropertiesLoader();
                break;
        }
        LOADER_CACHE.put(suffix, propertiesLoader);
        return propertiesLoader;
    }

    /**
     * 处理Map占位符
     *
     * @param value
     * @param propertiesPlaceholderFactory
     * @return
     */
    private static Map doPlaceholderMapAnalyze(Map<Object, Object> value, PropertiesPlaceholderFactory propertiesPlaceholderFactory) {
        Map result = new HashMap();
        for (Map.Entry<Object, Object> entry : value.entrySet()) {
            Object value1 = entry.getValue();
            if (value1 instanceof List) {
                value1 = doPlaceholderListAnalyze((List) value1, propertiesPlaceholderFactory);
            } else if (value1 instanceof Map) {
                value1 = doPlaceholderMapAnalyze((Map) value1, propertiesPlaceholderFactory);
            } else {
                value1 = propertiesPlaceholderFactory.placeholder(value1);
            }

            result.put(entry.getKey(), value1);
        }

        return result;
    }

    /**
     * 处理List占位符
     *
     * @param value
     * @return
     */
    private static List doPlaceholderListAnalyze(List value, PropertiesPlaceholderFactory propertiesPlaceholderFactory) {
        List result = new ArrayList();
        for (Object o : value) {
            if (o instanceof List) {
                result.add(doPlaceholderListAnalyze((List) o, propertiesPlaceholderFactory));
            } else if (o instanceof Map) {
                result.add(doPlaceholderMapAnalyze((Map) o, propertiesPlaceholderFactory));
            } else {
                result.add(propertiesPlaceholderFactory.placeholder(o));
            }
        }

        return result;
    }

    /**
     * 获取ConcurrentHashMap
     *
     * @return
     */
    public ConcurrentHashMap<String, Object> toConcurrentHashMap() {
        HashMultimap<String, Properties> multimap = CACHE.get(getCacheKey(cfgConfig));
        Properties properties = FinderHelper.firstElement(multimap.values());
        return PropertiesHelper.toConcurrentHashMap(properties);
    }


    /**
     * 用于排序的一个类
     */
    @Getter
    @Setter
    @AllArgsConstructor
    private static class CfgFile {
        private final URL url;
        private final int order;
        private final Map map;
    }
}
