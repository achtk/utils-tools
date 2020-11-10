package com.chua.utils.tools.tree;

import lombok.Data;

/**
 * B+树的定义：
 * 1.任意非叶子结点最多有M个子节点；且M>2；M为B+树的阶数
 * 2.除根结点以外的非叶子结点至少有 (M+1)/2个子节点；
 * 3.根结点至少有2个子节点；
 * 4.除根节点外每个结点存放至少（M-1）/2和至多M-1个关键字；（至少1个关键字）
 * 5.非叶子结点的子树指针比关键字多1个；
 * 6.非叶子节点的所有key按升序存放，假设节点的关键字分别为K[0], K[1] … K[M-2],指向子女的指针分别为P[0], P[1]…P[M-1]。则有：
 * 　　　P[0] < K[0] <= P[1] < K[1] …..< K[M-2] <= P[M-1]
 * 7.所有叶子结点位于同一层；
 * 8.为所有叶子结点增加一个链指针；
 * 9.所有关键字都在叶子结点出现
 */

@SuppressWarnings("all")
@Data
public class BPlusTree<K extends Comparable<K>, V> {

    /**
     * 根节点
     */
    protected BPlusNode<K, V> root;

    /**
     * 阶数，M值
     */
    protected int order;

    /**
     * 叶子节点的链表头
     */
    protected BPlusNode<K, V> head;

    /**
     * 树高
     */
    protected int height = 0;

    /**
     * 插入数据
     *
     * @param key   索引
     * @param value 值
     */
    public void insertOrUpdate(K key, V value) {
        root.insertOrUpdate(key, value, this);
    }

    /**
     * 排序数据
     *
     * @param order
     */
    public BPlusTree(int order) {
        if (order < 3) {
            throw new IllegalStateException("order must be greater than 2");
        }
        this.order = order;
        root = new BPlusNode<K, V>(true, true);
        head = root;
    }

    /**
     * 获取数据
     *
     * @param key 索引
     * @return 数据
     */
    public V get(K key) {
        return root.get(key);
    }

    /**
     * 删除数据
     *
     * @param key 索引
     * @return 数据
     */
    public V remove(K key) {
        return root.remove(key, this);
    }

    /**
     * @return
     */
    public PrintTree createPrintTree() {
        return this.root.print(this.root);
    }
}