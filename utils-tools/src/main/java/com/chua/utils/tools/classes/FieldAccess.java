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
    int fieldIndex(String name);

    // Get primitive type fields
    //
    boolean getBooleanField(T obj, int fieldIndex);

    byte getByteField(T obj, int fieldIndex);

    char getCharField(T obj, int fieldIndex);

    double getDoubleField(T obj, int fieldIndex);

    float getFloatField(T obj, int fieldIndex);

    int getIntField(T obj, int fieldIndex);

    long getLongField(T obj, int fieldIndex);

    short getShortField(T obj, int fieldIndex);

    // Get boxed primitive type fields
    //
    Boolean getBoxedBooleanField(T obj, int fieldIndex);

    Byte getBoxedByteField(T obj, int fieldIndex);

    Character getBoxedCharField(T obj, int fieldIndex);

    Double getBoxedDoubleField(T obj, int fieldIndex);

    Float getBoxedFloatField(T obj, int fieldIndex);

    Integer getBoxedIntField(T obj, int fieldIndex);

    Long getBoxedLongField(T obj, int fieldIndex);

    Short getBoxedShortField(T obj, int fieldIndex);

    // Set primitive type fields
    //
    void setBooleanField(T obj, int fieldIndex, boolean x);

    void setByteField(T obj, int fieldIndex, byte x);

    void setCharField(T obj, int fieldIndex, char x);

    void setDoubleField(T obj, int fieldIndex, double x);

    void setFloatField(T obj, int fieldIndex, float x);

    void setIntField(T obj, int fieldIndex, int x);

    void setLongField(T obj, int fieldIndex, long x);

    void setShortField(T obj, int fieldIndex, short x);

    // Set primitive wrapper type fields
    //
    void setBoxedBooleanField(T obj, int fieldIndex, Boolean x);

    void setBoxedByteField(T obj, int fieldIndex, Byte x);

    void setBoxedCharField(T obj, int fieldIndex, Character x);

    void setBoxedDoubleField(T obj, int fieldIndex, Double x);

    void setBoxedFloatField(T obj, int fieldIndex, Float x);

    void setBoxedIntField(T obj, int fieldIndex, Integer x);

    void setBoxedLongField(T obj, int fieldIndex, Long x);

    void setBoxedShortField(T obj, int fieldIndex, Short x);

    // Common reference types
    //
    BigDecimal getBigDecimalField(T obj, int fieldIndex);

    void setBigDecimalField(T obj, int fieldIndex, BigDecimal x);

    Date getDateField(T obj, int fieldIndex);

    void setDateField(T obj, int fieldIndex, Date x);

    LocalDate getLocalDateField(T obj, int fieldIndex);

    void setLocalDateField(T obj, int fieldIndex, LocalDate x);

    LocalDateTime getLocalDateTimeField(T obj, int fieldIndex);

    void setLocalDateTimeField(T obj, int fieldIndex, LocalDateTime x);

    String getStringField(T obj, int fieldIndex);

    default String getStringField(T obj, String field) {
        return getStringField(obj, fieldIndex(field));
    }

    void setStringField(T obj, int fieldIndex, String x);

    default void setStringField(T obj, String field) {
        setStringField(obj, fieldIndex(field), field);
    }

    // For all fields
    //
    Object getField(T obj, int fieldIndex);

    void setField(T obj, int fieldIndex, Object x);
}
