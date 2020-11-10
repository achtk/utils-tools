package com.chua.utils.tools.collects.iterator;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.function.OperatorIterator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据索引
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
@RequiredArgsConstructor
public class IndexIterator implements Iterator<Integer> {
    /**
     * 原始数据
     */
    @NonNull
    private final Collection<?> collection;
    /**
     * 数据索引
     */
    @NonNull
    private final Object key;
    /**
     * 计数器
     */
    private AtomicInteger atomicCount = new AtomicInteger(-1);
    /**
     * 索引位置
     */
    private int index;

    /**
     * 是否存在下一个索引
     *
     * @return 存在返回true
     */
    @Override
    public boolean hasNext() {
        if (!CollectionHelper.contains(collection, key)) {
            return false;
        }
        this.index = CollectionHelper.indexOf(collection, key, atomicCount.get());
        if (index != -1) {
            atomicCount.set(index + 1);
        }
        return index != -1;
    }

    /**
     * 获取索引位置
     *
     * @return
     */
    @Override
    public Integer next() {
        return index;
    }
}
