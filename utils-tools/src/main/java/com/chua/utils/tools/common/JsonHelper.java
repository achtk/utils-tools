package com.chua.utils.tools.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * json工具类
 *
 * @author CH
 */
@Slf4j
public class JsonHelper {
    /**
     * 对象绑定
     */
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectMapper YAML_OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());
    private static final ObjectMapper XML_OBJECT_MAPPER = new ObjectMapper(new XmlFactory());
    public static JavaType MAP_STRING_OBJECT_TYPE = null;
    private static TypeFactory TYPE_FACTORY = null;

    static {
        TYPE_FACTORY = OBJECT_MAPPER.getTypeFactory();
        MAP_STRING_OBJECT_TYPE = TYPE_FACTORY.constructMapType(Map.class, String.class, Object.class);
        //忽略空对象转化异常
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //忽略对象不存在属性异常
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //忽略空对象转化异常
        YAML_OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //忽略对象不存在属性异常
        YAML_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //忽略空对象转化异常
        XML_OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //忽略对象不存在属性异常
        XML_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //OBJECT_MAPPER.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }

    /**
     * 格式化对象为JSON字符串
     *
     * @param obj 对象
     * @return String
     */
    public static String toFormatJson(final Object obj) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转对象
     *
     * @param json json
     * @return String
     */
    public static Object toObject(final String json) {
        try {
            return OBJECT_MAPPER.readValue(json, Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转对象
     *
     * @param json json
     * @return
     */
    public static List<Object> fromJson2List(final String json) {
        return fromJson(json, List.class);
    }

    /**
     * json转对象
     *
     * @param json   json
     * @param tClass 类型
     * @param <T>
     * @return
     */
    public static <T> T fromJson(final String json, final Class<T> tClass) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructType(tClass);
        return fromJson(json, javaType);
    }

    /**
     * json转对象
     *
     * @param json     json
     * @param javaType 类型
     * @param <T>
     * @return
     */
    private static <T> T fromJson(final String json, final JavaType javaType) {
        if (null == json) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转对象
     *
     * @param url      url
     * @param javaType 类型
     * @param <T>
     * @return
     */
    private static <T> T fromJson(final URL url, final JavaType javaType) {
        if (null == url) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(url, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转对象
     *
     * @param json   json
     * @param tClass 类型
     * @return
     */
    public static <T> List<T> fromJson2List(final String json, final Class<T> tClass) {
        CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, tClass);
        return fromJson(json, collectionType);
    }

    /**
     * json转对象
     *
     * @param url    url
     * @param tClass 类型
     * @return
     */
    public static <T> List<T> fromJson2List(final URL url, final Class<T> tClass) {
        CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, tClass);
        return fromJson(url, collectionType);
    }

    /**
     * json转对象
     *
     * @param json   json
     * @param tClass 类型
     * @return
     */
    public static <T> Set<T> fromJson2Set(final String json, final Class<T> tClass) {
        CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(Set.class, tClass);
        return fromJson(json, collectionType);
    }

    /**
     * json转对象
     *
     * @param json json
     * @return
     */
    public static Map<String, Object> fromJson2Map(final String json) {
        return fromJson(json, MAP_STRING_OBJECT_TYPE);
    }

    /**
     * json转对象
     *
     * @param bytes json
     * @return
     */
    public static Map<String, Object> fromJson2Map(final byte[] bytes) {
        if (null == bytes) {
            return Collections.emptyMap();
        }
        return fromJson(bytes, MAP_STRING_OBJECT_TYPE);
    }

    /**
     * json转对象
     *
     * @param bytes    json
     * @param javaType 类型
     * @param <T>
     * @return
     */
    private static <T> T fromJson(final byte[] bytes, final JavaType javaType) {
        if (null == bytes) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(bytes, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转对象
     *
     * @param json json
     * @return
     */
    public static <V> Map<String, V> fromJson2Map(final String json, final Class<V> valueClass) {
        MapType mapType = OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, String.class, valueClass);
        return fromJson(json, mapType);
    }

    /**
     * json转对象
     *
     * @param json json
     * @return
     */
    public static <K, V> Map<K, V> fromJson2Map(final String json, final Class<K> keyClass, final Class<V> valueClass) {
        MapType mapType = OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        return fromJson(json, mapType);
    }

    /**
     * json转对象
     *
     * @param json json
     * @return
     */
    public static Object fromJson2Object(final String json) {
        return fromJson(json, Object.class);
    }

    /**
     * 字符串转对象
     *
     * @param xmlString 字符串
     * @param tClass    类型
     * @param <T>
     * @return
     */
    public static <T> T fromXmlJson(final String xmlString, final Class<T> tClass) {
        try {
            return YAML_OBJECT_MAPPER.readValue(xmlString, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转对象
     *
     * @param ymlString 字符串
     * @param tClass    类型
     * @param <T>
     * @return
     */
    public static <T> T fromYmlJson(final String ymlString, final Class<T> tClass) {
        try {
            return YAML_OBJECT_MAPPER.readValue(ymlString, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件转对象
     *
     * @param file   文件
     * @param tClass 类型
     * @param <T>
     * @return
     */
    public static <T> T fromJson(final File file, final Class<? extends T> tClass) {
        if (null == file) {
            return null;
        }
        try {
            return fromJson(new FileInputStream(file), tClass);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 流转对象
     *
     * @param inputStream 流
     * @param tClass      类型
     * @param <T>
     * @return
     */
    public static <T> T fromJson(final InputStream inputStream, final Class<T> tClass) {
        return fromJson(new InputStreamReader(inputStream, Charsets.UTF_8), tClass);
    }

    /**
     * 流转对象
     *
     * @param inputStreamReader 流
     * @param tClass            类型
     * @param <T>
     * @return
     */
    public static <T> T fromJson(final InputStreamReader inputStreamReader, final Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(inputStreamReader, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoHelper.closeQuietly(inputStreamReader);
        }
        return null;
    }

    /**
     * 文件转对象
     *
     * @param file   文件
     * @param tClass 类型
     * @param <T>
     * @return
     */
    public static <T> T fromYamlJson(final File file, final Class<T> tClass) {
        if (null == file) {
            return null;
        }
        try {
            return fromYamlJson(new FileInputStream(file), tClass);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 流转对象
     *
     * @param inputStream 流
     * @param tClass      类型
     * @param <T>
     * @return
     */
    public static <T> T fromYamlJson(final InputStream inputStream, final Class<T> tClass) {
        return fromYamlJson(new InputStreamReader(inputStream, Charsets.UTF_8), tClass);
    }

    /**
     * 流转对象
     *
     * @param inputStreamReader 流
     * @param tClass            类型
     * @param <T>
     * @return
     */
    public static <T> T fromYamlJson(final InputStreamReader inputStreamReader, final Class<T> tClass) {
        try {
            return YAML_OBJECT_MAPPER.readValue(inputStreamReader, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoHelper.closeQuietly(inputStreamReader);
        }
        return null;
    }

    /**
     * 文件转对象
     *
     * @param file   文件
     * @param tClass 类型
     * @param <T>
     * @return
     */
    public static <T> T fromXmlJson(final File file, final Class<T> tClass) {
        if (null == file) {
            return null;
        }
        try {
            return fromXmlJson(new FileInputStream(file), tClass);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 流转对象
     *
     * @param inputStream 流
     * @param tClass      类型
     * @param <T>
     * @return
     */
    public static <T> T fromXmlJson(final InputStream inputStream, final Class<T> tClass) {
        return fromXmlJson(new InputStreamReader(inputStream, Charsets.UTF_8), tClass);
    }

    /**
     * 流转对象
     *
     * @param inputStreamReader 流
     * @param tClass            类型
     * @param <T>
     * @return
     */
    public static <T> T fromXmlJson(final InputStreamReader inputStreamReader, final Class<T> tClass) {
        try {
            return YAML_OBJECT_MAPPER.readValue(inputStreamReader, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoHelper.closeQuietly(inputStreamReader);
        }
        return null;
    }

    /**
     * 对象转对象
     *
     * @param obj    对象
     * @param tClass 类型
     * @param <T>
     * @return
     */
    public static <T> T fromObject(final Object obj, final Class<T> tClass) {
        if (null == obj) {
            return null;
        }
        return fromJson(toJson(obj), tClass);
    }

    /**
     * 格式化对象为JSON字符串
     *
     * @param obj 对象
     * @return String
     */
    public static String toJson(final Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件转对象
     *
     * @param url    文件
     * @param tClass 类型
     * @param <T>
     * @return
     */
    public static <T> T fromJson(final URL url, final Class<T> tClass) {
        if (null == url) {
            return null;
        }
        try {
            return fromJson(url.openStream(), tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转对象
     *
     * @param bytes  json
     * @param tClass 类型
     * @param <T>
     * @return
     */
    public static <T> T fromJson(final byte[] bytes, final Class<T> tClass) {
        if (null == bytes || null == tClass) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(bytes, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否为json
     *
     * @param json json
     * @return
     */
    public static boolean isJson(String json) {
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 流转对象
     *
     * @param openStream 流
     * @return
     */
    public static Map<String, List<Map<String, Object>>> toMapListMap(InputStream openStream) {
        if (null == openStream) {
            return null;
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructType(String.class);
        JavaType javaTypeList = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, MAP_STRING_OBJECT_TYPE);

        MapType mapType = OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, javaType, javaTypeList);
        try (InputStreamReader inputStreamReader = new InputStreamReader(openStream, Charsets.UTF_8)) {
            return OBJECT_MAPPER.readValue(inputStreamReader, mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
