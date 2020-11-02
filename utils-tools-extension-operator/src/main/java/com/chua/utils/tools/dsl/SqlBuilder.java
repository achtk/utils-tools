package com.chua.utils.tools.dsl;

/**
 * 获取sql接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public interface SqlBuilder {

    /**
     * 获取表达式
     *
     * @return 表达式
     */
    String toSql();
}
