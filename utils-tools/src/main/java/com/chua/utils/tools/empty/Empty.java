package com.chua.utils.tools.empty;

import com.chua.utils.tools.collects.collections.ListHelper;
import com.chua.utils.tools.collects.map.MapOperableHelper;

import java.util.*;

/**
 * 默认对象
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/6
 */
@SuppressWarnings("all")
public class Empty {
    /**
     * 空List
     */
    public static final List<?> EMPTY_LIST = Collections.emptyList();
    /**
     * 空 Set
     */
    public static final Set<?> EMPTY_SET = Collections.emptySet();
    /**
     * 空properties
     */
    public static final Properties EMPTY_PROPERTIES = new Properties();

    /**
     * 空 map
     */
    public static final Map<?, ?> EMPTY_MAP = Collections.emptyMap();
    /**
     * Class<Map<String, Object>>
     */
    public static final Class<Map<String, Object>> MAP_STRING_OBJECT = (Class<Map<String, Object>>) MapOperableHelper.newMapStringObject().getClass();
    /**
     * Class<Map<String, String>>
     */
    public static final Class<Map<Object, Object>> MAP_OBJECT_OBJECT = (Class<Map<Object, Object>>) MapOperableHelper.newMapObjectObject().getClass();
    /**
     * Class<List<String>>
     */
    public static final Class<List<String>> LIST_STRING = (Class<List<String>>) ListHelper.newArrayList().getClass();
    /**
     * Class<List<Map<String, Object>>>
     */
    public static final Class<List<Map<String, Object>>> LIST_MAP_STRING_OBJECT = (Class<List<Map<String, Object>>>) ListHelper.newArrayList().getClass();
    /**
     * Class<Map<String, List<Map<String, Object>>>>
     */
    public static final Class<Map<String, List<Map<String, Object>>>> MAP_LIST_MAP = (Class<Map<String, List<Map<String, Object>>>>) MapOperableHelper.newArrayList().getClass();
    /**
     * Class<Map<String, String>>
     */
    public static final Class<Map<String, String>> MAP_STRING_STRING = (Class<Map<String, String>>) MapOperableHelper.newMapStringString().getClass();
    /**
     * new String[0]
     */
    public static final String[] EMPTY_STRING = new String[0];
}
