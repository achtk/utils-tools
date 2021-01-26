package com.chua.utils.tools.example

import com.chua.utils.netx.flink.table.Table
import com.chua.utils.netx.flink.table.TableFactory
import com.chua.utils.netx.flink.table.Tables
import com.chua.utils.tools.example.entity.TDemoInfo
import com.github.jsonzou.jmockdata.JMockData
import com.github.jsonzou.jmockdata.TypeReference
import org.apache.flink.types.Row

/**
 * @author CH* @since 2021/1/19
 * @version 1.0.0
 */
class FlinkExample {

    static void main(String[] args) {
        Tables tables = Tables.newInstance()
        tables.register(
                TableFactory.ofRedis()
                        .table("redis1")
                        .source("demo1")
                        .columns(new String[]{"userId", "itemId", "categoryId"} , "VARCHAR").build() as Table)

        tables.register(
                TableFactory.ofMem()
                        .table("mem1")
                        .source(JMockData.mock(new TypeReference<List<TDemoInfo>>() {}))
                        .columns(new String[]{"id", "name", "title"} , "VARCHAR").build() as Table)

        def query = tables.sqlQuery("SELECT * FROM mem1", Row.class)
        query.collect() {
            println it.toString()
        }

        tables.sqlUpdate("insert into mem1 values('1', '2', '3')");

        query = tables.sqlQuery("SELECT * FROM mem1", Row.class)
        query.collect() {
            println it.toString()
        }
    }
}
