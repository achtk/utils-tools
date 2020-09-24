package com.chua.utils.tools.common;

import com.chua.unified.function.ListComparator;
import com.google.common.collect.*;

import java.io.File;
import java.util.*;

import static com.chua.utils.tools.common.BooleanHelper.isEmpty;

/**
 * List工具类
 * @author CH
 */
public class ListHelper {
    /**
     * 空集合
     */
    public static final List EMPTY_LIST = Collections.EMPTY_LIST;

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
     * 创建 LinkedListMultimap
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> LinkedListMultimap<Object, Object> newLinkedListMultimap() {
        return LinkedListMultimap.create();
    }

    /**
     * 创建 LinkedListMultimap
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> LinkedListMultimap<Object, Object> newLinkedListMultimap(int initialCapacity) {
        return LinkedListMultimap.create(initialCapacity);
    }

    /**
     * 创建 ArrayList
     *
     * @param <V>
     * @return
     */
    public static <V> LinkedList<V> newLinkedList() {
        return new LinkedList();
    }

    /**
     * 创建 ArrayListMultimap
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ArrayListMultimap<K, V> newArrayListMultimap() {
        return ArrayListMultimap.<K, V>create();
    }


    /**
     * 初始化
     *
     * @param <T>
     * @return
     */
    public static <T> ImmutableList<T> newImmutableList(T... ts) {
        return ImmutableList.<T>copyOf(ts);
    }

    /**
     * 初始化
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Multimap<K, V> newMultimap() {
        return ArrayListMultimap.<K, V>create();
    }

    /**
     * 初始化
     *
     * @param <T>
     * @return
     */
    public static <T> ImmutableSet<T> newImmutableSet(T... ts) {
        return ImmutableSet.<T>copyOf(ts);
    }

    /**
     * 创建集合
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> unmodifiableList() {
        return (List<T>) Collections.unmodifiableCollection(newLinkedList());
    }

    /**
     * list转安全线程
     *
     * @param <T>
     * @return
     */
    public static <T> List<T> synchronizedList(List<T> list) {
        if (null == list) {
            return null;
        }
        return Collections.synchronizedList(list);
    }

    /**
     * 初始化
     *
     * @param size 长度
     * @param <T>
     * @return
     */
    public static <T> List<T> newArrayList(final int size) {
        return new ArrayList<T>(size);
    }

    /**
     * 初始化
     *
     * @param t   数据
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> List<T> newArrayList(final T... t) {
        return ArraysHelper.newArrays(t);
    }


    /**
     * 获取list中的元素
     * <pre>
     *     ListHelper.get([], 1) = null;
     *     ListHelper.get([], 0) = null;
     *     ListHelper.get([1], 1) = 1;
     *     ListHelper.get([1], 2) = null;
     *     ListHelper.get([1, 2], -1) = 2;
     *     ListHelper.get(null, 2) = null;
     * </pre>
     *
     * @param <T>
     * @return
     */
    public static <T> T get(final List<T> source, int index) {
        if (!BooleanHelper.hasLength(source)) {
            return null;
        }

        int length = source.size();
        if (index < 0) {
            Collections.reverse(source);
            index = index * -1 - 1;
        }
        return length < index ? null : source.get(index);
    }

    /**
     * 第一条数据
     *
     * @param srcList 原始数据
     * @return
     * @see ListHelper#firstElement(Collection)
     */
    public static <T> T one(Collection<T> srcList) {
        return firstElement(srcList);
    }

    /**
     * 保留原始数据插入新数据
     * <p>
     * ListHelper.insert([1, 2, 3], [4, 5, 6]) = [1, 2, 3, 4, 5, 6]
     * ListHelper.insert(null, [1]) = [1]
     * </p>
     *
     * @param source     原始数据集
     * @param collection 新数据集
     */
    public static <T> void insert(Collection<T> source, Collection<T> collection) {
        if (BooleanHelper.hasLength(source)) {
            source.addAll(collection);
        } else {
            source = collection;
        }
    }


    /**
     * 删除元素
     * <pre>
     *     ListHelper.remove([1,2,3,4], 1) = [2,3,4]
     *     ListHelper.remove([1,2,3,4], null) = [1,2,3,4]
     *     ListHelper.remove(null, null) = null
     *     ListHelper.remove(null, 1) = null
     * </pre>
     *
     * @param source 原始数据
     * @param item   元素
     * @return
     */
    public static <T> Collection<T> remove(final Collection<T> source, final T item) {
        return removeAll(source, item);
    }

