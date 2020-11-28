package com.chua.utils.tools.tuple;

/**
 * 二元元组
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface Pair<L, R> {

    /**
     * <p>从二个推断泛型类型的对象中获得一个不变的二元组。</ p>
     * <p>该工厂允许使用推断来创建二元组以获取泛型类型。</ p>
     *
     * @param <L>   左元素类型
     * @param <R>   正确的元素类型
     * @param left  左边的元素，可以为null
     * @param right 正确的元素，可以为null
     * @return 由二个参数组成的二元组，不为null
     */
    static <L, R> Pair<L, R> of(final L left, final R right) {
        return new ImmutablePair<>(left, right);
    }

    /**
     * <p>从二个推断泛型类型的对象中获得一个不变的二元组。</ p>
     * <p>该工厂允许使用推断来创建二元组以获取泛型类型。</ p>
     *
     * @param <L>   左元素类型
     * @param <R>   正确的元素类型
     * @param left  左边的元素，可以为null
     * @param right 正确的元素，可以为null
     * @return 由二个参数组成的二元组，不为null
     */
    static <L, R> Pair<L, R> with(final L left, final R right) {
        return new ImmutablePair<>(left, right);
    }
    //-----------------------------------------------------------------------

    /**
     * <p>从这个二元组中获取左侧元素.</p>
     *
     * @return 左侧元素
     */
    L getLeft();

    /**
     * <p>从这个二元组中获取右侧的元素.</p>
     *
     * @return 右侧的元素
     */
    R getRight();


}
