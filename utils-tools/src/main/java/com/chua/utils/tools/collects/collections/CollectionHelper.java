package com.chua.utils.tools.collects.collections;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.collects.iterator.IndexIterator;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.Matcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.chua.utils.tools.constant.NumberConstant.INDEX_NOT_FOUND;

/**
 * 集合工具类
 *
 * @author CH
 */
public class CollectionHelper {
    /**
     * 获取数据集合索引
     *
     * @param collection 集合
     * @param key        索引
     * @return 索引位置, 查询不到数据返回 -1, 反之返回具体位置
     */
    public static int indexOf(final Collection<?> collection, final Object key) {
        return indexOf(collection, key, 0);
    }

    /**
     * 获取数据集合索引
     *
     * @param collection 集合
     * @param key        索引
     * @return 索引位置, 查询不到数据返回 -1, 反之返回具体位置
     */
    public static IndexIterator indexOfIterator(final Collection<?> collection, final Object key) {
        return new IndexIterator(collection, key);
    }

    /**
     * 获取数据集合索引
     *
     * @param collection 集合
     * @param key        索引
     * @param start      最小的开始位置
     * @return 索引位置, 查询不到数据返回 -1, 反之返回具体位置
     */
    public static int indexOf(final Collection<?> collection, final Object key, final int start) {
        if (isEmpty(collection) || null == key || !collection.contains(key)) {
            return INDEX_NOT_FOUND;
        }
        AtomicInteger count = new AtomicInteger(-1);
        int index = -1;
        for (Object next : collection) {
            count.incrementAndGet();
            if (key.equals(next) && count.get() >= start) {
                index = count.get();
                break;
            }
        }
        return index;
    }

    /**
     * <pre>
     *      CollectorsHelper.isEmpty(null)     = true
     *      CollectorsHelper.isEmpty(new HashMap())  = false
     * </pre>
     * 是否有内容
     *
     * @param kvMap 集合
     * @return Boolean
     */
    public static boolean isEmpty(final Map<?, ?> kvMap) {
        return null == kvMap || kvMap.isEmpty();
    }

    /**
     * <pre>
     *      CollectorsHelper.isEmpty(null)     = true
     *      CollectorsHelper.isEmpty(new ArrayList())  = false
     * </pre>
     * 是否有内容
     *
     * @param collection 集合
     * @return Boolean
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }


    /**
     * <pre>
     *     CollectorsHelper.size(null) = 0
     *     CollectorsHelper.size(new ArrayList()) = 0
     * </pre>
     * 获取长度
     *
     * @param collection 集合
     * @return 长度
     */
    public static <E> int size(final Collection<E> collection) {
        return null == collection ? 0 : collection.size();
    }

    /**
     * <pre>
     *     CollectorsHelper.size(null) = 0
     *     CollectorsHelper.size(new HashMap()) = 0
     * </pre>
     * 获取长度
     *
     * @param kvMap 集合
     * @return 长度
     */
    public static <K, V> int size(final Map<K, V> kvMap) {
        return null == kvMap ? 0 : kvMap.size();
    }

    /**
     * 过滤元素
     *
     * @param list      集合
     * @param predicate predicate
     * @param <E>       类型
     * @see com.google.common.base.Predicate
     */
    public static <E> Collection<E> doWithFilter(final Collection<E> list, final Predicate<E> predicate) {
        if (isEmpty(list)) {
            return list;
        }
        if (null == predicate) {
            return list;
        }
        List<E> result = new ArrayList<>();
        for (E e : list) {
            if (predicate.test(e)) {
                result.add(e);
            }
        }
        return result;
    }


    /**
     * 清空集合数据
     *
     * @param source 集合
     */
    public static synchronized <T> void clear(final Collection<T> source) {
        if (null == source) {
            return;
        }
        if (isEmpty(source)) {
            return;
        }
        source.clear();
    }

    /**
     * 清空集合数据
     *
     * @param source 集合
     */
    public static synchronized <K, V> void clear(final Map<K, V> source) {
        if (null == source) {
            return;
        }
        if (!BooleanHelper.hasLength(source)) {
            return;
        }
        source.clear();
    }

