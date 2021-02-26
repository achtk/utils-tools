package com.chua.utils.netx.flink.table;

import com.chua.utils.tools.table.Table;
import com.chua.utils.tools.util.ObjectUtils;
import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.Map;

/**
 * table of redis
 *
 * @author CH* @version 1.0.0* @since 2021/1/25
 */
public class RedisTable implements Table {

    private String host = "127.0.0.1:6379";
    private String password;
    private int database;
    private int maxIdle;
    private int minIdle;
    private String index;
    private String tableName;
    private Map<String, String> columns = new HashMap<>();
    private int maxTotal;

    @Override
    public String create() {
        String column = Joiner.on(",\n").withKeyValueSeparator(" ").join(columns);
        String format = "CREATE TABLE %s ( %s) WITH (\"connector.type\" = \"redis\",\"redis.host\" = \"${host}\", \"redis.maxIdle\" = \"${maxIdle}\",\"redis.minIdle\" = \"${minIdle}\",\"redis.database\" = \"${database}\",\"redis.password\" = \"${password}\",\"redis.maxTotal\" = \"${maxTotal}\",\"redis.index\" = \"${index}\",\"schema.table\" = \"${tableName}\")";
        return String.format(format, tableName, column, host, maxIdle, minIdle, database, password, maxTotal, index, tableName);
    }

    static class Builder implements Table.Builder<RedisTable, String> {

        private RedisTable redisTable = new RedisTable();

        /**
         * 设置host
         *
         * @param host host
         * @return this;
         */
        public Builder host(String host) {
            redisTable.host = ObjectUtils.ifEmpty(host, "127.0.0.1:6379");
            return this;
        }

        /**
         * 设置 password
         *
         * @param password password
         * @return this;
         */
        public Builder password(String password) {
            redisTable.password = password;
            return this;
        }

        /**
         * 设置 database
         *
         * @param database database
         * @return this;
         */
        public Builder database(int database) {
            redisTable.database = ObjectUtils.ifEmpty(database, 0);
            return this;
        }

        /**
         * 设置 maxTotal
         *
         * @param maxTotal maxTotal
         * @return this;
         */
        public Builder maxTotal(int maxTotal) {
            redisTable.maxTotal = ObjectUtils.ifEmpty(maxTotal, 8);
            return this;
        }

        /**
         * 设置 maxIdle
         *
         * @param maxIdle maxIdle
         * @return this;
         */
        public Builder maxIdle(int maxIdle) {
            redisTable.maxIdle = ObjectUtils.ifEmpty(maxIdle, 8);
            return this;
        }

        /**
         * 设置 index
         *
         * @param index index
         * @return this;
         */
        @Override
        public Builder source(String index) {
            redisTable.index = index;
            return this;
        }

        /**
         * 设置 minIdle
         *
         * @param minIdle minIdle
         * @return this;
         */
        public Builder minIdle(int minIdle) {
            redisTable.minIdle = ObjectUtils.ifEmpty(minIdle, 1);
            return this;
        }

        /**
         * 构建
         *
         * @return
         */
        @Override
        public RedisTable build() {
            return redisTable;
        }

        @Override
        public Builder table(String name) {
            redisTable.tableName = name;
            return this;
        }

        @Override
        public Table.Builder column(String columnName, String columnType) {
            redisTable.columns.put(columnName, columnType);
            return this;
        }

    }
}
