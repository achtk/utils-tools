package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.function.Filter;

/**
 * 过滤器
 * @author CH
 * @version 1.0.0
 * @since 2020/10/26
 */
public class TrueFilter<T> implements Filter<T> {

    public static final Filter INSTANCE = new TrueFilter<>();

    @Override
    public boolean matcher(T item) {
        return true;
    }
}
