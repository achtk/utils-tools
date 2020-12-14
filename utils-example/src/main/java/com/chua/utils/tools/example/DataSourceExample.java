package com.chua.utils.tools.example;

import com.alibaba.druid.pool.DruidDataSource;
import com.chua.utils.netx.datasource.template.StandardJdbcOperatorTemplate;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.data.factory.DataFactory;
import com.chua.utils.tools.data.factory.StandardDataFactory;
import com.chua.utils.tools.data.table.wrapper.TableWrapper;
import com.chua.utils.tools.template.template.JdbcOperatorTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/14
 */
public class DataSourceExample {

    public static void main(String[] args) throws Exception {
        DataFactory dataFactory = new StandardDataFactory();
        dataFactory.addSchema("system", TableWrapper.createMemTable("test").source(DataFactoryExample.createTDemoInfos()).create());
        dataFactory.addSchema("druid", TableWrapper.createDruidTable().source().create());

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataFactory.getUrl());
        druidDataSource.setDriverClassName(dataFactory.getDriver());

        JdbcOperatorTemplate jdbcOperatorTemplate = new StandardJdbcOperatorTemplate(druidDataSource);
        List<Map<String, Object>> forList = jdbcOperatorTemplate.queryForList("select * from \"test\"");
        System.out.println(forList);
        while (true) {
            ThreadHelper.sleep(10, TimeUnit.SECONDS);
        }
    }
}
