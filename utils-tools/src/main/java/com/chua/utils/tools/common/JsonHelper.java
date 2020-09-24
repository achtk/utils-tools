package com.chua.utils.tools.common;

import com.chua.utils.tools.common.adaptor.GsonMapTypeAdapterFactory;
import com.google.common.base.Charsets;
import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 * @author CH
 */
public class JsonHelper {

    private static final Gson GSON_GLOBAL_CONFIG = new GsonBuilder()
            .setLenient()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(new GsonMapTypeAdapterFactory())
            .excludeFieldsWithModifiers(Modifier.PROTECTED)
            .serializeNulls()
            .create();

    /**
     *
     * @param obj
     * @return
     */
    public static String toFormatJson(Object obj) {
        return toJson(obj);
    }
    /**
     * 转成json
     * <p>
     * JsonHelper.toFormatJson("item1, item2, item3") = ["item1", "item2", "item3"]
     * </p>
     *
     * @param json 字符串
     * @return
     */
    public static List<String> toList(final String json) {
        return toEntity(json, TypeHelper.LIST_STRING);
    }

    /**
     * 转成json
     * <p>
     * JsonHelper.toFormatJson("{item1: item2}") = {"item1": "item2"}
     * </p>
     *
     * @param json 字符串
     * @return
     */
    public static List<Map<String, Object>> toListMapStringObject(final String json) {
        return toEntity(json, TypeHelper.LIST_MAP_STRING_OBJECT);
    }

    /**
     * 转成Object
     * <p>
     * JsonHelper.toFormatJson("{item1: item2}") = {"item1": "item2"}
     * JsonHelper.toFormatJson("item1, item2, item3") = ["item1", "item2", "item3"]
     * </p>
     *
     * @param json 字符串
     * @return
     */
    public static Object toObject(final String json) {
        return toEntity(json, Object.class);
    }

    /**
     * 转成 Map<String, String>
     *
     * @param url url
     * @return
     */
    public static Map<String, String> toMapStringString(final URL url) {
        return toEntity(url, TypeHelper.MAP_STRING_STRING);
    }

    /**
     * 转成 Map<String, String>
     *
     * @param is 流
     * @return
     */
    public static Map<String, String> toMapStringString(final InputStream is) {
        return toEntity(is, TypeHelper.MAP_STRING_STRING);
    }

    /**
     * 转成 Map<String, Object>
     * <p>
     * JsonHelper.toFormatJson("{item1: item2}") = {"item1": "item2"}
     * </p>
     *
     * @param json 字符串
     * @return
     */
    public static Map<String, Object> toMapStringObject(final String json) {
        return toEntity(json, TypeHelper.MAP_STRING_OBJECT);
    }

    /**
     * 转成 Map<String, Object>
     *
     * @param url url
     * @return
     */
    public static Map<String, Object> toMapStringObject(final URL url) {
        return toEntity(url, TypeHelper.MAP_STRING_OBJECT);
    }

    /**
     * 转成 Map<String, Object>
     *
     * @param is 流
     * @return
     */
    public static Map<String, Object> toMapStringObject(final InputStream is) {
        return toEntity(is, TypeHelper.MAP_STRING_OBJECT);
    }

    /**
     * 转成 Map<String, Object>
     *
     * @param reader 流
     * @return
     */
    public static Map<String, Object> toMapStringObject(final Reader reader) {
        return toEntity(reader, TypeHelper.MAP_STRING_OBJECT);
    }

    /**
     * 转成 Map<K, V>
     * <p>
     * JsonHelper.toFormatJson("{item1: item2}") = {"item1": "item2"}
     * </p>
     *
     * @param json 字符串
     * @return
     */
    public static <K, V> Map<K, V> toMap(final String json) {
        return toEntity(json, Map.class);
    }

    /**
     * 转成 Map<String, String>
     * <p>
     * JsonHelper.toFormatJson("{item1: item2}") = {"item1": "item2"}
     * </p>
     *
     * @param json 字符串
     * @return
     */
    public static Map<String, String> toMapStringString(final String json) {
        return toEntity(json, Map.class);
    }

