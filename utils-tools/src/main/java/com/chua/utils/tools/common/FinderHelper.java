package com.chua.utils.tools.common;

import com.chua.utils.tools.exceptions.NonUniqueException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_LEFT_BIG_PARENTHESES;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_LEFT_SQUARE_BRACKET;

/**
 * 查询器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/5/14 13:38
 */
public class FinderHelper {

    /**
     * 获取第一个元素
     * <p>
     * firstElement(null) = null
     * firstElement({k1: v1, k2, k2}) = k1
     * </p>
     *
     * @param source
     * @return
     */
    public static <K, T> K firstKeyElement(final Map<K, T> source) {
        return findKeyElement(0, true, source);
    }

    /**
     * 获取最后一个元素
     * <p>
     * lastElement({k1: v1, k2, v2}) = k2
     * </p>
     *
     * @param source
     * @return
     */
    public static <K, T> K lastKeyElement(final Map<K, T> source) {
        return findKeyElement(SizeHelper.getSize(source), true, source);
    }

    /**
     * 获取第{index}个元素
     * <p>
     * findElement(0, {k1: v1, k2, v2}) = k1
     * findElement(3, {k1: v1, k2, v2}) = null
     * findElement(-1, {k1: v1, k2, v2}) = null
     * </p>
     *
     * @param source 数据
     * @param index  索引
     * @return
     */
    public static <K, T> K findKeyElement(int index, final Map<K, T> source) {
        return findKeyElement(index, false, source);
    }


    /**
     * 获取第{index}个元素
     * <p>
     * findElement(0, false, {k1: v1, k2, v2}) = k1
     * findElement(3, false, {k1: v1, k2, v2}) = null
     * findElement(-1, false, {k1: v1, k2, v2}) = null
     * findElement(0, true, {k1: v1, k2, v2}) = k1
     * findElement(3, true, {k1: v1, k2, v2}) = k2
     * findElement(-1, true, {k1: v1, k2, v2}) = k1
     * </p>
     *
     * @param source 数据
     * @param index  索引
     * @param cross  是否允许索引越界
     * @return
     */
    public static <K, T> K findKeyElement(int index, final boolean cross, final Map<K, T> source) {
        if (!BooleanHelper.hasLength(source)) {
            return null;
        }
        int size = source.size();
        int count = 0;
        if (cross) {
            index = Math.max(index, 0);
            index = Math.min(size, index);
        } else if (index < 0 || index > size) {
            return null;
        }

        for (K key : source.keySet()) {
            if (count == index) {
                return key;
            }
        }
        return null;
    }

    /**
     * 获取第{index}个元素
     * <p>
     * findElement(0, {k1: v1, k2, v2}) = v1
     * findElement(3, {k1: v1, k2, v2}) = null
     * findElement(-1, {k1: v1, k2, v2}) = null
     * </p>
     *
     * @param source 数据
     * @param index  索引
     * @return
     */
    public static <T> T findElement(int index, final Map<String, T> source) {
        return findElement(index, false, source);
    }


