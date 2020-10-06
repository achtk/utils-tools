package com.chua.utils.tools.cfg;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.TypeHelper;
import com.chua.utils.tools.common.loader.PropertiesLoader;
import com.chua.utils.tools.options.IOptions;
import com.chua.utils.tools.prop.placeholder.AbstractPropertiesPlaceholderResolver;
import com.chua.utils.tools.prop.placeholder.PropertiesPlaceholderFactory;
import com.chua.utils.tools.spi.common.SpiConfigs;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
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

    private static final ConcurrentMap<String, ConcurrentHashMap<String, Object>> CACHE = new ConcurrentHashMap<>();

    /**
     * 初始化cfg信息
     *
     * @param master 文件名称
     * @return
     */
    public static IOptions options(String master) {
        CfgConfig cfgConfig = new CfgConfig();
        cfgConfig.setMaster(master);
        return new IOptions() {
            @Override
            public ConcurrentMap<String, Object> values() {
                return initialCfg(cfgConfig);
            }
        };
    }

    /**
     * 初始化cfg信息
     *
     * @param cfgConfig 文件名称
     * @return
     */
    public static IOptions options(CfgConfig cfgConfig) {
        return new IOptions() {
            @Override
            public ConcurrentMap<String, Object> values() {
                return initialCfg(cfgConfig);
            }
        };
    }

    /**
     * 初始化cfg信息
     *
     * @param cfgConfig 文件名称
     * @return
     */
    public static PropertiesLoader cfgLoader(CfgConfig cfgConfig) {
        PropertiesLoader propertiesLoader = PropertiesLoader.newLoader();
        propertiesLoader.add(initialCfg(cfgConfig));
        return propertiesLoader;
    }

    /**
     * 初始化cfg信息
     *
     * @param cfgConfig 文件名称
     * @return
     */
    public static ConcurrentHashMap<String, Object> initialCfg(CfgConfig cfgConfig) {
        ConcurrentHashMap<String, Object> cfg = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Object> result = new ConcurrentHashMap<>();

        String master = cfgConfig.getMaster();
        ConcurrentHashMap<String, Object> concurrentHashMap = CACHE.get(master);
        if (null != concurrentHashMap) {
            return concurrentHashMap;
        }
        File file = new File(master);

        Map masterCfg = null;
        //本地文件存在
        if (file.exists()) {
            log.info("检测到本地文件[{}]", master);
            masterCfg = doAnalysisLocationFile(master);
        } else {
            try {
                masterCfg = doAnalysisClasspathFile(master, null == cfgConfig.getClassLoader() ? CfgOptions.class.getClassLoader() : cfgConfig.getClassLoader());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null == masterCfg) {
            log.warn("未找到对应的配置");
        }

        cfg.putAll(masterCfg);

        List<String> salvers = cfgConfig.getSlavers();
        String slaverName = cfgConfig.getSlaverName();
        if (Strings.isNullOrEmpty(slaverName)) {
            slaverName = master.replace(".default", "").replace("-default", "");
        }
        for (String custom : salvers) {
            String path = custom.endsWith("/") ? custom + slaverName : custom + "/" + slaverName;
            try {
                loadCustom(path, cfg, cfgConfig.getOrder());
            } catch (IOException e) {
                continue;
            }
        }

        if (null != masterCfg && null != cfgConfig.getSlaverKey()) {
            Object o = masterCfg.get(cfgConfig.getSlaverKey());
            if (null == o) {
                return cfg;
            }
            String strings = o.toString();
            List<String> splitToList = Splitter.on(",").splitToList(strings);
            for (String s : splitToList) {
                try {
                    loadCustom(s, cfg, cfgConfig.getOrder());
                } catch (IOException e) {
                    continue;
                }
            }
        }
        if (!SPI_CONFIG_DEFAULT.equals(master)) {
            String env = MapHelper.strings(SYSTEM_PLACEHOLDER_PROP, "system", cfg);
            AbstractPropertiesPlaceholderResolver extension = ExtensionFactory.getExtensionLoader(AbstractPropertiesPlaceholderResolver.class).getExtension(env);
            PropertiesPlaceholderFactory propertiesPlaceholderFactory = PropertiesPlaceholderFactory.newBuilder().dataMapper(cfg).addResolver(extension).build();

            for (Map.Entry<String, Object> entry : cfg.entrySet()) {
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
        } else {
            result.putAll(cfg);
        }

        CACHE.put(master, result);
        return result;
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
     * 解析classpath文件
     *
     * @param master
     * @param classLoader 类加载器
     * @return
     */
    private static Map doAnalysisClasspathFile(final String master, final ClassLoader classLoader) throws IOException {
        Map result = new HashMap();
        Enumeration<URL> resources = classLoader.getResources(master);
        List<CfgFile> cfgFiles = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            Map map = JsonHelper.fromJson(url, Map.class);
            if (null == map) {
                continue;
            }
            CfgFile cfgFile = new CfgFile(url, MapHelper.ints(SYSTEM_PRIORITY_PROP, 0, map), map);
            cfgFiles.add(cfgFile);
        }

        Collections.sort(cfgFiles, new Comparator<CfgFile>() {
            @Override
            public int compare(CfgFile o1, CfgFile o2) {
                return o1.order > o2.order ? 1 : -1;
            }
        });

        for (CfgFile cfgFile : cfgFiles) {
            combine(result, cfgFile);
        }

        return result;
    }

    /**
     * 合并结果
     *
     * @param result
     * @param cfgFile
     */
    private static void combine(Map result, CfgFile cfgFile) {
        Map<Object, Object> map = cfgFile.getMap();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object sourceValue = entry.getValue();
            if (sourceValue instanceof Double) {
                sourceValue = TypeHelper.toInt(entry.getValue(), 0);
            }
            Object targetValue = result.get(entry.getKey());
            if (sourceValue instanceof List || targetValue instanceof List) {
                combineList(result, entry.getKey(), entry.getValue());
            } else if (sourceValue instanceof Map || targetValue instanceof Map) {
                combineMap(result, entry.getKey(), entry.getValue());
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 合并 map
     *
     * @param result
     * @param key
     * @param sourceValue
     */
    private static void combineMap(Map result, Object key, Object sourceValue) {
        Object targetValue = result.get(key);

        if (null == sourceValue && null == targetValue) {
            return;
        }

        if (sourceValue instanceof Map && targetValue instanceof Map) {
            Map sourceValueList = null != sourceValue ? (Map) sourceValue : Collections.emptyMap();
            Map targetValueList = null != targetValue ? (Map) targetValue : Collections.emptyMap();

            targetValueList.putAll(sourceValueList);
            result.put(key, targetValueList);
        } else if (sourceValue instanceof Map && !(targetValue instanceof Map)) {
            Map sourceValueList = null != sourceValue ? (Map) sourceValue : Collections.emptyMap();
            try {
                sourceValueList.put(targetValue, targetValue);
            } catch (Exception e) {

            }
            result.put(key, sourceValueList);
        } else {
            Map targetValueList = null != targetValue ? (Map) targetValue : Collections.emptyMap();
            try {
                targetValueList.put(sourceValue, sourceValue);
            } catch (Exception e) {

            }
            result.put(key, targetValueList);
        }
    }

    /**
     * 合并list
     *
     * @param result
     * @param key
     * @param sourceValue
     */
    private static void combineList(Map result, Object key, Object sourceValue) {
        Object targetValue = result.get(key);

        if (null == sourceValue && null == targetValue) {
            return;
        }

        if (sourceValue instanceof List && targetValue instanceof List) {
            List sourceValueList = null != sourceValue ? (List) sourceValue : Collections.emptyList();
            List targetValueList = null != targetValue ? (List) targetValue : Collections.emptyList();

            if (null != sourceValueList) {
                targetValueList.addAll(sourceValueList);
            }

            result.put(key, targetValueList);
        } else if (sourceValue instanceof List && !(targetValue instanceof List)) {
            List sourceValueList = null != sourceValue ? (List) sourceValue : Collections.emptyList();

            if (null != targetValue) {
                sourceValueList.add(targetValue);
            }
            result.put(key, sourceValueList);
        } else {
            List targetValueList = null != targetValue ? (List) targetValue : Collections.emptyList();
            if (null != sourceValue) {
                targetValueList.add(sourceValue);
            }
            result.put(key, targetValueList);
        }
    }

    /**
     * 解析本地文件
     *
     * @param master
     * @return
     */
    private static Map doAnalysisLocationFile(String master) {
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(master), CHARSET_UTF_8)) {
            return JsonHelper.fromJson(isr, Map.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 加载自定义配置文件
     *
     * @param fileName  文件名
     * @param cfg       配置项
     * @param orderName 排序字段
     * @throws IOException 加载异常
     */
    private static void loadCustom(String fileName, ConcurrentHashMap<String, Object> cfg, String orderName) throws IOException {
        ClassLoader classLoader = ClassHelper.getClassLoader(SpiConfigs.class);
        Enumeration<URL> urls = classLoader != null ? classLoader.getResources(fileName) : ClassLoader.getSystemResources(fileName);
        // 可能存在多个文件
        if (urls != null) {
            List<CfgFile> allFile = new ArrayList<CfgFile>();
            while (urls.hasMoreElements()) {
                // 读取每一个文件
                URL url = urls.nextElement();
                try (InputStreamReader input = new InputStreamReader(url.openStream(), "utf-8")) {
                    Map map = JsonHelper.fromJson(input, Map.class);
                    //获取排序字段
                    Integer order = null;
                    if (null != orderName) {
                        order = (Integer) map.get(orderName);
                    }
                    allFile.add(new CfgFile(url, order == null ? 0 : order, map));
                }
            }

            Collections.sort(allFile, new Comparator<CfgFile>() {
                @Override
                public int compare(CfgFile o1, CfgFile o2) {
                    return o1.order > o2.order ? 1 : -1;
                }
            });

            for (CfgFile file : allFile) {
                Map<String, Object> fileMap = file.getMap();
                for (Map.Entry<String, Object> entry : fileMap.entrySet()) {
                    // 顺序加载，越大越后加载
                    cfg.put(entry.getKey(), entry.getValue());
                }
            }
        }
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
