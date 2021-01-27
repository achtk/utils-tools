package com.chua.utils.tools.classes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 属性
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/30
 */
public interface PropertyAccess<T> {
    /**
     * 属性索引
     *
     * @param name 名称
     * @return 索引
     */
    int propertyIndex(String name);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    boolean getBooleanProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    byte getByteProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    char getCharProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    double getDoubleProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    float getFloatProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    int getIntProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    long getLongProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    short getShortProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Boolean getBoxedBooleanProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Byte getBoxedByteProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Character getBoxedCharProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Double getBoxedDoubleProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Float getBoxedFloatProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Integer getBoxedIntProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Long getBoxedLongProperty(T obj, int propertyIndex);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Short getBoxedShortProperty(T obj, int propertyIndex);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setBooleanProperty(T obj, int propertyIndex, boolean x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setByteProperty(T obj, int propertyIndex, byte x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setCharProperty(T obj, int propertyIndex, char x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setDoubleProperty(T obj, int propertyIndex, double x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setFloatProperty(T obj, int propertyIndex, float x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setIntProperty(T obj, int propertyIndex, int x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setLongProperty(T obj, int propertyIndex, long x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setShortProperty(T obj, int propertyIndex, short x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setBoxedBooleanProperty(T obj, int propertyIndex, Boolean x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setBoxedByteProperty(T obj, int propertyIndex, Byte x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setBoxedCharProperty(T obj, int propertyIndex, Character x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setBoxedDoubleProperty(T obj, int propertyIndex, Double x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setBoxedFloatProperty(T obj, int propertyIndex, Float x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setBoxedIntProperty(T obj, int propertyIndex, Integer x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setBoxedLongProperty(T obj, int propertyIndex, Long x);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setBoxedShortProperty(T obj, int propertyIndex, Short x);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    BigDecimal getBigDecimalProperty(T obj, int propertyIndex);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     * @return 值
     */
    void setBigDecimalProperty(T obj, int propertyIndex, BigDecimal x);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Date getDateProperty(T obj, int propertyIndex);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     * @return 值
     */
    void setDateProperty(T obj, int propertyIndex, Date x);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    LocalDate getLocalDateProperty(T obj, int propertyIndex);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setLocalDateProperty(T obj, int propertyIndex, LocalDate x);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    LocalDateTime getLocalDateTimeProperty(T obj, int propertyIndex);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setLocalDateTimeProperty(T obj, int propertyIndex, LocalDateTime x);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    String getStringProperty(T obj, int propertyIndex);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setStringProperty(T obj, int propertyIndex, String x);

    /**
     * 获取值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @return 值
     */
    Object getProperty(T obj, int propertyIndex);

    /**
     * 设置值
     *
     * @param obj           对象
     * @param propertyIndex 属性索引
     * @param x             值
     */
    void setProperty(T obj, int propertyIndex, Object x);
}
