package com.chua.utils.tools.collects.collections;

import com.chua.utils.tools.common.BooleanHelper;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * List工具类
 *
 * @author CH
 */
public class ListHelper {

    /**
     * 初始化
     *
     * @param <T>
     * @return
     */
    public static <T> List<T> newArrayList() {
        return new ArrayList<T>();
    }

    /**
     * 交集
     * <pre>
     *     ListHelper.intersection([0,2,4,6], [0,1,3,5]) = [0]
     * </pre>
     *
     * @param source1 集合1
     * @param source2 集合2
     * @return Collection
     */
    public static <T> Collection<T> intersection(Collection<T> source1, Collection<T> source2) {
        if (null == source1 || null == source2) {
            return null;
        }

        return source1.retainAll(source2) ? source1 : null;
    }

    /**
     * 差集
     * <pre>
     *     ListHelper.difference([0,2,4,6], [0,1,3,5]) = [2,4,6]
     * </pre>
     *
     * @param source1 集合1
     * @param source2 集合2
     * @return Collection
     */
    public static <T> Collection<T> difference(Collection<T> source1, Collection<T> source2) {
        if (null == source1 || null == source2) {
            return null;
        }
        return source1.removeAll(source2) ? source1 : null;
    }

    /**
     * 差集
     * <pre>
     *     ListHelper.unionDifference([0,2,4,6], [0,1,3,5]) = [1, 2, 3, 4, 5, 6]
     * </pre>
     *
     * @param source1 集合1
     * @param source2 集合2
     * @return Collection
     */
    public static <T> Collection<T> unionDifference(Collection<T> source1, Collection<T> source2) {
        if (null == source1 || null == source2) {
            return null;
        }
        Collection<T> source1Copy = Collections.synchronizedCollection(source1);
        source1.removeAll(source2);
        source2.removeAll(source1Copy);

        return source1.addAll(source2) ? source1 : null;
    }

    /**
     * 并集
     *
     * @param source1 集合1
     * @param source2 集合2
     * @return Collection
     */
    public static <T> Collection<T> union(Collection<T> source1, Collection<T> source2) {
        if (null == source1 || null == source2) {
            return null;
        }
        return source1.addAll(source2) ? source1 : null;
    }

    /**
     * 笛卡尔积
     *
     * @param item  第一个数据集
     * @param items 元素 第二个数据集
     * @param <T>   类型
     * @return 笛卡尔积
     */
    @SafeVarargs
    public static <T> List<String> descartes(List<T> item, List<T>... items) {
        if (!BooleanHelper.hasAllLength(items)) {
            return null;
        }
        List<List<T>> str;
        if (null == item || item.isEmpty()) {
            str = new ArrayList<>();
            Collections.addAll(str, items);
        } else {
            str = Lists.asList(item, items);
        }
        int total = 1;
        for (List<T> ts : str) {
            total *= ts.size();
        }

        String[] result = new String[total];
        int now = 1;
        //每个元素循环打印个数
        int itemLoop;
        //循环次数
        int loop;

        for (List<T> ts : str) {
            now *= ts.size();
            int index = 0;
            int currentSize = ts.size();
            itemLoop = total / now;
            loop = total / (itemLoop * currentSize);
            int myIndex = 0;

            for (int i = 0; i < ts.size(); i++) {
                for (int j = 0; j < loop; j++) {
                    if (myIndex == ts.size()) {
                        myIndex = 0;
                    }
                    for (int k = 0; k < itemLoop; k++) {
                        result[index] = (null == result[index] ? "" : result[index] + ",") + ts.get(myIndex);
                        index++;
                    }
                    myIndex++;
                }
            }
        }
        return Arrays.asList(result);
    }
}
