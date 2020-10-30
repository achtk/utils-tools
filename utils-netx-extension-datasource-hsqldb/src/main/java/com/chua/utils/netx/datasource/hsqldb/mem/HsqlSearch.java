package com.chua.utils.netx.datasource.hsqldb.mem;

import com.chua.utils.netx.datasource.mem.AbstractMemSearch;
import com.chua.utils.netx.datasource.properties.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 内存查询
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/19 20:35
 */
@Slf4j
public class HsqlSearch<T> extends AbstractMemSearch<T> {

    private static final String URL = "jdbc:hsqldb:mem:.";
    public static final String DRIVER_URL = "org.hsqldb.jdbcDriver";

    @Override
    protected DataSourceProperties dataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setDriverClassName(DRIVER_URL);
        dataSourceProperties.setJdbcUrl(URL);
        return dataSourceProperties;
    }


}
