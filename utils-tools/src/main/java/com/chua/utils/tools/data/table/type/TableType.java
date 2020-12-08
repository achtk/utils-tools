package com.chua.utils.tools.data.table.type;

/**
 * 表类型
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
public enum TableType {
    /**
     * 内存
     */
    MEM,
    /**
     * 文件
     */
    FILE,
    /**
     * 流
     */
    IO,
    /**
     * solr
     */
    SOLR,
    /**
     * redis
     */
    REDIS,
    /**
     * kafka
     */
    KAFKA,
    /**
     * mongo
     */
    MONGO,
    /**
     * dataSource
     */
    DATA_SOURCE;
}
