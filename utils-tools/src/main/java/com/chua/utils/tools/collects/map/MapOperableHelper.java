package com.chua.utils.tools.collects.map;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.empty.Empty;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.properties.NetProperties;
import net.sf.cglib.beans.BeanMap;
import org.checkerframework.checker.units.qual.K;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * Map工具类
 *
 * @author CH
 * @version 1.0.0
 * @see MultiMapOperableHelper
 * @since 2020/10/31
 */
public class MapOperableHelper extends MultiMapOperableHelper {

    /**
     * 初始化大小
     */
    public static final int DEFAULT_SIZE = 1 << 4;

    /**
     * 创建Map<K, V>
     *
     * @return Map
     */
    public static <K, V> Map<K, V> newMap() {
        return new HashMap<>(DEFAULT_SIZE);
    }

    /**
     * 创建 ConcurrentHashMap
     *
     * @param <K> 索引类型
     * @param <V> 值类型
     * @return ConcurrentHashMap
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * 创建Map<String, String>
     *
     * @return Map
     */
    public static Map<String, String> newMapStringString() {
        return newMap();
    }

    /**
     * 创建 Map<Object, Object>
     *
     * @return Map
     */
    public static Map<String, Object> newMapStringObject() {
        return newMap();
    }

    /**
     * 创建 Map<Object, Object>
     *
     * @return Map
     */
    public static Map<Object, Object> newMapObjectObject() {
        return newMap();
    }

    /**
     * 创建 Map<String, List<Map<String, Object>>>
     *
     * @return Map
     */
    public static Map<String, List<Map<String, Object>>> newArrayList() {
        return new HashMap<>(DEFAULT_SIZE);
    }

