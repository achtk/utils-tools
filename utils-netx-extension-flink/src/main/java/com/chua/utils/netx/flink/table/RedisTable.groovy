package com.chua.utils.netx.flink.table

import com.chua.utils.tools.table.Table
import com.google.common.base.Joiner

/**
 * table of redis
 *
 * @author CH* @version 1.0.0* @since 2021/1/25
 */
class RedisTable implements Table {

    private String host = "127.0.0.1:6379"
    private String password
    private int database
    private int maxIdle
    private int minIdle
    private String index
    private String tableName
    private Map<String, String> columns = [:]
    private int maxTotal

    @Override
    String create() {
        def column = Joiner.on(",\n").withKeyValueSeparator(" ").join(columns)
        """
        CREATE TABLE ${tableName} (
          ${column}
        ) WITH (
          'connector.type' = 'redis',
           'redis.host' = '${host}',
           'redis.maxIdle' = '${maxIdle}',
           'redis.minIdle' = '${minIdle}',
           'redis.database' = '${database}',
           'redis.password' = '${password}',
           'redis.maxTotal' = '${maxTotal}',
           'redis.index' = '${index}',
           'schema.table' = '${tableName}'
        )
      """
    }

    static class Builder implements Table.Builder<RedisTable, String> {

        private RedisTable redisTable = new RedisTable()

        /**
         * 设置host
         *
         * @param host host
         * @this
         */
        Builder host(String host = "127.0.0.1:6379") {
            redisTable.host = host
            this
        }

        /**
         * 设置 password
         *
         * @param password password
         * @this
         */
        Builder password(String password) {
            redisTable.password = password
            this
        }

        /**
         * 设置 database
         *
         * @param database database
         * @this
         */
        Builder database(int database = 0) {
            redisTable.database = database
            this
        }
        /**
         * 设置 maxTotal
         *
         * @param maxTotal maxTotal
         * @this
         */
        Builder maxTotal(int maxTotal = 8) {
            redisTable.maxTotal = maxTotal
            this
        }
        /**
         * 设置 maxIdle
         *
         * @param maxIdle maxIdle
         * @this
         */
        Builder maxIdle(int maxIdle = 8) {
            redisTable.maxIdle = maxIdle
            this
        }

        /**
         * 设置 index
         *
         * @param index index
         * @this
         */
        @Override
        Builder source(String index) {
            redisTable.index = index
            this
        }
        /**
         * 设置 minIdle
         *
         * @param minIdle minIdle
         * @this
         */
        Builder minIdle(int minIdle = 1) {
            redisTable.minIdle = minIdle
            this
        }

        /**
         * 构建
         * @return
         */
        RedisTable build() {
            redisTable
        }

        @Override
        Builder table(String name) {
            redisTable.tableName = name
            this
        }

        @Override
        Table.Builder column(String columnName, String columnType) {
            redisTable.columns[columnName] = columnType
            this
        }

    }
}
