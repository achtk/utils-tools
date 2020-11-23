package com.chua.utils.netx.elasticsearch.spring.context;

import com.chua.utils.netx.elasticsearch.spring.factory.ElasticSearchFactory;
import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.function.Template;
import com.chua.utils.tools.properties.NetProperties;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.springframework.data.elasticsearch.core.AbstractElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.util.CloseableIterator;

import java.util.List;

/**
 * es上线文
 * @author CH
 */
public class ElasticSearchContext implements Template<AbstractElasticsearchTemplate>, AutoCloseable {

    private NetProperties netProperties;
    private final INetFactory<AbstractElasticsearchTemplate> netxFactory;
    private final AbstractElasticsearchTemplate elasticsearchTemplate;

    public ElasticSearchContext(NetProperties netProperties) {
        this.netProperties = netProperties;
        this.netxFactory = new ElasticSearchFactory(netProperties);
        this.elasticsearchTemplate = this.netxFactory.client();
    }

    public IndexOperations indexOps(Class<?> clazz) {
        return this.elasticsearchTemplate.indexOps(clazz);
    }


    public IndexOperations indexOps(IndexCoordinates index) {
        return this.elasticsearchTemplate.indexOps(index);
    }


    public ElasticsearchConverter getElasticsearchConverter() {
        return this.elasticsearchTemplate.getElasticsearchConverter();
    }


    public IndexCoordinates getIndexCoordinatesFor(Class<?> clazz) {
        return this.elasticsearchTemplate.getIndexCoordinatesFor(clazz);
    }


    public <T> T save(T entity) {
        return this.elasticsearchTemplate.save(entity);
    }


    public <T> T save(T entity, IndexCoordinates index) {
        return this.elasticsearchTemplate.save(entity, index);
    }


    public <T> Iterable<T> save(Iterable<T> entities) {
        return this.elasticsearchTemplate.save(entities);
    }


    public <T> Iterable<T> save(Iterable<T> entities, IndexCoordinates index) {
        return this.elasticsearchTemplate.save(entities, index);
    }


    public <T> Iterable<T> save(T... entities) {
        return this.elasticsearchTemplate.save(entities);
    }


    public String index(IndexQuery query, IndexCoordinates index) {
        return this.elasticsearchTemplate.index(query, index);
    }


    public <T> T get(String id, Class<T> clazz) {
        return this.elasticsearchTemplate.get(id, clazz);
    }


    public <T> T get(String id, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.get(id, clazz, index);
    }


    public <T> List<T> multiGet(Query query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.multiGet(query, clazz, index);
    }


    public boolean exists(String id, Class<?> clazz) {
        return this.elasticsearchTemplate.exists(id, clazz);
    }


    public boolean exists(String id, IndexCoordinates index) {
        return this.elasticsearchTemplate.exists(id, index);
    }


    public List<String> bulkIndex(List<IndexQuery> queries, BulkOptions bulkOptions, IndexCoordinates index) {
        return this.elasticsearchTemplate.bulkIndex(queries, bulkOptions, index);
    }


    public void bulkUpdate(List<UpdateQuery> queries, BulkOptions bulkOptions, IndexCoordinates index) {
        this.elasticsearchTemplate.bulkUpdate(queries, bulkOptions, index);
    }


    public String delete(String id, IndexCoordinates index) {
        return this.elasticsearchTemplate.delete(id, index);
    }


    public String delete(String id, Class<?> entityType) {
        return this.elasticsearchTemplate.delete(id, entityType);
    }


    public String delete(Object entity) {
        return this.elasticsearchTemplate.delete(entity);
    }


    public String delete(Object entity, IndexCoordinates index) {
        return this.elasticsearchTemplate.delete(entity, index);
    }


    public void delete(Query query, Class<?> clazz, IndexCoordinates index) {
        this.elasticsearchTemplate.delete(query, clazz, index);
    }


    public UpdateResponse update(UpdateQuery updateQuery, IndexCoordinates index) {
        return this.elasticsearchTemplate.update(updateQuery, index);
    }


    public void delete(DeleteQuery query, IndexCoordinates index) {
        this.elasticsearchTemplate.delete(query, index);
    }


    public <T> T get(GetQuery query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.get(query, clazz, index);
    }


    public <T> T queryForObject(GetQuery query, Class<T> clazz) {
        return this.elasticsearchTemplate.queryForObject(query, clazz);
    }


    public long count(Query query, Class<?> clazz) {
        return this.elasticsearchTemplate.count(query, clazz);
    }


    public long count(Query query, Class<?> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.count(query, clazz, index);
    }


    public <T> CloseableIterator<T> stream(Query query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.stream(query, clazz, index);
    }


    public SearchResponse suggest(SuggestBuilder suggestion, IndexCoordinates index) {
        return this.elasticsearchTemplate.suggest(suggestion, index);
    }


    public <T> List<SearchHits<T>> multiSearch(List<? extends Query> queries, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.multiSearch(queries, clazz, index);
    }


    public List<SearchHits<?>> multiSearch(List<? extends Query> queries, List<Class<?>> classes, IndexCoordinates index) {
        return this.elasticsearchTemplate.multiSearch(queries, classes, index);
    }


    public <T> SearchHits<T> search(Query query, Class<T> clazz) {
        return this.elasticsearchTemplate.search(query, clazz);
    }


    public <T> SearchHits<T> search(Query query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.search(query, clazz, index);
    }


    public <T> SearchHits<T> search(MoreLikeThisQuery query, Class<T> clazz) {
        return this.elasticsearchTemplate.search(query, clazz);
    }


    public <T> SearchHits<T> search(MoreLikeThisQuery query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.search(query, clazz, index);
    }


    public <T> SearchHitsIterator<T> searchForStream(Query query, Class<T> clazz) {
        return this.elasticsearchTemplate.searchForStream(query, clazz);
    }


    public <T> SearchHitsIterator<T> searchForStream(Query query, Class<T> clazz, IndexCoordinates index) {
        return this.elasticsearchTemplate.searchForStream(query, clazz, index);
    }

    @Override
    public void close() throws Exception {
        if(null != this.netxFactory) {
            netxFactory.close();
        }
    }

    @Override
    public AbstractElasticsearchTemplate getTemplate() {
        return elasticsearchTemplate;
    }
}
