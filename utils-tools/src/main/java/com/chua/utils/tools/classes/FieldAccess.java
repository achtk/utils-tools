package com.chua.utils.tools.classes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 字段
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/30
 */
public interface FieldAccess<T> {
    /**
     * 字段索引
     *
     * @param name 名称
     * @return 索引
     */
    int fieldIndex(String name);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    boolean getBooleanField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    byte getByteField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    char getCharField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    double getDoubleField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    float getFloatField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    int getIntField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    long getLongField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    short getShortField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Boolean getBoxedBooleanField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Byte getBoxedByteField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Character getBoxedCharField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Double getBoxedDoubleField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Float getBoxedFloatField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Integer getBoxedIntField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Long getBoxedLongField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Short getBoxedShortField(T obj, int fieldIndex);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBooleanField(T obj, int fieldIndex, boolean x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setByteField(T obj, int fieldIndex, byte x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setCharField(T obj, int fieldIndex, char x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setDoubleField(T obj, int fieldIndex, double x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setFloatField(T obj, int fieldIndex, float x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setIntField(T obj, int fieldIndex, int x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setLongField(T obj, int fieldIndex, long x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setShortField(T obj, int fieldIndex, short x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBoxedBooleanField(T obj, int fieldIndex, Boolean x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBoxedByteField(T obj, int fieldIndex, Byte x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBoxedCharField(T obj, int fieldIndex, Character x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBoxedDoubleField(T obj, int fieldIndex, Double x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBoxedFloatField(T obj, int fieldIndex, Float x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBoxedIntField(T obj, int fieldIndex, Integer x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBoxedLongField(T obj, int fieldIndex, Long x);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBoxedShortField(T obj, int fieldIndex, Short x);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    BigDecimal getBigDecimalField(T obj, int fieldIndex);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setBigDecimalField(T obj, int fieldIndex, BigDecimal x);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Date getDateField(T obj, int fieldIndex);

    /**
     * 设置时间
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setDateField(T obj, int fieldIndex, Date x);

    /**
     * 获取值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    LocalDate getLocalDateField(T obj, int fieldIndex);

    /**
     * 设置时间
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setLocalDateField(T obj, int fieldIndex, LocalDate x);

    /**
     * 获取时间
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    LocalDateTime getLocalDateTimeField(T obj, int fieldIndex);

    /**
     * 设置时间
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setLocalDateTimeField(T obj, int fieldIndex, LocalDateTime x);

    /**
     * 获取字段名称
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 名称
     */
    String getStringField(T obj, int fieldIndex);

    /**
     * 获取值
     *
     * @param obj   对象
     * @param field 字段名称
     * @return 值
     */
    default String getStringField(T obj, String field) {
        return getStringField(obj, fieldIndex(field));
    }

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setStringField(T obj, int fieldIndex, String x);

    /**
     * 设置值
     *
     * @param obj   对象
     * @param field 字段名称
     */
    default void setStringField(T obj, String field) {
        setStringField(obj, fieldIndex(field), field);
    }

    /**
     * 获取字段值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @return 值
     */
    Object getField(T obj, int fieldIndex);

    /**
     * 设置值
     *
     * @param obj        对象
     * @param fieldIndex 字段索引
     * @param x          值
     */
    void setField(T obj, int fieldIndex, Object x);
}
