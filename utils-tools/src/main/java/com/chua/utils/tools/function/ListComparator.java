package com.chua.utils.tools.function;

import java.util.Comparator;
import java.util.Objects;

/**
 * 集合基础比较方法
 * @author Administrator
 */
public interface ListComparator {
    /**
     * 长度比较
     */
    public static final Comparator LENGTH_COMPARATOR = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            return Objects.toString(o1).length() - Objects.toString(o2).length();
        }
    };
}
