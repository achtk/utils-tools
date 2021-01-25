package com.chua.utils.netx.flink.format;

import lombok.Data;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.descriptors.DescriptorProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接器
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public class FormatConnector {
    private static final Map<String, Format> CACHE = new ConcurrentHashMap<>();

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
        format.setDescriptorProperties(descriptorProperties);
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
    }
}
