package com.chua.utils.tools.dsl.impl;

import com.chua.utils.tools.dsl.CreateDataBaseStepBuilder;
import lombok.AllArgsConstructor;

/**
 * 简单的创建数据库建造者
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
@AllArgsConstructor
public class SimpleCreateDataBaseStepBuilder implements CreateDataBaseStepBuilder {

    private String dbName;

    @Override
    public String toSql() {
        return "CREATE DATABASE " + dbName;
    }
}
