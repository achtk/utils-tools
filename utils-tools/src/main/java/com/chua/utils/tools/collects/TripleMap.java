package com.chua.utils.tools.collects;

import java.util.List;

/**
 * 三元集合
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface TripleMap<L, M, R> {
    /**
     * 获取集合长度
     *
     * @return 集合长度
     */
    int size();

    /**
     * 设置数据
     *
     * @param left   左侧数据
     * @param middle 中间数据
     * @param right  右侧数据
     */
    void put(L left, M middle, R right);

    /**
     * 获取中端, 右端数据
     *
     * @param left 左端数据
     * @return 二元元组
     */
    MultiListValueMap<M, R> byLeft(L left);

    /**
     * 获取左端, 右端数据
     *
     * @param middle 中端数据
     * @return 二元元组
     */
    MultiListValueMap<L, R> byMiddle(M middle);

    /**
     * 获取左端, 中端数据
     *
     * @param right 右端数据
     * @return 二元元组
     */
    MultiListValueMap<L, M> byRight(R right);

    /**
     * 获取右端数据
     *
     * @param left   左端数据
     * @param middle 中端数据
     * @return 集合
     */
    List<R> getRight(L left, M middle);

    /**
     * 获取中端数据
     *
     * @param left  左端数据
     * @param right 右端数据
     * @return 集合
     */
    List<M> getMiddle(L left, R right);

    /**
     * 包含左边数据
     *
     * @param left 左侧索引
     * @return 包含左侧索引返回true
     */
    default boolean containsLeft(L left) {
        return byLeft(left).size() != 0;
    }

    /**
     * 包含左, 中边数据
     *
     * @param left   左侧索引
     * @param middle 中测索引
     * @return 包含左侧索引返回true
     */
    default boolean containsLm(L left, M middle) {
        return getRight(left, middle).isEmpty();
    }

    /**
     * 包含右边数据
     *
     * @param right 右侧索引
     * @return 包含右侧索引返回true
     */
    default boolean containsRight(R right) {
        return byRight(right).size() != 0;
    }

    /**
     * 包含右边数据
     *
     * @param right  右侧索引
     * @param middle 中测索引
     * @return 包含右侧索引返回true
     */
    default boolean containsRm(R right, M middle) {
        return getLeft(middle, right).isEmpty();
    }

    /**
     * 包含中边数据
     *
     * @param middle 中侧索引
     * @return 包含中侧索引返回true
     */
    default boolean containsMiddle(M middle) {
        return byMiddle(middle).size() != 0;
    }

    /**
     * 包含左, 右边数据
     *
     * @param left  左侧索引
     * @param right 右侧索引
     * @return 包含中侧索引返回true
     */
    default boolean containsLr(L left, R right) {
        return getMiddle(left, right).isEmpty();
    }

    /**
     * 获取右端数据
     *
     * @param middle 中端数据
     * @param right  右端数据
     * @return 集合
     */
    List<L> getLeft(M middle, R right);

    /**
     * 集合是否为空
     *
     * @return 集合为空返回true, 反之false
     */
    default boolean isEmpty() {
        return size() == 0;
    }
}
