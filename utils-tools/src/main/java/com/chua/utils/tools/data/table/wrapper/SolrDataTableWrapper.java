package com.chua.utils.tools.data.table.wrapper;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.type.TableType;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * solr
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/14
 */
public class SolrDataTableWrapper {

    private final TableType tableType = TableType.ELASTIC_SEARCH;

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6379;
    private static final String INDEX = "default";
    private static final String PASSWORD = "";
    private DataTable dataTable;

    /**
     * 构建数据
     *
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperTableBuilder source() {
        return source(HOST, INDEX, PASSWORD);
    }


    /**
     * 构建数据
     *
     * @param host 主机
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperTableBuilder source(String host) {
        return source(host, INDEX, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host  主机
     * @param index 索引
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperTableBuilder source(String host, String index) {
        return source(host, index, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host       主机
     * @param collection 索引
     * @param zkHosts    zk
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperTableBuilder source(String host, String collection, String zkHosts) {
        DataTable.DataTableBuilder builder = DataTable.builder();
        OperateHashMap.create(hashOperateMap -> {
            hashOperateMap.put("solrServerURL", host);
            hashOperateMap.put("solrCollection", collection);
            hashOperateMap.put("solrZkHosts", zkHosts);
            builder.operate(hashOperateMap);
        });
        builder.tableType(tableType);

        this.dataTable = builder.build();

        return new DataTableWrapperTableBuilder(dataTable, this);
    }

    /**
     * 构建
     *
     * @return
     */
    public DataTableWrapperBuilder build() {
        return () -> dataTable;
    }


    @AllArgsConstructor
    public class DataTableWrapperTableBuilder {

        private DataTable dataTable;
        private SolrDataTableWrapper solrDataTableWrapper;
        private final List<String> columns = new ArrayList<>();
        private final List<String> mappers = new ArrayList<>();

        /**
         * 创建字段
         *
         * @param name   名称
         * @param type   类型
         * @param mapper 映射
         * @return this
         */
        public DataTableWrapperTableBuilder withColumn(final String name, final String type, final String mapper) {
            this.columns.add(name + " " + type);
            this.mappers.add(name + "->" + mapper);
            return this;
        }

        /**
         * 创建字段
         *
         * @param name 名称
         * @param type 类型
         * @return this
         */
        public DataTableWrapperTableBuilder withColumn(final String name, final String type) {
            return withColumn(name, type, name);
        }


        public SolrDataTableWrapper end() {
            dataTable.getOperate().put("columns", Joiner.on(",").join(columns));
            dataTable.getOperate().put("columnMapping", Joiner.on(",").join(mappers));
            return solrDataTableWrapper;
        }
    }
}
