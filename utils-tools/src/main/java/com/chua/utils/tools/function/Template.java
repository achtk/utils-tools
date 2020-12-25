package com.chua.utils.tools.function;

/**
 * 模板
 * <p>对应spring template</p>
 * @author CH
 * @version 1.0.0
 * @since 2020/10/21
 */
@FunctionalInterface
public interface Template<T> {
    /**
     * 获取模板
     * @return
     */
    T getTemplate();
}
