package com.chua.utils.tools.example


import org.apache.flink.api.java.ExecutionEnvironment
import org.apache.flink.table.api.Table
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment
import org.apache.flink.types.Row

/**
 * @author CH* @since 2021/1/19
 * @version 1.0.0
 */
class FlinkExample {

    static void main(String[] args) {
//        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
//        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
//        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(environment, settings);
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment()
        BatchTableEnvironment tableEnvironment = BatchTableEnvironment.create(environment)

//        tableEnvironment.executeSql("""
//                CREATE TABLE file1 (
//                   userId VARCHAR,
//                   itemId VARCHAR,
//                   categoryId VARCHAR
//                ) WITH (
//                  'connector.type' = 'filesystem',
//                  'connector.path' = 'file:///E:/TEMP.csv',
//                  'format.type' = 'csv',
//                  'format.fields.0.name' = 'userId',
//                  'format.fields.0.type' = 'VARCHAR',
//                  'format.fields.1.name' = 'itemId',
//                  'format.fields.1.type' = 'VARCHAR',
//                  'format.fields.2.name' = 'categoryId',
//                  'format.fields.2.type' = 'VARCHAR')
//              """  )

        tableEnvironment.executeSql("""
                CREATE TABLE redis1 (
                   userId STRING,
                   itemId STRING,
                   categoryId STRING
                ) WITH (
                  'connector.type' = 'redis')
              """  )

        println tableEnvironment.listTables()
        Table fileTable1 = tableEnvironment.sqlQuery("SELECT * FROM redis1")
        //tableEnvironment.executeSql("insert into file1 values('1', '1', '1')")
        fileTable1.collect()
        def fileRow1 = tableEnvironment.toDataSet(fileTable1, Row.class)
        println fileRow1.collect()
    }

    static class WC {
        String word; //hello
        long frequency;

        //创建构造方法，让flink进行实例化
        WC() {}

        static WC of(String word, long frequency) {
            WC wc = new WC();
            wc.word = word;
            wc.frequency = frequency;
            return wc;
        }

    }

    static class WC1 {
        String word; //hello
        String frequency;
        String frequency1;

    }
}
