package com.chua.utils.tools.bean.interpreter;

/**
 * 字段名称解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public interface MapperInterpreter {
    /**
     * 解释名称
     *
     * @param name 解释前名称
     * @return 解释后的名称
     */
    String resolve(String name);
}
