package com.chua.utils.tools.function;

import java.util.function.Function;

/**
 * 失败
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public interface Failure<T> extends Function<Throwable, T> {


}
