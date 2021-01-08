package com.chua.utils.tools.tree;

import com.chua.utils.tools.logger.LogUtils;
import com.chua.utils.tools.tree.printer.LevelOrderPrinter;

/**
 * 创建一颗平衡二叉树，支持泛型对象E，支持E排序
 * avl tree
 *
 * @author Ethan
 * @version 1.0.0
 * @since 2020/12/18
 */
public class AvlTree<E extends Comparable<E>> implements PrintTree<AvlTree.Node> {
    /**
     * 单独指定根节点
     */
    public Node<E> root;

    /**
     * 构造方法，初始化根节点为null
     */
    public AvlTree() {
        this.root = null;
    }

    /**
     * 打印树
     */
    public void print() {
        LogUtils.println(new LevelOrderPrinter(this).printString());
    }

    @Override
    public Node root() {
        return root;
    }

    @Override
    public Node left(Node node) {
        return node.left;
    }

    @Override
    public Node right(Node node) {
        return node.right;
    }

    @Override
    public Object string(Node node) {
        return node.value;
    }

    /**
     * 内部类Node树对象，存值E value，左子节点，
     * 右子节点 Node<E>表示泛型，可以支持对传入类型的排序存储
     */
    public static class Node<E> {
        public E value;
        Node<E> left;
        Node<E> right;
        // 值0表示根节点，值1表示左节点，值2表示右节点
        // 如图中的节点5、7、10、22、23、24、30都属于左节点类型，
        // isLeftRight属性值为1;
        // 如图中的15、17、30、36、38都属于右节点类型,
        // isLeftRight属性值为2;
        Integer isLeftRight;

        public Node(E value, Node<E> leftNode, Node<E> rightNode, Integer isLeftRight) {
            this.value = value;
            this.left = leftNode;
            this.right = rightNode;
            this.isLeftRight = isLeftRight;
        }
    }

    /**
     * 1.1、为树添加节点，提供对外的方法
     *
     * @param e e
     */
    public Node<E> addNode(E e) {
        return insert(e, root);
    }

    /**
     * 1.2、为树添加节点，内部实现
     */
    private Node<E> insert(E e, Node<E> node) {

        // 添加的第一个元素为根节点
        if (null == root) {
            root = new Node<E>(e, null, null, 0);
            System.out.println("root:" + root.value);
            return root;
        }
        // 比较插入的元素，与节点的大小，如果大于，则往节点右边插入
        // 如果小于该节点，反之左边（第一次与root节点比较）
        int compareInt = e.compareTo(node.value);

        if (compareInt > 0) {
            if (node.right != null) {
                // 右边有子节点，继续递归调用
                insert(e, node.right);
            } else {
                Node<E> rightNode = new Node<E>(e, null, null, 2);
                // 创建单向的父子节点关系，父节点right指向子节点;
                node.right = rightNode;
                return rightNode;
            }
        } else {
            if (node.left != null) {
                // 左边有子节点，继续递归调用
                insert(e, node.left);
            } else {
                Node<E> leftNode = new Node<E>(e, null, null, 1);
                // 创建单向的父子节点关系，父节点left指向子节点;
                node.left = leftNode;
                // 插入一次节点，做一次平衡操作
                return leftNode;
            }
        }
        return node;
    }

    /**
     * 2.1、外部方法，查找节点
     *
     * @param e e
     * @return Node
     */
    public Node<E> findNode(E e) {
        if (root == null) {
            return null;
        }

        if (e.compareTo(root.value) == 0) {
            return root;
        }
        return findNode(e, root);
    }

