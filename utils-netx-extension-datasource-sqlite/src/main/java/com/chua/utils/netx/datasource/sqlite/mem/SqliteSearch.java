package com.chua.utils.netx.datasource.sqlite.mem;

import com.chua.utils.netx.datasource.dialect.SqlDialect;
import com.chua.utils.netx.datasource.mem.AbstractMemSearch;
import com.chua.utils.netx.datasource.properties.DataSourceProperties;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 内存查询
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/19 20:35
 */
@Slf4j
@NoArgsConstructor
public class SqliteSearch<T> extends AbstractMemSearch<T> {

    /**
     * jdbc:sqlite
     */
    private static final String URL = SqlDialect.SQLITE_MEMORY_URL;
    private static final String DRIVER_URL = SqlDialect.SQLITE_DRIVER;
    @Override
    protected DataSourceProperties dataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setDriverClassName(DRIVER_URL);
        dataSourceProperties.setJdbcUrl(URL);
        return dataSourceProperties;
    }
}
