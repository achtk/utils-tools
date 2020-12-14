package com.chua.utils.tools.data.table.wrapper;


/**
 * table-wrapper
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/12
 */
public final class TableWrapper {
    /**
     * 创建内存表
     *
     * @param name 表名
     * @return 表
     */
    public static MemDataTableWrapper createMemTable(String name) {
        return new MemDataTableWrapper(name);
    }

    /**
     * 创建druid适配器
     *
     * @return 表
     */
    public static DruidDataTableWrapper createDruidTable() {
        return new DruidDataTableWrapper();
    }

    /**
     * 创建文件表
     *
     * @param name 表名
     * @return 表
     */
    public static FileDataTableWrapper createFileTable(String name) {
        return new FileDataTableWrapper(name);
    }

    /**
     * 创建数据源表
     *
     * @return 表
     */
    public static DataSourceDataTableWrapper createDataSourceTable() {
        return new DataSourceDataTableWrapper(null);
    }

    /**
     * 创建数据源表
     *
     * @param name 表名
     * @return 表
     */
    public static DataSourceDataTableWrapper createDataSourceTable(String name) {
        return new DataSourceDataTableWrapper(name);
    }

    /**
     * 创建表
     *
     * @return 表
     */
    public static RedisDataTableWrapper creatRedisTable() {
        return new RedisDataTableWrapper();
    }

    /**
     * 创建表
     *
     * @return 表
     */
    public static MongoDataTableWrapper creatMongoTable() {
        return new MongoDataTableWrapper();
    }
}
