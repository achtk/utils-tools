package com.chua.utils.tools.tree;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 打印树
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
@RequiredArgsConstructor
public class PrintTree {

    @NonNull
    private final BPlusNode root;
    /**
     * 计算最大偏移量
     */
    private Map<Integer, PrintLine> lineMap = new HashMap<>();

    /**
     * 打印
     */
    public void print() {
        // 找到左边的最大偏移量
        int maxLeftOffset = findMaxOffset(root, 0, true);
        int maxRightOffset = findMaxOffset(root, 0, false);
        int offset = Math.max(maxLeftOffset, maxRightOffset);

        calculateLines(root, offset, 0, true);
        Iterator<Integer> lineNumbers = lineMap.keySet().iterator();
        int maxLine = 0;
        while (lineNumbers.hasNext()) {
            int lineNumber = lineNumbers.next();
            if (lineNumber > maxLine) {
                maxLine = lineNumber;
            }
        }

        for (int i = 0; i <= maxLine; i++) {
            PrintLine line = lineMap.get(i);
            if (line != null) {
                System.out.println(line.getLineString());
            }
        }

    }

    private void calculateLines(BPlusNode bPlusNode, int offset, int level, boolean right) {
        if (bPlusNode == null) {
            return;
        }
        int nameOffset = bPlusNode.toString().length() / 2;
        PrintLine line = lineMap.get(level);
        if (line == null) {
            line = new PrintLine();
            lineMap.put(level, line);
        }
        String name = "";
        List<Map.Entry<?, ?>> list = bPlusNode.entries;
        for (Map.Entry<?, ?> entry : list) {
            name += "|" + entry.getKey();
        }
        line.putString(right ? offset : (offset - nameOffset), name.length() > 0 ? name.substring(1) : name);

        List<BPlusNode> children = bPlusNode.children;
        if(null == children) {
            return;
        }
        for (BPlusNode child : children) {
            calculateLines(child, offset, level, nameOffset);
        }

    }

    private void calculateLines(BPlusNode parent, int offset, int level, int nameOffset) {
        // 判断有没有下一级
        if (parent.getPrevious() == null && parent.getNext() == null) {
            return;
        }
        // 如果有，添加分割线即/\
        PrintLine separateLine = lineMap.get(level + 1);
        if (separateLine == null) {
            separateLine = new PrintLine();
            lineMap.put(level + 1, separateLine);
        }

        if (parent.getPrevious() != null) {
            separateLine.putString(offset - 1, "/");
            calculateLines(parent.getPrevious(), offset - nameOffset - 1, level + 2, false);
        }

        if (parent.getNext() != null) {
            separateLine.putString(offset + nameOffset + 1, "\\");
            calculateLines(parent.getNext(), offset + nameOffset + 1, level + 2, true);
        }
    }

    /**
     * 需要打印的某一行
     *
     * @author zhuguohui
     */
    private static class PrintLine {
        /**
         * 记录了offset和String的map
         */
        Map<Integer, String> printItemsMap = new HashMap<>();
        int maxOffset = 0;

        public void putString(int offset, String info) {
            printItemsMap.put(offset, info);
            if (offset > maxOffset) {
                maxOffset = offset;
            }
        }

        public String getLineString() {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i <= maxOffset; i++) {
                String info = printItemsMap.get(i);
                if (info == null) {
                    buffer.append(" ");
                } else {
                    buffer.append(info);
                    i += info.length();
                }
            }
            return buffer.toString();
        }

    }

    private static int findMaxOffset(BPlusNode parent, int offset, boolean findLeft) {
        if (parent != null) {
            offset += parent.toString().length();
        }
        if (findLeft && parent.getPrevious() != null) {
            offset += 1;
            return findMaxOffset(parent.getPrevious(), offset, findLeft);
        }
        if (!findLeft && parent.getNext() != null) {
            return findMaxOffset(parent.getNext(), offset, findLeft);
        }
        return offset;
    }
}
