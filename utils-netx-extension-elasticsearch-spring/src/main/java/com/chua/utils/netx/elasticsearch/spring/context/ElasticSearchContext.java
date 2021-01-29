package com.chua.utils.netx.elasticsearch.spring.context;

import com.chua.utils.netx.elasticsearch.spring.factory.ElasticSearchFactory;
import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.properties.NetProperties;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.util.CloseableIterator;

import java.util.List;

/**
 * es上线文
 *
 * @author CH
 */
public class ElasticSearchContext implements ElasticsearchOperations, AutoCloseable {

    private NetProperties netProperties;
    private final INetFactory<AbstractElasticsearchTemplate> netxFactory;
    private final AbstractElasticsearchTemplate elasticsearchTemplate;

    public ElasticSearchContext(NetProperties netProperties) {
        this.netProperties = netProperties;
        this.netxFactory = new ElasticSearchFactory(netProperties);
        this.elasticsearchTemplate = this.netxFactory.client();
    }

    @Override
    public IndexOperations indexOps(Class<?> clazz) {
        return this.elasticsearchTemplate.indexOps(clazz);
    }


    @Override
    public IndexOperations indexOps(IndexCoordinates index) {
        return this.elasticsearchTemplate.indexOps(index);
    }


    @Override
    public ElasticsearchConverter getElasticsearchConverter() {
        return this.elasticsearchTemplate.getElasticsearchConverter();
    }


    @Override
    public IndexCoordinates getIndexCoordinatesFor(Class<?> clazz) {
        return this.elasticsearchTemplate.getIndexCoordinatesFor(clazz);
    }


    @Override
    public <T> T save(T entity) {
        return this.elasticsearchTemplate.save(entity);
    }


    @Override
    public <T> T save(T entity, IndexCoordinates index) {
        return this.elasticsearchTemplate.save(entity, index);
    }


    @Override
    public <T> Iterable<T> save(Iterable<T> entities) {
        return this.elasticsearchTemplate.save(entities);
    }


    @Override
    public <T> Iterable<T> save(Iterable<T> entities, IndexCoordinates index) {
        return this.elasticsearchTemplate.save(entities, index);
    }


    @Override
    public <T> Iterable<T> save(T... entities) {
        return this.elasticsearchTemplate.save(entities);
    }


    @Override
    public String index(IndexQuery query, IndexCoordinates index) {
        return this.elasticsearchTemplate.index(query, index);
    }


    @Override
    public <T> T get(String id, Class<T> clazz) {
        return this.elasticsearchTemplate.get(id, clazz);
    }


    @Override
    public <T> T get(String id, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.get(id, clazz, index);
    }


    @Override
    public <T> List<T> multiGet(Query query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.multiGet(query, clazz, index);
    }


    @Override
    public boolean exists(String id, Class<?> clazz) {
        return this.elasticsearchTemplate.exists(id, clazz);
    }


    @Override
    public boolean exists(String id, IndexCoordinates index) {
        return this.elasticsearchTemplate.exists(id, index);
    }


    @Override
    public List<String> bulkIndex(List<IndexQuery> queries, BulkOptions bulkOptions, IndexCoordinates index) {
        return this.elasticsearchTemplate.bulkIndex(queries, bulkOptions, index);
    }


    @Override
    public void bulkUpdate(List<UpdateQuery> queries, BulkOptions bulkOptions, IndexCoordinates index) {
        this.elasticsearchTemplate.bulkUpdate(queries, bulkOptions, index);
    }


    @Override
    public String delete(String id, IndexCoordinates index) {
        return this.elasticsearchTemplate.delete(id, index);
    }


    @Override
    public String delete(String id, Class<?> entityType) {
        return this.elasticsearchTemplate.delete(id, entityType);
    }


    @Override
    public String delete(Object entity) {
        return this.elasticsearchTemplate.delete(entity);
    }


    @Override
    public String delete(Object entity, IndexCoordinates index) {
        return this.elasticsearchTemplate.delete(entity, index);
    }


    @Override
    public void delete(Query query, Class<?> clazz, IndexCoordinates index) {
        this.elasticsearchTemplate.delete(query, clazz, index);
    }


    @Override
    public UpdateResponse update(UpdateQuery updateQuery, IndexCoordinates index) {
        return this.elasticsearchTemplate.update(updateQuery, index);
    }


    @Override
    public void delete(DeleteQuery query, IndexCoordinates index) {
        this.elasticsearchTemplate.delete(query, index);
    }


    @Override
    public <T> T get(GetQuery query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.get(query, clazz, index);
    }


    @Override
    public <T> T queryForObject(GetQuery query, Class<T> clazz) {
        return this.elasticsearchTemplate.queryForObject(query, clazz);
    }


    @Override
    public long count(Query query, Class<?> clazz) {
        return this.elasticsearchTemplate.count(query, clazz);
    }


    @Override
    public long count(Query query, Class<?> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.count(query, clazz, index);
    }


    @Override
    public <T> CloseableIterator<T> stream(Query query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.stream(query, clazz, index);
    }


    @Override
    public SearchResponse suggest(SuggestBuilder suggestion, IndexCoordinates index) {
        return this.elasticsearchTemplate.suggest(suggestion, index);
    }


    @Override
    public <T> List<SearchHits<T>> multiSearch(List<? extends Query> queries, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.multiSearch(queries, clazz, index);
    }


    @Override
    public List<SearchHits<?>> multiSearch(List<? extends Query> queries, List<Class<?>> classes, IndexCoordinates index) {
        return this.elasticsearchTemplate.multiSearch(queries, classes, index);
    }


    @Override
    public <T> SearchHits<T> search(Query query, Class<T> clazz) {
        return this.elasticsearchTemplate.search(query, clazz);
    }


    @Override
    public <T> SearchHits<T> search(Query query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.search(query, clazz, index);
    }


    @Override
    public <T> SearchHits<T> search(MoreLikeThisQuery query, Class<T> clazz) {
        return this.elasticsearchTemplate.search(query, clazz);
    }


    @Override
    public <T> SearchHits<T> search(MoreLikeThisQuery query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.search(query, clazz, index);
    }


    @Override
    public <T> SearchHitsIterator<T> searchForStream(Query query, Class<T> clazz) {
        return this.elasticsearchTemplate.searchForStream(query, clazz);
    }


    @Override
    public <T> SearchHitsIterator<T> searchForStream(Query query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.searchForStream(query, clazz, index);
    }

    @Override
    public void close() throws Exception {
        if (null != this.netxFactory) {
            netxFactory.close();
        }
    }

    public AbstractElasticsearchTemplate getTemplate() {
        return elasticsearchTemplate;
    }
}
