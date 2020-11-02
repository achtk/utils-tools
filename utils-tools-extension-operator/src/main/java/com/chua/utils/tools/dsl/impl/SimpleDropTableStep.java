package com.chua.utils.tools.dsl.impl;

import com.chua.utils.tools.dsl.DropTableStep;
import com.chua.utils.tools.dsl.DropTableStepBuilder;

/**
 * 简答模式删除表
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public class SimpleDropTableStep implements DropTableStep {
    @Override
    public DropTableStepBuilder table(String name) {
        return new SimpleDropTableStepBuilder(name);
    }
}
