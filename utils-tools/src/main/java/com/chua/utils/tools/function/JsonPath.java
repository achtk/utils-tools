package com.chua.utils.tools.function;

import java.util.List;
import java.util.Map;

/**
 * json path
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
}
