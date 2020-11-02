package com.chua.utils.tools.dsl.impl;

import com.chua.utils.tools.dsl.CreateTableConditionStep;
import com.chua.utils.tools.dsl.CreateTableStepSqlBuilder;
import com.chua.utils.tools.dsl.condition.Column;
import com.chua.utils.tools.operator.enums.Case;
import com.chua.utils.tools.operator.util.OperatorUtil;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.GeneratedValue;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
@RequiredArgsConstructor
public class SimpleCreateTableConditionStep implements CreateTableConditionStep {

    @NonNull
    private final String tableName;

    @Getter
    private final Set<Column> columns = new HashSet<>();
    @Getter
    private final Set<String> others = new HashSet<>();
    @Getter
    private Case caseType = Case.NONE;


    @Override
    public CreateTableConditionStep withCase(Case caseType) {
        this.caseType = caseType;
        return this;
    }

    @Override
    public CreateTableConditionStep withColumn(String columnName, String columnType, int length, String other) {
        Column column = new Column();
        column.name(columnName).length(length).type(columnType).other(other);
        columns.add(column);
        return this;
    }

    @Override
    public CreateTableConditionStep withColumn(Column column) {
        columns.add(column);
        return this;
    }

    @Override
    public CreateTableConditionStep withColumnJpa(Class<?> tClass) {
        if (null != tClass) {
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                String name = getColumnName(field, true);
                if (null == name) {
                    continue;
                }
                Column column = new Column();
                if (null != isPrimary(field)) {
                    column.other("NOT NULL AUTO_INCREMENT COMMENT '主键ID'");
                }

                String type = getColumnType(field);
                int length = getColumnLength(field, true);
                column.name(name).length(length).type(type);
                withColumn(column);
            }
        }
        return this;
    }

    /**
     * 是注解
     *
     * @param field
     * @return
     */
    private Object isPrimary(Field field) {
        String name = getColumnNameById(field);
        GeneratedValue generatedValue = field.getDeclaredAnnotation(GeneratedValue.class);
        return null != name ? generatedValue.strategy() : null;
    }

    @Override
    public CreateTableConditionStep withColumn(Class<?> tClass) {
        if (null != tClass) {
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                String name = getColumnName(field, false);
                if (null == name) {
                    continue;
                }
                Column column = new Column();
                if (null != isPrimary(field)) {
                    column.other("NOT NULL AUTO_INCREMENT COMMENT '主键ID'");
                }

                String type = getColumnType(field);
                int length = getColumnLength(field, false);
                column.name(name).length(length).type(type);
                withColumn(column);
            }
        }
        return this;
    }

    /**
     * 获取字段类型
     *
     * @param field 字段
     * @return 字段类型
     * @see javax.persistence.Column
     */
    private String getColumnType(Field field) {
        return OperatorUtil.getColumnType(field.getType());
    }

    /**
     * 获取字段长度
     *
     * @param field 字段
     * @param isJpa Column注解
     * @return 字段长度
     * @see javax.persistence.Column
     */
    private int getColumnLength(Field field, boolean isJpa) {
        if (!isJpa) {
            return 0;
        }

        javax.persistence.Column column = field.getDeclaredAnnotation(javax.persistence.Column.class);
        if (null == column) {
            return 0;
        }
        return column.length();
    }

    /**
     * 获取字段名称
     *
     * @param field 字段
     * @param isJpa Column注解
     * @return 字段名称
     * @see javax.persistence.Column
     */
    private String getColumnName(Field field, boolean isJpa) {
        String name = field.getName();
        if (!isJpa) {
            return OperatorUtil.humpToUnderline(name);
        }

        javax.persistence.Column column = field.getDeclaredAnnotation(javax.persistence.Column.class);
        if (null == column) {
            return getColumnNameById(field);
        }
        String name1 = column.name();
        return Strings.isNullOrEmpty(name1) ? OperatorUtil.humpToUnderline(name) : name1;
    }

    /**
     * 获取字段名称
     *
     * @param field 字段
     * @return 字段名称
     * @see javax.persistence.Column
     */
    private String getColumnNameById(Field field) {
        javax.persistence.Id column = field.getDeclaredAnnotation(javax.persistence.Id.class);
        return null != column ? OperatorUtil.humpToUnderline(field.getName()) : null;
    }

    @Override
    public CreateTableConditionStep withOther(String other) {
        others.add(other);
        return this;
    }

    @Override
    public CreateTableStepSqlBuilder create() {
        return new SimpleCreateTableStepSqlBuilder(tableName, this);
    }
}