    /**
     * 获取Boolean
     *
     * @param map 集合
     * @param key 查找的关键
     * @return Boolean
     */
    public static <K, V> Boolean getBoolean(final Map<K, V> map, final K key) {
        Object answer = getObject(map, key);
        if (answer != null) {
            if (answer instanceof Boolean) {
                return (Boolean) answer;

            } else if (answer instanceof String) {
                return Boolean.valueOf((String) answer);

            } else if (answer instanceof Number) {
                Number n = (Number) answer;
                return (n.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
            }
        }
        return null;
    }

    /**
     * 获取Boolean
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return Boolean
     */
    public static <K, V> Boolean getBoolean(final Map<K, V> map, final K key, final Boolean defaultValue) {
        Boolean aBoolean = getBoolean(map, key);
        return null == aBoolean ? defaultValue : aBoolean;
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> null </ code>
     */
    public static <K, V> byte getByteValue(final Map<K, V> map, final K key) {
        Byte aByte = getByte(map, key);
        return null == aByte ? (byte) 0 : aByte;
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> null </ code>
     */
    public static <K, V> byte getByteValue(final Map<K, V> map, final K key, final byte defaultValue) {
        Byte aByte = getByte(map, key);
        return null == aByte ? defaultValue : aByte;
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> null </ code>
     */
    public static <K, V> Byte getByte(final Map<K, V> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Byte) {
            return (Byte) answer;
        }
        return answer.byteValue();
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> null </ code>
     */
    public static <K, V> Byte getByte(final Map<K, V> map, final K key, final Byte defaultValue) {
        Byte aByte = getByte(map, key);
        return null == aByte ? defaultValue : aByte;
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 如果输入的Map为空，则以Short<code> 0 </ code>返回Map中的值
     */
    public static <K, V> short getShortValue(final Map<K, V> map, final K key) {
        Short aShort = getShort(map, key);
        return null == aShort ? 0 : aShort;
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则以Short<code> 0 </ code>返回Map中的值
     */
    public static <K, V> short getShortValue(final Map<K, V> map, final K key, final short defaultValue) {
        Short aShort = getShort(map, key);
        return null == aShort ? defaultValue : aShort;
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 如果输入的Map为空，则以Short<code> null </ code>返回Map中的值
     */
    public static <K, V> Short getShort(final Map<K, V> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Short) {
            return (Short) answer;
        }
        return answer.shortValue();
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则以Short<code> null </ code>返回Map中的值
     */
    public static <K, V> Short getShort(final Map<K, V> map, final K key, final Short defaultValue) {
        Short aShort = getShort(map, key);
        return null == aShort ? defaultValue : aShort;
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> 0 </ code>
     */
    public static <K, V> int getIntValue(final Map<K, V> map, final K key) {
        Integer integer = getInteger(map, key);
        return null == integer ? 0 : integer;
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> 0 </ code>
     */
    public static <K, V> int getIntValue(final Map<K, V> map, final K key, final int defaultValue) {
        Integer integer = getInteger(map, key);
        return null == integer ? defaultValue : integer;
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> null </ code>
     */
    public static <K, V> Integer getInteger(final Map<K, V> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Integer) {
            return (Integer) answer;
        }
        return answer.intValue();
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> null </ code>
     */
    public static <K, V> Integer getInteger(final Map<K, V> map, final K key, final Integer defaultValue) {
        Integer integer = getInteger(map, key);
        return null == integer ? defaultValue : integer;
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> 0 </ code>
     */
    public static <K, V> long getLongValue(final Map<K, V> map, final K key) {
        Long aLong = getLong(map, key);
        return null == aLong ? 0L : aLong;
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> 0 </ code>
     */
    public static <K, V> long getLongValue(final Map<K, V> map, final K key, final long defaultValue) {
        Long aLong = getLong(map, key);
        return null == aLong ? defaultValue : aLong;
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> null </ code>
     */
    public static <K, V> Long getLong(final Map<K, V> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Long) {
            return (Long) answer;
        }
        return answer.longValue();
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> null </ code>
     */
    public static <K, V> Long getLong(final Map<K, V> map, final K key, final Long defaultValue) {
        Long aLong = getLong(map, key);
        return null == aLong ? defaultValue : aLong;
    }

    /**
     * 以null安全的方式从Map获取Float。
     * <p> 浮点数是根据{@link #getNumber（Object）}的结果获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 以浮点数形式返回Map中的值，如果输入的Map为空，则为<code> 0 </ code>
     */
    public static <K, V> float getFloatValue(final Map<K, V> map, final K key) {
        Float aFloat = getFloat(map, key);
        return null == aFloat ? 0f : aFloat;
    }

    /**
     * 以null安全的方式从Map获取Float。
     * <p> 浮点数是根据{@link #getNumber（Object）}的结果获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 以浮点数形式返回Map中的值，如果输入的Map为空，则为<code> 0 </ code>
     */
    public static <K, V> float getFloatValue(final Map<K, V> map, final K key, final float defaultValue) {
        Float aFloat = getFloat(map, key);
        return null == aFloat ? defaultValue : aFloat;
    }

    /**
     * 以null安全的方式从Map获取Float。
     * <p> 浮点数是根据{@link #getNumber（Object）}的结果获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 以浮点数形式返回Map中的值，如果输入的Map为空，则为<code> null </ code>
     */
    public static <K, V> Float getFloat(final Map<K, V> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Float) {
            return (Float) answer;
        }
        return answer.floatValue();
    }

    /**
     * 以null安全的方式从Map获取Float。
     * <p> 浮点数是根据{@link #getNumber（Object）}的结果获得的。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 以浮点数形式返回Map中的值，如果输入的Map为空，则为<code> null </ code>
     */
    public static <K, V> Float getFloat(final Map<K, V> map, final K key, final Float defaultValue) {
        Float aFloat = getFloat(map, key);
        return null == aFloat ? defaultValue : aFloat;
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 如果输入的Map为空，则Map中的值为Double，<code> 0 </ code>
     */
    public static <K, V> double getDoubleValue(final Map<K, V> map, final K key) {
        Double aDouble = getDouble(map, key);
        return null == aDouble ? 0D : aDouble;
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则Map中的值为Double，<code> 0 </ code>
     */
    public static <K, V> double getDoubleValue(final Map<K, V> map, final K key, final double defaultValue) {
        Double aDouble = getDouble(map, key);
        return null == aDouble ? defaultValue : aDouble;
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 如果输入的Map为空，则Map中的值为Double，<code> null </ code>
     */
    public static <K, V> Double getDouble(final Map<K, V> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Double) {
            return (Double) answer;
        }
        return answer.doubleValue();
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param map          集合
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则Map中的值为Double，<code> null </ code>
     */
    public static <K, V> Double getDouble(final Map<K, V> map, final K key, final Double defaultValue) {
        Double aDouble = getDouble(map, key);
        return null == aDouble ? defaultValue : aDouble;
    }

    /**
     * 在给定映射中查找给定键，如果转换失败，则使用默认值将结果转换为。
     *
     * @param map          集合
     * @param key          在Map中查找的值的关键
     * @param defaultValue 如果值为null或转换失败，将返回defaultValue
     * @return 映射中的值为数字的值；如果
     * 原始值为null，映射为null或数字转换
     * 失败，则为defaultValue
     */
    public static <K, V> Number getNumber(final Map<K, V> map, K key, Number defaultValue) {
        Number answer = getNumber(map, key);
        return null == answer ? defaultValue : answer;
    }

    /**
     * 以null安全的方式从Map获取数字。
     * <p> 如果该值为<code> Number </ code>，则直接返回。
     * 如果值是<code> String </ code>，则使用系统默认格式程序上的
     * {@link NumberFormat ＃parse（String）}进行转换
     * 如果转换失败，则返回<code> null </ code>。
     * 否则，返回<code> null </ code>。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return Map中的值作为Number，如果Map输入为空，则为<code> null </ code>
     */
    public static <K, V> Number getNumber(final Map<K, V> map, final K key) {
        Object answer = getObject(map, key);
        if (answer != null) {
            if (answer instanceof Number) {
                return (Number) answer;
            } else if (answer instanceof String) {
                try {
                    String text = (String) answer;
                    return NumberFormat.getInstance().parse(text);
                } catch (ParseException e) {
                    // 失败意味着返回null
                }
            }
        }
        return null;
    }

    /**
     * 以null安全的方式从Map获取Map。
     * <p> *如果从指定映射返回的值不是Map，则返回<code> null </ code>。
     *
     * @param key 查找的关键
     * @return Map中作为Map的值，如果为null，则<code> null </ code>
     */
    public static <K, V> Map<?, ?> getMap(final Map<K, V> map, final K key) {
        Object answer = map.get(key);
        if (answer instanceof Map) {
            return (Map<?, ?>) answer;
        }
        return null;
    }

    /**
     * 以null安全的方式从Map获取Map。
     * <p> *如果从指定映射返回的值不是Map，则返回<code> {} </ code>。
     *
     * @param key 查找的关键
     * @return Map中作为Map的值，如果为null，则<code> {} </ code>
     */
    public static <K, V> Map<?, ?> getMapOrDefault(final Map<K, V> map, final K key) {
        Object answer = map.get(key);
        if (answer instanceof Map) {
            return (Map<?, ?>) answer;
        }
        return Collections.emptyMap();
    }

    /**
     * 以null安全的方式从Map获取Set。
     * <p> 如果从指定映射返回的值不是Map，则返回<code> null </ code>。
     *
     * @param key 查找的关键
     * @return Map中作为Set的值，如果为null，则<code> null </ code>
     */
    public static <K, V> Set<?> getSet(final Map<K, V> map, final K key) {
        Object answer = map.get(key);
        if (answer instanceof Set) {
            return (Set<?>) answer;
        }
        return null;
    }

    /**
     * 以null安全的方式从Map获取Set。
     * <p> 如果从指定映射返回的值不是Map，则返回<code> [] </ code>。
     *
     * @param key 查找的关键
     * @return Map中作为Set的值，如果为null，则<code> [] </ code>
     */
    public static <K, V> Set<?> getSetOrDefault(final Map<K, V> map, final K key) {
        Object answer = map.get(key);
        if (answer instanceof Set) {
            return (Set<?>) answer;
        }
        return Collections.emptySet();
    }

    /**
     * 以null安全的方式从Map获取List。
     * <p> 如果从指定映射返回的值不是Map，则返回<code> null </ code>。
     *
     * @param key 查找的关键
     * @return Map中作为List的值，如果为null，则<code> null </ code>
     */
    public static <K, V> List<?> getList(final Map<K, V> map, final K key) {
        Object answer = map.get(key);
        if (answer instanceof List) {
            return (List<?>) answer;
        }
        return null;
    }

    /**
     * 以null安全的方式从Map获取List。
     * <p> 如果从指定映射返回的值不是Map，则返回<code> [] </ code>。
     *
     * @param key 查找的关键
     * @return Map中作为List的值，如果为null，则<code>  [] </ code>
     */
    public static <K, V> List<?> getListOrDefault(final Map<K, V> map, final K key) {
        Object answer = map.get(key);
        if (answer instanceof List) {
            return (List<?>) answer;
        }
        return Collections.emptyList();
    }

    /**
     * 以null安全的方式从Map获取字符串数组。
     * <p>字符串是通过<code> toString </ code>获得的。
     *
     * @param map       集合
     * @param key       查找的关键
     * @param delimiter 分隔符
     * @return Map中的值作为字符串，如果为null，则<code> [] </ code>
     */
    public static <K, V> String[] getStringArray(final Map<K, V> map, final K key, final String delimiter) {
        String string = getString(map, key);
        return null == string ? ArraysHelper.emptyString() : string.split(delimiter);
    }

    /**
     * 以null安全的方式从Map获取字符串数组。
     * <p>字符串是通过<code> toString </ code>获得的。
     *
     * @param map       集合
     * @param key       查找的关键
     * @param delimiter 分隔符
     * @return Map中的值作为字符串，如果为null，则<code> [] </ code>
     */
    public static <K, V> List<String> getStringList(final Map<K, V> map, final K key, final String delimiter) {
        String string = getString(map, key);
        return null == string ? Collections.emptyList() : Arrays.asList(string.split(delimiter));
    }

    /**
     * 以null安全的方式从Map获取字符串。
     * <p>字符串是通过<code> toString </ code>获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return Map中的值作为字符串，如果为null，则<code> null </ code>
     */
    public static <K, V> String getString(final Map<K, V> map, final K key) {
        Object object = getObject(map, key);
        if (null == object) {
            return null;
        }
        return object.toString();
    }

    /**
     * 在给定映射中查找给定键，如果转换失败，则使用默认值将结果转换为字符串。
     *
     * @param map          集合
     * @param key          在该Map中查找的值的关键
     * @param defaultValue 如果该值为null或转换失败，将返回{defaultValue}
     * @return 映射中的值作为字符串；如果原始值为null，映射为null或字符串转换，则为defaultValue
     */
    public static <K, V> String getString(final Map<K, V> map, final K key, final String defaultValue) {
        String answer = getString(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * 获取 Object
     *
     * @param map 集合
     * @param key 索引
     * @return Map中的值，如果Map输入为空，则为<code> null </ code>
     */
    public static <K, V> Object getObject(final Map<K, V> map, final K key) {
        return map != null ? map.get(key) : null;
    }

    /**
     * 获取 Object
     *
     * @param map 集合
     * @param key 索引
     * @return Map中的值，如果Map输入为空，则为<code> null </ code>
     */
    public static <K, V> List<Object> getObjectList(final Map<K, V> map, final K key) {
        return map != null ? Collections.singletonList(map.get(key)) : Collections.emptyList();
    }

    /**
     * 获取 Object
     *
     * @param map          集合
     * @param key          索引
     * @param defaultValue 默认值
     * @return Map中的值，如果Map输入为空，则为<code> null </ code>
     */
    public static <K, V> Object getObject(final Map<K, V> map, final K key, final Object defaultValue) {
        Object object = getObject(map, key);
        if (object != null) {
            return object;
        }
        return defaultValue;
    }

    /**
     * 获取 Object
     *
     * @param map    集合
     * @param key    索引
     * @param tClass 类型
     * @return Map中的值，如果Map输入为空，则为<code> null </ code>
     */
    public static <T, K, V> T getType(final Map<K, V> map, final K key, final Class<T> tClass) {
        Object object = getObject(map, key);
        if (object != null && null != tClass && tClass.isAssignableFrom(object.getClass())) {
            return (T) object;
        }
        return null;
    }

    /**
     * 获取Map长度
     *
     * @param map 集合
     * @return map为空返回0, 否则返回真实长度
     */
    public static <K, V> int getSize(final Map<K, V> map) {
        return null == map ? 0 : map.size();
    }

    /**
     * 空安全检查指定的Dictionary是否为空。 * * <p> Null返回true。
     *
     * @param map 要检查的集合，可以为null
     * @return 如果为空或null，则为true
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 空安全检查指定的Dictionary是否为空。 * * <p> Null返回true。
     *
     * @param dictionary 要检查的集合，可以为null
     * @return 如果为空或null，则为true
     */
    public static boolean isEmpty(Dictionary<?, ?> dictionary) {
        return (dictionary == null || dictionary.isEmpty());
    }

    /**
     * 存储不为空的值.
     *
     * @param target 集合
     * @param key    索引
     * @param value  值
     */
    public static void putIfValNoNull(Map target, Object key, Object value) {
        Objects.requireNonNull(key, "key");
        if (target == null) {
            target = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        }
        if (value != null) {
            target.put(key, value);
        }
    }

    /**
     * 存储不存在的值.
     *
     * @param target target map
     * @param key    key
     * @param value  value
     */
    public static void computeIfAbsent(Map target, Object key, Object value) {
        Objects.requireNonNull(key, "key");
        if (target == null) {
            target = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        }
        if (value != null && !target.containsKey(key)) {
            target.put(key, value);
        }
    }


    /**
     * 获取过滤集合
     *
     * @param map    集合
     * @param filter 过滤
     * @return map 过滤集合
     */
    public static <K, V> Map<K, V> doWith(final Map<K, V> map, final Filter<V> filter) {
        if (null == map) {
            return null;
        }
        Map<K, V> result = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        map.forEach((o, o2) -> {
            boolean matcher = filter.matcher(o2);
            if (matcher) {
                result.put(o, o2);
            }
        });
        return result;
    }

    /**
     * 匹配集合
     *
     * @param map     集合
     * @param matcher 匹配
     */
    public static <K, V> void doWith(final Map<K, V> map, final Matcher<Map.Entry<K, V>> matcher) {
        if (null == map) {
            return;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            try {
                matcher.doWith(entry);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * 以null安全的方式从Map中获取Date，。
     * <p> Date是从 的结果中获得的。
     *
     * @param map 集合
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为Date，<code> null </ code>
     */
    public static <K, V> Date getDate(final Map<K, V> map, final K key) {
        Object answer = getObject(map, key);
        if (null == answer) {
            return null;
        }
        if (answer instanceof Date) {
            return (Date) answer;
        }
        if (answer instanceof Long) {
            return new Date((Long) answer);
        }

        if (answer instanceof String) {
            DateFormat dateFormat = new SimpleDateFormat();
            try {
                return dateFormat.parse((String) answer);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 格式化时间
     *
     * @param map    集合
     * @param key    索引
     * @param format 格式化表达式
     * @param <K>    索引类型
     * @return 格式化时间数据
     */
    public static <K> String dateFormatter(Map<K, Object> map, K key, String format) {
        Object object = getObject(map, key);
        if (null == object) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if (object instanceof Date) {
            return simpleDateFormat.format(object);
        }

        if (object instanceof Long) {
            return simpleDateFormat.format(new Date((Long) object));
        }

        if (object instanceof String) {
            try {
                return simpleDateFormat.format(simpleDateFormat.parse((String) object));
            } catch (Exception e) {
            }
        }

        return null;
    }

    /**
     * map转 Properties
     *
     * @param map 数据集合
     * @return Properties
     */
    public static Properties toProp(Map<?, ?> map) {
        if (map == null) {
            return Empty.EMPTY_PROPERTIES;
        }
        Properties properties = new Properties();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            properties.put(entry.getKey(), entry.getValue());
        }
        return properties;
    }

    /**
     * 配置文件化
     *
     * @param object 对象
     * @return
     */
    public static Properties toProfile(Object object) {
        Properties properties1 = new Properties();
        Map<String, Object> pendingMap = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        if (!(object instanceof Map)) {
            BeanMap beanMap = BeanMap.create(object);
            pendingMap.putAll(beanMap);
        } else {
            pendingMap.putAll((Map<? extends String, ?>) object);
        }
        analysisMap(pendingMap, properties1);
        return properties1;
    }

    /**
     * 解析map
     *
     * @param map    数据
     * @param result 返回结果集
     */
    private static void analysisMap(Map<String, Object> map, final Properties result) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            dataFormatProfile(key, value, result);
        }
    }

    /**
     * 数据格式化成 配置格式
     *
     * @param parentName  父配置名称
     * @param valueObject 数据
     * @param result      返回对象
     */
    private static void dataFormatProfile(String parentName, Object valueObject, Properties result) {
        if (valueObject instanceof Map) {
            doAnalysisMapValue(parentName, (Map<String, Object>) valueObject, result);
        } else {
            result.put(parentName, valueObject);
        }
    }

    /**
     * 循环解析 Map
     *
     * @param parentName 父配置名称
     * @param map        数据
     * @param result     返回对象
     */
    private static void doAnalysisMapValue(String parentName, Map<String, Object> map, Properties result) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            dataFormatProfile(parentName + "." + key, value, result);
        }
    }

    /**
     * 是否存索引
     *
     * @param key  索引
     * @param maps 集合
     * @return boolean
     */
    public static boolean isValid(String key, Map<?, ?>... maps) {
        for (Map<?, ?> map : maps) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取唯一值或者字段值
     *
     * @param source 元数据
     * @return Object
     */
    public static Object getIfOnly(final Map<String, Object> source) {
        return getIfOnly(source, Object.class);
    }

    /**
     * 获取唯一值或者字段值
     *
     * @param source     元数据
     * @param valueClass 数据类型
     * @param <T>
     * @return
     */
    public static <T> T getIfOnly(final Map<String, Object> source, final Class<T> valueClass) {
        if (isEmpty(source)) {
            return null;
        }
        Set<String> keySet = source.keySet();
        if (CollectionHelper.size(keySet) != 1) {
            return null;
        }
        Object o = source.get(FinderHelper.firstElement(keySet));
        if (null == valueClass) {
            return (T) o;
        }
        return null == o ? null : (valueClass.isAssignableFrom(o.getClass()) ? (T) o : null);
    }
}
