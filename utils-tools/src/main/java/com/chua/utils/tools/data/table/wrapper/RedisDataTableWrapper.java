package com.chua.utils.tools.data.table.wrapper;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.type.TableType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 内存表修饰器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/12
 */
public class RedisDataTableWrapper {

    private static final String FACTORY = "org.apache.calcite.adapter.redis.RedisTableFactory";
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6379;
    private static final int DATABASE = 0;
    private static final String PASSWORD = "";
    private static final String RAW = "raw";
    private static final String CSV = "csv";
    private final TableType tableType = TableType.REDIS;

    /**
     * 构建数据
     *
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperTableBuilder source() {
        return source(HOST, PORT, DATABASE, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host 主机
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperTableBuilder source(String host) {
        return source(host, PORT, DATABASE, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host 主机
     * @param port 端口
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperTableBuilder source(String host, int port) {
        return source(host, port, DATABASE, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host     主机
     * @param port     端口
     * @param database 数据库
     * @param password 密码
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperTableBuilder source(String host, int port, int database, String password) {
        DataTable.DataTableBuilder builder = DataTable.builder();
        OperateHashMap operate = OperateHashMap.create();
        operate.put("host", host);
        operate.put("port", port);
        operate.put("database", database);
        operate.put("password", password);
        builder.operate(operate);
        builder.tableType(tableType);

        return new DataTableWrapperTableBuilder(builder.build());
    }

    /**
     * 构建标
     */
    @AllArgsConstructor
    public
    class DataTableWrapperTableBuilder {

        private final List<RedisTable> tables = new ArrayList<>();
        /**
         * 数据表
         */
        private final DataTable dataTable;

        /**
         * 创建表
         *
         * @param name         表名
         * @param dataFormat   数据格式
         * @param keyDelimiter 分隔符
         * @return this
         */
        public DataTableWrapperTableColumnBuilder createTable(final String name, final String dataFormat, final String keyDelimiter) {
            RedisTable redisTable = new RedisTable();
            redisTable.setName(name);

            RedisColumn redisColumn = new RedisColumn();
            redisColumn.setDataFormat(dataFormat);
            redisColumn.setKeyDelimiter(keyDelimiter);

            redisTable.setOperand(redisColumn);

            tables.add(redisTable);
            return new DataTableWrapperTableColumnBuilder(redisTable, this);
        }

        /**
         * 创建表
         *
         * @param name       表名
         * @param dataFormat 数据格式
         * @return this
         */
        public DataTableWrapperTableColumnBuilder createTable(final String name, final String dataFormat) {
            return createTable(name, dataFormat, ",");
        }

        /**
         * 创建表
         *
         * @param name 表名
         * @return this
         */
        public DataTableWrapperTableColumnBuilder createTable(final String name) {
            return createTable(name, CSV, ",");
        }

        /**
         * 构建
         *
         * @return
         */
        public DataTableWrapperBuilder build() {
            return () -> {
                dataTable.setOperate2(OperateHashMap.create("tables", tables));
                return dataTable;
            };
        }
    }

    /**
     * 构建标
     */
    @AllArgsConstructor
    public class DataTableWrapperTableColumnBuilder {

        private final RedisTable redisTable;
        private final DataTableWrapperTableBuilder dataTableWrapperTableBuilder;

        /**
         * 创建字段
         *
         * @param name   名称
         * @param type   类型
         * @param mapper mapper
         * @return this
         */
        public DataTableWrapperTableColumnBuilder createColumn(final String name, final String type, final int mapper) {
            RedisField redisField = new RedisField();
            redisField.setName(name);
            redisField.setType(type);
            redisField.setMapping(mapper);
            redisTable.getOperand().getFields().add(redisField);
            return this;
        }

        /**
         * 创建字段
         *
         * @param name 名称
         * @return this
         */
        public DataTableWrapperTableColumnBuilder createColumn(String name) {
            return createColumn(name, "varchar", 1);
        }

        /**
         * 创建字段
         *
         * @param names 名称
         * @return this
         */
        public DataTableWrapperTableColumnBuilder createColumn(String... names) {
            for (String name : names) {
                createColumn(name, "varchar", 1);
            }
            return this;
        }

        /**
         * 创建字段
         *
         * @param name 名称
         * @param type 类型
         * @return this
         */
        public DataTableWrapperTableColumnBuilder createColumn(final String name, final String type) {
            return createColumn(name, type, 1);
        }

        /**
         * 结束
         *
         * @return
         */
        public DataTableWrapperTableBuilder end() {
            return dataTableWrapperTableBuilder;
        }
    }

    /**
     * redis表
     */
    @Data
    class RedisTable {
        /**
         * 名称
         */
        private String name;
        /**
         * factory
         */
        private String factory = FACTORY;
        /**
         * operand
         */
        private RedisColumn operand;
    }

    /**
     * redis字段
     */
    @Data
    class RedisColumn {
        /**
         * 数据格式
         */
        private String dataFormat = CSV;
        /**
         * key分隔符
         */
        private String keyDelimiter = ",";
        /**
         * 字段
         */
        private List<RedisField> fields = new ArrayList<>();
    }

    /**
     * redis字段
     */
    @Data
    class RedisField {
        /**
         * 名称
         */
        private String name;
        /**
         * factory
         */
        private String type = "varchar";
        /**
         * mapping
         */
        private int mapping = 1;
    }
}
