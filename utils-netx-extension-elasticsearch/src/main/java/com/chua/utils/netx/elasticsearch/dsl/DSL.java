package com.chua.utils.netx.elasticsearch.dsl;

import lombok.Getter;
import lombok.Setter;

/**
 *  dsl语句
 */
@Getter
@Setter
public class DSL {
    /**
     * 请求地址
     */
    private String uri;
    /**
     * 语句
     */
    private String query;
}
