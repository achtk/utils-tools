package com.chua.utils.netx.datasource.template;

import com.chua.utils.netx.datasource.transform.JdbcOperatorTransform;
import com.chua.utils.tools.dsl.CreateTableStepSqlBuilder;
import com.chua.utils.tools.properties.OperatorProperties;
import com.chua.utils.tools.template.template.JdbcOperatorTemplate;
import com.chua.utils.tools.transform.OperatorTransform;
import com.google.common.base.Preconditions;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * commons-dbutils实现方式
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class SimpleJdbcOperatorTemplate implements JdbcOperatorTemplate {

    private final QueryRunner queryRunner;
    private OperatorTransform<DataSource> operatorTransform = new JdbcOperatorTransform();
    private DataSource dataSource;

    public SimpleJdbcOperatorTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
        this.queryRunner = new QueryRunner(dataSource);
    }

    public SimpleJdbcOperatorTemplate(OperatorProperties operatorProperties) {
        this.dataSource = operatorTransform.transform(operatorProperties);
        this.queryRunner = new QueryRunner(dataSource);
    }

    @Override
    public Object execute(String express, Object[] params) throws Exception {
        return queryRunner.execute(express, params);
    }

    @Override
    public int update(String express, Object... params) throws Exception {
        return queryRunner.update(express, params);
    }

    @Override
    public int[] batch(String express, List<List<Object>> data) throws Exception {
        Object[][] objects = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            List<Object> objects1 = data.get(i);
            for (int j = 0; j < objects1.size(); j++) {
                objects[i][j] = objects1.get(j);
            }
        }
        return batch(express, objects);
    }

    @Override
    public int[] batch(String express, Object[][] data) throws Exception {
        return queryRunner.batch(express, data);
    }

    @Override
    public List<Map<String, Object>> queryForList(String express, Object... params) throws Exception {
        return queryRunner.query(express, new MapListHandler(), params);
    }

    @Override
    public <T> List<T> queryForList(String express, Class<T> tClass, Object... params) throws Exception {
        return (List<T>) queryRunner.query(express, new BeanHandler(tClass), params);
    }

    @Override
    public void createTable(CreateTableStepSqlBuilder createTableStepSqlBuilder) throws Exception {
        Preconditions.checkArgument(null != createTableStepSqlBuilder);
        String sql = createTableStepSqlBuilder.toSql();
        execute(sql, null);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


}