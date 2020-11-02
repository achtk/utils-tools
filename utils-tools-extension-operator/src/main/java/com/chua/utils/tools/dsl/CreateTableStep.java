package com.chua.utils.tools.dsl;

/**
 * 创建
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface CreateTableStep {
    /**
     * 创建表
     *
     * @param tableName 表
     * @return
     */
    CreateTableConditionStep table(String tableName);

    /**
     * 创建表
     *
     * @param aClass 类
     * @return
     */
    CreateTableConditionStep table(Class<?> aClass);

    /**
     * 创建表
     *
     * @param aClass 类
     * @return
     */
    CreateTableConditionStep tableJpa(Class<?> aClass);
}
