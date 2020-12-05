package com.chua.utils.tools.data.schema;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.data.DataSchema;
import com.chua.utils.tools.data.DataSourceDataSchema;
import com.chua.utils.tools.data.table.DataTable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源数据表
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/5
 */
public class DataSourceSchema implements DataSourceDataSchema {

    private DataSource dataSource;

    public DataSourceSchema(DataSource dataSource) {
        this.dataSource(dataSource);
    }

    @Override
    public void dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public String schema() {
        try (Connection connection = dataSource.getConnection()){
            return BeanCopy.of(connection).asMap().getString("catalog");
        } catch (SQLException throwables) {
            return null;
        }
    }
}
