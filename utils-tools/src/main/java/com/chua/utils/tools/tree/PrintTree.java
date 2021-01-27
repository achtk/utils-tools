package com.chua.utils.tools.tree;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/18
 */
public interface PrintTree<Node> {
    /**
     * who is the root node
     *
     * @return Node
     */
    Node root();

    /**
     * how to get the left child of the node
     *
     * @param node Node
     * @return Node
     */
    Node left(Node node);

    /**
     * how to get the right child of the node
     *
     * @param node Node
     * @return Node
     */
    Node right(Node node);

    /**
     * how to print the node
     *
     * @param node Node
     * @return 值
     */
    Object string(Node node);
}
