package com.chua.utils.tools.data.parser;

import com.chua.utils.tools.collects.HashOperateMap;

import java.util.Map;

/**
 * 数据解析器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
public interface DataParser {
    /**
     * 获取源
     *
     * @return 源
     */
    Object getSource();

    /**
     * 设置源
     *
     * @param source 源
     */
    void setSource(Object source);

    /**
     * 获取参数
     *
     * @return 参数
     */
    HashOperateMap getOperate();

    /**
     * 设置参数
     *
     * @param hashOperateMap 参数
     */
    void setOperate(HashOperateMap hashOperateMap);

    /**
     * 获取表头
     *
     * @return 字段-数据类型
     */
    Map<String, String> getDataType();

    /**
     * 当前数据
     *
     * @return 数据
     */
    Object[] getCurrent();

    /**
     * 是否还有数据
     *
     * @return
     */
    boolean hasNext();

    /**
     * 重置
     */
    void reset();
}
