package com.chua.utils.tools.example;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.prepare.PlannerImpl;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
public class CalciteExample {


    public static void main(String[] args) throws Exception {
        final SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        //创建Mysql的数据源schema
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/xxl_job?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        Schema schema = JdbcSchema.create(rootSchema, "xxl_job_log_report", dataSource, null, "xxl_job");
        rootSchema.add("xxl_job", schema);
        final FrameworkConfig config = Frameworks.newConfigBuilder().defaultSchema(rootSchema).build();
        String sql = "select * from \"xxl_job\".\"xxl_job_log_report\"";
        PlannerImpl planner = new PlannerImpl(config);//执行计划需要进行解析，验证，转换三步
        SqlNode parse = planner.parse(sql);
        SqlNode validate = planner.validate(parse);
        RelNode convert = planner.convert(validate);//
        String s = RelOptUtil.toString(convert);//输出可读的关系表达式
        System.out.println(s);
    }

}
