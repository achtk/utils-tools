package com.chua.utils.netx.elasticsearch.interfaces;

/**
 * es同步查询接口
 * @author CH
 * @since 1.0
 */
public interface IElasticSearchSyncFactory extends IElasticSearchFactory {
    /**
     * 分词
     * @param indexName 索引
     * @param indexType 类型
     */
    public void notAnalyzed(final String indexName, final String indexType, final String field);

    /**
     * 分词
     * @param indexName 索引
     * @param field 字段
     */
    default public void notAnalyzed(final String indexName, final String field) {
        notAnalyzed(field, "text", field);
    }
}
