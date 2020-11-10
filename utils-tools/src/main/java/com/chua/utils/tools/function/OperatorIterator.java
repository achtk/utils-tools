package com.chua.utils.tools.function;

/**
 * 操作迭代器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface OperatorIterator<T> {
    /**
     * 是否存在下一个索引
     *
     * @return 存在返回true
     */
    boolean hasNext();

    /**
     * 获取存在的数据
     *
     * @return 存在的数据
     */
    T next();
}
