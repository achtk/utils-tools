package com.chua.utils.tools.example;

import com.chua.utils.tools.dsl.factory.DataSourceDSL;
import org.jooq.impl.DSL;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class DslExample {

    public static void main(String[] args) {
        DSL.select().from("").where();
        DataSourceDSL.create().table("demo").create();
    }
}
