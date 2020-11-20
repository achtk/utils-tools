package com.chua.utils.netx.datasource.mem;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 内存查询
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/19 20:33
 */
public interface MemSearch<T> {
    /**
     * 创建内存表
     *
     * @param tClass 类型
     * @throws Exception
     */
    void create(Class<T> tClass) throws Exception;

    /**
     * 删除内存表
     *
     * @throws Exception
     */
    void remove() throws Exception;

    /**
     * 添加数据
     *
     * @param data 数据
     * @return MemSearch
     * @throws Exception Exception
     */
    MemSearch addData(List<T> data) throws Exception;

    /**
     * 查询数据
     *
     * @param ddl ddl
     * @return List
     * @throws Exception Exception
     */
    List<Map<String, Object>> query(String ddl) throws Exception;

    /**
     * 查询数据
     *
     * @param ddl ddl
     * @return List
     * @throws Exception Exception
     */
    List<T> queryForObject(String ddl) throws Exception;
}