    /**
     * 转成 T
     *
     * @param json 字符串
     * @param type 类型
     * @return
     */
    public static <T> T toEntity(final String json, final Class<T> type) {
        if (json == null) {
            return null;
        }
        StringReader reader = new StringReader(json);
        try {
            return toEntity(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转成 T
     *
     * @param url     url
     * @param classes 转化类
     * @param <T>
     * @return
     */
    public static <T> T toEntity(URL url, Class<T> classes) {
        try {
            return toEntity(url.openStream(), classes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转成 T
     *
     * @param inputStream 流
     * @param typeOfT     转化类对象
     * @param <T>
     * @return
     */
    public static <T> T toEntity(final InputStream inputStream, final Class<T> typeOfT) {
        if (null == inputStream) {
            return null;
        }
        try (InputStreamReader isr = new InputStreamReader(inputStream, Charsets.UTF_8)) {
            return fromJson(isr, typeOfT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOHelper.closeQuietly(inputStream);
        }
        return null;
    }

    /**
     * 转成 T
     *
     * @param reader  流
     * @param classes 转化类对象
     * @param <T>
     * @return
     */
    public static <T> T toEntity(final Reader reader, final Class<T> classes) {
        if (null == reader) {
            return null;
        }
        try {
            return fromJson(reader, classes);
        } finally {
            IOHelper.closeQuietly(reader);
        }
    }

    /**
     * 是否是json字符串
     *
     * @param jsonStr json字符串
     * @return
     */
    public static boolean isJson(String jsonStr) {
        try {
            JsonParser.parseString(jsonStr);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    /**
     * 加载json
     *
     * @param resource 资源文件
     */
    public static String loadJson(URL resource) {
        try {
            return loadJson(resource.openStream());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载json
     *
     * @param file json文件
     * @return
     */
    public static String loadJson(final File file) {
        try {
            return loadJson(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载json
     *
     * @param inputStream 流
     * @return
     */
    public static String loadJson(final InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8")) {
            return loadJson(inputStreamReader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 加载json
     *
     * @param resource 资源文件
     */
    public static <T>T loadJson(URL resource, Class<T> tClass) {
        return null == resource ? null : toEntity(resource, tClass);
    }
    /**
     * 加载json,并转化类型
     *
     * @param file json文件
     * @param tClass 类
     * @return
     */
    public static <T>T loadJson(final File file, Class<T> tClass) {
        try {
            return null == file ? null: toEntity(file.toURL(), tClass);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 加载json,并转化类型
     *
     * @param inputStream 流
     * @param tClass 类
     * @return
     */
    public static <T>T loadJson(final InputStream inputStream, Class<T> tClass) {
        return toEntity(inputStream, tClass);
    }

    /**
     * 加载json
     *
     * @param reader 流
     * @return
     */
    public static String loadJson(final Reader reader) {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.setLenient(true);
            JsonElement parse = Streams.parse(jsonReader);
            return toJson(parse);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 转成 Map<String, T>
     *
     * @param url     url
     * @param classes 转化类
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> toMap(URL url, Class<T> classes) {
        Map<String, T> map = new HashMap<>();
        try {
            return toEntity(url.openStream(), map.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载json
     *
     * @param reader 流
     * @return
     */
    public static Map<String, Object> loadEntity(final Reader reader) {
        String s = loadJson(reader);
        return toMapStringObject(s);
    }

    /**
     * 解析流
     *
     * @param stream
     * @return
     */
    public static Map<String, List<Map<String, Object>>> toMapListMap(InputStream stream) {
        return toEntity(stream, TypeHelper.MAP_LIST_MAP);
    }

    /**
     * json转对象
     * @param typeOfT
     * @param <T>
     * @return
     */
    private static <T> T fromJson(Reader reader, Class<T> typeOfT) {
        try{
            return GSON_GLOBAL_CONFIG.fromJson(reader, typeOfT);
        } finally {
            IOHelper.closeQuietly(reader);
        }
    }

    /**
     * 转成json
     *
     * @param obj 对象
     * @return
     */
    public static String toJson(final Object obj) {
        return GSON_GLOBAL_CONFIG.toJson(obj);
    }


}
