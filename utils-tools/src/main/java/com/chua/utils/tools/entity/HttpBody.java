package com.chua.utils.tools.entity;

import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * http消息体
 *
 * @author CH
 * @date 2020-09-30
 */
@NoArgsConstructor(staticName = "newBody")
public class HttpBody extends HashMap {
    /**
     * 添加数据
     *
     * @param key   索引
     * @param value 值
     * @return
     */
    public HttpBody add(Object key, Object value) {
        this.put(key, value);
        return this;
    }
}
