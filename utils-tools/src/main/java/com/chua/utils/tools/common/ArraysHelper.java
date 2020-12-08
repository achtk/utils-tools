package com.chua.utils.tools.common;


import com.chua.utils.tools.empty.EmptyOrBase;
import com.google.common.base.Splitter;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.chua.utils.tools.constant.NumberConstant.INDEX_NOT_FOUND;

/**
 * 数组工具类
 *
 * @author CH
 */
public class ArraysHelper {

    private static final String[] EMPTY_ARRAY = new String[0];

    private static final ConcurrentHashMap<Class<?>, Object> CACHE_ARRAY = new ConcurrentHashMap<>();

    /**
     * 数组初始化
     *
     * @param tClass 类型
     * @param size   长度
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArrays(final Class<T> tClass, final int size) {
        return (T[]) Array.newInstance(tClass, size);
    }

    /**
     * 数组初始化
     *
     * @param tClass 类型
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArrays(final Class<T> tClass) {
        return (T[]) Array.newInstance(tClass, 16);
    }

    /**
     * 数组初始化
     *
     * @param <T>
     * @param t   数据
     * @return
     */
    public static <T> List<T> newArrays(@SuppressWarnings("unchecked") final T... t) {
        if (null != t && t.length > 0) {
            List<T> tList = new ArrayList<>(t.length);
            for (T t1 : t) {
                tList.add(t1);
            }
            return tList;
        }
        return Collections.emptyList();
    }

