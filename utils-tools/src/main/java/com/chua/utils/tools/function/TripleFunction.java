package com.chua.utils.tools.function;

/**
 * 三元回调
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/5
 */
@FunctionalInterface
public interface TripleFunction<R, R1, T> {


    /**
     * Applies this function to the given argument.
     *
     * @param r  the function argument
     * @param r1 the function argument
     * @return the function result
     */
    T apply(R r, R1 r1);

}
