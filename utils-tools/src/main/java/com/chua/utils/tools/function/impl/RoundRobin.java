package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.function.Balanceable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class RoundRobin<T> implements Balanceable<T> {

    private static final AtomicInteger CNT = new AtomicInteger();

    @Override
    public T balance(List<T> ts) {
        return null == ts ? null : ts.get(CNT.getAndIncrement() % ts.size());
    }
}
