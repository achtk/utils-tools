package com.chua.utils.tools.dsl;

import com.chua.utils.tools.dsl.condition.Column;

/**
 * 创建条件
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface CreateConditionStep {
    /**
     * 添加字段
     *
     * @param columnName 字段名
     * @param columnType 字段类型
     * @param length     字段长度
     * @param other      其它参数
     * @return CreateConditionStep
     */
    CreateConditionStep withColumn(String columnName, String columnType, int length, String other);

    /**
     * 添加字段
     *
     * @param columnName 字段名
     * @param columnType 字段类型
     * @param length     字段长度
     * @return CreateConditionStep
     */
    default CreateConditionStep withColumn(String columnName, String columnType, int length) {
        return withColumn(columnName, columnType, length, null);
    }

    /**
     * 添加字段
     *
     * @param columnName 字段名
     * @param columnType 字段类型
     * @return CreateConditionStep
     */
    default CreateConditionStep withColumn(String columnName, String columnType) {
        return withColumn(columnName, columnType, 0, null);
    }

    /**
     * 添加字段
     *
     * @param column 字段
     * @return CreateConditionStep
     */
    CreateConditionStep withColumn(Column column);

    /**
     * 后缀条件
     * @param other 条件
     * @return CreateConditionStep
     */
    CreateConditionStep withOther(String other);

    /**
     * 创建
     * @return
     */
    CreateStep create();
}
