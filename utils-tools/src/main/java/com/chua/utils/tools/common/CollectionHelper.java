package com.chua.utils.tools.common;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

/**
 * 集合工具类
 *
 * @author CH
 */
public class CollectionHelper {

    /**
     * <pre>
     * CollectorsHelper.isBlank(null)     = true
     * CollectorsHelper.isBlank(new HashMap())  = false
     * </pre>
     * 是否有内容
     *
     * @param collection
     * @return
     */
    public static <V> Boolean isBlank(Collection<V> collection) {
        return !BooleanHelper.hasLength(collection);
    }

    /**
     * <pre>
     * CollectorsHelper.isBlank(null)     = true
     * CollectorsHelper.isBlank(new HashMap())  = true
     * </pre>
     * 是否有内容
     *
     * @param kvMap
     * @return
     */
    public static <K, V> Boolean isBlank(Map<K, V> kvMap) {
        return !BooleanHelper.hasLength(kvMap);
    }


    /**
     * <pre>
     * CollectorsHelper.isEmpty(null)     = true
     * CollectorsHelper.isEmpty(new ArrayList())  = false
     * </pre>
     * 是否有内容
     *
     * @param collection
     * @return
     */
    public static <V> Boolean isEmpty(Collection<V> collection) {
        return null == collection || collection.size() == 0;
    }

    /**
     * <pre>
     * CollectorsHelper.isEmpty(null)     = true
     * CollectorsHelper.isEmpty(new HashMap())  = false
     * </pre>
     * 是否有内容
     *
     * @param kvMap
     * @return
     */
    public static <K, V> Boolean isEmpty(Map<K, V> kvMap) {
        return null == kvMap || kvMap.isEmpty();
    }


    /**
     * <pre>
     * CollectorsHelper.of(null)     = List
     * CollectorsHelper.of("")     = List
     * </pre>
     * 初始化集合
     *
     * @param items
     * @param <E>
     * @return
     */
    public static <E> ImmutableList<E> form(E... items) {
        return null == items ? ImmutableList.<E>of() : ImmutableList.<E>builder().add(items).build();
    }

    /**
     * <pre>
     *     CollectorsHelper.size(null) = 0
     *     CollectorsHelper.size(new ArrayList()) = 0
     * </pre>
     * 获取长度
     *
     * @param collection
     * @return
     */
    public static <E> int size(Collection<E> collection) {
        return isEmpty(collection) ? 0 : collection.size();
    }

    /**
     * <pre>
     *     CollectorsHelper.size(null) = 0
     *     CollectorsHelper.size(new HashMap()) = 0
     * </pre>
     * 获取长度
     *
     * @param kvMap
     * @return
     */
    public static <K, V> int size(Map<K, V> kvMap) {
        return isEmpty(kvMap) ? 0 : kvMap.size();
    }

