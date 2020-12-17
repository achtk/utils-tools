package com.chua.utils.tools.collects;

/**
 * 区间Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public interface RangeMap<K extends Comparable, V> extends com.google.common.collect.RangeMap<K, V> {
    /**
     * put value
     *
     * @param left  the left value
     * @param right the right value
     * @param value value
     */
    void putCloseOpen(K left, K right, V value);
    /**
     * put value
     *
     * @param left  the left value
     * @param right the right value
     * @param value value
     */
    void putClose(K left, K right, V value);
    /**
     * put value
     *
     * @param left  the left value
     * @param right the right value
     * @param value value
     */
    void putOpen(K left, K right, V value);
    /**
     * put value
     *
     * @param left  the left value
     * @param right the right value
     * @param value value
     */
    void putOpenClose(K left, K right, V value);
}
