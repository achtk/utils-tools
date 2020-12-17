package com.chua.utils.netx.mongodb.context;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.netx.mongodb.factory.MongodbFactory;
import com.chua.utils.tools.properties.NetProperties;
import com.mongodb.ClientSessionOptions;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.data.util.CloseableIterator;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * mongodb上下文
 *
 * @author CH
 */
@SuppressWarnings("ALL")
public class MongodbContext implements MongoOperations, AutoCloseable {

    private final MongoTemplate mongoTemplate;
    private final INetFactory<MongoTemplate> netxFactory;

    public MongodbContext(NetProperties netProperties) {
        this.netxFactory = new MongodbFactory(netProperties);
        this.netxFactory.start();
        this.mongoTemplate = this.netxFactory.client();
    }


    @Override
    public String getCollectionName(Class<?> entityClass) {
        return this.mongoTemplate.getCollectionName(entityClass);
    }


    @Override
    public Document executeCommand(String jsonCommand) {
        return this.mongoTemplate.executeCommand(jsonCommand);
    }


    @Override
    public Document executeCommand(Document command) {
        return this.mongoTemplate.executeCommand(command);
    }


    @Override
    public Document executeCommand(Document command, ReadPreference readPreference) {
        return this.mongoTemplate.executeCommand(command, readPreference);
    }


    @Override
    public void executeQuery(Query query, String collectionName, DocumentCallbackHandler dch) {
        this.mongoTemplate.executeQuery(query, collectionName, dch);
    }


    @Override
    public <T> T execute(DbCallback<T> action) {
        return this.mongoTemplate.execute(action);
    }


    @Override
    public <T> T execute(Class<?> entityClass, CollectionCallback<T> action) {
        return this.mongoTemplate.execute(entityClass, action);
    }


    @Override
    public <T> T execute(String collectionName, CollectionCallback<T> action) {
        return this.mongoTemplate.execute(collectionName, action);
    }

    @Override
    public SessionScoped withSession(ClientSessionOptions sessionOptions) {
        return mongoTemplate.withSession(sessionOptions);
    }

    @Override
    public MongoOperations withSession(com.mongodb.client.ClientSession session) {
        return mongoTemplate.withSession(session);
    }


    @Override
    public <T> CloseableIterator<T> stream(Query query, Class<T> entityType) {
        return this.mongoTemplate.stream(query, entityType);
    }

    @Override
    public <T> CloseableIterator<T> stream(Query query, Class<T> entityType, String collectionName) {
        return this.mongoTemplate.stream(query, entityType, collectionName);
    }


    @Override
    public <T> MongoCollection<Document> createCollection(Class<T> entityClass) {
        return this.mongoTemplate.createCollection(entityClass);
    }


    @Override
    public <T> MongoCollection<Document> createCollection(Class<T> entityClass, CollectionOptions collectionOptions) {
        return this.mongoTemplate.createCollection(entityClass, collectionOptions);
    }


    @Override
    public MongoCollection<Document> createCollection(String collectionName) {
        return this.mongoTemplate.createCollection(collectionName);
    }


    @Override
    public MongoCollection<Document> createCollection(String collectionName, CollectionOptions collectionOptions) {
        return this.mongoTemplate.createCollection(collectionName, collectionOptions);
    }


    @Override
    public Set<String> getCollectionNames() {
        return this.mongoTemplate.getCollectionNames();
    }


    @Override
    public MongoCollection<Document> getCollection(String collectionName) {
        return this.mongoTemplate.getCollection(collectionName);
    }


    @Override
    public <T> boolean collectionExists(Class<T> entityClass) {
        return this.mongoTemplate.collectionExists(entityClass);
    }


    @Override
    public boolean collectionExists(String collectionName) {
        return this.mongoTemplate.collectionExists(collectionName);
    }


    @Override
    public <T> void dropCollection(Class<T> entityClass) {
        this.mongoTemplate.dropCollection(entityClass);
    }


    @Override
    public void dropCollection(String collectionName) {
        this.mongoTemplate.dropCollection(collectionName);
    }


    @Override
    public IndexOperations indexOps(String collectionName) {
        return this.mongoTemplate.indexOps(collectionName);
    }


