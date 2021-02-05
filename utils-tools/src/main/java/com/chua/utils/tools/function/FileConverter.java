package com.chua.utils.tools.function;


/**
 * 转化对象
 *
 * @author CH
 */
public interface FileConverter<A, B> {
    /**
     * 转化
     *
     * @param a 数据1
     * @return 数据2
     */
    B doForward(A a);

    /**
     * 转化
     *
     * @param b 数据2
     * @return 数据1
     */
    A doBackward(B b);

}
