package com.chua.utils.tools.tuple;

/**
 * 三元元组实现
 *
 * @param <L> the left element type
 * @param <M> the middle element type
 * @param <R> the right element type
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public final class ImmutableTriple<L, M, R> implements Triple<L, M, R> {

    private final L left;
    private final M middle;
    private final R right;

    public ImmutableTriple(final L left, final M middle, final R right) {
        super();
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    public static <L, M, R> Triple<L, M, R> of(final L left, final M middle, final R right) {
        return new ImmutableTriple<>(left, middle, right);
    }

    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public M getMiddle() {
        return middle;
    }

    @Override
    public R getRight() {
        return right;
    }

}
