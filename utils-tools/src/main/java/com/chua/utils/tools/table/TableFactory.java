package com.chua.utils.tools.table;


import java.util.List;

/**
 * Table Factory
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/26
 */
public interface TableFactory {
    /**
     * 注册表
     *
     * @param table 表
     * @return this
     */
    TableFactory register(Table table);

    /**
     * 显示表列表
     *
     * @return
     */
    String[] listTables();

    /**
     * 查询数据
     *
     * @param sql    sql
     * @param tClass 类型
     * @param <T>    类型
     * @return 数据
     * @throws Exception Exception
     */
    <T> List<T> sqlQuery(String sql, Class<T> tClass) throws Exception;

    /**
     * 更新数据
     *
     * @param sql sql
     * @return 长度
     * @throws Exception Exception
     */
    Integer sqlUpdate(String sql) throws Exception;
}
