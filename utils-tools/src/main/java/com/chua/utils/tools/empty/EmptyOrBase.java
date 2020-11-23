package com.chua.utils.tools.empty;

import com.chua.utils.tools.collects.collections.ListHelper;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.function.able.InitializingCacheable;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.chua.utils.tools.constant.ClassConstant.*;

/**
 * 默认对象
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/6
 */
@SuppressWarnings("all")
public class EmptyOrBase extends InitializingCacheable {
    /**
     * 空List
     */
    public static final List<?> EMPTY_LIST = Collections.emptyList();
    /**
     * 空 Set
     */
    public static final Set<?> EMPTY_SET = Collections.emptySet();
    /**
     * 空properties
     */
    public static final Properties EMPTY_PROPERTIES = new Properties();

    /**
     * 空 map
     */
    public static final Map<?, ?> EMPTY_MAP = Collections.emptyMap();
    /**
     * Class<Map<String, Object>>
     */
    public static final Class<Map<String, Object>> MAP_STRING_OBJECT = (Class<Map<String, Object>>) MapOperableHelper.newMapStringObject().getClass();
    /**
     * Class<Map<String, Object>>
     */
    public static final Class<Map<String, Map<String, Object>>> MAP_MAP_OBJECT = (Class<Map<String, Map<String, Object>>>) MapOperableHelper.newMapStringMap().getClass();
    /**
     * Class<Map<String, String>>
     */
    public static final Class<Map<Object, Object>> MAP_OBJECT_OBJECT = (Class<Map<Object, Object>>) MapOperableHelper.newMapObjectObject().getClass();
    /**
     * Class<List<String>>
     */
    public static final Class<List<String>> LIST_STRING = (Class<List<String>>) ListHelper.newArrayList().getClass();
    /**
     * Class<List<Map<String, Object>>>
     */
    public static final Class<List<Map<String, Object>>> LIST_MAP_STRING_OBJECT = (Class<List<Map<String, Object>>>) ListHelper.newArrayList().getClass();
    /**
     * Class<Map<String, List<Map<String, Object>>>>
     */
    public static final Class<Map<String, List<Map<String, Object>>>> MAP_LIST_MAP = (Class<Map<String, List<Map<String, Object>>>>) MapOperableHelper.newArrayList().getClass();
    /**
     * Class<Map<String, String>>
     */
    public static final Class<Map<String, String>> MAP_STRING_STRING = (Class<Map<String, String>>) MapOperableHelper.newMapStringString().getClass();
    /**
     * new String[0]
     */
    public static final String[] EMPTY_STRING = new String[0];
    /**
     * 常用基础类型名称
     * "boolean", "char", "byte", "short", "int", "long", "float", "double", "void"
     */
    public static final List<String> BASE_NAME_LIST = Lists.newArrayList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
    /**
     * 常用基础类型
     * "boolean", "char", "byte", "short", "int", "long", "float", "double", "void"
     */
    public static final List<Class<?>> BASE_TYPE_LIST = Lists.newArrayList(
            boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class);
    /**
     * 常用基础类型表述
     * ""Z", "C", "B", "S", "I", "J", "F", "D", "V"
     */
    public static final List<String> BASE_DESCRIPT_LIST = Lists.newArrayList("Z", "C", "B", "S", "I", "J", "F", "D", "V");
    /**
     * 基础类型与默认值
     */
    public static final Map<Class<?>, Object> BASE_TYPE_VALUE = Collections.unmodifiableMap(new HashMap<Class<?>, Object>() {
        {
            put(boolean.class, false);
            put(byte.class, (byte) 0);
            put(float.class, (float) 0);
            put(double.class, (double) 0);
            put(char.class, (char) 0);
            put(short.class, (short) 0);
            put(int.class, 0);
            put(long.class, (long) 0);
        }
    });
    /**
     * 基础类型名称与类型关系
     */
    public static final Map<String, Class<?>> BASE_NAME_TYPE = Collections.unmodifiableMap(new HashMap<String, Class<?>>() {
        {
            put(CLASS_BOOLEAN, boolean.class);
            put(CLASS_BOOLEANS, boolean[].class);
            put(CLASS_BYTE, byte.class);
            put(CLASS_BYTES, byte[].class);
            put(CLASS_FLOAT, float.class);
            put(CLASS_FLOATS, float[].class);
            put(CLASS_LONG, long.class);
            put(CLASS_LONGS, long[].class);
            put(CLASS_INT, int.class);
            put(CLASS_INTS, int[].class);
            put(CLASS_INTEGER, Integer.class);
            put(CLASS_INTEGERS, Integer[].class);
            put(CLASS_DOUBLE, double.class);
            put(CLASS_DOUBLES, double[].class);
            put(CLASS_VOID, void.class);
            put(CLASS_CHAR, char.class);
            put(CLASS_CHARS, char[].class);
            put(CLASS_CHARACTER, Character.class);
            put(CLASS_CHARACTERS, Character[].class);
            put(CLASS_DATE, Date.class);
        }
    });
    /**
     * java_class_path
     */
    public static final String JAVA_CLASS_PATH = System.getProperty("java.class.path");
    /**
     * java_home
     */
    public static final String JAVA_HOME = System.getProperty("java.home");
    /**
     * sun.desktop
     */
    public static final String SUN_DESKTOP = System.getProperty("sun.desktop");
    /**
     * user_name
     */
    public static final String USER_NAME = System.getProperty("user.name");
    /**
     * os名称
     */
    public static final String OS_NAME = System.getProperty("os.name");
    /**
     * user.timezone
     */
    public static final String USER_TIMEZONE = System.getProperty("user.timezone");
    /**
     * user_home
     */
    public static final String USER_HOME = System.getProperty("user.home");
    /**
     * cpu个数
     */
    public static final int PROCESS_NUM = Runtime.getRuntime().availableProcessors();
    /**
     * 解析excel
     */
    public static final String EXCEL_PARSER = "com.chua.utils.tools.office.excel.parser.ExcelParser";
    /**
     * 解析csv
     */
    public static final String CSV_PARSER = "com.chua.utils.tools.office.csv.parser.CsvParser";
    /**
     * 空 byte[]
     */
    public static final byte[] EMPTY_BYTES =  new byte[0];

    /**
     * 常用基础类型
     * "boolean", "char", "byte", "short", "int", "long", "float", "double", "void"
     */
    public static List<String> baseNames() {
        return BASE_NAME_LIST;
    }

    /**
     * 常用基础类型表述
     * ""Z", "C", "B", "S", "I", "J", "F", "D", "V"
     */
    public static List<String> baseDescript() {
        return BASE_DESCRIPT_LIST;
    }

    /**
     * 常用基础类型
     * "boolean", "char", "byte", "short", "int", "long", "float", "double", "void"
     */
    public static List<Class<?>> baseTypes() {
        return BASE_TYPE_LIST;
    }

    /**
     * 类型转化器
     */
    public static final ConcurrentMap<Class, TypeConverter> TYPE_CONVERTER = new ConcurrentHashMap<Class, TypeConverter>() {
        {
            Set<TypeConverter> allSpiService = ExtensionFactory.getExtensionLoader(TypeConverter.class).getAllSpiService();
            for (TypeConverter typeConverter : allSpiService) {
                put(typeConverter.getType(), typeConverter);
            }
        }
    };

    /**
     * 获取类型的类型转化器
     *
     * @param aClass 类型
     */
    public static TypeConverter getTypeConverter(Class<?> aClass) {
        return TYPE_CONVERTER.get(aClass);
    }
}
