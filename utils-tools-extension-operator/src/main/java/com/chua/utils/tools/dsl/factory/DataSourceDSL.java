package com.chua.utils.tools.dsl.factory;

import com.chua.utils.tools.dsl.CreateStep;
import com.chua.utils.tools.dsl.impl.SimpleCreateStep;

/**
 * 数据源DSL
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class DataSourceDSL {
    /**
     * 创建表
     * @return
     */
    public static CreateStep create() {
        return new SimpleCreateStep();
    }
}
