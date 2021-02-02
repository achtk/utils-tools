package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.data.factory.DataFactory;
import com.chua.utils.tools.data.factory.StandardDataFactory;
import com.chua.utils.tools.data.table.wrapper.TableWrapper;
import com.chua.utils.tools.text.IdHelper;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
public class DataFactoryExample extends BaseExample {


    public static void main(String[] args) throws Exception {
        //测试数据源数据

        // SqlNode sqlNode = converter.convertNode(rexNode);
        //测试内存数据
        // testMemorySchema();
        //测试Csv数据
      //  testFileSchema("TEMP.csv");
        //测试Bcp数据
        // testFileSchema("TEMP.bcp");
        //测试redis数据
       // testRedisSchema();
        //测试Csv和共享和数据库数据
        testCsvAndShareAndDbSchema();
    }

    private static void testRedisSchema() throws Exception {
        log.info("===================================测试Redis数据=====================================");
        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema("DUAL", TableWrapper.creatRedisTable().source().createTable("demo1").createColumn(new String[]{"f1", "f2"}).end().build().create());
        printData(dataFactory.getConnection(), "SELECT * from \"DUAL\".\"demo1\"");
    }

    private static void testMemorySchema() throws Exception {
        log.info("===================================测试内存数据=====================================");
        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema("DUAL", TableWrapper.createMemTable("test").source(createRandomTDemoInfos()).create());
        printData(dataFactory.getConnection(), "SELECT * from \"DUAL\".\"test\"");
    }

    private static void testFileSchema(String name) throws Exception {
        log.info("===================================测试文件" + name + "数据=====================================");
        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema("DUAL", TableWrapper.createFileTable("test").source("TEMP.bcp").create());
        dataFactory.addSchema("DUAL", TableWrapper.createMemTable("test1").source(createRandomTDemoInfos()).create());

       // printData(dataFactory.getConnection(), "SELECT * from \"DUAL\".\"test\"");
      //  printData(dataFactory.getConnection(), "SELECT * from \"DUAL\".\"test1\"");
        printData(dataFactory.getConnection(),
                "SELECT dt1.* from \"DUAL\".\"test1\" dt1 " +
                "LEFT JOIN \"DUAL\".\"test1\" dt ON dt.ID = dt1.ID ");
    }

    private static void increament(String name) throws IOException {
        if (name.endsWith("bcp")) {
            URL url = Resources.getResource(name);
            List<String> strings = IoHelper.toList(url);
            FileHelper.write(new File(url.getFile()), "\r\n" + strings.size() + "\t" + IdHelper.createUuid(), StandardCharsets.UTF_8, true);
        }
    }


    private static void printData(Connection connection, String sql) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        log.info(JsonHelper.toFormatJson(getData(resultSet)));
    }

    private static void testCsvAndShareAndDbSchema() throws Exception {
        log.info("===================================测试Csv和共享和数据库数据=====================================");
        DataFactory dataFactory = new StandardDataFactory();
        //添加数据库数据
        dataFactory.addSchema("xxl_job", TableWrapper.createDataSourceTable().source("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/xxl_job?serverTimezone=UTC", "root", "root").create());
        //添加bcp数据
        dataFactory.addSchema("DUAL1", TableWrapper.createFileTable("test").source("TEMP.bcp").create());
        //添加内存数据
        dataFactory.addSchema("DUAL2", TableWrapper.createMemTable("test").source(createRandomTDemoInfos()).create());

        printData(dataFactory.getConnection(), "" +
                "select xx.*,dt1.*,dt2.* from \"xxl_job\".\"xxl_job_log_report\" xx " +
                "left join \"DUAL1\".\"test\" dt1 on dt1.ID = xx.\"id\" " +
                "left join \"DUAL2\".\"test\" dt2 on dt2.ID = xx.\"id\" "
        );
        log.info(dataFactory.schema());
    }


    public static List<TDemoInfoImpl> createRandomTDemoInfos() {
        List<TDemoInfoImpl> mock = JMockData.mock(new TypeReference<List<TDemoInfoImpl>>() {
        });
        TDemoInfoImpl tDemoInfoImpl = new TDemoInfoImpl();
        tDemoInfoImpl.setId(1);
        tDemoInfoImpl.setName("demo");
        mock.add(tDemoInfoImpl);

        return mock;
    }

    public static List<Map<String, Object>> getData(ResultSet resultSet) throws Exception {
        List<Map<String, Object>> list = Lists.newArrayList();
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