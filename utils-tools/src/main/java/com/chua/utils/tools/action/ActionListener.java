package com.chua.utils.tools.action;

/**
 * 动作属性
 * @author CH
 */
public interface ActionListener {
    /**
     * 监听
     * @param key 索引
     * @param items 值
     */
    void listener(Object key, Object... items);
}
