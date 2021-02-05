package com.chua.utils.tools.function;

/**
 * 三元回调
 * @author CH
 * @version 1.0.0
 * @since 2021/2/5
 */
@FunctionalInterface
public interface TripleFunction<R, R1, T> {


    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t);

}
