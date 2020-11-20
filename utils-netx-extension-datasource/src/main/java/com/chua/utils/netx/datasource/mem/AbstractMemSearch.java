package com.chua.utils.netx.datasource.mem;

import com.chua.utils.netx.datasource.dialect.SQLDialectEnum;
import com.chua.utils.netx.datasource.dialect.SqlDialect;
import com.chua.utils.netx.datasource.info.TableInfo;
import com.chua.utils.netx.datasource.properties.DataSourceProperties;
import com.chua.utils.netx.datasource.template.StandardJdbcOperatorTemplate;
import com.chua.utils.netx.datasource.transform.JdbcOperatorTransform;
import com.chua.utils.tools.properties.OperatorProperties;
import com.chua.utils.tools.template.template.JdbcOperatorTemplate;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private JdbcOperatorTemplate jdbcOperatorTemplate;
    private TableInfo tableInfo;
    private static final String DRIVER_NAME = "driverClassName";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String JDBC_URL = "jdbcUrl";
    private static final String URL = "url";
    private static final String DRIVER = "driver";

    /**
     * 获取 DataSourceProperties
     *
     * @return DataSourceProperties
     */
    protected abstract DataSourceProperties dataSourceProperties();

    public void initialDataBase() {
        OperatorProperties operatorProperties = new OperatorProperties();
        Properties properties = new Properties();
        BeanMap beanMap = BeanMap.create(dataSourceProperties());
        beanMap.forEach((key, value) -> {
            if (null == key || null == value) {
                return;
            }
            properties.put(key, value);
        });

        if (properties.containsKey(DRIVER_NAME) || properties.containsKey(DRIVER)) {
            operatorProperties.driver(
                    properties.getOrDefault(DRIVER_NAME,
                            properties.getOrDefault(DRIVER, "")).toString());
        }

        if (properties.containsKey(JDBC_URL) || properties.containsKey(URL)) {
            operatorProperties.url(
                    properties.getOrDefault(JDBC_URL,
                            properties.getOrDefault(URL, "")).toString());
        }

        if (properties.containsKey(USERNAME)) {
            operatorProperties.username(properties.getProperty(USERNAME));
        }

        if (properties.containsKey(PASSWORD)) {
            operatorProperties.password(properties.getProperty(PASSWORD));
        }

        operatorProperties.properties(properties);
        this.jdbcOperatorTemplate = new StandardJdbcOperatorTemplate(new JdbcOperatorTransform().transform(operatorProperties));
    }

    @Override
    public void create(Class<T> tClass) throws Exception {
        if(null == jdbcOperatorTemplate) {
            initialDataBase();
        }
        if (null == tableInfo && null != tClass) {
            synchronized (tClass) {
                //简单双锁
                if (null == tableInfo) {
                    synchronized (tClass) {
                        if (null == tableInfo) {
                            tableInfo = new TableInfo();
                            tableInfo.objectToTable(tClass);
                            try {
                                this.jdbcOperatorTemplate.execute(tableInfo.initialConfig(SQLDialectEnum.findByDriver(jdbcOperatorTemplate.getDriver(), jdbcOperatorTemplate.getUrl())));
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void remove() throws Exception {
        try {
            this.jdbcOperatorTemplate.execute(tableInfo.prepare());
        } catch (Exception e) {
        }
    }

    @Override
    public MemSearch addData(List<T> data) throws Exception {
        if (null == data || data.size() == 0) {
            return this;
        }
        long startTime = 0L;
        if (log.isDebugEnabled()) {
            startTime = System.currentTimeMillis();
        }
        List<List<Object>> params = new ArrayList<>();
        for (T datum : data) {
            List<Object> param = new ArrayList<>();
            tableInfo.addBatch(datum, (BiConsumer<Integer, Object>) (integer, o) -> {
                param.add(o);
            });
            params.add(param);
        }

        int[] ints = new int[0];
        try {
            ints = jdbcOperatorTemplate.batch(tableInfo.prepareInsertBatch(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
