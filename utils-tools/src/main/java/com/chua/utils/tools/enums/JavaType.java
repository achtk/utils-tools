package com.chua.utils.tools.enums;

import com.chua.utils.tools.classes.ClassHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;

/**
 * JavaType -> JdbcType
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/5
 */
@Getter
@AllArgsConstructor
public enum JavaType {
    /**
     * CHAR
     */
    CHAR("CHAR", String.class.getName()),
    /**
     * VARCHAR
     */
    VARCHAR("VARCHAR", String.class.getName()),
    /**
     * LONGVARCHAR
     */
    LONGVARCHAR("LONGVARCHAR", String.class.getName()),
    /**
     * NUMERIC
     */
    NUMERIC("NUMERIC", BigDecimal.class.getName()),
    /**
     * DECIMAL
     */
    DECIMAL("DECIMAL", BigDecimal.class.getName()),
    /**
     * BIT
     */
    BIT("BIT", Boolean.class.getName()),
    /**
     * BOOLEAN
     */
    BOOLEAN("BOOLEAN", Boolean.class.getName()),
    /**
     * TINYINT
     */
    TINYINT("TINYINT", Byte.class.getName()),
    /**
     * SMALLINT
     */
    SMALLINT("SMALLINT", Short.class.getName()),
    /**
     * INTEGER
     */
    INTEGER("INTEGER", Integer.class.getName()),
    /**
     * BIGINT
     */
    BIGINT("BIGINT", Long.class.getName()),
    /**
     * REAL
     */
    REAL("REAL", Float.class.getName()),
    /**
     * FLOAT
     */
    FLOAT("FLOAT", Double.class.getName()),
    /**
     * DOUBLE
     */
    DOUBLE("DOUBLE", Double.class.getName()),
    /**
     * BINARY
     */
    BINARY("BINARY", Byte[].class.getName()),
    /**
     * VARBINARY
     */
    VARBINARY("VARBINARY", Byte[].class.getName()),
    /**
     * LONGVARBINARY
     */
    LONGVARBINARY("LONGVARBINARY", Byte[].class.getName()),
    /**
     * DATE
     */
    DATE("DATE", Date.class.getName()),
    /**
     * TIME
     */
    TIME("TIME", Time.class.getName()),
    /**
     * TIMESTAMP
     */
    TIMESTAMP("TIMESTAMP", Timestamp.class.getName()),
    /**
     * CLOB
     */
    CLOB("CLOB", Clob.class.getName()),
    /**
     * BLOB
     */
    BLOB("BLOB", Blob.class.getName()),
    /**
     * ARRAY
     */
    ARRAY("ARRAY", Array.class.getName()),
    /**
     * DISTINCT
     */
    DISTINCT("DISTINCT", "mapping of underlying type"),
    /**
     * STRUCT
     */
    STRUCT("STRUCT", Struct.class.getName()),
    /**
     * REF
     */
    REF("REF", Ref.class.getName()),
    /**
     * DATALINK
     */
    DATALINK("DATALINK", URL.class.getName()),
    /**
     * ANY
     */
    ANY("ANY", URL.class.getName());

    private static final JavaType DEFAULT = ANY;
    private final String jdbcType;
    private final String javaType;

    /**
     * jdbcType -> javaType
     *
     * @param jdbcType jdbcType
     * @return javaType
     */
    public static String toJavaType(String jdbcType) {
        JavaType javaType = Arrays.stream(values()).filter(javaType1 -> javaType1.getJdbcType().equals(jdbcType)).findFirst().get();
        return null == javaType ? "" : javaType.getJdbcType();
    }


    /**
     * javaType -> jdbcType
     *
     * @param javaTypeStr jdbcType
     * @return jdbcType
     */
    public static String toJdbcType(String javaTypeStr) {
        return Arrays.stream(values()).filter(javaType1 -> javaType1.getJavaType().equals(javaTypeStr)).findFirst().orElse(DEFAULT).getJdbcType();
    }

    /**
     * javaType -> jdbcType
     *
     * @param javaTypeStr jdbcType
     * @return jdbcType
     */
    public static String toJdbcType(Object javaTypeStr) {
        if (null == javaTypeStr) {
            return DEFAULT.getJdbcType();
        }
        return Arrays.stream(values()).filter(javaType1 -> javaType1.getJavaType().equals(ClassHelper.getClass(javaTypeStr))).findFirst().orElse(DEFAULT).getJdbcType();
    }

}
