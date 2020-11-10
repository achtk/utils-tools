package com.chua.utils.tools.collects;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.collects.iterator.IndexIterator;
import com.chua.utils.tools.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 三元集合
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class HashTripleMap<L, M, R> implements TripleMap<L, M, R> {

    private final List<L> leftList = new ArrayList<>();
    private final List<M> middleList = new ArrayList<>();
    private final List<R> rightList = new ArrayList<>();

    @Override
    public int size() {
        return leftList.size();
    }

    @Override
    public void put(L left, M middle, R right) {
        leftList.add(left);
        middleList.add(middle);
        rightList.add(right);
    }

    @Override
    public ListMultiValueMap<M, R> byLeft(L left) {
        ListMultiValueMap<M, R> result = new MultiValueMap<>();
        IndexIterator indexIterator = new IndexIterator(leftList, left);
        while (indexIterator.hasNext()) {
            Integer integer = indexIterator.next();
            result.add(middleList.get(integer), rightList.get(integer));
        }

        return result;
    }

    @Override
    public ListMultiValueMap<L, R> byMiddle(M middle) {
        ListMultiValueMap<L, R> result = new MultiValueMap<>();
        IndexIterator indexIterator = new IndexIterator(middleList, middle);
        while (indexIterator.hasNext()) {
            Integer integer = indexIterator.next();
            result.add(leftList.get(integer), rightList.get(integer));
        }

        return result;
    }

    @Override
    public ListMultiValueMap<L, M> byRight(R right) {
        ListMultiValueMap<L, M> result = new MultiValueMap<>();
        IndexIterator indexIterator = new IndexIterator(rightList, right);
        while (indexIterator.hasNext()) {
            Integer integer = indexIterator.next();
            result.add(leftList.get(integer), middleList.get(integer));
        }

        return result;
    }

    @Override
    public List<R> getRight(L left, M middle) {
        if (left == null || middle == null) {
            throw new NullPointerException("The left and middle parameters cannot be empty!");
        }
        List<R> list = new ArrayList<>();
        IndexIterator indexIterator = new IndexIterator(leftList, left);
        while (indexIterator.hasNext()) {
            Integer integer = indexIterator.next();
            M m = middleList.get(integer);
            if (middle.equals(m)) {
                list.add(rightList.get(integer));
            }
        }
        return list;
    }

    @Override
    public List<M> getMiddle(L left, R right) {
        if (right == null || right == null) {
            throw new NullPointerException("The right and right parameters cannot be empty!");
        }
        List<M> list = new ArrayList<>();
        IndexIterator indexIterator = new IndexIterator(leftList, left);
        while (indexIterator.hasNext()) {
            Integer integer = indexIterator.next();
            R r = rightList.get(integer);
            if (right.equals(r)) {
                list.add(middleList.get(integer));
            }
        }
        return list;
    }

    @Override
    public List<L> getLeft(M middle, R right) {
        if (right == null || middle == null) {
            throw new NullPointerException("The right and middle parameters cannot be empty!");
        }
        List<L> list = new ArrayList<>();
        IndexIterator indexIterator = new IndexIterator(rightList, right);
        while (indexIterator.hasNext()) {
            Integer integer = indexIterator.next();
            M m = middleList.get(integer);
            if (middle.equals(m)) {
                list.add(leftList.get(integer));
            }
        }
        return list;
    }
}
