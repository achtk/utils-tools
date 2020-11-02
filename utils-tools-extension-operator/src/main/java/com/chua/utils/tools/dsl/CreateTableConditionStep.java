package com.chua.utils.tools.dsl;

import com.chua.utils.tools.dsl.condition.Column;
import com.chua.utils.tools.operator.enums.Case;

/**
 * 创建条件
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface CreateTableConditionStep {
    /**
     * 字段全部按照类型
     *
     * @param caseType 类型
     * @return
     */
    CreateTableConditionStep withCase(Case caseType);

    /**
     * 字段全部大写
     *
     * @return
     */
    default CreateTableConditionStep withCaseUpper() {
        return withCase(Case.UPPER);
    }

    /**
     * 字段全部小写
     *
     * @return
     */
    default CreateTableConditionStep withCaseLower() {
        return withCase(Case.LOWER);
    }

    /**
     * 添加字段
     *
     * @param columnName 字段名
     * @param columnType 字段类型
     * @param length     字段长度
     * @param other      其它参数
     * @return CreateConditionStep
     */
    CreateTableConditionStep withColumn(String columnName, String columnType, int length, String other);

    /**
     * 添加字段
     *
     * @param columnName 字段名
     * @param columnType 字段类型
     * @param length     字段长度
     * @return CreateConditionStep
     */
    default CreateTableConditionStep withColumn(String columnName, String columnType, int length) {
        return withColumn(columnName, columnType, length, null);
    }

    /**
     * 添加字段
     *
     * @param columnName 字段名
     * @param columnType 字段类型
     * @return CreateConditionStep
     */
    default CreateTableConditionStep withColumn(String columnName, String columnType) {
        return withColumn(columnName, columnType, 0, null);
    }

    /**
     * 添加字段
     *
     * @param column 字段
     * @return CreateConditionStep
     */
    CreateTableConditionStep withColumn(Column column);

    /**
     * 添加字段
     *
     * @param tClass 类
     * @return CreateConditionStep
     */
    CreateTableConditionStep withColumnJpa(Class<?> tClass);

    /**
     * 添加字段
     *
     * @param tClass 类
     * @return CreateConditionStep
     */
    CreateTableConditionStep withColumn(Class<?> tClass);

    /**
     * 后缀条件
     *
     * @param other 条件
     * @return CreateConditionStep
     */
    CreateTableConditionStep withOther(String other);

    /**
     * 创建
     *
     * @return
     */
    CreateTableStepSqlBuilder create();
}