    /**
     * 2.2、内部方法，查找节点
     */
    public Node<E> findNode(E e, Node<E> node) {

        if (e.compareTo(node.value) == 0) {
            return node;
        }
        // 大于从右边继续找
        if (e.compareTo(node.value) > 0) {

            if (node.right != null) {
                return findNode(e, node.right);
            } else {
                return null;
            }
        }
        // 小于从左边继续找
        else if (e.compareTo(node.value) < 0) {
            if (node.left != null) {
                return findNode(e, node.left);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 3.1、对外遍历节点方法
     * <p>
     * 前序遍历：先遍中节点，然后左子节点、右子节点
     * <p>
     * 中序遍历：左、中、右
     * <p>
     * 后续遍历：左、右、中
     */
    public void getAllNode() {
        if (root == null) {
            return;
        }
        getAllNode(root);
    }

    /**
     * 3.2、内部方法，前序遍历节点
     */
    private void getAllNode(Node<E> e) {
        if (e.left != null) {
            System.out.println("父节点：" + e.value);
            System.out.println("左子节点：" + e.left.value);
            getAllNode(e.left);
        }
        if (e.right != null) {
            System.out.println("父节点：" + e.value);
            System.out.println("右子节点：" + e.right.value);
            getAllNode(e.right);
        }
    }

    /**
     * 3.3、对外提供方法，遍历节点，顺便打印出结构
     */
    public void getAllNodeWithParent() {
        System.out.println("根节点" + root.value);
        getAllNodeWithParent(root);
    }

    /**
     * 3.4、内部方法，遍历节点，顺便打印出结构
     */
    private void getAllNodeWithParent(Node<E> node) {

        if (node.left == null && node.right == null) {
            System.out.println("叶子节点值:" + node.value);
        } else {
            System.out.println("父节点值:" + node.value);
        }

        if (node.left != null) {
            System.out.println(node.value + "的左子节点值:"
                    + node.left.value);
            getAllNodeWithParent(node.left);
        }
        if (node.right != null) {
            System.out.println(node.value + "的右子节点值:"
                    + node.right.value);
            getAllNodeWithParent(node.right);
        }
    }

    /**
     * 4.1、找到父节点
     */
    public Node<E> findParentNode(E e) {
        return findParentNode(root, e);
    }

    /**
     * 4.2、内部方法，找到父节点
     *
     * @param e    e
     * @param node node
     * @return Node
     */
    private Node<E> findParentNode(Node<E> node, E e) {

        // 父节点为空，或者该节点为root节点，那么root的父节点就不存在了
        if (root == null || root.value.compareTo(e) == 0) {
            return null;
        } else {// 当前节点的左子节点或右子节点值等于e，返回当前节点
            if (node.left != null
                    && node.left.value.compareTo(e) == 0
                    || node.right != null
                    && node.right.value.compareTo(e) == 0) {
                return node;
            }
            // 递归继续从左边查找
            else if (node != null
                    && node.value.compareTo(e) > 0) {
                return findParentNode(node.left, e);
            } // 递归继续从右边查找
            else if (node != null && node.value.compareTo(e) < 0) {
                return findParentNode(node.right, e);
            } else {
                return null;
            }
        }

    }

    /**
     * 5.1、删除节点
     *
     * @return
     */

    public boolean deleteNode(E e) {
        if (root == null) {
            return false;
        }
        Node<E> node = findNode(e);
        // a、先查找此节点是否存在
        if (node == null) {
            return false;
        }
        // b、节点存在，则需要找到它的父节点
        else {
            Node<E> parentNode = findParentNode(root, e);
            System.out.println(e + "的父亲节点是:"
                    + parentNode.value + "(在删除方法中)");
            if (parentNode != null) {
                // c、如果需要删除的节点恰好是叶子节点（没有左右子节点了）
                if (node.left == null
                        && node.right == null) {
                    // d、如果要删除的节点是他父亲节点的左边子节点
                    if (parentNode.left != null
                            && parentNode.left.value.
                            compareTo(node.value) == 0) {
                        parentNode.left = null;

                        return true;
                    }
                    // e、如果要删除的节点是他父亲节点的右边子节点
                    else {
                        parentNode.right = null;
                    }

                    // 删除一次节点，做一次平衡操作

                    return true;
                }

                // f、如果需要删除的节点有一个左子节点
                if (node.left != null
                        && node.right == null) {
                    // 如果需要删除的节点是一个左节点
                    // 则让父亲节点左边指向他的左子节点
                    if (parentNode.isLeftRight == 1) {
                        parentNode.left = node.left;
                        // 删除一次节点，做一次平衡操作

                        return true;
                    }
                    // 如果需要删除的节点是一个右节点
                    // 则让父亲节点右边指向他的左子节点
                    else if (parentNode.isLeftRight == 2) {
                        parentNode.right = node.left;

                        // 删除一次节点，做一次平衡操作

                        return true;
                    }
                }

                // g、如果需要删除的节点有一个右子节点
                if (node.right != null
                        && node.left == null) {
                    // 如果需要删除的节点是一个左节点
                    // 则让父亲节点左边指向他的右子节点
                    if (parentNode.isLeftRight == 1) {
                        parentNode.left = node.right;
                        // 删除一次节点，做一次平衡操作
                        return true;
                    }
                    // 如果需要删除的节点是一个右节点
                    // 则让父亲节点右边指向他的右子节点
                    else if (parentNode.isLeftRight == 2) {
                        parentNode.right = node.right;

                        // 删除一次节点，做一次平衡操作

                        return true;
                    }
                }

                // h、如果需要删除的节点有两个子节点
                // （删除根节点时也适用）
                if (node.left != null
                        && node.right != null) {
                    // 删除右子树中最小节点，获取到最小节点的值;
                    // 把最小节点的值，赋值给要删除节点的值;
                    // （实际上并不是删除该节点，只是改变赋值）;
                    // 一般右边最小节点值，位于右子节点
                    // 下面的最左的子节点;

                    Node<E> minNode = deleteGetMin(
                            node.right);
                    // 找到该最小节点的父节点
                    Node<E> minNodeParent =
                            findParentNode(minNode.value);
                    // 找到了最小的左节点，不可能再有左子节点
                    if (minNode.right != null) {
                        // 然后把找到的最小值minNode.value
                        // 赋值给要删除的节点
                        // 最小节点肯定是左子节点
                        node.value = minNode.value;
                        // 如果左最小节点minNode是一个右节点
                        // （右边全是右节点）
                        if (minNode.isLeftRight == 2) {
                            minNodeParent.right =
                                    minNode.right;
                            // 删除一次节点，做一次平衡操作
                            return true;
                        }
                        // 如果左最小节点minNode是一个左节点
                        else {
                            // 最小节点的父节点,的左子节点
                            //则指向最小节点的右子节点
                            minNodeParent.left =
                                    minNode.right;
                            // 删除一次节点，做一次平衡操作
                            return true;
                        }
                    }
                    // 得到的右子树最小节点，已经是一个叶子
                    // 节点了，直接赋值
                    else {
                        // 如果左最小节点minNode是一个右节点
                        // （右边全是右节点）
                        if (minNode.isLeftRight == 2) {
                            node.value = minNode.value;
                            // 最小节点的父节点,的右子节点
                            // 则指向空
                            minNodeParent.right = null;
                            // 删除一次节点，做一次平衡操作
                            return true;
                        } else {
                            // 然后把找到的最小值minNode.value
                            //赋值给要删除的节点
                            // 最小节点肯定是左子节点
                            node.value = minNode.value;
                            // 最小节点的父节点,的左子节点则指向空
                            minNodeParent.left = null;
                            // 删除一次节点，做一次平衡操作
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 5.2、获取右边最小节点值，位于右子节点下面的最左的子节点;
     *
     * @param node node
     * @return Node
     */
    private Node<E> deleteGetMin(Node<E> node) {

        // 如果左边一直有节点，那么一直找到最小的
        // 非递归写法
        while (node.left != null) {
            node = node.left;
        }
        return node;
        //
        // //递归写法
        // if (node.left != null) {
        // return deleteGetMin(node.left);
        // }
        // return node;

    }

    /**
     * 6.1、判断该节点是属于左节点(返回1)还是右节点(返回2)
     * <p>
     * 也可用Node内部类isLeftRight判断
     */

    public int isLeftRight(E e) {
        // 根节点返回0
        if (root.value.compareTo(e) == 0) {
            return 0;
        }
        return isLeftRight(root, e);
    }

    /**
     * 6.2、判断该节点是属于左节点(返回1)还是右节点(返回2)
     * <p>
     * 也可用Node内部类isLeftRight判断
     */

    private int isLeftRight(Node<E> node, E e) {

        // 是node的左子节点
        if (node.left != null
                && node.left.value.compareTo(e) == 0) {
            return 1;
        } // 是node的右子节点
        if (node.right != null
                && node.right.value.compareTo(e) == 0) {
            return 2;
        }
        // 子节点nodeSon比node节点小，往左边递归
        if (node.left != null
                && e.compareTo(node.value) < 0) {
            return isLeftRight(node.left, e);
        }
        // 子节点nodeSon比node节点大，往右边递归
        if (node.right != null
                && e.compareTo(node.value) > 0) {
            return isLeftRight(node.right, e);
        }
        return -1;

    }

    /**
     * 7.1求树子节点的高度
     * <p>
     * 在Node内部类里面有height方法可用，求高度
     */

    public int heigth(E e) {
        if (e == null) {
            return 0;
        }
        // 先找到节点
        Node<E> node = findNode(e);
        // 递归它的子树的高度，加上自己的高度
        return heigth(node) + 1;

    }

    /**
     * 7.2求树子节点的高度
     * <p>
     * 在Node内部类里面有height方法可用，求高度
     */

    private int heigth(Node<E> node) {
        if (node == null) {
            return -1;
        }
        return Math.max(
                node.left == null ? 0
                        : heigth(node.left) + 1,
                node.right == null ? 0
                        : heigth(node.right) + 1);
    }

    /**
     * 左左情况一之向右旋转
     * <p>
     * 8.1树右旋转
     */

    private void rightRotate(Node<E> node) {

        // （a）、创建一个新的节点，值等于当前节点的值
        Node<E> newNode = new Node<E>(node.value, null,
                null, 0);
        // （b）、把新节点的右子树设置为当前节点的右子树
        if (node.right != null) {
            newNode.right = node.right;
        }
        // （c）、把新节点左子树设置为当前节点的左子树的右子树
        if (node.left.right != null) {
            newNode.left = node.left.right;
        }
        // （d）、当前节点的值变成它的左子树的值
        node.value = node.left.value;
        // （e）、当前的左子节点变为当前节点的左子节点的左子节点
        node.left = node.left.left;
        // （f）、当前节点的右节点指向新节点
        node.right = newNode;
    }

    /**
     * 8.2树左旋转
     */

    private void leftRotate(Node<E> node) {
        // （a）、创建一个新的节点，值等于当前节点的值
        Node<E> newNode = new Node<E>(node.value, null,
                null, 0);
        // （b）、把新节点的左子树设置为当前节点的左子树
        if (node.left != null) {
            newNode.left = node.left;
        }
        // （c）、把新节点右子树设置为当前节点的右子树的左子树
        if (node.right.left != null) {
            newNode.right = node.right.left;
        }
        // （d）、当前节点的值变成它的右子树的值
        node.value = node.right.value;
        // （e）、当前的右子节点变为当前节点的右子节点的右子节点
        node.right = node.right.right;
        // （f）、当前节点的左节点指向新节点
        node.left = newNode;
    }

    /**
     * 9.1求节点的高度
     *
     * @param left
     * @return
     */
    public int height(Node<E> left) {
        if (left == null) {
            return 0;
        }
        return Math.max(
                left.left == null ? 0 : height(left.left),
                left.right == null ? 0 : height(left.right))
                + 1;
    }

    /**
     * 9.2求左子树的高度
     *
     * @param node
     * @return
     * @主要用于求与右树差值，做树的旋转操作
     */
    public int leftHeight(Node<E> node) {

        if (node == null) {
            return 0;
        }
        return height(node.left);
    }

    /**
     * 9.3求右子树的高度
     *
     * @param node
     * @return
     * @主要用于求与左树差值，做树的旋转操作
     */
    public int rightHeight(Node<E> node) {
        if (node == null) {
            return 0;
        }
        return height(node.right);
    }

    /**
     * 10.1平衡操作，旋转
     * <p>
     * 左左向右单旋转
     * <p>
     * 左左向左先旋转，再向右的双旋转
     * <p>
     * 右右向左单旋转
     * <p>
     * 右右向右先旋转，再向左的双旋转
     *
     * @param node
     */
    public void reBalance(Node<E> node) {

        System.out.println("旋转值节点：" + node.value);

        System.out.println("旋转值节点左高：" +
                leftHeight(node));

        System.out.println("旋转值节点右高：" +
                rightHeight(node));

        // 左左情况，该进行右旋转
        if (leftHeight(node) - rightHeight(node) > 1) {
            // 在进行右旋转的时候，如果该节点左边的子树
            // 的左高度比右高度小，那么需要先把该节点左边
            // 的子树进行一次左旋转，然后对该节点再进行右旋转
            // 这里就是双旋转了，先左旋转，再右旋转；
            if (node.left != null && leftHeight(
                    node.left) < rightHeight(node.left)) {

                System.out.println(
                        "双旋转开始，左旋:" + node.left.value);
                System.out.println("双旋转开始，右旋:"
                        + node.value);

                leftRotate(node.left);
                rightRotate(node);
            } else {
                // 如果只执行这一步操作，就是单旋转，右旋转

                System.out.println("单旋转开始，右旋:"
                        + node.value);

                rightRotate(node);
            }

        }

        // 同样，相反方向的右右情况，该进行左旋转

        // 右右情况，该进行左旋转
        if (rightHeight(node) - leftHeight(node) > 1) {
            // 在进行左旋转的时候，如果该节点右边的子树
            // 的右高度比左高度小，那么需要先把该节点右边
            // 的子树进行一次右旋转，然后对该节点再进行左旋转
            // 这里就是双旋转了，先右旋转，再左旋转；
            if (node.right != null && rightHeight(
                    node.right) < leftHeight(node.right)) {
                rightRotate(node.right);
                leftRotate(node);
            } else {
                // 如果只执行这一步操作，就是单旋转，左旋转
                leftRotate(node);
            }

        }
    }


}
