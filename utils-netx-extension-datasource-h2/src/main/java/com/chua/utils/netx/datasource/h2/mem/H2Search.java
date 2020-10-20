package com.chua.utils.netx.datasource.h2.mem;

import com.chua.utils.netx.datasource.factory.DataSourceFactory;
import com.chua.utils.netx.datasource.info.TableInfo;
import com.chua.utils.netx.datasource.mem.AbstractMemSearch;
import com.chua.utils.netx.datasource.mem.MemSearch;
import com.chua.utils.netx.datasource.properties.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

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
        return dataSourceProperties;
    }

}
