package com.chua.utils.tools.collects.collections;

import com.chua.utils.tools.common.*;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.Matcher;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
     * @param collection 集合collection
     * @return Boolean
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
     * @param kvMap 集合 kvMap
     * @return Boolean
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
     * @param collection 集合
     * @return Boolean
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
     * @param kvMap 集合
     * @return Boolean
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
     * @param items 元素
     * @param <E>   类型
     * @return ImmutableList
     */
    @SafeVarargs
    public static <E> ImmutableList<E> form(E... items) {
        return null == items ? ImmutableList.of() : ImmutableList.<E>builder().add(items).build();
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
     * @param kvMap 集合
     * @return 长度
     */
    public static <K, V> int size(Map<K, V> kvMap) {
        return isEmpty(kvMap) ? 0 : kvMap.size();
    }

    /**
     * 过滤元素
     *
     * @param list      集合
     * @param predicate predicate
     * @param <E>       类型
     * @see com.google.common.base.Predicate
     */
    public static <E> Collection<E> doWithFilter(Collection<E> list, Predicate<E> predicate) {
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
     * @param <E>    类型
     * @return Collection
     */
    public static <E> Collection<E> difference(Collection<E> source, Collection<E> target) {
        if (isBlank(source) || isBlank(target)) {
            return source;
        }
        Set<E> set = new HashSet<>();
        Iterator<E> iterator = source.iterator();
        E elem;

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
     * @param <E>    类型
     * @return Collection
     */
    public static <E> Collection<E> same(Collection<E> source, Collection<E> target) {
        if (isBlank(source) || isBlank(target)) {
            return source;
        }
        Set<E> set = new HashSet<>();
        Iterator<E> iterator = source.iterator();

        E elem;
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
     * @param collection 集合
     * @param index      索引
     * @return 元素
     */
    public static <E> E get(Collection<E> collection, int index) {
        if (isBlank(collection) || collection.size() < index) {
            return null;
        }

        int cnt = 0;
        for (E e : collection) {
            if (cnt++ != index) {
                continue;
            }
            return e;
        }
        return null;
    }

    /**
     * 获取值
     *
     * @param objectMap 集合
     * @param key       索引
     * @return 元素
     */
    public static String get(Map<String, Object> objectMap, String key) {
        return get(objectMap, key, null);
    }

    /**
     * 获取值
     *
     * @param objectMap    集合
     * @param key          索引
     * @param defaultValue 默认值
     * @param <E>          类型
     * @return 元素
     */
    @SuppressWarnings("unchecked")
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
     * @return String
     */
    public static String toString(Collection<?> collection) {
        return null == collection ? "[]" : "[" + Joiner.on(",").join(collection.iterator()) + "]";
    }

    /**
     * 转字符串
     *
     * @param array 数组
     * @return 集合字符串
     */
    public static <T> String toString(T[] array) {
        return null == array ? "[]" : "[" + Joiner.on(",").join(array) + "]";
    }

    /**
     * 转字符串
     *
     * @param array 数组
     * @return 集合字符串
     */
    public static String toString(byte[] array) {
        if (null == array) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length);
        for (byte b : array) {
            sb.append(",").append(b);
        }
        return sb.length() == 0 ? "[]" : sb.substring(1);
    }

    /**
     * 转字符串
     *
     * @param map 集合
     * @return 集合字符串
     */
    public static String toString(Map<?, ?> map) {
        return null == map ? "{}" : JsonHelper.toJson(map);
    }

    /**
     * 获取索引位置
     *
     * @param source  源数据
     * @param element 节点
     * @return 位置
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
     * @return 位置
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
     * @param params 集合
     * @return 第一个元素
     */
    public static <T> T firstElement(final Map<String, T> params) {
        return FinderHelper.firstElement(params);
    }

    /**
     * 获取第一个元素
     *
     * @param list 集合
     * @return 第一个元素
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
     * @return 不为空集合
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
     * @return 不为空集合
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
     * @return 不为空集合
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
     * @return 不为空集合
     */
    public static <K, V> Map<K, V> ifEmpty(final Map<K, V> source, final Map<K, V> defaultTarget) {
        return BooleanHelper.hasLength(source) ? source : defaultTarget;
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

    /**
     * 合并数据
     *
     * @param source 数据1
     * @param items  元素  数据2
     * @return 集合
     */
    @SafeVarargs
    public static <T> List<T> merge(final List<T> source, final T... items) {
        List<T> result = ObjectHelper.firstNonNull(source, new ArrayList<>());
        if (!BooleanHelper.hasLength(items)) {
            return result;
        }

        result.addAll(Arrays.asList(items));

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
            fileResolvers = new ArrayList<>();
        }
        if (null != fileResolvers1) {
            fileResolvers.addAll(fileResolvers1);
        }
    }

    /**
     * 集合转数组
     *
     * @param sources 数据源
     * @return 数组
     */
    @SuppressWarnings("all")
    public static <T> T[] toArray(List<T> sources) {
        if (!BooleanHelper.hasLength(sources)) {
            return null;
        }

        return (T[]) sources.toArray();
    }

    /**
     * 返回第一个有效的数据
     *
     * @param <T>        类型
     * @param collection 集合数据集合
     * @return 元素
     */
    public static <T> T findFirstOne(Collection<T> collection) {
        return FinderHelper.firstElement(collection);
    }

    /**
     * 返回第一个有效的数据
     *
     * @param <T>        类型
     * @param collection 集合数据集合
     * @return 元素
     */
    public static <T> T findLastOne(Collection<T> collection) {
        return FinderHelper.lastElement(collection);
    }

    /**
     * 清空集合数据
     *
     * @param source 集合
     */
    public static synchronized <T> void clear(final List<T> source) {
        if (null == source) {
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
    public static synchronized <T> void clear(final Set<T> source) {
        if (null == source) {
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
    public static <T> List<T> toEntity(List<String> source, String delimiter, Class<T> tClass) {
        if (null == tClass) {
            return null;
        }
        if (!BooleanHelper.hasLength(source)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(source.size());
        for (String item : source) {
            List<String> strings = Splitter.on(StringHelper.defaultIfBlank(delimiter, ",")).trimResults().omitEmptyStrings().splitToList(item);
            T entity = BeansHelper.setProperty(tClass, strings);
            if (null == entity) {
                continue;
            }
            result.add(entity);
        }
        return result;

    }

    /**
     * 如果集合为null返回空集合，反之返回原始数据
     *
     * @param source 集合
     * @return 返回不为空集合
     */
    public static <T> List<T> forEmpty(final List<T> source) {
        if (null == source) {
            return Collections.emptyList();
        }
        return source;
    }

    /**
     * 如果集合为null返回空集合，反之返回原始数据
     *
     * @param source 集合
     * @return 返回不为空集合
     */
    public static <T> Set<T> forEmpty(final Set<T> source) {
        if (null == source) {
            return Collections.emptySet();
        }
        return source;
    }

    /**
     * 过滤类型数据
     *
     * @param source 数据源
     * @param type   类型
     * @param <T>    类型
     * @return 过滤的数据
     */
    public static <T> Set<T> doWithFilter(Collection<?> source, Class<T> type) {
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
    public static <Item> void forEach(Collection<Item> source, Consumer<Item> consumer) {
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
    public static <T> List<T> toList(T[] source) {
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
    public static <T> Set<T> toSet(T[] source) {
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
     * 合并集合
     *
     * @param sources 集合
     * @param <E>     类型
     * @return 集合
     */
    @SafeVarargs
    public static <E> Collection<E> combine(Collection<? extends E>... sources) {
        if (!BooleanHelper.hasLength(sources)) {
            return Collections.emptyList();
        }
        List<E> allElements = new ArrayList<>();
        for (Collection<? extends E> e : sources) {
            allElements.addAll(e);
        }
        return allElements;
    }

    /**
     * 包含值
     *
     * @param collection 集合集合
     * @param value      值
     * @param <V>        类型
     * @return boolean
     */
    public static <V> boolean contains(Collection<V> collection, V value) {
        return BooleanHelper.hasLength(collection) && collection.contains(value);
    }

    /**
     * 过滤元素
     *
     * @param collection 集合集合
     * @param filter     过滤器
     * @return List<V>
     */
    public static <V> Set<V> doWithFilter(Set<V> collection, Filter<V> filter) {
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
    public static <V> List<V> doWithFilter(List<V> collection, Filter<V> filter) {
        if (!BooleanHelper.hasLength(collection)) {
            return collection;
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
    public static <V> void doWithMatcher(Collection<V> collection, Matcher<V> matcher) {
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
    public static int getSize(Collection<?> collection) {
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
    public static <T> List<T> getListIfFeasible(T value) {
        if (null == value) {
            return Collections.emptyList();
        }
        if (value instanceof Collection) {
            return new ArrayList<T>((Collection) value);
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
    public static <T> T getRandom(List<T> source) {
        if (isEmpty(source)) {
            return null;
        }
        Random random = new Random();
        int i = random.nextInt(source.size());
        return source.get(i);
    }
}
