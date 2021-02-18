package com.chua.utils.tools.function;

import com.chua.utils.tools.collects.FlatMap;

import java.util.List;
import java.util.Map;

/**
 * json path
 * <p>通过路径查询数据</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/26
 */
public interface JsonPath {
    /**
     * 查询数据
     *
     * @param path 路径
     * @return List
     */
    List<Object> find(String path);

    /**
     * 查询数据
     *
     * @param path 路径
     * @return Map
     */
    Map<String, Object> findMap(String path);

    /**
     * 查询数据
     *
     * @param path 路径
     * @return Map
     */
    FlatMap flatMap(String path);

    /**
     * 赋值
     *
     * @param path  路径
     * @param value 值
     */
    void set(String path, Object value);
}
