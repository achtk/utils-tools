package com.chua.utils.tools.data.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.sql.DataSource;

/**
 * 数据数据工厂
 * @author CH
 * @version 1.0.0
 * @since 2020/12/7
 */
@Data
@AllArgsConstructor
public class DataDataSource {
    /**
     * 实例
     */
    public static DataSource INSTANCE;
    /**
     * 数据源
     */
    public String dataSourceClass;

    public DataDataSource(DataSource dataSource) {
        this.dataSourceClass = DataDataSource.class.getName();
        INSTANCE = dataSource;
    }
}