    /**
     * 删除元素
     * <pre>
     *     ListHelper.remove([1,2,3,4], 0) = [2,3,4]
     *     ListHelper.remove([1,2,3,4], 1) = [1,3,4]
     *     ListHelper.remove(null, 1) = null
     * </pre>
     *
     * @param source 原始数据
     * @param index  索引
     * @return
     */
    public static <T> Collection<T> remove(final Collection<T> source, final int index) {
        if (!BooleanHelper.hasLength(source) || index < 0) {
            return source;
        }

        int count = 0;
        Iterator<T> iterator = source.iterator();
        while (iterator.hasNext()) {
            if (count++ == index) {
                iterator.remove();
            }
        }
        return source;
    }

    /**
     * 删除元素
     * <pre>
     *     ListHelper.removeAll([1,2,3,4], 1, 5) = [2,3,4]
     *     ListHelper.removeAll([1,2,3,4], null) = [1,2,3,4]
     *     ListHelper.removeAll(null, null) = null
     *     ListHelper.removeAll(null, 1) = null
     * </pre>
     *
     * @param source 原始数据
     * @param items  元素
     * @return
     */
    public static <T> Collection<T> removeAll(final Collection<T> source, final T... items) {
        if (!BooleanHelper.hasLength(items)) {
            return source;
        }
        if (!BooleanHelper.hasLength(source)) {
            return null;
        }
        source.removeAll(Arrays.asList(items));
        return source;
    }

    /**
     * 获取长度
     * <pre>
     *     ListHelper.listSize([1,2,3,4]) = 4
     *     ListHelper.listSize(null) = 0
     * </pre>
     *
     * @param sources 原始数据
     * @param <T>
     * @return
     */
    public static <T> int listSize(final Collection<T> sources) {
        return null == sources ? 0 : sources.size();
    }


    /**
     * 添加
     * <pre>
     *     ListHelper.add([xxx, xxxx, xx], 1) = [xxx, xxxx, xx, 1]
     *     ListHelper.add([xxx, xxxx, xx], null) = [xxx, xxxx, xx]
     *     ListHelper.add(null, 4) = [4]
     * </pre>
     *
     * @param t   集合
     * @param t1  数据
     * @param <T>
     */
    public static <T> void add(List<T> t, final T t1) {
        if (isEmpty(t)) {
            t = newArrayList();
        }
        if (null != t1) {
            t.add(t1);
        }
    }

    /**
     * 添加 list
     * <pre>
     *     ListHelper.addAll([xxx, xxxx, xx], 1) = [xxx, xxxx, xx, 1]
     *     ListHelper.addAll([xxx, xxxx, xx], null) = [xxx, xxxx, xx]
     *     ListHelper.addAll(null, 4) = [4]
     * </pre>
     *
     * @param source
     * @param target
     */
    public static <T> void addAll(List<T> source, final T[] target) {
        if (null == source) {
            source = new ArrayList<>();
        }
        if (null != target && target.length > 0) {
            source.addAll(newArrayList(target));
        }

    }

    /**
     * set 转 list
     * <p>
     * ListHelper.toList([1, 2, 3]) = [1, 2, 3]
     * ListHelper.toList(null) = []
     * </p>
     *
     * @param collection 集合
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(final Collection<T> collection) {
        if (null != collection) {
            List<T> kList = newArrayList(collection.size());
            kList.addAll(collection);
            return kList;
        }
        return newArrayList();
    }

    /**
     * 是否长度大于i
     * <pre>
     *     ListHelper.lessLength([xxx, xxxx, xx], 1) = false
     *     ListHelper.lessLength([xxx, xxxx, xx], 3) = false
     *     ListHelper.lessLength([xxx, xxxx, xx], 4) = true
     * </pre>
     *
     * @param source 原始数据
     * @param length 长度
     * @return
     */
    public static <T> boolean moreLength(List<T> source, int length) {
        return !isEmpty(source) && source.size() > length;
    }

    /**
     * 是否长度小于i
     * <pre>
     *     ListHelper.lessLength([xxx, xxxx, xx], 1) = true
     *     ListHelper.lessLength([xxx, xxxx, xx], 3) = true
     *     ListHelper.lessLength([xxx, xxxx, xx], 4) = false
     * </pre>
     *
     * @param source 原始数据
     * @param length 长度
     * @return
     */
    public static <T> boolean lessLength(List<T> source, int length) {
        return isEmpty(source) || source.size() < length;
    }

    /**
     * 排序
     * <pre>
     *     ListHelper.sortByLength([xxx, xxxx, xx], Comparator) = [xx, xxx, xxxx]
     * </pre>
     *
     * @param files
     */
    public static void sort(List<File> files, Comparator comparator) {
        if (!isEmpty(files)) {
            Collections.sort(files, comparator);
        }
    }

