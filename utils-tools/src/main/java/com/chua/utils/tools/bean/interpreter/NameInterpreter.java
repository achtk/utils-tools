package com.chua.utils.tools.bean.interpreter;

/**
 * 字段名称解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public interface NameInterpreter {
    /**
     * 是否需要调整
     *
     * @param name 名称
     * @return 需要调整返回true
     */
    boolean isMatcher(String name);

    /**
     * 解释名称
     *
     * @param name 解释前名称
     * @return 解释后的名称
     */
    String resolve(String name);
}
