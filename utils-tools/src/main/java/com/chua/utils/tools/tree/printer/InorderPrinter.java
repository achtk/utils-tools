package com.chua.utils.tools.tree.printer;

import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.tree.PrintTree;

/**
 * ┌──800
 * ┌──760
 * │   └──600
 * ┌──540
 * │   └──476
 * │       └──445
 * ┌──410
 * │   └──394
 * 381
 * │     ┌──190
 * │     │   └──146
 * │  ┌──40
 * │  │  └──35
 * └──12
 * └──9
 *
 * @author MJ Lee
 */
public final class InorderPrinter implements Printer {
    private static final String rightAppend;
    private static final String leftAppend;
    private static final String blankAppend;
    private static final String lineAppend;

    static {
        int length = 2;
        rightAppend = "┌" + StringHelper.repeat("─", length);
        leftAppend = "└" + StringHelper.repeat("─", length);
        blankAppend = StringHelper.blank(length + 1);
        lineAppend = "│" + StringHelper.blank(length);
    }

    private final PrintTree tree;

    public InorderPrinter(PrintTree tree) {
        this.tree = tree;
    }

    @Override
    public String printString() {
        StringBuilder string = new StringBuilder(
                printString(tree.root(), "", "", ""));
        string.deleteCharAt(string.length() - 1);
        return string.toString();
    }

    /**
     * 生成node节点的字符串
     *
     * @param nodePrefix  node那一行的前缀字符串
     * @param leftPrefix  node整棵左子树的前缀字符串
     * @param rightPrefix node整棵右子树的前缀字符串
     * @return
     */
    private String printString(
            Object node,
            String nodePrefix,
            String leftPrefix,
            String rightPrefix) {
        Object left = tree.left(node);
        Object right = tree.right(node);
        String string = tree.string(node).toString();

        int size = 2;
        int length = string.length();
        if (length % size == 0) {
            length--;
        }
        length >>= 1;

        String nodeString = "";
        if (right != null) {
            rightPrefix += StringHelper.blank(length);
            nodeString += printString(right,
                    rightPrefix + rightAppend,
                    rightPrefix + lineAppend,
                    rightPrefix + blankAppend);
        }
        nodeString += nodePrefix + string + "\n";
        if (left != null) {
            leftPrefix += StringHelper.blank(length);
            nodeString += printString(left,
                    leftPrefix + leftAppend,
                    leftPrefix + blankAppend,
                    leftPrefix + lineAppend);
        }
        return nodeString;
    }
}
