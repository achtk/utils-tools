package com.chua.utils.netx.elasticsearch.spring.resolver;

import com.chua.utils.netx.elasticsearch.spring.context.ElasticSearchContext;
import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.collector.NetCollector;
import com.chua.utils.netx.resolver.document.NetDocument;
import com.chua.utils.netx.resolver.entity.NetCollectorConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.tools.properties.NetProperties;
import com.google.common.base.Strings;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * elastic-search
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class ElasticSearchResolver extends NetResolver<ElasticSearchContext> implements
        NetCollector, NetDocument<Query> {

    private ElasticSearchContext elasticSearchContext;

    @Override
    public void setProperties(Properties properties) {
        this.elasticSearchContext = new ElasticSearchContext(NetProperties.newProperty(properties));
        super.setProperties(properties);
    }

    @Override
    public Service<ElasticSearchContext> get() {
        return new Service(elasticSearchContext);
    }

    @Override
    public boolean createCollection(NetCollectorConf netCollectorConf) {
        IndexOperations indexOps = elasticSearchContext.indexOps(IndexCoordinates.of(netCollectorConf.getCollectionName()));
        return indexOps.create();
    }

    @Override
    public boolean deleteCollection(String collectionName) {
        IndexOperations indexOps = elasticSearchContext.indexOps(IndexCoordinates.of(collectionName));
        return indexOps.delete();
    }

    @Override
    public Set<String> listCollection() {
        throw new IllegalStateException("Unavailable");
    }

    @Override
    public boolean existCollection(String collectionName) {
        IndexOperations indexOps = elasticSearchContext.indexOps(IndexCoordinates.of(collectionName));
        return indexOps.exists();
    }

    @Override
    public Object execute(Object action, Class aClass) {
        throw new IllegalStateException("Unavailable");
    }

    @Override
    public <T> List<T> query(Query query, Class<T> entityClass, String collectionName) {
        SearchHits<T> search;
        if (Strings.isNullOrEmpty(collectionName)) {
            search = elasticSearchContext.search(query, entityClass);
        } else {
            search = elasticSearchContext.search(query, entityClass, IndexCoordinates.of(collectionName));
        }
        return search.stream().parallel().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public <T> T addDocument(T objectToSave, String collectionName) {
        if (Strings.isNullOrEmpty(collectionName)) {
            return elasticSearchContext.save(objectToSave);
        }
        return elasticSearchContext.save(objectToSave, IndexCoordinates.of(collectionName));
    }

    @Override
    public <T> Collection<T> addDocument(Collection<T> objectToSave, String collectionName) {
        if (Strings.isNullOrEmpty(collectionName)) {
            elasticSearchContext.save(objectToSave);
        } else {
            elasticSearchContext.save(objectToSave, IndexCoordinates.of(collectionName));
        }
        return objectToSave;
    }

    @Override
    public <T> long removeDocument(T objectToSave, String collectionName) {
        if (Strings.isNullOrEmpty(collectionName)) {
            String delete = elasticSearchContext.delete(objectToSave);
            return null == delete ? 0 : 1;
        }
        String delete = elasticSearchContext.delete(objectToSave, IndexCoordinates.of(collectionName));
        return null == delete ? 0 : 1;
    }

    @Override
    public long removeDocumentByAction(Query query, String collectionName) {
        String delete;
        if (Strings.isNullOrEmpty(collectionName)) {
            delete = elasticSearchContext.delete(query);
        } else {
            delete = elasticSearchContext.delete(query, IndexCoordinates.of(collectionName));
        }
        return null == delete ? 0 : 1;
    }
}
