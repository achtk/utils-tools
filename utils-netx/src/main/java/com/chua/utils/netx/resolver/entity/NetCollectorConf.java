package com.chua.utils.netx.resolver.entity;

import lombok.Data;

/**
 * 集合
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
@Data
public class NetCollectorConf {
    /**
     * 集合名称
     */
    private String collectionName;
    /**
     * 分片(lucene)
     */
    private int fragmentation = 1;
}
