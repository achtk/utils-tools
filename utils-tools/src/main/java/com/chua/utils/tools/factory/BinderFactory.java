package com.chua.utils.tools.factory;

/**
 * 数据绑定工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public interface BinderFactory<S, T> {
    /**
     * 获取目标数据
     *
     * @param s 源数据
     * @return 目标数据
     */
    T get(S s);
}
