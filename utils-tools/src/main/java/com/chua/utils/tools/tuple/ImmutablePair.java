package com.chua.utils.tools.tuple;

/**
 * 二元元组实现
 *
 * @param <L> the left element type
 * @param <R> the right element type
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public final class ImmutablePair<L, R> implements Pair<L, R> {

    private final L left;
    private final R right;

    public ImmutablePair(final L left, final R right) {
        super();
        this.left = left;
        this.right = right;
    }

    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new ImmutablePair<>(left, right);
    }

    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public R getRight() {
        return right;
    }

}
