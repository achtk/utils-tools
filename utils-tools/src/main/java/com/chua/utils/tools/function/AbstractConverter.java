package com.chua.utils.tools.function;

/**
 * 转化对象
 * @author CH
 */
public abstract class AbstractConverter<A, B> {
    /**
     * 转化
     * @param a
     * @return
     */
    abstract public B doForward(A a);
    /**
     * 转化
     * @param b
     * @return
     */
    abstract public A doBackward(B b);

}
