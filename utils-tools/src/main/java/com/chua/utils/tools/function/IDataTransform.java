package com.chua.utils.tools.function;

/**
 * 数据转化
 * @author CH
 * @date 2020-09-30
 */
public interface IDataTransform<Out, In> {
    /**
     * 数据转化
     * @param in
     * @return
     */
    Out transTo(In in);

    /**
     * 数据回转
     * @param entity
     * @return
     */
    In transFrom(Out entity);
}
