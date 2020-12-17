package com.chua.utils.tools.collects;

import java.util.List;

/**
 * the loop list
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public interface LoopList<E> extends List<E> {
    /**
     * 获取节点
     * @param index 索引
     * @return 节点
     */
    Node<E> getNode(int index);

    /**
     * 节点
     *
     * @param <E>
     */
    final class Node<E> {
        /**
         * 首元节点
         */
        public E data;

        /**
         * 前一个指针
         */
        public Node<E> before;

        /**
         * 后一个指针
         */
        public Node<E> after;

        public Node(E data, Node<E> before, Node<E> after) {
            this.data = data;
            this.before = before;
            this.after = after;
        }
    }
}