    /**
     * 获取第{index}个元素
     * <p>
     * findElement(0, false, {k1: v1, k2, v2}) = v1
     * findElement(3, false, {k1: v1, k2, v2}) = null
     * findElement(-1, false, {k1: v1, k2, v2}) = null
     * findElement(0, true, {k1: v1, k2, v2}) = v1
     * findElement(3, true, {k1: v1, k2, v2}) = v2
     * findElement(-1, true, {k1: v1, k2, v2}) = v1
     * </p>
     *
     * @param source 数据
     * @param index  索引
     * @param cross  是否允许索引越界
     * @return
     */
    public static <T> T findElement(int index, final boolean cross, final Map<String, T> source) {
        if (!BooleanHelper.hasLength(source)) {
            return null;
        }
        int size = source.size();
        int count = 0;
        if (cross) {
            index = Math.max(index, 0);
            index = Math.min(size, index);
        } else if (index < 0 || index > size) {
            return null;
        }

        for (T value : source.values()) {
            if (count == index) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取第一个元素
     * <p>
     * firstElement([e1, e2, e3]) = e1
     * firstElement([]) = null
     * firstElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <T> T firstElement(final Collection<T> source) {
        return findElement(0, true, source);
    }

    /**
     * 有且只有一个元素时获取
     * <p>
     * getIfOnlyElement([e1, e2, e3]) = null
     * getIfOnlyElement([e1]) = e1
     * getIfOnlyElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <T> T getIfOnlyElement(final Collection<T> source) {
        if (!BooleanHelper.hasLength(source) || source.size() != 1) {
            throw new NonUniqueException();
        }
        return firstElement(source);
    }

    /**
     * 有且只有一个元素时获取
     * <p>
     * getIfOnlyElement([e1, e2, e3]) = null
     * getIfOnlyElement([e1]) = e1
     * getIfOnlyElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <K, V> V getIfOnlyElement(final Map<K, V> source) {
        if (!BooleanHelper.hasLength(source) || source.size() != 1) {
            throw new NonUniqueException();
        }
        return firstElement(source.values());
    }

    /**
     * 有且只有一个元素时获取
     * <p>
     * getIfOnlyElement([e1, e2, e3]) = null
     * getIfOnlyElement([e1]) = e1
     * getIfOnlyElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <T> T safeIfOnlyElement(final Collection<T> source) {
        if (!BooleanHelper.hasLength(source) || source.size() != 1) {
            return null;
        }
        return firstElement(source);
    }

    /**
     * 获取最后一个元素
     * <p>
     * lastElement([e1, e2, e3]) = e3
     * lastElement([]) = null
     * lastElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <T> T lastElement(final Collection<T> source) {
        return findElement(SizeHelper.getSize(source), true, source);
    }

    /**
     * 获取第{index}个元素
     * <p>
     * findElement(0, [e1, e2, e3]) = e1
     * findElement(4, [e1, e2, e3]) = null
     * findElement(-1, [e1, e2, e3]) = null
     * findElement(1, []) = null
     * findElement(1, null) = null
     * </p>
     *
     * @param source 数据
     * @param index  索引
     * @return
     */
    public static <T> T findElement(int index, final Collection<T> source) {
        return findElement(index, false, source);
    }

    /**
     * 获取第{index}个元素
     * <p>
     * findElement( 0, false, [e1, e2, e3]) = e1
     * findElement( 4, false, [e1, e2, e3]) = null
     * findElement( -1, false, [e1, e2, e3]) = null
     * findElement(1, false, []) = null
     * findElement(1, false, null) = null
     * findElement( 0, true, [e1, e2, e3]) = e1
     * findElement( 4, true, [e1, e2, e3]) = e3
     * findElement( -1, true, [e1, e2, e3]) = e1
     * findElement(1, false, []) = null
     * findElement( 1, false, null) = null
     * </p>
     *
     * @param source 数据
     * @param index  索引
     * @param cross  是否允许索引越界
     * @return
     */
    public static <T> T findElement(int index, final boolean cross, final Collection<T> source) {
        if (!BooleanHelper.hasLength(source)) {
            return null;
        }

        int size = source.size();

        if (cross) {
            index = Math.max(index, 0);
            index = Math.min(size, index);
        } else if (index < 0 || index > size) {
            return null;
        }

        int count = 0;
        Iterator<T> iterator = source.iterator();
        while (iterator.hasNext()) {
            if (count == index) {
                return iterator.next();
            }
        }

        return null;
    }


    /**
     * 获取第一个元素
     * <p>
     * firstElement([e1, e2, e3]) = e1
     * firstElement([]) = null
     * firstElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <T> T firstElement(final T[] source) {
        return findElement(0, true, source);
    }

    /**
     * 获取最后一个元素
     * <p>
     * lastElement([e1, e2, e3]) = e3
     * lastElement([]) = null
     * lastElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <T> T lastElement(final T[] source) {
        return findElement(SizeHelper.getSize(source), true, source);
    }

    /**
     * 获取第{index}个元素
     * <p>
     * findElement( 0, [e1, e2, e3]) = e1
     * findElement( 4, [e1, e2, e3]) = null
     * findElement( -1, [e1, e2, e3]) = null
     * findElement(1, []) = null
     * findElement(1, null) = null
     * </p>
     *
     * @param source 数据
     * @param index  索引
     * @return
     */
    public static <T> T findElement(int index, final T[] source) {
        return findElement(index, false, source);
    }

    /**
     * 获取第{index}个元素
     * <p>
     * findElement( 0, false, [e1, e2, e3]) = e1
     * findElement( 4, false, [e1, e2, e3]) = null
     * findElement( -1, false, [e1, e2, e3]) = null
     * findElement(1, false, []) = null
     * findElement(1, false, null) = null
     * findElement( 0, true, [e1, e2, e3]) = e1
     * findElement( 4, true, [e1, e2, e3]) = e3
     * findElement( -1, true, [e1, e2, e3]) = e1
     * findElement(1, false, []) = null
     * findElement(1, false, null) = null
     * </p>
     *
     * @param source 数据
     * @param index  索引
     * @param cross  是否允许索引越界
     * @return
     */
    public static <T> T findElement(int index, final boolean cross, final T[] source) {
        if (!BooleanHelper.hasLength(source)) {
            return null;
        }

        int size = source.length;

        if (cross) {
            index = Math.max(index, 0);
            index = Math.min(size, index);
        } else if (index < 0 || index > size) {
            return null;
        }

        return source[index];
    }

    /**
     * 获取第一个元素
     * <p>
     * firstElement([e1, e2, e3]) = e1
     * firstElement([]) = null
     * firstElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <T> T firstElement(final Iterable<T> source) {
        return findElement(0, source);
    }

    /**
     * 获取最后一个元素
     * <p>
     * lastElement([e1, e2, e3]) = e3
     * lastElement([]) = null
     * lastElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <T> T lastElement(final Iterable<T> source) {
        return findElement(SizeHelper.getSize(source), source);
    }


    /**
     * 获取第{index}个元素
     * <p>
     * findElement( 0, false, [e1, e2, e3]) = e1
     * findElement( 4, false, [e1, e2, e3]) = null
     * findElement( -1, false, [e1, e2, e3]) = null
     * findElement(1, false, []) = null
     * findElement(1, false, null) = null
     * findElement( 0, true, [e1, e2, e3]) = e1
     * findElement( 4, true, [e1, e2, e3]) = e3
     * findElement( -1, true, [e1, e2, e3]) = e1
     * findElement(1, false, []) = null
     * findElement(1, false, null) = null
     * </p>
     *
     * @param source 数据
     * @param index  索引
     * @return
     */
    public static <T> T findElement(int index, final Iterable<T> source) {
        if (!BooleanHelper.hasLength(source)) {
            return null;
        }

        T t = null;
        int count = 0;
        Iterator<T> iterator = source.iterator();
        while (iterator.hasNext()) {
            if (count == index) {
                t = iterator.next();
                break;
            }
        }
        return t;
    }


    /**
     * 获取第一个元素
     * <p>
     * firstElement([e1, e2, e3]) = e1
     * firstElement({k1: v1, k2:v2}) = v1
     * firstElement([]) = null
     * firstElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static Object firstElement(final String source) {
        if (!BooleanHelper.hasLength(source)) {
            return null;
        }

        if (source.startsWith(SYMBOL_LEFT_SQUARE_BRACKET) || source.startsWith(SYMBOL_LEFT_BIG_PARENTHESES)) {
            Object object = JsonHelper.toObject(source);
            if (object instanceof Collection) {
                return firstElement((Collection) object);
            } else if (object instanceof Map) {
                return firstElement((Map) object);
            }
        }
        return null;
    }

    /**
     * 获取最后一个元素
     * <p>
     * lastElement([e1, e2, e3]) = e3
     * lastElement({k1: v1, k2:v2}) = v2
     * lastElement([]) = null
     * lastElement(null) = null
     * </p>
     *
     * @param source 数据源
     * @return
     */
    public static <T> Object lastElement(final String source) {
        if (!BooleanHelper.hasLength(source)) {
            return null;
        }
        if (source.startsWith(SYMBOL_LEFT_SQUARE_BRACKET) || source.startsWith(SYMBOL_LEFT_BIG_PARENTHESES)) {
            Object object = JsonHelper.toObject(source);
            if (object instanceof Collection) {
                return firstElement((Collection) object);
            } else if (object instanceof Map) {
                return firstElement((Map) object);
            }
        }
        return null;
    }

    /**
     * 获取数组长度
     *
     * @param ts
     * @return
     */
    public static <T> int getLength(T[] ts) {
        return null == ts ? 0 : ts.length;
    }

    /**
     * 获取集合长度
     *
     * @param kvMap
     * @return
     */
    public static <K, V> int getLength(Map<K, V> kvMap) {
        return null == kvMap ? 0 : kvMap.size();
    }

    /**
     * 获取集合长度
     *
     * @param collection
     * @return
     */
    public static int getLength(Collection collection) {
        return null == collection ? 0 : collection.size();
    }

    /**
     * 查询节点
     *
     * @param collection 集合
     * @param index      序列
     * @param <T>
     * @return
     */
    public static <T> T findElement(final Collection<T> collection, final int index) {
        if (!BooleanHelper.hasLength(collection) || index < 0 || index > collection.size()) {
            return null;
        }
        Iterator<T> iterator = collection.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (index == count++) {
                return t;
            }
        }
        return null;
    }

    /**
     * 查询节点
     *
     * @param arrays 集合
     * @param index  序列
     * @param <T>
     * @return
     */
    public static <T> T findElement(final T[] arrays, final int index) {
        if (!BooleanHelper.hasLength(arrays) || index < 0 || index > arrays.length) {
            return null;
        }
        for (int i = 0; i < arrays.length; i++) {
            if (i == index) {
                return arrays[i];
            }
        }
        return null;
    }

    /**
     * 查询最后一个节点
     *
     * @param kvMap 集合
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V lastElement(final Map<K, V> kvMap) {
        return findElement(kvMap, getLength(kvMap));
    }

    /**
     * 查询第一个节点
     *
     * @param kvMap 集合
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V firstElement(final Map<K, V> kvMap) {
        return findElement(kvMap, 0);
    }

    /**
     * 查询节点
     *
     * @param kvMap 集合
     * @param index 序列
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V findElement(final Map<K, V> kvMap, final int index) {
        if (!BooleanHelper.hasLength(kvMap) || index < 0 || index > kvMap.size()) {
            return null;
        }
        Collection<K> ks = kvMap.keySet();
        K element = findElement(ks, index);
        return null == element ? null : kvMap.get(element);
    }

    /**
     * 获取一个随机元素
     *
     * @param collector 集合
     * @return 元素
     */
    public static synchronized  <T> T getRandomOne(Collection<T> collector) {
        if (null == collector || collector.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int nextInt = random.nextInt(collector.size());
        return findElement(nextInt, collector);
    }
}
