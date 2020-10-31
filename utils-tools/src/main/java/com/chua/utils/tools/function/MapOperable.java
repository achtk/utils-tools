package com.chua.utils.tools.function;

import com.chua.utils.tools.common.MapOperableHelper;

import java.text.NumberFormat;
import java.util.Map;

/**
 * Map可操作
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface MapOperable<K> extends Operable<K, Map<K, Object>> {
    /**
     * 获取Map
     *
     * @return map
     */
    Map<K, Object> getMap();

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
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> 0 </ code>
     */
    default int getIntValue(final K key) {
        return MapOperableHelper.getIntValue(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getNumber（Object）}的结果中获得的。
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
     * <p> 整数是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 以整数形式返回Map中的值，如果输入的Map为空，则为<code> null </ code>
     */
    default Integer getInteger(final K key) {
        return MapOperableHelper.getInteger(getMap(), key);
    }

    /**
     * 以null安全的方式从Map获取一个整数。
     * <p> 整数是从{@link #getNumber（Object）}的结果中获得的。
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
     * <p> Long是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> 0 </ code>
     */
    default long getLongValue(final K key) {
        return MapOperableHelper.getLongValue(getMap(), key);
    }

    /**
     * 以null安全的方式从Map中获取Long。
     * <p> Long是从{@link #getNumber（Object）}的结果中获得的。
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
     * <p> Long是从{@link #getNumber（Object）}的结果中获得的。
     *
     * @param key 查找的关键
     * @return 如果输入的Map为空，则将Map中的值返回为Long，<code> null </ code>
     */
    default Long getLong(final K key) {
        return MapOperableHelper.getLong(getMap(), key);
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
        return MapOperableHelper.getString(getMap(), key);
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
}