    @Override
    public IndexOperations indexOps(Class<?> entityClass) {
        return this.mongoTemplate.indexOps(entityClass);
    }


    @Deprecated
    @Override
    public ScriptOperations scriptOps() {
        return this.mongoTemplate.scriptOps();
    }


    @Override
    public BulkOperations bulkOps(BulkOperations.BulkMode mode, String collectionName) {
        return this.mongoTemplate.bulkOps(mode, collectionName);
    }


    @Override
    public BulkOperations bulkOps(BulkOperations.BulkMode mode, Class<?> entityType) {
        return this.mongoTemplate.bulkOps(mode, entityType);
    }


    @Override
    public BulkOperations bulkOps(BulkOperations.BulkMode mode, Class<?> entityType, String collectionName) {
        return this.mongoTemplate.bulkOps(mode, entityType, collectionName);
    }


    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        return this.mongoTemplate.findAll(entityClass);
    }


    @Override
    public <T> List<T> findAll(Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAll(entityClass, collectionName);
    }


    @Override
    public <T> GroupByResults<T> group(String inputCollectionName, GroupBy groupBy, Class<T> entityClass) {
        return this.mongoTemplate.group(inputCollectionName, groupBy, entityClass);
    }


    @Override
    public <T> GroupByResults<T> group(Criteria criteria, String inputCollectionName, GroupBy groupBy, Class<T> entityClass) {
        return this.mongoTemplate.group(criteria, inputCollectionName, groupBy, entityClass);
    }


    @Override
    public <O> AggregationResults<O> aggregate(TypedAggregation<?> aggregation, String collectionName, Class<O> outputType) {
        return this.mongoTemplate.aggregate(aggregation, collectionName, outputType);
    }


    @Override
    public <O> AggregationResults<O> aggregate(TypedAggregation<?> aggregation, Class<O> outputType) {
        return this.mongoTemplate.aggregate(aggregation, outputType);
    }


    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<?> inputType, Class<O> outputType) {
        return this.mongoTemplate.aggregate(aggregation, inputType, outputType);
    }


    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, String collectionName, Class<O> outputType) {
        return this.mongoTemplate.aggregate(aggregation, collectionName, outputType);
    }


    @Override
    public <O> CloseableIterator<O> aggregateStream(TypedAggregation<?> aggregation, String collectionName, Class<O> outputType) {
        return this.mongoTemplate.aggregateStream(aggregation, collectionName, outputType);
    }


    @Override
    public <O> CloseableIterator<O> aggregateStream(TypedAggregation<?> aggregation, Class<O> outputType) {
        return this.mongoTemplate.aggregateStream(aggregation, outputType);
    }


    @Override
    public <O> CloseableIterator<O> aggregateStream(Aggregation aggregation, Class<?> inputType, Class<O> outputType) {
        return this.mongoTemplate.aggregateStream(aggregation, inputType, outputType);
    }


    @Override
    public <O> CloseableIterator<O> aggregateStream(Aggregation aggregation, String collectionName, Class<O> outputType) {
        return this.mongoTemplate.aggregateStream(aggregation, collectionName, outputType);
    }


    @Override
    public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction, Class<T> entityClass) {
        return this.mongoTemplate.mapReduce(inputCollectionName, mapFunction, reduceFunction, entityClass);
    }


    @Override
    public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction, MapReduceOptions mapReduceOptions, Class<T> entityClass) {
        return this.mongoTemplate.mapReduce(inputCollectionName, mapFunction, reduceFunction, mapReduceOptions, entityClass);
    }


    @Override
    public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction, String reduceFunction, Class<T> entityClass) {
        return this.mongoTemplate.mapReduce(query, inputCollectionName, mapFunction, reduceFunction, entityClass);
    }


    @Override
    public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction, String reduceFunction, MapReduceOptions mapReduceOptions, Class<T> entityClass) {
        return this.mongoTemplate.mapReduce(query, inputCollectionName, mapFunction, reduceFunction, mapReduceOptions, entityClass);
    }


    @Override
    public <T> GeoResults<T> geoNear(NearQuery near, Class<T> entityClass) {
        return this.mongoTemplate.geoNear(near, entityClass);
    }


    @Override
    public <T> GeoResults<T> geoNear(NearQuery near, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.geoNear(near, entityClass);
    }


    @Override
    public <T> T findOne(Query query, Class<T> entityClass) {
        return this.mongoTemplate.findOne(query, entityClass);
    }


    @Override
    public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findOne(query, entityClass);
    }


    @Override
    public boolean exists(Query query, String collectionName) {
        return this.mongoTemplate.exists(query, collectionName);
    }


    @Override
    public boolean exists(Query query, Class<?> entityClass) {
        return this.mongoTemplate.exists(query, entityClass);
    }


    @Override
    public boolean exists(Query query, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.exists(query, entityClass, collectionName);
    }


    @Override
    public <T> List<T> find(Query query, Class<T> entityClass) {
        return this.mongoTemplate.find(query, entityClass);
    }


    @Override
    public <T> List<T> find(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.find(query, entityClass, collectionName);
    }


    @Override
    public <T> T findById(Object id, Class<T> entityClass) {
        return this.mongoTemplate.findById(id, entityClass);
    }


    @Override
    public <T> T findById(Object id, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findById(id, entityClass, collectionName);
    }


    @Override
    public <T> List<T> findDistinct(Query query, String field, Class<?> entityClass, Class<T> resultClass) {
        return this.mongoTemplate.findDistinct(query, field, entityClass, resultClass);
    }


    @Override
    public <T> List<T> findDistinct(Query query, String field, String collectionName, Class<?> entityClass, Class<T> resultClass) {
        return this.mongoTemplate.findDistinct(query, field, collectionName, entityClass, resultClass);
    }


    @Override
    public <T> T findAndModify(Query query, UpdateDefinition update, Class<T> entityClass) {
        return this.mongoTemplate.findAndModify(query, update, entityClass);
    }


    @Override
    public <T> T findAndModify(Query query, UpdateDefinition update, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAndModify(query, update, entityClass, collectionName);
    }


    @Override
    public <T> T findAndModify(Query query, UpdateDefinition update, FindAndModifyOptions options, Class<T> entityClass) {
        return this.mongoTemplate.findAndModify(query, update, options, entityClass);
    }


    @Override
    public <T> T findAndModify(Query query, UpdateDefinition update, FindAndModifyOptions options, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAndModify(query, update, options, entityClass, collectionName);
    }


    @Override
    public <S, T> T findAndReplace(Query query, S replacement, FindAndReplaceOptions options, Class<S> entityType, String collectionName, Class<T> resultType) {
        return this.mongoTemplate.findAndReplace(query, replacement, options, entityType, collectionName, resultType);
    }


    @Override
    public <T> T findAndRemove(Query query, Class<T> entityClass) {
        return this.mongoTemplate.findAndRemove(query, entityClass);
    }


    @Override
    public <T> T findAndRemove(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAndRemove(query, entityClass, collectionName);
    }


    @Override
    public long count(Query query, Class<?> entityClass) {
        return this.mongoTemplate.count(query, entityClass);
    }


    @Override
    public long count(Query query, String collectionName) {
        return this.mongoTemplate.count(query, collectionName);
    }

    @Override
    public long estimatedCount(String collectionName) {
        return mongoTemplate.estimatedCount(collectionName);
    }


    @Override
    public long count(Query query, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.count(query, entityClass, collectionName);
    }


    @Override
    public <T> T insert(T objectToSave) {
        return this.mongoTemplate.insert(objectToSave);
    }


    @Override
    public <T> T insert(T objectToSave, String collectionName) {
        return this.mongoTemplate.insert(objectToSave, collectionName);
    }


    @Override
    public <T> Collection<T> insert(Collection<? extends T> batchToSave, Class<?> entityClass) {
        return this.mongoTemplate.insert(batchToSave, entityClass);
    }


    @Override
    public <T> Collection<T> insert(Collection<? extends T> batchToSave, String collectionName) {
        return this.mongoTemplate.insert(batchToSave, collectionName);
    }


    @Override
    public <T> Collection<T> insertAll(Collection<? extends T> objectsToSave) {
        return this.mongoTemplate.insertAll(objectsToSave);
    }


    @Override
    public <T> T save(T objectToSave) {
        return this.mongoTemplate.save(objectToSave);
    }


    @Override
    public <T> T save(T objectToSave, String collectionName) {
        return this.mongoTemplate.save(objectToSave, collectionName);
    }


    @Override
    public UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass) {
        return this.mongoTemplate.upsert(query, update, entityClass);
    }


    @Override
    public UpdateResult upsert(Query query, UpdateDefinition update, String collectionName) {
        return this.mongoTemplate.upsert(query, update, collectionName);
    }


    @Override
    public UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.upsert(query, update, collectionName);
    }


    @Override
    public UpdateResult updateFirst(Query query, UpdateDefinition update, Class<?> entityClass) {
        return this.mongoTemplate.updateFirst(query, update, entityClass);
    }


    @Override
    public UpdateResult updateFirst(Query query, UpdateDefinition update, String collectionName) {
        return this.mongoTemplate.updateFirst(query, update, collectionName);
    }


    @Override
    public UpdateResult updateFirst(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.updateFirst(query, update, entityClass, collectionName);
    }


    @Override
    public UpdateResult updateMulti(Query query, UpdateDefinition update, Class<?> entityClass) {
        return this.mongoTemplate.updateMulti(query, update, entityClass);
    }


    @Override
    public UpdateResult updateMulti(Query query, UpdateDefinition update, String collectionName) {
        return this.mongoTemplate.updateMulti(query, update, collectionName);
    }


    @Override
    public UpdateResult updateMulti(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.updateMulti(query, update, entityClass, collectionName);
    }


    @Override
    public DeleteResult remove(Object object) {
        return this.mongoTemplate.remove(object);
    }


    @Override
    public DeleteResult remove(Object object, String collectionName) {
        return this.mongoTemplate.remove(object, collectionName);
    }


    @Override
    public DeleteResult remove(Query query, Class<?> entityClass) {
        return this.mongoTemplate.remove(query, entityClass);
    }


    @Override
    public DeleteResult remove(Query query, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.remove(query, entityClass, collectionName);
    }


    @Override
    public DeleteResult remove(Query query, String collectionName) {
        return this.mongoTemplate.remove(query, collectionName);
    }


    @Override
    public <T> List<T> findAllAndRemove(Query query, String collectionName) {
        return this.mongoTemplate.findAllAndRemove(query, collectionName);
    }


    @Override
    public <T> List<T> findAllAndRemove(Query query, Class<T> entityClass) {
        return this.mongoTemplate.findAllAndRemove(query, entityClass);
    }


    @Override
    public <T> List<T> findAllAndRemove(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAllAndRemove(query, entityClass, collectionName);
    }


    @Override
    public MongoConverter getConverter() {
        return this.mongoTemplate.getConverter();
    }


    @Override
    public <T> ExecutableAggregationOperation.ExecutableAggregation<T> aggregateAndReturn(Class<T> domainType) {
        return this.mongoTemplate.aggregateAndReturn(domainType);
    }


    @Override
    public <T> ExecutableFindOperation.ExecutableFind<T> query(Class<T> domainType) {
        return this.mongoTemplate.query(domainType);
    }


    @Override
    public <T> ExecutableInsertOperation.ExecutableInsert<T> insert(Class<T> domainType) {
        return this.mongoTemplate.insert(domainType);
    }


    @Override
    public <T> ExecutableMapReduceOperation.MapReduceWithMapFunction<T> mapReduce(Class<T> domainType) {
        return this.mongoTemplate.mapReduce(domainType);
    }


    @Override
    public <T> ExecutableRemoveOperation.ExecutableRemove<T> remove(Class<T> domainType) {
        return this.mongoTemplate.remove(domainType);
    }


    @Override
    public <T> ExecutableUpdateOperation.ExecutableUpdate<T> update(Class<T> domainType) {
        return this.mongoTemplate.update(domainType);
    }

    @Override
    public void close() throws Exception {
        if (null != this.netxFactory) {
            netxFactory.close();
        }
    }
}