    /**
     * 排序
     * <pre>
     *     ListHelper.sortByLength([xxx, xxxx, xx]) = [xx, xxx, xxxx]
     * </pre>
     *
     * @param files
     */
    public static void sortByLength(List<File> files) {
        sort(files, ListComparator.LENGTH_COMPARATOR);
    }

    /**
     * 获取最后一个值
     * <p>
     * ListHelper.lastElement([]) = null
     * ListHelper.lastElement(null) = null
     * ListHelper.lastElement([1, 2, 3]) = 3
     * </p>
     *
     * @param strings 集合
     * @return
     */
    public static <T> T lastElement(Collection<T> strings) {
        if (isEmpty(strings)) {
            return null;
        }
        Iterator<T> iterator = strings.iterator();
        T entity = null;
        while (iterator.hasNext()) {
            entity = iterator.next();
        }
        return entity;
    }

    /**
     * 获取第一个一个值
     * <p>
     * ListHelper.firstElement([]) = null
     * ListHelper.firstElement(null) = null
     * ListHelper.firstElement([1, 2, 3]) = 1
     * </p>
     *
     * @param strings 集合
     * @return
     */
    public static <T> T firstElement(Collection<T> strings) {
        if (isEmpty(strings)) {
            return null;
        }
        return strings.iterator().next();
    }

    /**
     * 交集
     * <pre>
     *     ListHelper.intersection([0,2,4,6], [0,1,3,5]) = [0]
     * </pre>
     * @param source1 集合1
     * @param source2 集合2
     * @return
     */
    public static <T>Collection<T> intersection(Collection<T> source1, Collection<T> source2) {
        if(null == source1 || null == source2) {
            return null;
        }

        return source1.retainAll(source2) ? source1 : null;
    }
    /**
     * 差集
     * <pre>
     *     ListHelper.difference([0,2,4,6], [0,1,3,5]) = [2,4,6]
     * </pre>
     * @param source1 集合1
     * @param source2 集合2
     * @return
     */
    public static <T>Collection<T> difference(Collection<T> source1, Collection<T> source2) {
        if(null == source1 || null == source2) {
            return null;
        }
        return source1.removeAll(source2) ? source1 : null;
    }
    /**
     * 差集
     * <pre>
     *     ListHelper.unionDifference([0,2,4,6], [0,1,3,5]) = [1, 2, 3, 4, 5, 6]
     * </pre>
     * @param source1 集合1
     * @param source2 集合2
     * @return
     */
    public static <T>Collection<T> unionDifference(Collection<T> source1, Collection<T> source2) {
        if(null == source1 || null == source2) {
            return null;
        }
        Collection<T> source1Copy = Collections.synchronizedCollection(source1);
        source1.removeAll(source2);
        source2.removeAll(source1Copy);

        return source1.addAll(source2) ? source1 : null;
    }
    /**
     * 并集
     * @param source1 集合1
     * @param source2 集合2
     * @return
     */
    public static <T>Collection<T> union(Collection<T> source1, Collection<T> source2) {
        if(null == source1 || null == source2) {
            return null;
        }
        return source1.addAll(source2) ? source1 : null;
    }

    /**
     * 获取List
     * @param keys
     * @return
     */
    public static List<String> toList(Enumeration<String> keys) {
        List<String> result = new ArrayList<>();
        while (keys.hasMoreElements()) {
            result.add(keys.nextElement());
        }
        return result;
    }

    /**
     * 获取set值
     * @param sources
     * @return
     */
    public static <T>Set<T> toSet(T[] sources) {
        if(null == sources) {
            return Collections.EMPTY_SET;
        }
        Set<T> set = new HashSet<>(sources.length);
        for (T scan : sources) {
            set.add(scan);
        }
        return set;
    }
    /**
     * 获取list值
     * @param sources
     * @return
     */
    public static <T>List<T> toList(T[] sources) {
        if(null == sources) {
            return Collections.EMPTY_LIST;
        }
        List<T> list = new ArrayList<>(sources.length);
        for (T scan : sources) {
            list.add(scan);
        }
        return list;
    }

    /**
     * 返回有值的数据
     * @param source 原数据1
     * @param source1 原数据2
     * @return
     */
    public static <T>List<T> firstNonNull(List<T> source, List<T> source1) {
        if(BooleanHelper.hasLength(source)) {
            return source;
        }
        return null == source1 ? Collections.emptyList() : source1;
    }
}
