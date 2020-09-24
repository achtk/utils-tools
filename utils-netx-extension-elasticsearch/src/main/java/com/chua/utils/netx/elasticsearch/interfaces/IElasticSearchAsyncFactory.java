package com.chua.utils.netx.elasticsearch.interfaces;

import com.chua.utils.netx.elasticsearch.config.IndexConfig;

/**
 * es异步查询接口
 *
 * @author CH
 * @since 1.0
 */
public interface IElasticSearchAsyncFactory extends IElasticSearchFactory {
    /**
     * @param indexConfig
     * @return
     */
    default public boolean createIndexAsync(IndexConfig indexConfig) {
        return createIndex(indexConfig);
    }

    /**
     * @param indexConfig
     * @return
     */
    default public boolean deleteIndexAsync(IndexConfig indexConfig) {
        return deleteIndex(indexConfig);
    }

    /**
     * @param indexConfig
     * @return
     */
    default public boolean addDocumentAsync(IndexConfig indexConfig) {
        return addDocument(indexConfig);
    }
    /**
     * @param indexConfig
     * @return
     */
    default public boolean getDocumentAsync(IndexConfig indexConfig) {
        return getDocumentAsync(indexConfig);
    }
}