    /**
     * 查询元素在数组中的位置
     *
     * @param arrays
     * @param object
     * @return
     */
    public static int binarySearch(Object[] arrays, Object object) {
        int index = -1, line = -1;
        if (null != arrays && (line = arrays.length) > 0 && null != object) {
            for (int i = 0; i < line; i++) {
                Object array = arrays[i];
                if (array.equals(object)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    /**
     * 显示字符串数组的内容，用,分隔
     *
     * @param args 字符串数组
     * @return 字符串数组的内容
     */
    public static String toString(String[] args) {
        return toString(args, ",");
    }

    /**
     * 显示字符串数组的内容
     *
     * @param args      字符串数组
     * @param separator 分隔符
     * @return 字符串数组的内容
     */
    public static String toString(String[] args, String separator) {
        if (args == null || args.length == 0) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                buffer.append(separator);
            }
            buffer.append(args[i]);
        }
        return buffer.toString();
    }

    /**
     * 取得字符串数组的第一个元素
     *
     * @param stringArray 字符串数组
     * @return 字符串数组的第一个元素
     */
    public static String getFirst(String[] stringArray) {
        if (stringArray == null || stringArray.length == 0) {
            return null;
        }
        return stringArray[0];
    }

    /**
     * 取得数组的第一个元素
     *
     * @param array 数组
     * @return 数组的第一个元素
     */
    public static <T> T getFirst(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[0];
    }

    /**
     * 把List转换成字符串数组
     *
     * @param list 字符串List
     * @return 字符串数组
     */
    public static String[] toArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    /**
     * 把List转换成字符串数组
     *
     * @param collection 字符串List
     * @return 字符串数组
     */
    public static Object[] toArray(Collection<?> collection) {
        return collection.toArray(new Object[collection.size()]);
    }

    /**
     * 把List转换成T数组
     *
     * @param list TList
     * @return T数组
     */
    public static <T> T[] toArray(List<T> list, Class<T> tClass) {
        T[] ts = (T[]) Array.newInstance(tClass);
        return list.toArray(ts);
    }

    /**
     * 把Set转换成字符串数组
     *
     * @param set 字符串Set
     * @return 字符串数组
     */
    public static String[] toArray(Set<String> set) {
        return set.toArray(new String[set.size()]);
    }

    /**
     * 判断字符串数组是否包含指定的字符串
     *
     * @param array 字符串数组
     * @param str   指定的字符串
     * @return 包含true，否则false
     */
    public static <T> boolean contains(T[] array, T str) {
        if (array == null || array.length == 0) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null && str == null) {
                return true;
            }
            if (array[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串数组是否有不为Empty的值
     *
     * @param args 字符串数组
     * @return 有true，否则false
     */
    public static boolean hasValue(String[] args) {
        if (null == args || 0 == args.length) {
            return false;
        }
        for (int i = 0, length = args.length; i < length; i++) {
            if (args[i] != null || args[i].trim().length() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 联合两个数组
     *
     * @param first 第一个数组
     * @param last  另一个数组
     * @return 内容合并后的数组
     */
    public static Object[] combine(Object[] first, Object[] last) {
        if (first.length == 0 && last.length == 0) {
            return null;
        }
        Object[] result = new Object[first.length + last.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(last, 0, result, first.length, last.length);
        return result;
    }

    /**
     * 把数组转换成 列表，如果数组为 null，则会返回一个空列表。
     *
     * @param array 数组
     * @return 列表对象
     */
    public static <T> List<T> toList(T[] array, T... defaultT) {
        List<T> list = new ArrayList<>();
        if (array == null) {
            if (!isEmpty(defaultT)) {
                list.addAll(Arrays.asList(defaultT));
            }
            return list;
        }

        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        if (!isEmpty(defaultT)) {
            list.addAll(Arrays.asList(defaultT));
        }

        return list;
    }

    /**
     * 清除字符串数组中的null
     *
     * @param array 字符串数组
     * @return 清除null后的字符串数组
     */
    public static String[] clearNull(String[] array) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                list.add(array[i]);
            }
        }
        return toArray(list);
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @return 当数组为空或者null, 返回: true, 负责返回 false
     */
    public static <T> boolean isEmpty(T[] array) {
        return null == array || array.length == 0;
    }

    /**
     * 数组复制
     *
     * @param array 原数组
     * @param <T>   类型
     * @return 新数组
     */
    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * 数组合并
     * <pre>
     * ArraysHelper.addAll(null, null)     = null
     * ArraysHelper.addAll(array1, null)   = cloned copy of array1
     * ArraysHelper.addAll(null, array2)   = cloned copy of array2
     * ArraysHelper.addAll([], [])         = []
     * ArraysHelper.addAll([null], [null]) = [null, null]
     * ArraysHelper.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
     * </pre>
     *
     * @param array1 数组1
     * @param array2 数组2
     * @param <T>
     * @return
     */
    public static <T> T[] addAll(final T[] array1, final T... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final Class<?> type1 = array1.getClass().getComponentType();
        final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (final ArrayStoreException ase) {
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
            }
            throw ase;
        }
        return joinedArray;
    }

    /**
     * 数组添加元素
     *
     * @param array   数组
     * @param element 元素
     * @param <T>
     * @return
     */
    public static <T> T[] add(final T[] array, final T element) {
        if (array == null) {
            return null == element ? null : ((Supplier<T[]>) () -> {
                T[] newArray = (T[]) Array.newInstance(element.getClass(), 1);
                newArray[0] = element;
                return newArray;
            }).get();
        } else if (element == null) {
            return array;
        }
        final Class<?> type1 = array.getClass().getComponentType();
        final T[] newArray = (T[]) Array.newInstance(type1, array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * 数组删除元素
     * <pre>
     * ArraysHelper.remove(null, 1)        = null
     * ArraysHelper.remove([], 1)          = []
     * ArraysHelper.remove([1], 0)         = [1]
     * ArraysHelper.remove([1, 0], 0)      = [1]
     * ArraysHelper.remove([1, 0, 1], 1)   = [0, 1]
     * </pre>
     *
     * @param array   数组
     * @param element 索引位置
     * @param <T>
     * @return
     */
    public static <T> T[] remove(final T[] array, final T element) {
        if (null == array) {
            return null;
        }
        int index = indexOf(array, element);
        return remove(array, index);
    }

    /**
     * 查询数组元素
     *
     * @param array   数组
     * @param element 索引位置
     * @param <T>
     * @return
     */
    public static <T> int indexOf(final T[] array, final T element) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (element.equals(array[i])) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;

    }

    /**
     * 获取元素位置
     *
     * @param source  源数据
     * @param element 元素
     * @return
     */
    public static int indexOf(char[] source, char element) {
        if (source == null) {
            return INDEX_NOT_FOUND;
        }
        for (int i = 0; i < source.length; i++) {
            if (element == source[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 数组删除元素
     * <pre>
     * ArraysHelper.remove([1], 0)          = []
     * ArraysHelper.remove([1, 0], 0)       = [0]
     * ArraysHelper.remove([1, 0], 1)       = [1]
     * ArraysHelper.remove([1, 0, 1], 1)    = [1, 1]
     * </pre>
     *
     * @param array 数组
     * @param index 索引位置
     * @param <T>
     * @return
     */
    public static <T> T[] remove(final T[] array, final int index) {
        if (array == null) {
            return null;
        }

        final int length = Array.getLength(array);
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }

        final Class<?> type1 = array.getClass().getComponentType();
        final T[] newArray = (T[]) Array.newInstance(type1, array.length - 1);
        System.arraycopy(array, 0, newArray, 0, index);
        if (index < length - 1) {
            System.arraycopy(array, index, newArray, index, length - index - 1);
        }
        return newArray;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public static String[] join(String[] x, String[] y) {
        String[] result = new String[x.length + y.length];
        System.arraycopy(x, 0, result, 0, x.length);
        System.arraycopy(y, 0, result, x.length, y.length);
        return result;
    }

    /**
     * @param x
     * @param y
     * @param use
     * @return
     */
    public static String[] join(String[] x, String[] y, boolean[] use) {
        String[] result = new String[x.length + countTrue(use)];
        System.arraycopy(x, 0, result, 0, x.length);
        int k = x.length;
        for (int i = 0; i < y.length; i++) {
            if (use[i]) {
                result[k++] = y[i];
            }
        }
        return result;
    }

    /**
     * @param coll
     * @return
     */
    public static String[] toStringArray(Collection coll) {
        return (String[]) coll.toArray(new String[coll.size()]);
    }

    /**
     * @param array
     * @return
     */
    public static int countTrue(boolean... array) {
        int result = 0;
        for (boolean anArray : array) {
            if (anArray) {
                result++;
            }
        }
        return result;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public static int[] join(int[] x, int[] y) {
        int[] result = new int[x.length + y.length];
        System.arraycopy(x, 0, result, 0, x.length);
        System.arraycopy(y, 0, result, x.length, y.length);
        return result;
    }

    /**
     * @param x
     * @param y
     * @param <T>
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] join(T[] x, T... y) {
        T[] result = (T[]) Array.newInstance(x.getClass().getComponentType(), x.length + y.length);
        System.arraycopy(x, 0, result, 0, x.length);
        System.arraycopy(y, 0, result, x.length, y.length);
        return result;
    }

    /**
     * 获取第一个节点
     *
     * @param arrays
     * @param <T>
     * @return
     */
    public static <T> T firstElement(T[] arrays) {
        return BooleanHelper.hasLength(arrays) ? arrays[0] : null;
    }

    /**
     * 空数组
     *
     * @param componentType 数据类型
     * @return
     */
    public static <T> T[] emptyArray(Class<T> componentType) {
        if (CACHE_ARRAY.containsKey(componentType)) {
            return (T[]) CACHE_ARRAY.get(componentType);
        }
        T[] result = (T[]) Array.newInstance(componentType, 0);
        CACHE_ARRAY.put(componentType, result);
        return result;
    }

    /**
     * 空数组
     *
     * @return
     */
    public static String[] emptyStringArray() {
        return EMPTY_ARRAY;
    }

    /**
     * 空字符串数组
     *
     * @return
     */
    public static String[] emptyString() {
        return EMPTY_ARRAY;
    }

    /**
     * 获取数据
     *
     * @param mapSource 集合数据
     * @param fields    字段
     * @return 数据
     */
    public static Object[] toArray(Map<String, Object> mapSource, List<String> fields) {
        if (null == mapSource || mapSource.isEmpty() || null == fields || fields.isEmpty()) {
            return EmptyOrBase.EMPTY_OBJECT;
        }
        Object[] result = new Object[fields.size()];
        int length = fields.size();
        for (int i = 0; i < length; i++) {
            result[i] = mapSource.get(fields.get(i));
        }
        return result;
    }

    /**
     * 分割数据
     *
     * @param source           数据
     * @param delimiter        分隔符
     * @param omitEmptyStrings 是否去除空值
     * @return 分割后数据
     */
    public static String[] splice(String source, String delimiter, final boolean omitEmptyStrings) {
        if (null == source || null == delimiter) {
            return EmptyOrBase.EMPTY_STRING;
        }
        Splitter splitter = null;
        if (!EmptyOrBase.SpecialSymbol(delimiter)) {
            splitter = Splitter.onPattern(delimiter);
        } else {
            splitter = Splitter.on(delimiter);
        }
        if(omitEmptyStrings) {
            splitter.trimResults().omitEmptyStrings();
        }
        return toArray(splitter.splitToList(source));
    }

    /**
     * 是否包含重复值
     *
     * @param array 数组
     * @return
     */
    public <T> boolean containsDuplicate(final T[] array) {
        Set<T> set = new HashSet<>();
        for (T i : array) {
            if (!set.add(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 遍历
     *
     * @param <T>
     * @param sources  元数据
     * @param consumer 遍历方法
     * @return
     */
    public static <T> void iterator(final T[] sources, final Consumer<T> consumer) {
        if (isEmpty(sources)) {
            return;
        }
        T t = null;
        for (int i = 0, size = sources.length; i < size; i++) {
            t = sources[i];
            if (null != t && null != consumer) {
                consumer.accept(t);
            }
        }
    }
}
