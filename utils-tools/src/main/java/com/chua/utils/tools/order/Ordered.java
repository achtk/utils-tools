package com.chua.utils.tools.order;

import com.chua.utils.tools.aware.OrderAware;
import com.chua.utils.tools.manager.parser.description.FieldDescription;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static com.chua.utils.tools.constant.NumberConstant.INDEX_NOT_FOUND;

/**
 * 排序
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/2
 */
public class Ordered {
    /**
     * 升序
     *
     * @param source 集合
     * @param <C>    Comparable
     */
    public static <C extends Comparable<C>> void asc(final List<C> source) {
        if (null == source || source.isEmpty()) {
            return;
        }
        source.sort(Comparator.naturalOrder());
    }

    /**
     * 升序
     *
     * @param source 集合
     * @param key    对象字段(必须Integer类型)
     * @param <C>    Comparable
     */
    public static <C> void asc(final List<C> source, final String key) {
        if (null == source || source.isEmpty()) {
            return;
        }
        source.sort((o1, o2) -> {
            Integer order1 = calc(o1, key);
            Integer order2 = calc(o2, key);
            return Comparator.<Integer>naturalOrder().compare(order1, order2);
        });
    }

    /**
     * 升序
     *
     * @param source   集合
     * @param function 回调
     * @param <C>      Comparable
     */
    public static <C> void asc(final List<C> source, final Function<C, Integer> function) {
        if (null == source || source.isEmpty()) {
            return;
        }
        source.sort((o1, o2) -> {
            Integer order1 = function.apply(o1);
            Integer order2 = function.apply(o2);
            return Comparator.<Integer>naturalOrder().compare(order1, order2);
        });
    }

    /**
     * 计算整型
     *
     * @param o1  对象
     * @param key 索引
     * @param <C> 排序对象
     * @return 整型
     */
    private static <C> Integer calc(C o1, String key) {
        if (null == o1) {
            return INDEX_NOT_FOUND;
        }
        if (o1 instanceof OrderAware) {
            return ((OrderAware) o1).order();
        }

        if (null == key) {
            return INDEX_NOT_FOUND;
        }
        FieldDescription<C> fieldDescription = new FieldDescription<>(o1, key);
        Object value = fieldDescription.get();
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 降序
     *
     * @param source 集合
     * @param <C>    Comparable
     */
    public static <C extends Comparable<C>> void desc(final List<C> source) {
        if (null == source || source.isEmpty()) {
            return;
        }
        source.sort(Comparator.reverseOrder());
    }

    /**
     * 升序
     *
     * @param source 集合
     * @param key    对象字段(必须Integer类型)
     * @param <C>    Comparable
     */
    public static <C> void desc(final List<C> source, final String key) {
        if (null == source || source.isEmpty()) {
            return;
        }
        source.sort((o1, o2) -> {
            Integer order1 = calc(o1, key);
            Integer order2 = calc(o2, key);
            return Comparator.<Integer>reverseOrder().compare(order1, order2);
        });
    }

    /**
     * 升序
     *
     * @param source   集合
     * @param function 回调
     * @param <C>      Comparable
     */
    public static <C> void desc(final List<C> source, final Function<C, Integer> function) {
        if (null == source || source.isEmpty()) {
            return;
        }
        source.sort((o1, o2) -> {
            Integer order1 = function.apply(o1);
            Integer order2 = function.apply(o2);
            return Comparator.<Integer>reverseOrder().compare(order1, order2);
        });
    }
}
