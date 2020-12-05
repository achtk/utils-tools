package com.chua.utils.tools.example;

import com.alibaba.fastjson.JSON;
import com.chua.utils.netx.data.CalciteFileDataSchema;
import com.chua.utils.netx.data.CalciteMemoryDataSchema;
import com.chua.utils.netx.data.CalciteShareDataSchema;
import com.chua.utils.netx.datasource.transform.JdbcOperatorTransform;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.data.DataFactory;
import com.chua.utils.tools.data.DataSchema;
import com.chua.utils.tools.data.StandardDataFactory;
import com.chua.utils.tools.data.schema.DataSourceSchema;
import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.properties.OperatorProperties;
import com.chua.utils.tools.random.RandomUtil;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.text.IdHelper;
import com.chua.utils.tools.transform.OperatorTransform;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
public class DataFactoryExample {


    private static OperatorTransform<DataSource> operatorTransform = new JdbcOperatorTransform();
    private static  OperatorProperties operatorProperties = new OperatorProperties();
    static {
        operatorProperties.url("jdbc:mysql://localhost:3306/xxl_job?serverTimezone=UTC");
        operatorProperties.username("root");
        operatorProperties.password("root");
        operatorProperties.dialect("mysql");
    }


    public static void main(String[] args) throws Exception {
        //测试共享数据
        testShareSchema();
        //测试Csv数据
        testCsvSchema();
        //测试Csv和共享数据
        testCsvAndShareSchema();
        //测试数据源数据
        testDataSourceSchema();
        //测试Csv和共享和数据库数据
        testCsvAndShareAndDbSchema();
    }
    private static void testCsvAndShareAndDbSchema() throws Exception {
        System.out.println("===================================测试Csv和共享和数据库数据=====================================");
        DataSchema fileSchema = new CalciteFileDataSchema("TEMP.csv");
        DataSchema dataSchema = new DataSourceSchema(operatorTransform.transform(operatorProperties));
        DataSchema memorySchema = new CalciteShareDataSchema(createTDemoInfos());
        memorySchema.schema("MEM");

        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema(fileSchema);
        dataFactory.addSchema(memorySchema);
        dataFactory.addSchema(dataSchema);

        Connection connection = dataFactory.createConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select mt.*, t.* from mem.TEMP mt left join TEMP t on mt.id =t.id left join xxl_job.xxl_job_log l on mt.id = l.id");
        System.out.println(JsonHelper.toFormatJson((getData(resultSet))));
    }
    private static void testCsvAndShareSchema() throws Exception {
        System.out.println("===================================测试Csv和共享数据=====================================");
        DataSchema fileSchema = new CalciteFileDataSchema("TEMP.csv");
        DataSchema memorySchema = new CalciteShareDataSchema(createTDemoInfos());
        memorySchema.schema("MEM");

        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema(fileSchema);
        dataFactory.addSchema(memorySchema);

        Connection connection = dataFactory.createConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select mt.*, t.* from mem.TEMP mt left join TEMP t on mt.id =t.id  ");
        System.out.println(JsonHelper.toFormatJson(getData(resultSet)));
    }

    private static void testCsvSchema() throws Exception {
        System.out.println("===================================测试Csv数据=====================================");
        DataSchema dataSchema = new CalciteFileDataSchema("TEMP.csv");

        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema(dataSchema);
        Connection connection = dataFactory.createConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from TEMP");
        System.out.println(JSON.toJSONString(getData(resultSet)));
    }

    private static void testDataSourceSchema() throws Exception {
        System.out.println("===================================测试数据源数据=====================================");

        DataSchema dataSchema = new DataSourceSchema(operatorTransform.transform(operatorProperties));

        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema(dataSchema);
        Connection connection = dataFactory.createConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from xxl_job_log");
        System.out.println(JSON.toJSONString(getData(resultSet)));
    }

    private static void testShareSchema() throws Exception {
        System.out.println("===================================测试共享数据=====================================");
        DataSchema dataSchema = new CalciteShareDataSchema(createTDemoInfos());

        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema(dataSchema);
        Connection connection = dataFactory.createConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from TEMP");
        System.out.println(JSON.toJSONString(getData(resultSet)));
    }


    public static List<TDemoInfo> createTDemoInfos() {
        int num = RandomUtil.randomInt(100);
        List<TDemoInfo> result = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            TDemoInfo tDemoInfo = new TDemoInfo();
            tDemoInfo.setUuid(IdHelper.createUuid());
            tDemoInfo.setId(i);
            tDemoInfo.setName("demo" + i);

            result.add(tDemoInfo);
        }

        return result;
    }

    public static List<Map<String,Object>> getData(ResultSet resultSet)throws Exception{
        List<Map<String,Object>> list = Lists.newArrayList();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> map = Maps.newLinkedHashMap();
            for (int i = 1; i < columnSize + 1; i++) {
                map.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            list.add(map);
        }
        return list;
    }
}