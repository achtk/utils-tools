package com.chua.utils.tools.function;

import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.function.converter.Converter;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.google.common.collect.Lists;

import java.text.NumberFormat;
import java.util.*;
import java.util.function.Predicate;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_COMMA;

/**
 * Map可操作
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
@FunctionalInterface
public interface MapOperable<K> extends Operable<K, Map<K, Object>> {
    /**
     * 获取Map
     *
     * @return map
     */
    Map<K, Object> getMap();

    /**
     * 获取 Map
     *
     * @param source 输入
     * @return Map
     */
    @Override
    default Map<K, Object> operate(K source) {
        return getMap();
    }

    /**
     * 获取Boolean
     *
     * @param key 查找的关键
     * @return Boolean
     */
    default Boolean getBoolean(final K key) {
        return MapOperableHelper.getBoolean(getMap(), key);
    }

    /**
     * 获取Boolean
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return Boolean
     */
    default Boolean getBoolean(final K key, final Boolean defaultValue) {
        return MapOperableHelper.getBoolean(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> 0 </ code>
     */
    default byte getByteValue(final K key) {
        return MapOperableHelper.getByteValue(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> 0 </ code>
     */
    default byte getByteValue(final K key, final byte defaultValue) {
        return MapOperableHelper.getByteValue(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> null </ code>
     */
    default Byte getByte(final K key) {
        return MapOperableHelper.getByte(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> null </ code>
     */
    default Byte getByte(final K key, final Byte defaultValue) {
        return MapOperableHelper.getByte(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getByte（Object）}的结果中获得的。
     *
     * @param keys         查找的关键
     * @param defaultValue 默认值
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> null </ code>
     */
    default Byte getByte(final List<K> keys, final Byte defaultValue) {
        for (K key : keys) {
            Byte aByte = getByte(key);
            if (null != aByte) {
                return aByte;
            }
        }
        return defaultValue;
    }

    /**
     * 以null安全的方式从Map获取一个字节。
     * <p> 字节是从{@link #getByte（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param key2         查找的关键
     * @param defaultValue 默认值
     * @return 将Map中的值作为字节返回，如果输入的Map为空，则为<code> 默认值 </ code>
     */
    default Byte getByte(final K key, final K key2, final Byte defaultValue) {
        return getByte(Lists.newArrayList(key, key2), defaultValue);
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则以Short<code> 0 </ code>返回Map中的值
     */
    default short getShortValue(final K key) {
        return MapOperableHelper.getShortValue(getMap(), key);
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则以Short<code> 0 </ code>返回Map中的值
     */
    default short getShortValue(final K key, final short defaultValue) {
        return MapOperableHelper.getShortValue(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则以Short<code> null </ code>返回Map中的值
     */
    default Short getShort(final K key) {
        return MapOperableHelper.getShort(getMap(), key);
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则以Short<code> null </ code>返回Map中的值
     */
    default Short getShort(final K key, final Short defaultValue) {
        return MapOperableHelper.getShort(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param keys         查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则以Short<code> null </ code>返回Map中的值
     */
    default Short getShort(final List<K> keys, final Short defaultValue) {
        for (K key : keys) {
            Short aShort = getShort(key);
            if (null != aShort) {
                return aShort;
            }
        }
        return defaultValue;
    }

    /**
     * 以null安全的方式从Map中获取一个Short。
     * <p> Short是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param key2         查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则以Short<code> 默认值 </ code>返回Map中的值
     */
    default Short getShort(final K key, final K key2, final Short defaultValue) {
        return getShort(Lists.newArrayList(key, key2), defaultValue);
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getInteger（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> 0 </ code>
     */
    default int getIntValue(final K key) {
        return MapOperableHelper.getIntValue(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getInteger（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> 0 </ code>
     */
    default int getIntValue(final K key, final int defaultValue) {
        return MapOperableHelper.getIntValue(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getInteger（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> null </ code>
     */
    default Integer getInteger(final K key) {
        return MapOperableHelper.getInteger(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getInteger（Object）}的结果中获得的。
     *
     * @param keys         查找的关键
     * @param defaultValue 默认值
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> defaultValue </ code>
     */
    default Integer getInteger(final List<K> keys, final Integer defaultValue) {
        for (K key : keys) {
            Integer integer = getInteger(key);
            if (null != integer) {
                return integer;
            }
        }
        return defaultValue;
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getInteger（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param key2         查找的关键
     * @param defaultValue 默认值
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> defaultValue </ code>
     */
    default Integer getInteger(final K key, final K key2, final Integer defaultValue) {
        return getInteger(Lists.newArrayList(key, key2), defaultValue);
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getInteger（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> null </ code>
     */
    default Integer getInteger(final K key, final Integer defaultValue) {
        return MapOperableHelper.getInteger(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getLong（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> 0 </ code>
     */
    default long getLongValue(final K key) {
        return MapOperableHelper.getLongValue(getMap(), key);
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getLong（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> 0 </ code>
     */
    default long getLongValue(final K key, final long defaultValue) {
        return MapOperableHelper.getLongValue(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getLong（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> null </ code>
     */
    default Long getLong(final K key) {
        return MapOperableHelper.getLong(getMap(), key);
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getLong（Object）}的结果中获得的。
     *
     * @param keys         查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> 默认值 </ code>
     */
    default Long getLong(final List<K> keys, final Long defaultValue) {
        for (K key : keys) {
            Long aLong = getLong(key);
            if (aLong != null) {
                return aLong;
            }
        }
        return defaultValue;
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getLong（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param key2         查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> 默认值 </ code>
     */
    default Long getLong(final K key, final K key2, final Long defaultValue) {
        return getLong(Lists.newArrayList(key, key2), defaultValue);
    }

    /**
     * 以null安全的方式从Map中获取Date。
     * <p> Long是从{@link #getDate（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为Date，<code> null </ code>
     */
    default Date getDate(final K key) {
        return MapOperableHelper.getDate(getMap(), key);
    }

    /**
     * 以null安全的方式从Map中获取Date。
     * <p> Long是从{@link #getDate（Object）}的结果中获得的。
     *
     * @param key    查找的关键
     * @param format 日期格式
     * @return 如果输入的Map为空，则将Map中的值返回为Date，<code> null </ code>
     */
    default String getDate(final K key, final String format) {
        return dateFormatter(key, format);
    }

    /**
     * 以null安全的方式从Map中格式化时间。
     * <p> 时间是从{@link #getObject（Object）}的结果中获得的。
     *
     * @param key    查找的关键
     * @param format 格式
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> null </ code>
     */
    default String dateFormatter(final K key, final String format) {
        return MapOperableHelper.dateFormatter(getMap(), key, format);
    }

    /**
     * 以null安全的方式从Map中分隔数据。
     * <p> 数据从{@link #getObject（Object）}的结果中获得的。
     *
     * @param key   查找的关键
     * @param regex 分隔符
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> null </ code>
     */
    default String[] splitToArray(final K key, final String regex) {
        Object object = getObject(key);
        if (null == object) {
            return null;
        }

        if (object instanceof String) {
            return ((String) object).split(regex);
        }

        return null;
    }

    /**
     * 以null安全的方式从Map中分隔数据。
     * <p> 数据从{@link #splitToArray}的结果中获得的。
     * <p>默认以,分隔</p>
     *
     * @param key    查找的关键
     * @param <T>    类型
     * @param tClass 基础原型封装类
     * @return 如果输入的Map为空，则将Map中的值返回为 List，<code> null </ code>
     */
    default <T> List<T> splitToList(final K key, final Class<T> tClass) {
        return splitToList(key, SYMBOL_COMMA, tClass);
    }

    /**
     * 以null安全的方式从Map中分隔数据。
     * <p> 数据从{@link #splitToArray}的结果中获得的。
     *
     * @param key    查找的关键
     * @param regex  分隔符
     * @param <T>    类型
     * @param tClass 基础原型封装类
     * @return 如果输入的Map为空，则将Map中的值返回为 List，<code> null </ code>
     */
    default <T> List<T> splitToList(final K key, final String regex, final Class<T> tClass) {
        String[] strings = splitToArray(key, regex);

        if (null == strings) {
            return null;
        }
        TypeConverter<T> typeConverter = Converter.getTypeConverter(tClass);
        List<T> result = new ArrayList<>(strings.length);

        for (String string : strings) {
            T convert = typeConverter.convert(string);
            if (null == convert) {
                continue;
            }

            result.add(convert);
        }
        return result;
    }

    /**
     * 以null安全的方式从Map中分隔数据。
     * <p> 数据从{@link #splitToArray}的结果中获得的。
     *
     * @param key   查找的关键
     * @param regex 分隔符
     * @return 如果输入的Map为空，则将Map中的值返回为 List，<code> null </ code>
     */
    default List<String> splitToList(final K key, final String regex) {
        String[] strings = splitToArray(key, regex);
        if (null == strings) {
            return null;
        }
        return Arrays.asList(strings.clone());
    }

    /**
     * 以null安全的方式从Map中分隔数据。
     * <p> 数据从{@link #splitToArray}的结果中获得的。
     * <p>默认以,分隔</p>
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为 List，<code> null </ code>
     */
    default List<String> splitToList(final K key) {
        return splitToList(key, SYMBOL_COMMA);
    }

    /**
     * 以null安全的方式从Map中分隔数据。
     * <p> 数据从{@link #splitToArray}的结果中获得的。
     *
     * @param key   查找的关键
     * @param regex 分隔符
     * @return 如果输入的Map为空，则将Map中的值返回为 Set，<code> null </ code>
     */
    default Set<String> splitToSet(final K key, final String regex) {
        List<String> strings = splitToList(key, regex);
        if (null == strings) {
            return null;
        }
        return new HashSet<>(strings);
    }

    /**
     * 以null安全的方式从Map中分隔数据。
     * <p> 数据从{@link #splitToArray}的结果中获得的。
     * <p>默认以,分隔</p>
     *
     * @param key    查找的关键
     * @param <T>    类型
     * @param tClass 基础原型封装类
     * @return 如果输入的Map为空，则将Map中的值返回为 List，<code> null </ code>
     */
    default <T> Set<T> splitToSet(final K key, final Class<T> tClass) {
        return splitToSet(key, SYMBOL_COMMA, tClass);
    }

    /**
     * 以null安全的方式从Map中分隔数据。
     * <p> 数据从{@link #splitToArray}的结果中获得的。
     *
     * @param key    查找的关键
     * @param regex  分隔符
     * @param <T>    类型
     * @param tClass 基础原型封装类
     * @return 如果输入的Map为空，则将Map中的值返回为 Set，<code> null </ code>
     */
    default <T> Set<T> splitToSet(final K key, final String regex, final Class<T> tClass) {
        String[] strings = splitToArray(key, regex);
        if (null == strings) {
            return null;
        }
        TypeConverter<T> typeConverter = Converter.getTypeConverter(tClass);
        Set<T> result = new HashSet<>(strings.length);
        for (String string : strings) {
            T convert = typeConverter.convert(string);
            if (null == convert) {
                continue;
            }

            result.add(convert);
        }
        return result;
    }

    /**
     * 以null安全的方式从Map中分隔数据。
     * <p> 数据从{@link #splitToArray}的结果中获得的。
     * <p>默认以,分隔</p>
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为 Set，<code> null </ code>
     */
    default Set<String> splitToSet(final K key) {
        return splitToSet(key, SYMBOL_COMMA);
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> null </ code>
     */
    default Long getLong(final K key, final Long defaultValue) {
        return MapOperableHelper.getLong(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取Float。
     * <p> 浮点数是根据{@link #getNumber（Object）}的结果获得的。
     *
     * @param key 查找的关键
     * @return 以浮点数形式返回Map中的值，如果输入的Map为空，则为<code> 0 </ code>
     */
    default float getFloatValue(final K key) {
        return MapOperableHelper.getFloatValue(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取Float。
     * <p> 浮点数是根据{@link #getNumber（Object）}的结果获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 以浮点数形式返回Map中的值，如果输入的Map为空，则为<code> 0 </ code>
     */
    default float getFloatValue(final K key, final float defaultValue) {
        return MapOperableHelper.getFloatValue(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取Float。
     * <p> 浮点数是根据{@link #getNumber（Object）}的结果获得的。
     *
     * @param key 查找的关键
     * @return 以浮点数形式返回Map中的值，如果输入的Map为空，则为<code> null </ code>
     */
    default Float getFloat(final K key) {
        return MapOperableHelper.getFloat(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取Float。
     * <p> 浮点数是根据{@link #getNumber（Object）}的结果获得的。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 以浮点数形式返回Map中的值，如果输入的Map为空，则为<code> null </ code>
     */
    default Float getFloat(final K key, final Float defaultValue) {
        return MapOperableHelper.getFloat(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则Map中的值为Double，<code> 0 </ code>
     */
    default double getDoubleValue(final K key) {
        return MapOperableHelper.getDoubleValue(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则Map中的值为Double，<code> 0 </ code>
     */
    default double getDoubleValue(final K key, final double defaultValue) {
        return MapOperableHelper.getDoubleValue(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param keys         查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则Map中的值为Double，<code> 0 </ code>
     */
    default double getDoubleValue(final List<K> keys, final double defaultValue) {
        for (K key : keys) {
            Double aDouble = getDouble(key);
            if (null != aDouble) {
                return aDouble;
            }
        }
        return defaultValue;
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param key          查找的关键
     * @param key2         查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则Map中的值为Double，<code> 0 </ code>
     */
    default double getDoubleValue(final K key, final K key2, final double defaultValue) {
        return getDoubleValue(Lists.newArrayList(key, key2), defaultValue);
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则Map中的值为Double，<code> null </ code>
     */
    default Double getDouble(final K key) {
        return MapOperableHelper.getDouble(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param key          查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则Map中的值为Double，<code> null </ code>
     */
    default Double getDouble(final K key, final Double defaultValue) {
        return MapOperableHelper.getDouble(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param keys         查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则Map中的值为Double，<code> null </ code>
     */
    default Double getDouble(final List<K> keys, final Double defaultValue) {
        for (K key : keys) {
            Double aDouble = getDouble(key);
            if (null != aDouble) {
                return aDouble;
            }
        }
        return defaultValue;
    }

    /**
     * 以null安全的方式从Map获取Double。
     * <p> 从{@link #getNumber（Object）}的结果中获得Double。
     *
     * @param key          查找的关键
     * @param key2         查找的关键
     * @param defaultValue 默认值
     * @return 如果输入的Map为空，则Map中的值为Double，<code> null </ code>
     */
    default Double getDouble(final K key, final K key2, final Double defaultValue) {
        return getDouble(Lists.newArrayList(key, key2), defaultValue);
    }

    /**
     * 在给定映射中查找给定键，如果转换失败，则使用默认值将结果转换为。
     *
     * @param key          在Map中查找的值的关键
     * @param defaultValue 如果值为null或转换失败，将返回defaultValue
     * @return 映射中的值为数字的值；如果
     * 原始值为null，映射为null或数字转换
     * 失败，则为defaultValue
     */
    default Number getNumber(K key, Number defaultValue) {
        return MapOperableHelper.getNumber(getMap(), key, defaultValue);
    }

    /**
     * 以null安全的方式从Map获取数字。
     * <p> 如果该值为<code> Number </ code>，则直接返回。
     * 如果值是<code> String </ code>，则使用系统默认格式程序上的
     * {@link NumberFormat＃parse（String）}进行转换
     * 如果转换失败，则返回<code> null </ code>。
     * 否则，返回<code> null </ code>。
     *
     * @param key 查找的关键
     * @return Map中的值作为Number，如果Map输入为空，则为<code> null </ code>
     */
    default Number getNumber(final K key) {
        return MapOperableHelper.getNumber(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取数字。
     * <p> 如果该值为<code> Number </ code>，则直接返回。
     * 如果值是<code> String </ code>，则使用系统默认格式程序上的
     * {@link NumberFormat＃parse（String）}进行转换
     * 如果转换失败，则返回<code> null </ code>。
     * 否则，返回<code> null </ code>。
     *
     * @param keys 查找的关键
     * @return Map中的值作为Number，如果Map输入为空，则为<code> null </ code>
     */
    default Number getNumber(final List<K> keys) {
        for (K key : keys) {
            Number number = getNumber(key);
            if (null != number) {
                return number;
            }
        }
        return null;
    }

    /**
     * 以null安全的方式从Map获取Map。
     * <p> *如果从指定映射返回的值不是Map，则返回
     * <code> null </ code>。
     *
     * @param key 查找的关键
     * @return Map中作为Map的值，如果为null，则<code> null </ code>
     */
    default Map getMap(final K key) {
        return MapOperableHelper.getMap(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取字符串。
     * <p>字符串是通过<code> toString </ code>获得的。
     *
     * @param key 查找的关键
     * @return Map中的值作为字符串，如果为null，则<code> null </ code>
     */
    default String getString(final K key) {
        return MapOperableHelper.getString(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取字符串。
     * <p>字符串是通过<code> toString </ code>获得的。
     *
     * @param keys         查找的关键
     * @param defaultValue 默认值
     * @return Map中的值作为字符串，如果为null，则<code> null </ code>
     */
    default String getString(final List<K> keys, final String defaultValue) {
        for (K key : keys) {
            String string = getString(key);
            if (null != string) {
                return string;
            }
        }
        return defaultValue;
    }

    /**
     * 以null安全的方式从Map获取字符串。
     * <p>字符串是通过<code> toString </ code>获得的。
     *
     * @param key          查找的关键
     * @param key2         查找的关键
     * @param defaultValue 默认值
     * @return Map中的值作为字符串，如果为null，则<code> null </ code>
     */
    default String getString(final K key, K key2, final String defaultValue) {
        List<K> list = Lists.newArrayList(key, key2);
        return getString(list, defaultValue);
    }

    /**
     * 在给定映射中查找给定键，如果转换失败，则使用默认值将结果转换为字符串。
     *
     * @param key          在该Map中查找的值的关键
     * @param defaultValue 如果该值为null或转换失败，将返回{defaultValue}
     * @return 映射中的值作为字符串；如果原始值为null，映射为null或字符串转换，则为defaultValue
     */
    default String getString(K key, String defaultValue) {
        return MapOperableHelper.getString(getMap(), key, defaultValue);
    }

    /**
     * 获取 Object
     *
     * @param key 索引
     * @return Map中的值，如果Map输入为空，则为<code> null </ code>
     */
    default Object getObject(K key) {
        return MapOperableHelper.getObject(getMap(), key);
    }

    /**
     * 获取 Object
     *
     * @param key          索引
     * @param defaultValue 默认值
     * @return Map中的值，如果Map输入为空，则为<code> null </ code>
     */
    default Object getObject(final K key, final Object defaultValue) {
        return MapOperableHelper.getObject(getMap(), key, defaultValue);
    }

    /**
     * 获取枚举类型
     *
     * @param key      索引
     * @param enumType 枚举类型
     * @param <E>      枚举
     * @return 枚举
     */
    default <E> E getEnum(K key, Class<E> enumType) {
        if (null == key || null == enumType) {
            return null;
        }
        Object object = getObject(key);
        if (null == object) {
            return null;
        }

        if (enumType.isAssignableFrom(object.getClass())) {
            return (E) object;
        }

        if (object instanceof String) {
            Optional<E> any = Arrays.stream(enumType.getEnumConstants()).filter(new Predicate<E>() {
                @Override
                public boolean test(E e) {
                    return e.toString().toUpperCase().equals(object.toString().toUpperCase());
                }
            }).findAny();
            return any.isPresent() ? null : any.get();
        }

        return null;
    }

    /**
     * 索引重命名
     *
     * @param key    索引
     * @param newKey 新索引
     * @return 新索引对应的值
     */
    default Object rename(K key, K newKey) {
        Map<K, Object> map = getMap();
        if (null == map) {
            return null;
        }
        if (null == key) {
            if (null == newKey) {
                return null;
            }
            return map.put(newKey, null);
        }

        Object o = map.get(key);
        return map.put(newKey, o);
    }
}
