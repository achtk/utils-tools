package com.chua.utils.tools.expression;

/**
 * 表达式
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface Expression {
    /**
     * 设置环境
     *
     * @param key   索引
     * @param value 值
     * @throws Exception Exception
     */
    void set(String key, Object value) throws Exception;

    /**
     * 执行表达式
     *
     * @param expression 表达式
     * @return Object
     * @throws Exception Exception
     */
    Object eval(String expression) throws Exception;
}
