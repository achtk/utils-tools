package com.chua.utils.netx.datasource.h2.mem;

import com.chua.utils.netx.datasource.dialect.SQLDialectEnum;
import com.chua.utils.netx.datasource.dialect.SqlDialect;
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
public class H2Search<T> extends AbstractMemSearch<T> {

    private static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    public static final String DRIVER_URL = "org.h2.Driver";

    @Override
    protected DataSourceProperties dataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setDriverClassName(DRIVER_URL);
        dataSourceProperties.setJdbcUrl(URL);
        dataSourceProperties.setUsername("sa");
        return dataSourceProperties;
    }

}
