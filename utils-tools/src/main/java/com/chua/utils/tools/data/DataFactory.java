package com.chua.utils.tools.data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
public interface DataFactory {
    /**
     * 添加数据处理分析工厂
     *
     * @param dataSchema
     */
    void addSchema(DataSchema dataSchema);

    /**
     * 创建链接
     *
     * @return 链接
     */
    Connection createConnection() throws SQLException;
}
