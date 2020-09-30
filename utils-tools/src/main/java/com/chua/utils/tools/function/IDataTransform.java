package com.chua.utils.tools.function;

/**
 * 数据转化
 * @author CH
 * @date 2020-09-30
 */
public interface IDataTransform<T, Input> {
    /**
     * 数据转化
     * @param input
     * @return
     */
    T transfrom(Input input);
}
