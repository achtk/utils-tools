package com.chua.utils.tools.example;

import com.chua.utils.tools.example.entity.Product;
import com.chua.utils.tools.tree.AvlTree;
import com.chua.utils.tools.tree.BPlusTree;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/18
 */
public class TreeExample {
    public static void main(String[] args) {
        //测试Avl树
        testAvlTree();
        //测试B+树
        testBPlusTree();
    }

    private static void testBPlusTree() {
        System.out.println("===================================测试B+树===============================");
        BPlusTree<Integer, Product> bPlusTree = new BPlusTree<>(4);

        long time1 = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            Product p = new Product(i, "test", 1.0 * i);
            bPlusTree.insert(p.getId(), p);
        }
        long time2 = System.currentTimeMillis();
        Product p1 = bPlusTree.find(345);
        long time3 = System.currentTimeMillis();

        System.out.println("插入耗时: " + (time2 - time1) + "ms");
        System.out.println("查询耗时: " + (time3 - time2) + "ms");
    }

    private static void testAvlTree() {
        System.out.println("===================================测试Avl树===============================");
        // 这组元素用来新增、删除、查询测试用
        Integer integerArray[] = {20, 10, 30, 7,
                5, 8, 6, 15, 17, 16, 24, 25, 36,
                23, 22, 33, 38, 31, 32, 37, 10, 11, 100};

        // 这组不平衡树，用来做旋转测试用
        // Integer integerArray[] = {20, 10, 30, 7, 5, 11};

        // Integer integerArray[] = { 5, 4, 3, 1 };

        AvlTree<Integer> tree = new AvlTree<>();

        // 1、插入节点
        for (int i = 0; i < integerArray.length; i++) {
            tree.addNode(integerArray[i]);
        }
        //打印树
        tree.print();

        // 旋转根节点
        tree.reBalance(tree.root);
        // 可以递归旋转其他节点

        // 2、查找节点
        AvlTree.Node<Integer> node = tree.findNode(30);
        System.out.println(node.value);

        // 3、遍历节点
        // tree.getAllNodeWithParent();

        //4、查找父节点
        // int findParentValue = 10;
        // AVLTree.Node<Integer> parentNode =
        //  tree.findParentNode(findParentValue);
        // if (parentNode != null) {
        // System.out.println(findParentValue +
        // " 's parentNode is:" +
        // parentNode.value);
        // }
        // // 5、删除节点
        int deleteValue = 36;
        boolean isDel = tree.deleteNode(deleteValue);
        System.out.println(isDel);

    }
}
