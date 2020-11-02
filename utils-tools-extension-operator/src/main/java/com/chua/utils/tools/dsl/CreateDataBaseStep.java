package com.chua.utils.tools.dsl;

/**
 * 创建
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface CreateDataBaseStep {
    /**
     * 创建DB
     *
     * @param name db
     * @return CreateDBStepBuilder
     */
    CreateDataBaseStepBuilder db(String name);
}
