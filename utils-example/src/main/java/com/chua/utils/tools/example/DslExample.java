package com.chua.utils.tools.example;

import com.chua.utils.tools.dsl.CreateDataBaseStepBuilder;
import com.chua.utils.tools.dsl.CreateTableStepSqlBuilder;
import com.chua.utils.tools.dsl.DropTableStepBuilder;
import com.chua.utils.tools.dsl.factory.DataSourceDsl;
import com.chua.utils.tools.example.entity.TDemoInfo;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class DslExample {

    public static void main(String[] args) {

        CreateTableStepSqlBuilder createTableStepSqlBuilder = DataSourceDsl.createTable().table("demo").withColumn(TDemoInfo.class).create();
        System.out.println(createTableStepSqlBuilder.toSql());
        CreateDataBaseStepBuilder createDataBaseStepBuilder = DataSourceDsl.createDb().db("test_db");
        System.out.println(createDataBaseStepBuilder.toSql());
        DropTableStepBuilder dropTableStepBuilder = DataSourceDsl.dropTable().table("demo");
        System.out.println(dropTableStepBuilder.toSql());
    }
}
