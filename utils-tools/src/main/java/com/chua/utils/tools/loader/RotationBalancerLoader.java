package com.chua.utils.tools.loader;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.impl.TrueFilter;

import java.util.Collection;
import java.util.concurrent.atomic.LongAdder;

/**
 * 轮训均衡器
 *
 * @author CH
 */
public class RotationBalancerLoader<T> implements BalancerLoader<T> {

    private final LongAdder longAdder = new LongAdder();
    private Filter<T> filter;
    private Collection<T> source;
    private int size;

    @Override
    public BalancerLoader data(Collection<T> source) {
        this.source = source;
        this.size = CollectionHelper.getSize(source);
        return this;
    }

    @Override
    public BalancerLoader filter(Filter<T> filter) {
        this.filter = filter == null ? TrueFilter.INSTANCE : filter;
        return this;
    }

    @Override
    public T balancer() {
        if (size == 0) {
            return null;
        }
        int index = longAdder.intValue() % size;
        try {
            return FinderHelper.findElement(index, source);
        } finally {
            longAdder.increment();
        }
    }

    @Override
    public T random() {
        if (0 == size) {
            return null;
        }
        Double doubles = Math.random() * Math.pow(size, 10);
        return FinderHelper.findElement(doubles.intValue(), source);
    }
}
