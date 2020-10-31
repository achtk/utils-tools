package com.chua.utils.tools.dsl.impl;

import com.chua.utils.tools.dsl.CreateConditionStep;
import com.chua.utils.tools.dsl.CreateStep;
import com.google.common.base.Preconditions;

/**
 * 简单创建
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class SimpleCreateStep implements CreateStep {

    private SimpleCreateConditionStep step;

    @Override
    public CreateConditionStep table(String tableName) {
        Preconditions.checkArgument(null != tableName);
        this.step = new SimpleCreateConditionStep(tableName, this);
        return step;
    }
}
