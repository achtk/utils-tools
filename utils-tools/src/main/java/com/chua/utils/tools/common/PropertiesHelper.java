package com.chua.utils.tools.common;

import com.chua.utils.tools.common.loader.PropertiesLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * properties工具类
 *
 * @author CH
 * @since 1.0
 */
@Slf4j
public class PropertiesHelper {
    private static final Properties EMPTY = new Properties();
    private static String DOT = ".";
    private static final String XML_FILE_EXTENSION = ".xml";

    /**
     * 获取int数据
     *
     * @param properties 原始数据
     * @param index      索引
     * @return
     */
    public static int ints(Properties properties, String index) {
        return ints(properties, index, -1);
    }

    /**
     * 获取int数据
     *
     * @param properties   原始数据
     * @param index        索引
     * @param defaultValue 默认值
     * @return
     */
    public static int ints(Properties properties, String index, int defaultValue) {
        if (BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        String property = properties.getProperty(index);
        if (!BooleanHelper.isNumeric(property)) {
            return defaultValue;
        }

        return NumberHelper.toInt(property);
    }

    /**
     * 获取long数据
     *
     * @param properties 原始数据
     * @param index      索引
     * @return
     */
    public static long longs(Properties properties, String index) {
        return longs(properties, index, -1);
    }

    /**
     * 获取long数据
     *
     * @param properties   原始数据
     * @param index        索引
     * @param defaultValue 默认值
     * @return
     */
    public static long longs(Properties properties, String index, long defaultValue) {
        if (BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        String property = properties.getProperty(index);
        if (!BooleanHelper.isNumeric(property)) {
            return defaultValue;
        }

        return NumberHelper.toLong(property);
    }

    /**
     * 获取float数据
     *
     * @param properties 原始数据
     * @param index      索引
     * @return
     */
    public static float floats(Properties properties, String index) {
        return floats(properties, index, -1);
    }

    /**
     * 获取float数据
     *
     * @param properties   原始数据
     * @param index        索引
     * @param defaultValue 默认值
     * @return
     */
    public static float floats(Properties properties, String index, float defaultValue) {
        if (BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        String property = properties.getProperty(index);
        if (!BooleanHelper.isNumeric(property)) {
            return defaultValue;
        }

        return NumberHelper.toFloat(property);
    }

    /**
     * 获取double数据
     *
     * @param properties 原始数据
     * @param index      索引
     * @return
     */
    public static double doubles(Properties properties, String index) {
        return doubles(properties, index, -1);
    }

    /**
     * 获取float数据
     *
     * @param properties   原始数据
     * @param index        索引
     * @param defaultValue 默认值
     * @return
     */
    public static double doubles(Properties properties, String index, double defaultValue) {
        if (BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        String property = properties.getProperty(index);
        if (!BooleanHelper.isNumeric(property)) {
            return defaultValue;
        }

        return NumberHelper.toDouble(property);
    }

    /**
     * 获取string数据
     *
     * @param properties 原始数据
     * @param index      索引
     * @return
     */
    public static String strings(Properties properties, String index) {
        return strings(properties, index, null);
    }

    /**
     * 获取string数据
     *
     * @param properties   原始数据
     * @param index        索引
     * @param defaultValue 默认值
     * @return
     */
    public static String strings(Properties properties, String index, String defaultValue) {
        if (BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        return properties.getProperty(index);
    }

    /**
     * 获取boolean值
     *
     * @param properties 原数据
     * @param index      索引
     * @return
     */
    public static boolean booleans(Properties properties, String index) {
        return BooleanHelper.isValid(properties, index) ? false : BooleanHelper.toBoolean(properties.getProperty(index));
    }

    /**
     * 数组
     *
     * @param properties
     * @param index
     * @return
     */
    public static String[] arrays(Properties properties, String index) {
        if (!BooleanHelper.isValid(properties, index)) {
            return null;
        }
        Object o = properties.get(index);
        if (o instanceof List) {
            List<String> lists = (List<String>) o;
            return lists.toArray(new String[lists.size()]);
        } else if (o.getClass().isArray()) {
            return (String[]) o;
        }
        return new String[]{o + ""};
    }

    /**
     * 获取prop
     *
     * @param text 字符串
     * @return
     */
    public static Properties toProperties(String text) {
        Properties properties = new Properties();
        try {
            if (StringHelper.hasText(text)) {
                properties.load(new StringReader(text));
            }
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        return properties;
    }

    /**
     * 获取默认Properties
     *
     * @return
     */
    public static Properties emptyProperties() {
        return EMPTY;
    }

    /**
     * properties转ConcurrentHashMap
     *
     * @param properties
     * @return
     */
    public static ConcurrentHashMap<String, Object> toConcurrentHashMap(Properties properties) {
        return null == properties ? new ConcurrentHashMap<>() : new ConcurrentHashMap(properties);
    }

    /**
     * 加载URL
     *
     * @param url URL
     * @return Properties
     */
    public static Properties fillProperties(URL url) {
        Properties props = new Properties();
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(urlConnection.getClass().getSimpleName().startsWith("JNLP"));
            try (InputStream is = urlConnection.getInputStream()) {
                String filename = url.toExternalForm();
                if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
                    props.loadFromXML(is);
                } else {
                    props.load(is);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
