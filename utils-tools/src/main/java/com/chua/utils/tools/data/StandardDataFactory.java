package com.chua.utils.tools.data;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.common.JsonHelper;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 标准的数据工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
@NoArgsConstructor
public class StandardDataFactory implements DataFactory {

    /**
     * 配置项
     */
    private Properties properties = new Properties() {
        {
            put("defaultSchema", "system");
            put("version", "1.0.0");
        }
    };
    /**
     * 数据库对应表
     */
    private List<DataSchema> schemas = new CopyOnWriteArrayList<>();

    public StandardDataFactory(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void addSchema(DataSchema dataSchema) {
        schemas.add(dataSchema);
    }

    /**
     *
     * @return
     */
    @Override
    public Connection createConnection() throws SQLException {
        //{
        //  "name": "SALES",
        //  "type": "custom",
        //  "factory": "com.chua.utils.tools.example.factory.CsvSchemaFactory",
        //  "operand": {
        //      "directory": "SALES.csv"
        //  }
        //}
        if(this.schemas.size() == 1) {
            DataSchema dataSchema = this.schemas.get(0);
            if(dataSchema instanceof DataSourceDataSchema) {
                return ((DataSourceDataSchema) dataSchema).dataSource().getConnection();
            }
        }
        List<Schema> schemas = this.schemas.stream().map(dataSchema -> {
            Schema schema = new Schema();
            schema.setName(dataSchema.schema().toUpperCase());
            if(!(dataSchema instanceof DataSourceDataSchema)) {
                schema.setFactory(dataSchema.getClass().getName());
                schema.getOperand().putAll(dataSchema.operand());
            } else {
                DataSource dataSource = ((DataSourceDataSchema) dataSchema).dataSource();
                schema.setFactory("org.apache.calcite.adapter.jdbc.JdbcSchema$Factory");
                Operand operand = new Operand();
                // jdbcDriver: 'com.mysql.jdbc.Driver',
                // jdbcUrl: 'jdbc:mysql://localhost:3306/dbtest_1',
                // jdbcUser: 'xxx',
                //  jdbcPassword: 'xxx'

                HashOperateMap hashOperateMap = BeanCopy.of(dataSource).asMap();
              //  operand.putAll(hashOperateMap);
                operand.put("jdbcDriver", hashOperateMap.getString("driverClassName"));
                operand.put("jdbcUrl", hashOperateMap.getString("jdbcUrl"));
                operand.put("jdbcUser", hashOperateMap.getString("username"));
                operand.put("jdbcPassword", hashOperateMap.getString("password"));
                schema.setOperand(operand);
            }
            return schema;
        }).collect(Collectors.toList());
        CalciteInfo calciteInfo = new CalciteInfo();
        calciteInfo.setSchemas(schemas);
        return DriverManager.getConnection("jdbc:calcite:model=inline:" + JsonHelper.toJson(calciteInfo));
    }

    /**
     * Calcite信息
     */
    @Getter
    @Setter
    class CalciteInfo {
        /**
         * 版本
         */
        private String version = properties.getProperty("version", "1.0.0");
        /**
         * 默认数据库
         */
        private String defaultSchema = properties.getProperty("defaultSchema", "system");
        /**
         * 数据库
         */
        private List<Schema> schemas = Lists.newArrayList();
    }

    /**
     * 数据库
     */
    @Getter
    @Setter
    class Schema {
        /**
         * 数据库名称
         */
        private String name;
        /**
         * 创建类型
         */
        private String type = "custom";
        /**
         * 实现类
         */
        private String factory = "";
        /**
         * 额外参数
         */
        private Operand operand = new Operand();
    }

    /**
     * 额外参数
     */
    @Getter
    @Setter
    class Operand extends HashOperateMap {

    }
}
