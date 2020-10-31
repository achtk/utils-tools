package com.chua.utils.tools.common;

import java.util.*;

/**
 * 多Map工具类
 *
 * @author CH
 * @version 1.0.0
 * @see com.chua.utils.tools.common.MapOperableHelper
 * @since 2020/10/31
 */
public class MultiMapOperableHelper {
    /**
     * 根据索引获取所有集合中的数据
     *
     * @param key  索引
     * @param maps 集合
     * @param <K>
     * @return 集合索引不存在返回null
     */
    public <K> List<Object> getLists(final K[] key, final Map<K, Object>... maps) {
        if (null == maps || maps.length == 0) {
            return null;
        }
        List<Object> result = new ArrayList<>(maps.length * key.length);
        for (K k : key) {
            List<Object> lists = getLists(k, maps);
            if (null == lists) {
                continue;
            }
            result.addAll(lists);
        }
        return result;
    }

    /**
     * 根据索引获取所有集合中的{T}类型数据
     *
     * @param key    索引
     * @param tClass 类型
     * @param maps   集合
     * @param <K>
     * @return 集合索引不存在返回null
     */
    public <K, T> List<T> getLists(final K[] key, final Class<T> tClass, final Map<K, Object>... maps) {
        if (null == maps || maps.length == 0) {
            return null;
        }
        List<T> result = new ArrayList<>(maps.length * key.length);
        for (K k : key) {
            List<T> lists = getLists(key, tClass, maps);
            if (null == lists) {
                continue;
            }
            result.addAll(lists);
        }
        return result;
    }

    /**
     * 根据索引获取所有集合中的{T}类型数据
     *
     * @param key    索引
     * @param tClass 类型
     * @param maps   集合
     * @param <K>
     * @return 集合索引不存在返回null
     */
    public <K, T> List<T> getLists(final K key, final Class<T> tClass, final Map<K, Object>... maps) {
        if (null == maps || maps.length == 0 || null == tClass) {
            return null;
        }
        List<T> result = new ArrayList<>(maps.length);
        for (Map<K, Object> objectMap : maps) {
            if (!objectMap.containsKey(key)) {
                continue;
            }
            Object o = objectMap.get(key);
            if (null != o && tClass.isAssignableFrom(o.getClass())) {
                result.add((T) o);
            }
        }
        return result;
    }

    /**
     * 根据索引获取所有集合中的数据
     *
     * @param key  索引
     * @param maps 集合
     * @param <K>
     * @return 集合索引不存在返回null
     */
    public <K> List<Object> getLists(final K key, final Map<K, Object>... maps) {
        if (null == maps || maps.length == 0) {
            return null;
        }
        List<Object> result = new ArrayList<>(maps.length);
        for (Map<K, Object> objectMap : maps) {
            if (!objectMap.containsKey(key)) {
                continue;
            }
            result.add(objectMap.get(key));
        }
        return result;
    }

    /**
     * 根据索引获取所有集合中的数据
     *
     * @param key  索引
     * @param maps 集合
     * @param <K>
     * @return 集合索引不存在返回[]
     */
    public <K> List<Object> getListsOrDefault(final K key, final Map<K, Object>... maps) {
        List<Object> lists = getLists(key, maps);
        return null == lists ? Collections.emptyList() : lists;
    }

    /**
     * 根据索引获取所有集合中的数据
     *
     * @param key  索引
     * @param maps 集合
     * @param <K>
     * @return 集合索引不存在返回null
     */
    public <K> Set<Object> getSets(final K[] key, final Map<K, Object>... maps) {
        if (null == maps || maps.length == 0) {
            return null;
        }
        Set<Object> result = new HashSet<>(maps.length * key.length);
        for (K k : key) {
            Set<Object> lists = getSets(k, maps);
            if (null == lists) {
                continue;
            }
            result.addAll(lists);
        }
        return result;
    }

    /**
     * 根据索引获取所有集合中的{T}类型数据
     *
     * @param key    索引
     * @param tClass 类型
     * @param maps   集合
     * @param <K>
     * @return 集合索引不存在返回null
     */
    public <K, T> Set<T> getSets(final K key, final Class<T> tClass, final Map<K, Object>... maps) {
        if (null == maps || maps.length == 0 || null == tClass) {
            return null;
        }
        Set<T> result = new HashSet<>(maps.length);
        for (Map<K, Object> objectMap : maps) {
            if (!objectMap.containsKey(key)) {
                continue;
            }
            Object o = objectMap.get(key);
            if (null != o && tClass.isAssignableFrom(o.getClass())) {
                result.add((T) o);
            }
        }
        return result;
    }

    /**
     * 根据索引获取所有集合中的数据
     *
     * @param key  索引
     * @param maps 集合
     * @param <K>
     * @return 集合索引不存在返回null
     */
    public <K> Set<Object> getSets(final K key, final Map<K, Object>... maps) {
        if (null == maps || maps.length == 0) {
            return null;
        }
        Set<Object> result = new HashSet<>(maps.length);
        for (Map<K, Object> objectMap : maps) {
            if (!objectMap.containsKey(key)) {
                continue;
            }
            result.add(objectMap.get(key));
        }
        return result;
    }

    /**
     * 根据索引获取所有集合中的数据
     *
     * @param key  索引
     * @param maps 集合
     * @param <K>
     * @return 集合索引不存在返回[]
     */
    public <K> Set<Object> getSetsOrDefault(final K key, final Map<K, Object>... maps) {
        Set<Object> lists = getSets(key, maps);
        return null == lists ? Collections.emptySet() : lists;
    }
}
