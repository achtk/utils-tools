package com.chua.utils.tools.dsl.impl;

import com.chua.utils.tools.dsl.CreateDataBaseStep;
import com.chua.utils.tools.dsl.CreateDataBaseStepBuilder;

/**
 * 简单的数据库步骤器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public class SimpleCreateDataBaseStep implements CreateDataBaseStep {
    @Override
    public CreateDataBaseStepBuilder db(String name) {
        return new SimpleCreateDataBaseStepBuilder(name);
    }
}
