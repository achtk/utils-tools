package com.chua.utils.tools.loader;

import com.chua.utils.tools.common.FinderHelper;

import java.util.Collection;
import java.util.concurrent.atomic.LongAdder;

/**
 * 轮训均衡器
 * @author CH
 */
public class RotationBalancerLoader<T> implements BalancerLoader<T> {

    private final LongAdder longAdder = new LongAdder();

    @Override
    public T balancer(Collection<T> ts) {
        if(null == ts || ts.size() == 0) {
            return null;
        }
        int intValue = longAdder.intValue();
        int length = ts.size();
        int value = 0;
        if(length < intValue) {
            value = length;
            longAdder.reset();
        } else {
            value = intValue;
            longAdder.increment();
        }
        return FinderHelper.findElement(value, ts);
    }
}
