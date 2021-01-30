package com.chua.utils.tools.function;

import java.util.List;

/**
 * 可均衡
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public interface Balanceable<T> {
    /**
     * 均衡
     *
     * @param tList 集合
     * @return 对象
     */
    T balance(List<T> tList);
}
