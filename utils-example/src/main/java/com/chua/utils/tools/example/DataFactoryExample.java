package com.chua.utils.tools.example;

import com.chua.utils.netx.datasource.transform.JdbcOperatorTransform;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.data.factory.DataFactory;
import com.chua.utils.tools.data.factory.StandardDataFactory;
import com.chua.utils.tools.data.table.wrapper.TableWrapper;
import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.properties.OperatorProperties;
import com.chua.utils.tools.random.RandomUtil;
import com.chua.utils.tools.text.IdHelper;
import com.chua.utils.tools.transform.OperatorTransform;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
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
    private static OperatorProperties operatorProperties = new OperatorProperties();

    static {
        operatorProperties.url("jdbc:mysql://localhost:3306/xxl_job?serverTimezone=UTC");
        operatorProperties.username("root");
        operatorProperties.password("root");
        operatorProperties.dialect("mysql");
    }


    public static void main(String[] args) throws Exception {
        //测试数据源数据

        // SqlNode sqlNode = converter.convertNode(rexNode);
        //测试内存数据
        // testMemorySchema();
        //测试Csv数据
        //testFileSchema("TEMP.csv");
        //测试Bcp数据
        // testFileSchema("TEMP.bcp");
        //测试redis数据
        testRedisSchema();
        //测试Csv和共享和数据库数据
        //testCsvAndShareAndDbSchema();
    }

    private static void testRedisSchema() throws Exception {
        System.out.println("===================================测试内存数据=====================================");
        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema("DUAL", TableWrapper.creatRedisTable().source().createTable("demo1").createColumn(new String[]{"f1", "f2"}).end().build().create());
        printData(dataFactory.getConnection(), "SELECT * from \"DUAL\".\"demo1\"");
    }

    private static void testMemorySchema() throws Exception {
        System.out.println("===================================测试内存数据=====================================");
        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema("DUAL", TableWrapper.createMemTable("test").source(createTDemoInfos()).create());

        printData(dataFactory.getConnection(), "SELECT * from \"DUAL\".\"test\"");
    }

    private static void testFileSchema(String name) throws Exception {
        System.out.println("===================================测试" + name + "数据=====================================");
        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema("DUAL", TableWrapper.createFileTable("DUAL").source(name).create());

        System.out.println(dataFactory.schema());
        printData(dataFactory.getConnection(), "SELECT * from \"DUAL\".\"test\"");
        increament(name);
        printData(dataFactory.getConnection(), "SELECT * from \"DUAL\".\"test\"");
    }

    private static void increament(String name) throws IOException {
        if (name.endsWith("bcp")) {
            URL url = Resources.getResource(name);
            List<String> strings = IoHelper.toList(url);
            FileHelper.write(new File(url.getFile()), "\r\n" + strings.size() + "\t" + IdHelper.createUuid(), StandardCharsets.UTF_8, true);
        }
    }


    private static void printData(Connection connection, String sql) throws Exception {
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        System.out.println(JsonHelper.toFormatJson(getData(resultSet)));
    }

    private static void testCsvAndShareAndDbSchema() throws Exception {
        System.out.println("===================================测试Csv和共享和数据库数据=====================================");
        DataFactory dataFactory = new StandardDataFactory();
        //添加数据库数据
        dataFactory.addSchema("xxl_job", TableWrapper.createDataSourceTable().source("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/xxl_job?serverTimezone=UTC","root","root").create());
        //添加bcp数据
        dataFactory.addSchema("DUAL1", TableWrapper.createFileTable("test").source("TEMP.bcp").create());
        //添加内存数据
        dataFactory.addSchema("DUAL2", TableWrapper.createMemTable("test").source(createTDemoInfos()).create());

        printData(dataFactory.getConnection(), "" +
                "select xx.*,dt1.*,dt2.* from \"xxl_job\".\"xxl_job_log_report\" xx " +
                "left join \"DUAL1\".\"test\" dt1 on dt1.ID = xx.\"id\" " +
                "left join \"DUAL2\".\"test\" dt2 on dt2.ID = xx.\"id\" "
        );
        System.out.println(dataFactory.schema());
    }


    public static List<TDemoInfo> createTDemoInfos() {
        int num = RandomUtil.randomInt(100);
        List<TDemoInfo> result = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            String uuid = IdHelper.createUuid();
            TDemoInfo tDemoInfo = new TDemoInfo();
            tDemoInfo.setUuid(uuid);
            tDemoInfo.setId(i + "");
            tDemoInfo.setName("demo" + i);

            result.add(tDemoInfo);
        }

        return result;
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