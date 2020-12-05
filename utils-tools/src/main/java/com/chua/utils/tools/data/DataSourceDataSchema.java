package com.chua.utils.tools.data;

import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.data.table.DataTable;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * 数据库数据表
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/5
 */
public interface DataSourceDataSchema extends DataSchema {
    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     */
    void dataSource(DataSource dataSource);

    /**
     * 获取数据源
     * @return 数据源
     */
    DataSource dataSource();

    @Override
    default void initial(Object o) {

    }

    @Override
    default DataTable getTable() {
        return null;
    }

    @Override
    default String schema() {
        return null;
    }

    @Override
    default void schema(String schema) {

    }

    @Override
    default HashOperateMap operand() {
        return HashOperateMap.emptyMap();
    }
}
