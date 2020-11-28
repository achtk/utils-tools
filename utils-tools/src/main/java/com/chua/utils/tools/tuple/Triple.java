package com.chua.utils.tools.tuple;

/**
 * 三元元组
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface Triple<L, M, R> {

    /**
     * <p>从三个推断泛型类型的对象中获得一个不变的三元组。</ p>
     * <p>该工厂允许使用推断来创建三元组以获取泛型类型。</ p>
     *
     * @param <L>    左元素类型
     * @param <M>    中间元素类型
     * @param <R>    正确的元素类型
     * @param left   左边的元素，可以为null
     * @param middle 中间元素，可以为null
     * @param right  正确的元素，可以为null
     * @return 由三个参数组成的三元组，不为null
     */
    static <L, M, R> Triple<L, M, R> of(final L left, final M middle, final R right) {
        return new ImmutableTriple<L, M, R>(left, middle, right);
    }

    /**
     * <p>从三个推断泛型类型的对象中获得一个不变的三元组。</ p>
     * <p>该工厂允许使用推断来创建三元组以获取泛型类型。</ p>
     *
     * @param <L>    左元素类型
     * @param <M>    中间元素类型
     * @param <R>    正确的元素类型
     * @param left   左边的元素，可以为null
     * @param middle 中间元素，可以为null
     * @param right  正确的元素，可以为null
     * @return 由三个参数组成的三元组，不为null
     */
    static <L, M, R> Triple<L, M, R> with(final L left, final M middle, final R right) {
        return new ImmutableTriple<L, M, R>(left, middle, right);
    }
    //-----------------------------------------------------------------------

    /**
     * <p>从这个三元组中获取左侧元素.</p>
     *
     * @return 左侧元素
     */
    L getLeft();

    /**
     * <p>从这个三元组中获取中间的元素.</p>
     *
     * @return 中间的元素
     */
    M getMiddle();

    /**
     * <p>从这个三元组中获取右侧的元素.</p>
     *
     * @return 右侧的元素
     */
    R getRight();

    //-----------------------------------------------------------------------
}
