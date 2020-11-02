package com.chua.utils.tools.example;

import com.chua.utils.tools.dsl.CreateDataBaseStepBuilder;
import com.chua.utils.tools.dsl.CreateTableStepSqlBuilder;
import com.chua.utils.tools.dsl.DropTableStepBuilder;
import com.chua.utils.tools.dsl.factory.DataSourceDSL;
import com.chua.utils.tools.example.entity.TDemoInfo;
import org.jooq.impl.DSL;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class DslExample {

    public static void main(String[] args) {

        CreateTableStepSqlBuilder createTableStepSqlBuilder = DataSourceDSL.createTable().table("demo").withColumn(TDemoInfo.class).create();
        System.out.println(createTableStepSqlBuilder.toSql());
        CreateDataBaseStepBuilder createDataBaseStepBuilder = DataSourceDSL.createDB().db("test_db");
        System.out.println(createDataBaseStepBuilder.toSql());
        DropTableStepBuilder dropTableStepBuilder = DataSourceDSL.dropTable().table("demo");
        System.out.println(dropTableStepBuilder.toSql());
    }
}
