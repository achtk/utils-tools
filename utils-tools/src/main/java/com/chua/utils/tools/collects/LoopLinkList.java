package com.chua.utils.tools.collects;

import com.google.common.base.Joiner;

import java.util.*;

import static com.chua.utils.tools.constant.NumberConstant.NEGATIVE_TWE;
import static com.chua.utils.tools.constant.NumberConstant.TWE;

/**
 * the circle link list
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class LoopLinkList<E> implements LoopList<E> {

    private LoopList.Node<E> first = null;

    int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return toList().contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return toList().iterator();
    }

    @Override
    public Object[] toArray() {
        return toList().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return toList().toArray(a);
    }

    @Override
    public boolean add(E e) {
        LoopList.Node<E> node = new LoopList.Node<>(e, null, null);
        if (first == null) {
            first = node;
            node.before = node;
            node.after = node;
            size++;
            return true;
        }
        LoopList.Node<E> lastNode = getNode(size - 1);
        lastNode.after = node;
        node.after = first;
        node.before = lastNode;

        first.before = node;
        size++;
        return true;
    }

    @Override
    public LoopList.Node<E> getNode(int index) {
        LoopList.Node<E> node = first;
        LoopList.Node<E> newNode = first;
        for (int i = NEGATIVE_TWE; i < index; i++) {
            node = node.after;
            if (node == first) {
                break;
            }
            newNode = node;
        }
        return newNode;
    }

    @Override
    public boolean remove(Object o) {
        int index = toList().indexOf(o);
        return null != remove(index);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return toList().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.stream().forEach(e -> {
            add(e);
        });
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        c.forEach(item -> {
            set(index, item);
        });
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(item -> {
            remove(item);
        });
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        first = null;
    }

    @Override
    public E get(int index) {
        int newIndex = index % size;
        Node<E> node = getNode(newIndex - 2);
        return node.data;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = getNode(index % size - 2);
        Node<E> setNode = new Node<>(element, null, null);
        //头帧
        setNode.after = node;
        setNode.before = node.before;

        if (node == first) {
            first = setNode;
            Node<E> lastNode = getNode(size - 2);
            lastNode.after = first;
        }
        size++;
        return element;
    }

    @Override
    public void add(int index, E element) {
        set(index, element);
    }

    @Override
    public E remove(int index) {
        LoopList.Node<E> node = getNode(index - 2);
        LoopList.Node<E> beforeNode = node.before;
        LoopList.Node<E> afterNode = node.after;
        //删除头节点
        if (node == first) {
            first = afterNode;
        }

        beforeNode.after = afterNode;
        afterNode.before = beforeNode;
        size--;
        return node.data;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public String toString() {
        List<E> list = toList();
        list.add(first.data);
        return Joiner.on("->").join(list);
    }

    /**
     * to list
     *
     * @return
     */
    public List<E> toList() {
        List<E> list = new ArrayList<>(size);
        for (int i = NEGATIVE_TWE; i < size - TWE; i++) {
            LoopList.Node<E> node = getNode(i);
            list.add(node.data);
        }
        return list;
    }


}