    /**
     * 集合转对象
     *
     * @param source    数据
     * @param delimiter 分隔符
     * @param tClass    类型
     * @param <T>       类型
     * @return 对象集
     */
    public static <T> List<T> toEntity(final List<String> source, final String delimiter, final Class<T> tClass) {
        if (null == tClass) {
            return null;
        }
        if (!BooleanHelper.hasLength(source)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(source.size());
        for (String item : source) {
            List<String> strings = Splitter.on(StringHelper.getStringOrDefault(delimiter, ",")).trimResults().omitEmptyStrings().splitToList(item);
            T entity = BeanCopy.of(tClass).with(strings).create();
            if (null == entity) {
                continue;
            }
            result.add(entity);
        }
        return result;

    }


    /**
     * 过滤类型数据
     *
     * @param source 数据源
     * @param type   类型
     * @param <T>    类型
     * @return 过滤的数据
     */
    @SuppressWarnings("all")
    public static <T> Set<T> doWithFilter(final Collection<?> source, final Class<T> type) {
        if (!BooleanHelper.hasLength(source)) {
            return Collections.emptySet();
        }
        Set<T> result = new HashSet<>(source.size());
        for (Object o : source) {
            if (!o.getClass().isAssignableFrom(type)) {
                continue;
            }
            result.add((T) o);
        }
        return result;
    }

    /**
     * 循环集合(自动判空)
     *
     * @param source   数据
     * @param consumer 回调
     */
    public static <Item> void forEach(final Collection<Item> source, final Consumer<Item> consumer) {
        if (!BooleanHelper.hasLength(source) || null == consumer) {
            return;
        }
        for (Item item : source) {
            consumer.accept(item);
        }
    }

    /**
     * 数组转集合
     *
     * @param source 数据源
     * @param <T>    类型
     * @return 集合
     */
    public static <T> List<T> toList(final T[] source) {
        if (!BooleanHelper.hasLength(source)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(source.length);
        for (T t : source) {
            if (null == t) {
                continue;
            }
            result.add(t);
        }

        return result;
    }

    /**
     * 数组转集合
     *
     * @param source 数据源
     * @param <T>    类型
     * @return 集合
     */
    public static <T> Set<T> toSet(final T[] source) {
        if (!BooleanHelper.hasLength(source)) {
            return Collections.emptySet();
        }
        Set<T> result = new HashSet<>(source.length);
        for (T t : source) {
            if (null == t) {
                continue;
            }
            result.add(t);
        }

        return result;
    }


    /**
     * 包含值
     *
     * @param collection 集合集合
     * @param value      值
     * @return boolean
     */
    public static boolean contains(final Collection<?> collection, final Object value) {
        return BooleanHelper.hasLength(collection) && null != value && collection.contains(value);
    }

    /**
     * 过滤元素
     *
     * @param collection 集合集合
     * @param filter     过滤器
     * @return List<V>
     */
    public static <V> Set<V> doWithFilter(final Set<V> collection, final Filter<V> filter) {
        if (!BooleanHelper.hasLength(collection)) {
            return collection;
        }
        Set<V> result = new HashSet<>();
        collection.parallelStream().forEach(v -> {
            boolean matcher = filter.matcher(v);
            if (!matcher) {
                return;
            }
            result.add(v);
        });
        return result;
    }

    /**
     * 过滤元素
     *
     * @param collection 集合集合
     * @param filter     过滤器
     * @return List<V>
     */
    public static <V> List<V> doWithFilter(final Collection<V> collection, final Filter<V> filter) {
        if (!BooleanHelper.hasLength(collection)) {
            return Collections.emptyList();
        }
        List<V> result = new LinkedList<>();
        collection.parallelStream().forEach(v -> {
            boolean matcher = filter.matcher(v);
            if (!matcher) {
                return;
            }
            result.add(v);
        });
        return result;
    }

    /**
     * 匹配元素
     *
     * @param collection 集合集合
     * @param matcher    匹配器
     */
    public static <V> void doWithMatcher(final Collection<V> collection, final Matcher<V> matcher) {
        if (!BooleanHelper.hasLength(collection)) {
            return;
        }
        collection.parallelStream().forEach(v -> {
            try {
                matcher.doWith(v);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    /**
     * 获取长度
     *
     * @param collection 集合集合
     * @return int
     */
    public static int getSize(final Collection<?> collection) {
        return isEmpty(collection) ? 0 : collection.size();
    }

    /**
     * 尝试获取List
     *
     * @param value 值
     * @param <T>   类型
     * @return List
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getListIfFeasible(final T value) {
        if (null == value) {
            return Collections.emptyList();
        }
        if (value instanceof Collection) {
            return new ArrayList<>((Collection<T>) value);
        }
        Class<?> aClass = value.getClass();
        if (aClass.isArray()) {
            return Arrays.asList((T[]) value);
        }
        return Lists.newArrayList(value);
    }

    /**
     * 随机获取数据
     *
     * @param source 集合
     * @param <T>    类型
     * @return 元素
     */
    public static <T> T getRandom(final List<T> source) {
        if (isEmpty(source)) {
            return null;
        }
        Random random = new Random();
        int i = random.nextInt(source.size());
        return source.get(i);
    }

    /**
     * 获取默认值
     * <p>判断第一个数据集合是否有效, 有效返回当前集合, 反之返回第二个集合</p>
     *
     * @param source       原数据集合
     * @param defaultValue 默认集合
     * @param <T>          类型
     * @return 判断第一个数据集合是否有效, 有效返回当前集合, 反之返回第二个集合
     */
    public static <T> List<T> getOrDefault(final List<T> source, final List<T> defaultValue) {
        return isEmpty(source) ? defaultValue : source;
    }

    /**
     * 添加数据
     *
     * @param firstCollection  第一个数据集合
     * @param secondCollection 第二个数据集合
     */
    public static <T> void add(final Collection<T> firstCollection, final Collection<T> secondCollection) {
        if (isEmpty(firstCollection) || isEmpty(secondCollection)) {
            return;
        }

        firstCollection.addAll(secondCollection);
    }

    /**
     * 添加数据
     *
     * @param firstCollection  第一个数据集合
     * @param secondCollection 第二个数据集合
     */
    public static <T> Collection<? extends T> addIfAbsent(final Collection<? extends T> firstCollection, final Collection<? extends T> secondCollection) {
        if (isEmpty(firstCollection) || isEmpty(secondCollection)) {
            return firstCollection;
        }
        Collection<T> result = new ArrayList<>(firstCollection);
        result.addAll(secondCollection);
        return result;
    }

    /**
     * 添加数据
     *
     * @param firstCollection 第一个数据集合
     * @param items           集合元素
     */
    @SuppressWarnings("all")
    public static <T> void add(final Collection<T> firstCollection, final T... items) {
        if (null == firstCollection || ArraysHelper.isEmpty(items)) {
            return;
        }
        for (T item : items) {
            firstCollection.add(item);
        }
    }

    /**
     * 集合转数组
     *
     * @param source 集合
     * @param <T>    类型
     * @return 数组
     */
    @SuppressWarnings("all")
    public static <T> T[] toArray(final List<T> source) {
        if (null == source) {
            return null;
        }
        return (T[]) source.toArray();
    }

    /**
     * 集合转数组
     *
     * @param source 集合
     * @param <T>    类型
     * @return 数组
     */
    @SuppressWarnings("all")
    public static String toString(final Collection<?> source) {
        if (null == source) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("[\r\n");
        for (Object o : source) {
            stringBuilder.append("\t\t").append(o.toString()).append("\r\n");
        }

        stringBuilder.append("\r\n]");
        return stringBuilder.toString();
    }

    /**
     * 获取唯一元素
     *
     * @param tList 集合
     * @param <T>   元素类型
     * @return 当集合唯一存在一个元素则返回元素, 反之返回null
     */
    public static <T> T getIfOnly(final Collection<T> tList) {
        return null == tList || tList.size() != 1 ? null : tList.iterator().next();
    }
}
