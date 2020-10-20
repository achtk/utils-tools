package com.chua.utils.netx.datasource.mem;

import com.chua.utils.netx.datasource.factory.DataSourceFactory;
import com.chua.utils.netx.datasource.info.TableInfo;
import com.chua.utils.netx.datasource.properties.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 抽象实现类
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/20 17:09
 */
@Slf4j
public abstract class AbstractMemSearch<T> implements MemSearch<T> {

    private final DataSourceFactory dataSourceFactory;
    private TableInfo tableInfo;

    protected abstract DataSourceProperties dataSourceProperties();

    public AbstractMemSearch() {
        this.dataSourceFactory = DataSourceFactory.newBuilder().dataSourceProperties(dataSourceProperties()).build();
    }

    @Override
    public MemSearch addData(List<T> data) throws Throwable {
        if (null == data || data.size() == 0) {
            return this;
        }
        if (null == tableInfo) {
            synchronized (data) {
                if (null == tableInfo) {
                    tableInfo = new TableInfo();
                    tableInfo.objectToTable(data.get(0));
                    dataSourceFactory.getQueryRunner().execute(tableInfo.initialConfig());
                }
            }
        }
        QueryRunner queryRunner = dataSourceFactory.getQueryRunner();
        //获取连接对象
        Connection connection = queryRunner.getDataSource().getConnection();
        //设置禁用自动提交
        connection.setAutoCommit(false);
        //创建执行对象
        PreparedStatement preparedStatement = connection.prepareStatement(tableInfo.prepareInsertBatch());
        //这里可以通过addBatch()方法增加多条任意SQL语句
        long startTime = 0L;
        if (log.isDebugEnabled()) {
            startTime = System.currentTimeMillis();
        }
        for (T datum : data) {
            tableInfo.addBatch(datum, new BiConsumer<Integer, Object>() {
                @Override
                public void accept(Integer integer, Object o) {
                    try {
                        preparedStatement.setObject(integer, o);
                    } catch (SQLException throwables) {
                        return;
                    }
                }
            });
            preparedStatement.addBatch();
        }

        int[] ints = preparedStatement.executeBatch();
        if (log.isDebugEnabled()) {
            log.debug("插入{}条数据，成功{}/{}, 耗时{}ms", data.size(), ints.length, data.size(), System.currentTimeMillis() - startTime);
        }
        return this;
    }

    @Override
    public List<Map<String, Object>> query(String ddl) throws SQLException {
        QueryRunner queryRunner = dataSourceFactory.getQueryRunner();
        return queryRunner.query(tableInfo.parser(ddl), new MapListHandler());
    }

    @Override
    public List<T> queryForObject(String ddl) throws SQLException {
        QueryRunner queryRunner = dataSourceFactory.getQueryRunner();
        return (List<T>) queryRunner.query(tableInfo.parser(ddl), new BeanListHandler(tableInfo.getObjClass()));
    }

}
