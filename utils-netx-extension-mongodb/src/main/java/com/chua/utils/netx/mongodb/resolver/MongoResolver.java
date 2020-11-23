package com.chua.utils.netx.mongodb.resolver;

import com.chua.utils.netx.mongodb.context.MongodbContext;
import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.collector.NetCollector;
import com.chua.utils.netx.resolver.document.NetDocument;
import com.chua.utils.netx.resolver.entity.NetCollectorConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.tools.properties.NetProperties;
import com.google.common.base.Strings;
import org.springframework.data.mongodb.core.DbCallback;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * mongo-db
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class MongoResolver extends NetResolver<MongodbContext> implements NetCollector<DbCallback>, NetDocument<Query> {

    private MongodbContext mongodbContext;

    @Override
    public void setProperties(Properties properties) {
        NetProperties netProperties = NetProperties.newProperty(properties);
        this.mongodbContext = new MongodbContext(netProperties);
        super.setProperties(properties);
    }

    @Override
    public Service<MongodbContext> get() {
        return new Service(mongodbContext);
    }

    @Override
    public boolean createCollection(NetCollectorConf netCollectorConf) {
        if (!existCollection(netCollectorConf.getCollectionName())) {
            try {
                mongodbContext.createCollection(netCollectorConf.getCollectionName());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        throw new IllegalStateException("Collection already exists");
    }

    @Override
    public boolean deleteCollection(String collectionName) {
        try {
            mongodbContext.dropCollection(collectionName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Set<String> listCollection() {
        return mongodbContext.getCollectionNames();
    }

    @Override
    public boolean existCollection(String collectionName) {
        try {
            return mongodbContext.collectionExists(collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public <T> T execute(DbCallback action, Class<T> tClass) {
        return (T) this.mongodbContext.execute(action);
    }

    @Override
    public <T> List<T> query(Query query, Class<T> entityClass, String collectionName) {
        if (Strings.isNullOrEmpty(collectionName)) {
            mongodbContext.find(query, entityClass);
        }
        return mongodbContext.find(query, entityClass, collectionName);
    }

    @Override
    public <T> T addDocument(T objectToSave, String collectionName) {
        if (Strings.isNullOrEmpty(collectionName)) {
            return mongodbContext.insert(objectToSave);
        }
        return mongodbContext.insert(objectToSave, collectionName);
    }

    @Override
    public <T> Collection<T> addDocument(Collection<T> objectToSave, String collectionName) {
        if (Strings.isNullOrEmpty(collectionName)) {
            return objectToSave.stream().map(item -> {
                try {
                    mongodbContext.insert(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return item;
            }).collect(Collectors.toList());
        }
        return mongodbContext.insert(objectToSave, collectionName);
    }

    @Override
    public <T> long removeDocument(T objectToSave, String collectionName) {
        if (Strings.isNullOrEmpty(collectionName)) {
            return mongodbContext.remove(objectToSave).getDeletedCount();
        }
        return mongodbContext.remove(objectToSave, collectionName).getDeletedCount();
    }

    @Override
    public long removeDocumentByAction(Query query, String collectionName) {
        if (Strings.isNullOrEmpty(collectionName)) {
            return mongodbContext.remove(query).getDeletedCount();
        }
        return mongodbContext.remove(query, collectionName).getDeletedCount();
    }

}
