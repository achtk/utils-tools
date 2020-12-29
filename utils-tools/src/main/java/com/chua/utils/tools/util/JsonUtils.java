package com.chua.utils.tools.util;

import com.chua.utils.tools.common.JsonHelper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/21
 */
public class JsonUtils extends JsonHelper {

    public static final JavaType MAP_STRING_OBJECT = createStringObjectMap();
    public static final JavaType MAP_STRING_STRING = createStringStringMap();
    public static final JavaType LIST_STRING = createStringList();

    /**
     * 获取 Map<String, Object>
     *
     * @return Map
     */
    private static JavaType createStringObjectMap() {
        return createMap(String.class, Object.class);
    }

    /**
     * 获取 Map<String, String>
     *
     * @return Map
     */
    private static JavaType createStringStringMap() {
        return createMap(String.class, String.class);
    }

    /**
     * 获取 List<String>
     *
     * @return List
     */
    private static JavaType createStringList() {
        return createList(String.class);
    }

    /**
     * 创建 List<?>
     *
     * @param type 元素类型
     * @return List
     */
    public static JavaType createList(Class<?> type) {
        TypeFactory typeFactory = OBJECT_MAPPER.getTypeFactory();
        return typeFactory.constructCollectionType(List.class, type);
    }

    /**
     * 创建 List<?>
     *
     * @param keyClass   索引类型
     * @param valueClass 值类型
     * @return List
     */
    public static JavaType createMap(final Class<?> keyClass, final Class<?> valueClass) {
        TypeFactory typeFactory = OBJECT_MAPPER.getTypeFactory();
        return typeFactory.constructMapType(Map.class, keyClass, valueClass);
    }

    /**
     * 获取Json节点
     *
     * @param key     索引
     * @param jsonStr json数据
     * @return 索引对应的数据
     */
    public static String getStringValue(final String key, final String jsonStr) {
        try {
            return findPath(key, jsonStr).textValue();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Json节点
     *
     * @param key     索引
     * @param jsonStr json数据
     * @return 索引对应的数据
     */
    public static Float getFloatValue(final String key, final String jsonStr) {
        try {
            return findPath(key, jsonStr).floatValue();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Json节点
     *
     * @param key     索引
     * @param jsonStr json数据
     * @return 索引对应的数据
     */
    public static Double getDoubleValue(final String key, final String jsonStr) {
        try {
            return findPath(key, jsonStr).doubleValue();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Json节点
     *
     * @param key     索引
     * @param jsonStr json数据
     * @return 索引对应的数据
     */
    public static BigInteger getBigIntegerValue(final String key, final String jsonStr) {
        try {
            return findPath(key, jsonStr).bigIntegerValue();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Json节点
     *
     * @param key     索引
     * @param jsonStr json数据
     * @return 索引对应的数据
     */
    public static byte[] getBinaryValue(final String key, final String jsonStr) {
        try {
            return findPath(key, jsonStr).binaryValue();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Json节点
     *
     * @param key     索引
     * @param jsonStr json数据
     * @return 索引对应的数据
     */
    public static Short getShortValue(final String key, final String jsonStr) {
        try {
            return findPath(key, jsonStr).shortValue();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取Json节点
     *
     * @param key     索引
     * @param jsonStr json数据
     * @return 索引对应的数据
     */
    private static JsonNode findPath(final String key, final String jsonStr) throws IOException {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonStr);
        return jsonNode.findPath(key);
    }

    /**
     * 分隔符转化
     * <p>{}#Map => {}</p>
     *
     * @param source    数据
     * @param delimiter 分隔符
     * @return
     */
    public static Object fromJson(String source, String delimiter) {
        if (Strings.isNullOrEmpty(source) || null == delimiter || source.indexOf(delimiter) == -1) {
            return null;
        }

        List<String> strings = Splitter.on(delimiter).trimResults().omitEmptyStrings().limit(2).splitToList(source);
        String value = strings.get(0);
        String type = strings.get(1);
        return fromJson(value, ClassUtils.forName(type));
    }
}
