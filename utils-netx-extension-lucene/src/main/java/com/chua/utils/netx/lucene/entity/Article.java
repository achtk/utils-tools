package com.chua.utils.netx.lucene.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文档对象
 * @author CH
 * @version 1.0
 * @since 2020/10/20 18:13
 */
@Data
@EqualsAndHashCode
public class Article {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private Object value;

    private boolean stored;

}
