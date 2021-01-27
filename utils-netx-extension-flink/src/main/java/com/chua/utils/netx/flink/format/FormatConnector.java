package com.chua.utils.netx.flink.format;

import com.chua.utils.tools.util.CollectionUtils;
import com.chua.utils.tools.util.MapUtils;
import lombok.Data;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.descriptors.DescriptorProperties;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * 连接器
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public class FormatConnector {
    private static final Map<String, Format> CACHE = new ConcurrentHashMap<>();
    private static final Map<String, List<Object>> DATA = new ConcurrentHashMap<>();

    /**
     * 包含信号
     *
     * @param sign 信号
     * @return 包含返回true
     */
    public static boolean container(String sign) {
        return CACHE.containsKey(sign);
    }

    /**
     * 设置索引
     *
     * @param sign  标识
     * @param index 索引
     */
    public static void setIndex(String sign, String index) {
        Format format = CACHE.get(sign);
        if (null == format) {
            format = new Format();
        }
        format.setIndex(index);
        CACHE.put(sign, format);
    }

    /**
     * 设置属性
     *
     * @param sign  标识
     * @param key   索引
     * @param value 值
     */
    public static void setProperties(String sign, String key, Object value) {
        Format format = CACHE.get(sign);
        if (null == format) {
            format = new Format();
        }
        Map<String, Object> properties = format.getProperties();
        if (null == properties) {
            properties = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        }
        properties.put(key, value);
        format.setProperties(properties);
        CACHE.put(sign, format);
    }

    /**
     * 获取属性
     *
     * @param sign         标识
     * @param key          索引
     * @param defaultValue 默认值
     */
    public static int getIntValue(String sign, String key, int defaultValue) {
        Format format = CACHE.get(sign);
        if (null == format) {
            return defaultValue;
        }
        Map<String, Object> properties = format.getProperties();
        if (null == properties) {
            return defaultValue;
        }
        return MapUtils.getIntValue(properties, key, defaultValue);
    }

    /**
     * 获取属性
     *
     * @param sign         标识
     * @param key          索引
     * @param defaultValue 默认值
     */
    public static String getStringValue(String sign, String key, String defaultValue) {
        Format format = CACHE.get(sign);
        if (null == format) {
            return defaultValue;
        }
        Map<String, Object> properties = format.getProperties();
        if (null == properties) {
            return defaultValue;
        }
        return MapUtils.getString(properties, key, defaultValue);
    }

    /**
     * 获取属性
     *
     * @param sign         标识
     * @param key          索引
     * @param defaultValue 默认值
     */
    public static Long getLongValue(String sign, String key, long defaultValue) {
        Format format = CACHE.get(sign);
        if (null == format) {
            return defaultValue;
        }
        Map<String, Object> properties = format.getProperties();
        if (null == properties) {
            return defaultValue;
        }
        return MapUtils.getLongValue(properties, key, defaultValue);
    }

    /**
     * 设置kv delimiter
     *
     * @param sign        标识
     * @param kvDelimiter kv delimiter
     */
    public static void setKvDelimiter(String sign, String kvDelimiter) {
        Format format = CACHE.get(sign);
        if (null == format) {
            format = new Format();
        }
        format.setKvDelimiter(kvDelimiter);
        CACHE.put(sign, format);
    }

    /**
     * 设置data delimiter
     *
     * @param sign          标识
     * @param dataDelimiter data delimiter
     */
    public static void setDataDelimiter(String sign, String dataDelimiter) {
        Format format = CACHE.get(sign);
        if (null == format) {
            format = new Format();
        }
        format.setDataDelimiter(dataDelimiter);
        CACHE.put(sign, format);
    }

    /**
     * 设置属性描述
     *
     * @param sign                 标识
     * @param descriptorProperties 属性描述
     */
    public static void setDescriptorProperties(String sign, DescriptorProperties descriptorProperties) {
        Format format = CACHE.get(sign);
        if (null == format) {
            format = new Format();
        }
        String tableName = descriptorProperties.getString("schema.table");
        format.setDescriptorProperties(descriptorProperties);
        format.setTableName(tableName);
        if (DATA.containsKey(tableName)) {
            format.setData(DATA.get(tableName));
        }
        CACHE.put(sign, format);


    }

    /**
     * 设置tableSchema
     *
     * @param sign        标识
     * @param tableSchema tableSchema
     */
    public static void setSchema(String sign, TableSchema tableSchema) {
        Format format = CACHE.get(sign);
        if (null == format) {
            format = new Format();
        }
        format.setSchema(tableSchema);
        CACHE.put(sign, format);
    }

    /**
     * 设置 pool
     *
     * @param sign 标识
     * @param t    T
     * @param <T>  T
     */
    public static <T> void setPool(String sign, T t) {
        Format format = CACHE.get(sign);
        if (null == format) {
            format = new Format();
        }
        format.setPool(t);
        CACHE.put(sign, format);
    }

    /**
     * 获取格式数据
     *
     * @return 格式数据
     */
    public static Format getFormat(String sign) {
        return CACHE.get(sign);
    }

    /**
     * 获取池
     *
     * @param sign 信号
     * @param <T>  池类型
     * @return 池
     */
    public static <T> T getPool(String sign) {
        Format format = CACHE.get(sign);
        return null == format ? null : (T) format.getPool();
    }

    /**
     * 获取描述
     *
     * @param sign 信号
     * @return 描述
     */
    public static DescriptorProperties getDescriptorProperties(String sign) {
        Format format = CACHE.get(sign);
        return null == format ? null : format.getDescriptorProperties();
    }


    /**
     * getDataDelimiter
     *
     * @param sign 信号
     * @return 描述
     */
    public static String getDataDelimiter(String sign) {
        Format format = CACHE.get(sign);
        return null == format ? null : format.getDataDelimiter();
    }

    /**
     * getIndex
     *
     * @param sign 信号
     * @return 描述
     */
    public static String getIndex(String sign) {
        Format format = CACHE.get(sign);
        return null == format ? null : format.getIndex();
    }

    /**
     * getKvDelimiter
     *
     * @param sign 信号
     * @return 描述
     */
    public static String getKvDelimiter(String sign) {
        Format format = CACHE.get(sign);
        return null == format ? null : format.getKvDelimiter();
    }

    /**
     * getSchema
     *
     * @param sign 信号
     * @return 描述
     */
    public static TableSchema getSchema(String sign) {
        Format format = CACHE.get(sign);
        return null == format ? null : format.getSchema();
    }

    /**
     * 通过表名设置值
     *
     * @param tableName 表名
     * @param data      值
     */
    public static void setPropertiesByTable(String tableName, List<Object> data) {
        DATA.put(tableName, data);
    }

    /**
     * 获取数据
     *
     * @param sign   信号
     * @param tClass 类型
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T getData(String sign, Class<T> tClass) {
        Format format = getFormat(sign);
        if (null == format || null == tClass) {
            return null;
        }
        Object data = format.getData();
        if (null == data) {
            return null;
        }
        if (tClass.isAssignableFrom(data.getClass())) {
            return (T) data;
        }
        return null;
    }

    /**
     * 数据的数据类型
     *
     * @param sign 标识
     * @return 类型
     */
    public static Class<?> getDataType(String sign) {
        Object data = getData(sign, Object.class);
        if (data instanceof Collection) {
            Object first = CollectionUtils.findFirst((Collection) data);
            return null == first ? null : first.getClass();
        }

        return null == data ? null : data.getClass();
    }

    /**
     * 追加数据
     *
     * @param sign 标识
     * @param item 数据
     * @return
     */
    public static boolean addData(String sign, Object item) {
        Format format = getFormat(sign);
        if (null == format) {
            return false;
        }
        List<Object> dataList = format.getData();
        if (null != dataList && !dataList.isEmpty()) {
            Object o = dataList.get(0);
            if (o.getClass().isAssignableFrom(item.getClass())) {
                dataList.add(item);
                return true;
            }
        }
        return false;
    }

    /**
     * format
     *
     * @param <T>
     */
    @Data
    static final class Format<T> {
        private TableSchema schema;
        private DescriptorProperties descriptorProperties;
        private T pool;
        private String index;
        private String kvDelimiter;
        private String dataDelimiter;
        private Map<String, Object> properties;
        private String tableName;
        private List<Object> data;
    }
}
