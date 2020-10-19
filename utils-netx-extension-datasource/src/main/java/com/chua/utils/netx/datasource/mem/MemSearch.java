package com.chua.utils.netx.datasource.mem;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 内存查询
 * @author CH
 * @version 1.0
 * @since 2020/10/19 20:33
 */
public interface MemSearch<T> {
    /**
     * 添加数据
     * @param data
     * @return
     */
    MemSearch addData(List<T> data) throws Exception, Throwable;

    /**
     * 查询数据
     * @param ddl
     * @return
     */
    List<Map<String, Object>> query(String ddl) throws SQLException;
    /**
     * 查询数据
     * @param ddl
     * @return
     */
    List<T> queryForObject(String ddl) throws SQLException;
}
