package com.chua.utils.netx.rpc.adaptor;

/**
 * 接口适配器
 *
 * @author CH
 * @version 1.0.0
 * @ClassName IAdaptor.java
 * @createTime 2020/08/01 02:01:00
 */
public interface Adaptor {
    /**
     * 代理类
     *
     * @param obj   实现
     * @param clazz 原类
     * @return T
     * @throws Throwable Throwable
     */
    <T> T adaptor(T obj, Class<?> clazz) throws Throwable;
}