    /**
     * 过滤元素
     *
     * @param list
     * @param predicate
     * @param <E>
     */
    public static <E> Collection<E> filter(Collection<E> list, Predicate<E> predicate) {
        if (isBlank(list)) {
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
     * 获取源数据不同数据
     *
     * @param source 源数据
     * @param target 比对数据
     * @param <E>
     * @return
     */
    public static <E> Collection<E> difference(Collection<E> source, Collection<E> target) {
        if (isBlank(source) || isBlank(target)) {
            return source;
        }
        Set<E> set = new HashSet<>();
        Iterator<E> iterator = source.iterator();
        E elem = null;
        while (iterator.hasNext()) {
            elem = iterator.next();
            if (!target.contains(elem)) {
                set.add(elem);
            }
        }

        return set;
    }

    /**
     * 获取源数据相同数据
     *
     * @param source 源数据
     * @param target 比对数据
     * @param <E>
     * @return
     */
    public static <E> Collection<E> same(Collection<E> source, Collection<E> target) {
        if (isBlank(source) || isBlank(target)) {
            return source;
        }
        Set<E> set = new HashSet<>();
        Iterator<E> iterator = source.iterator();

        E elem = null;
        while (iterator.hasNext()) {
            elem = iterator.next();
            if (target.contains(elem)) {
                set.add(elem);
            }
        }
        return set;
    }

    /**
     * 获取值
     *
     * @param collection
     * @param index
     */
    public static <E> E get(Collection<E> collection, int index) {
        if (isBlank(collection) || collection.size() < index) {
            return null;
        }

        int cnt = 0;
        Iterator<E> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (cnt++ != index) {
                continue;
            }
            return iterator.next();
        }
        return null;
    }

    /**
     * 获取值
     *
     * @param objectMap
     * @param key
     */
    public static String get(Map<String, Object> objectMap, String key) {
        return get(objectMap, key, null);
    }

    /**
     * 获取值
     *
     * @param objectMap
     * @param key
     */
    public static <E> E get(Map<String, Object> objectMap, String key, E defaultValue) {
        if (isBlank(objectMap) || !objectMap.containsKey(key)) {
            return defaultValue;
        }
        Object o = objectMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        return (E) o;
    }

    /**
     * 转字符串
     *
     * @param collection 集合
     * @return
     */
    public static String toString(Collection collection) {
        return null == collection ? "[]" : "[" + Joiner.on(",").join(collection.iterator()) + "]";
    }

    /**
     * 转字符串
     *
     * @param array 数组
     * @return
     */
    public static <T> String toString(T[] array) {
        return null == array ? "[]" : "[" + Joiner.on(",").join(array) + "]";
    }

    /**
     * 转字符串
     *
     * @param array 数组
     * @return
     */
    public static String toString(byte[] array) {
        if (null == array) {
            return "[]";
        }
        StringBuffer sb = new StringBuffer(array.length);
        for (byte b : array) {
            sb.append(",").append(b);
        }
        return sb.length() == 0 ? "[]" : sb.substring(1);
    }

    /**
     * 转字符串
     *
     * @param map 集合
     * @return
     */
    public static String toString(Map map) {
        return null == map ? "{}" : JsonHelper.toJson(map);
    }

    /**
     * 获取索引位置
     *
     * @param source  源数据
     * @param element 节点
     * @return
     */
    public static int indexOf(Collection<String> source, String element) {
        if (!BooleanHelper.hasLength(source) || StringHelper.isBlank(element)) {
            return -1;
        }
        int index = -1;
        for (String item : source) {
            ++index;
            if (element.equals(item)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 获取索引位置
     *
     * @param source  源数据
     * @param element 节点
     * @return
     */
    public static int indexOf(Map<String, Object> source, String element) {
        if (!BooleanHelper.hasLength(source) || StringHelper.isBlank(element)) {
            return -1;
        }
        int index = -1;
        for (String item : source.keySet()) {
            ++index;
            if (element.equals(item)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 获取第一个元素
     *
     * @param params
     * @return
     */
    public static <T> T firstElement(final Map<String, T> params) {
        return FinderHelper.firstElement(params);
    }

    /**
     * 获取第一个元素
     *
     * @param list
     * @return
     */
    public static <T> T firstElement(final Collection<T> list) {
        return FinderHelper.firstElement(list);
    }

    /**
     * 默认获取集合
     * <pre>
     * ifNull(null, ["NULL"])  = ["NULL"]
     * ifNull([""], ["NULL"])    = [""]
     * </pre>
     *
     * @param source        源数据
     * @param defaultTarget 默认数组
     * @return
     */
    public static <T> Collection<T> ifNull(final Collection<T> source, final Collection<T> defaultTarget) {
        return null != source ? source : defaultTarget;
    }

    /**
     * 默认获取集合
     * <pre>
     * ifNull(null, {})  = {}
     * ifNull({}, {1:1})    = {}
     * </pre>
     *
     * @param source        源数据
     * @param defaultTarget 默认数组
     * @return
     */
    public static <K, V> Map<K, V> ifNull(final Map<K, V> source, final Map<K, V> defaultTarget) {
        return null != source ? source : defaultTarget;
    }

    /**
     * 默认获取集合
     * <pre>
     * ifNull(null, ["NULL"])  = ["NULL"]
     * ifNull([""], ["NULL"])    = [""]
     * </pre>
     *
     * @param source        源数据
     * @param defaultTarget 默认数组
     * @return
     */
    public static <T> Collection<T> ifEmpty(final Collection<T> source, final Collection<T> defaultTarget) {
        return BooleanHelper.hasLength(source) ? source : defaultTarget;
    }

    /**
     * 默认获取集合
     * <pre>
     * ifNull(null, {})  = {}
     * ifNull({}, {1:1})    = {1:1}
     * </pre>
     *
     * @param source        源数据
     * @param defaultTarget 默认数组
     * @return
     */
    public static <K, V> Map<K, V> ifEmpty(final Map<K, V> source, final Map<K, V> defaultTarget) {
        return BooleanHelper.hasLength(source) ? source : defaultTarget;
    }

    /**
     * 笛卡尔积
     *
     * @param item  第一个数据集
     * @param items 第二个数据集
     * @param <T>
     * @return
     */
    public static <T> List<String> descartes(List<T> item, List<T>... items) {
        if (!BooleanHelper.hasAllLength(items)) {
            return null;
        }
        List<List<T>> str = null;
        if (null == item || item.isEmpty()) {
            str = new ArrayList<>();
            for (List<T> ts : items) {
                str.add(ts);
            }
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
        int itemLoop = 1;
        //循环次数
        int loop = 1;

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
                        result[index] = (null == result[index] ? "" : result[index] + ",") + ((String) ts.get(myIndex));
                        index++;
                    }
                    myIndex++;
                }
            }
        }
        return Arrays.asList(result);
    }

    /**
     * 合并数据
     *
     * @param source 数据1
     * @param items  数据2
     * @return
     */
    public static <T> List<T> merge(final List<T> source, final T... items) {
        List<T> result = ObjectHelper.firstNonNull(source, new ArrayList<>());
        if (!BooleanHelper.hasLength(items)) {
            return result;
        }

        for (T item : items) {
            result.add(item);
        }

        return result;
    }

    /**
     * 合并两个数据
     *
     * @param fileResolvers  元数据1
     * @param fileResolvers1 元数据2
     */
    public static <T> void add(Collection<T> fileResolvers, Collection<T> fileResolvers1) {
        if (null == fileResolvers) {
            if (fileResolvers instanceof Set) {
                fileResolvers = new HashSet<>();
            } else {
                fileResolvers = new ArrayList<>();
            }
        }
        if (null != fileResolvers1) {
            fileResolvers.addAll(fileResolvers1);
        }
    }

    /**
     * 集合转数组
     *
     * @param sources 数据源
     * @return
     */
    public static <T> T[] toArray(List<T> sources) {
        if (!BooleanHelper.hasLength(sources)) {
            return null;
        }

        return (T[]) sources.toArray();
    }

    /**
     * 返回第一个有效的数据
     *
     * @param <T>
     * @param collection 数据集合
     * @return
     */
    public static <T> T findFirstOne(Collection<T> collection) {
        return FinderHelper.firstElement(collection);
    }

    /**
     * 返回第一个有效的数据
     *
     * @param <T>
     * @param collection 数据集合
     * @return
     */
    public static <T> T findLastOne(Collection<T> collection) {
        return FinderHelper.lastElement(collection);
    }

    /**
     * 清空集合数据
     *
     * @param source 集合
     */
    public static synchronized <T> void clear(List<T> source) {
        if (null == source) {
            source = new ArrayList<>();
            return;
        }
        if (!BooleanHelper.hasLength(source)) {
            return;
        }
        source.clear();
    }

    /**
     * 清空集合数据
     *
     * @param source 集合
     */
    public static synchronized <T> void clear(Set<T> source) {
        if (null == source) {
            source = new HashSet<>();
            return;
        }
        if (!BooleanHelper.hasLength(source)) {
            return;
        }
        source.clear();
    }

    /**
     * 清空集合数据
     *
     * @param source 集合
     */
    public static synchronized <K, V> void clear(Map<K, V> source) {
        if (null == source) {
            source = new HashMap<>();
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
     * @param <T>
     * @return
     */
    public static <T> List<T> toEntity(List<String> source, String delimiter, Class<T> tClass) {
        if(null == tClass) {
            return null;
        }
        if(!BooleanHelper.hasLength(source)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(source.size());
        for (String item : source) {
            List<String> strings = Splitter.on(StringHelper.defaultIfBlank(delimiter, ",")).trimResults().omitEmptyStrings().splitToList(item);
            T entity = BeansHelper.setProperty(tClass, strings);
            if(null == entity) {
                continue;
            }
            result.add(entity);
        }
        return result;

    }

    /**
     * 如果集合为null返回空集合，反之返回原始数据
     * @param source 集合
     * @return
     */
    public static <T>List<T> forEmpty(final List<T> source) {
        if(null == source) {
            return Collections.emptyList();
        }
        return source;
    }
    /**
     * 如果集合为null返回空集合，反之返回原始数据
     * @param source 集合
     * @return
     */
    public static <T>Set<T> forEmpty(final Set<T> source) {
        if(null == source) {
            return Collections.emptySet();
        }
        return source;
    }

    /**
     * 过滤类型数据
     * @param source 数据源
     * @param type 类型
     * @param <T>
     * @return
     */
    public static <T>Set<T> filter(Collection<?> source, Class<T> type) {
        if(!BooleanHelper.hasLength(source)) {
            return Collections.emptySet();
        }
        Set<T> result = new HashSet<>(source.size());
        for (Object o : source) {
            if(!o.getClass().isAssignableFrom(type)) {
                continue;
            }
            result.add((T) o);
        }
        return result;
    }

    /**
     * 循环集合(自动判空)
     * @param source 数据
     * @param consumer 回调
     */
    public static <Item>void forEach(Collection<Item> source, Consumer<Item> consumer) {
        if(!BooleanHelper.hasLength(source) || null == consumer) {
            return;
        }
        for (Item item : source) {
            consumer.accept(item);
        }
    }

    /**
     * 数组转集合
     * @param source 数据源
     * @param <T>
     * @return
     */
    public static <T>List<T> toList(T[] source) {
        if(!BooleanHelper.hasLength(source)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(source.length);
        for (T t : source) {
            if(null == t) {
                continue;
            }
            result.add(t);
        }

        return result;
    }
    /**
     * 数组转集合
     * @param source 数据源
     * @param <T>
     * @return
     */
    public static <T>Set<T> toSet(T[] source) {
        if(!BooleanHelper.hasLength(source)) {
            return Collections.emptySet();
        }
        Set<T> result = new HashSet<>(source.length);
        for (T t : result) {
            if(null == t) {
                continue;
            }
            result.add(t);
        }

        return result;
    }

    /**
     * 合并集合
     * @param sources
     * @param <E>
     * @return
     */
    public static <E> Collection<E> combine(Collection<? extends E>... sources) {
        if(!BooleanHelper.hasLength(sources)) {
            return Collections.emptyList();
        }
        List<E> allElements = new ArrayList<>();
        for (Collection<? extends E> e : sources) {
            allElements.addAll(e);
        }
        return allElements;
    }
}
