package com.chua.utils.netx.datasource.mem;

import com.chua.utils.netx.datasource.info.TableInfo;
import com.chua.utils.netx.datasource.properties.DataSourceProperties;
import com.chua.utils.netx.datasource.template.SimpleJdbcOperatorTemplate;
import com.chua.utils.netx.datasource.transform.JdbcOperatorTransform;
import com.chua.utils.tools.properties.OperatorProperties;
import com.chua.utils.tools.template.template.JdbcOperatorTemplate;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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

    private final JdbcOperatorTemplate jdbcOperatorTemplate;
    private TableInfo tableInfo;

    /**
     * 获取 DataSourceProperties
     *
     * @return DataSourceProperties
     */
    protected abstract DataSourceProperties dataSourceProperties();

    public AbstractMemSearch() {
        OperatorProperties operatorProperties = new OperatorProperties();
        Properties properties = new Properties();
        properties.putAll(BeanMap.create(dataSourceProperties()));
        operatorProperties.properties(properties);
        this.jdbcOperatorTemplate = new SimpleJdbcOperatorTemplate(new JdbcOperatorTransform().transform(operatorProperties));
    }

    @Override
    public MemSearch addData(List<T> data) throws Exception {
        if (null == data || data.size() == 0) {
            return this;
        }
        if (null == tableInfo) {
            synchronized (data) {
                if (null == tableInfo) {
                    tableInfo = new TableInfo();
                    tableInfo.objectToTable(data.get(0));
                    this.jdbcOperatorTemplate.execute(tableInfo.initialConfig());
                }
            }
        }
        //获取连接对象
        Connection connection = jdbcOperatorTemplate.getConnection();
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
    public List<Map<String, Object>> query(String ddl) throws Exception {
        return jdbcOperatorTemplate.queryForList(tableInfo.parser(ddl));
    }

    @Override
    public List<T> queryForObject(String ddl) throws Exception {
        return (List<T>) jdbcOperatorTemplate.queryForList(tableInfo.parser(ddl), tableInfo.getObjClass());
    }

}
