package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.function.Balanceable;
import com.chua.utils.tools.random.RandomUtil;

import java.util.List;

/**
 * 轮询
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class RandomRobin<T> implements Balanceable<T> {

    @Override
    public T balance(List<T> ts) {
        if (null == ts) {
            return null;
        }
        return ts.get(RandomUtil.randomInt(ts.size()));
    }
}
