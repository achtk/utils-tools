package com.chua.utils.netx.entity;

import com.chua.utils.tools.function.BiAppendable;

import java.io.IOException;
import java.util.HashMap;

/**
 * 文档
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class DocumentMap extends HashMap<String, Object> implements BiAppendable<String, Object> {

    @Override
    public BiAppendable append(String v1, Object v2) {
        this.put(v1, v2);
        return this;
    }
}
