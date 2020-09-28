package com.chua.utils.netx.mongodb.context;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.netx.mongodb.factory.MongodbFactory;
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
public class MongodbContext {

    private final MongoTemplate mongoTemplate;
    private final INetxFactory<MongoTemplate> mongoFactory;

    public MongodbContext(NetxProperties netxProperties) {
        this.mongoFactory = new MongodbFactory(netxProperties);
        this.mongoTemplate = this.mongoFactory.client();
    }


    public String getCollectionName(Class<?> entityClass) {
        return this.mongoTemplate.getCollectionName(entityClass);
    }


    public Document executeCommand(String jsonCommand) {
        return this.mongoTemplate.executeCommand(jsonCommand);
    }


    public Document executeCommand(Document command) {
        return this.mongoTemplate.executeCommand(command);
    }


    public Document executeCommand(Document command, ReadPreference readPreference) {
        return this.mongoTemplate.executeCommand(command, readPreference);
    }


    public void executeQuery(Query query, String collectionName, DocumentCallbackHandler dch) {
        this.mongoTemplate.executeQuery(query, collectionName, dch);
    }


    public <T> T execute(DbCallback<T> action) {
        return this.mongoTemplate.execute(action);
    }


    public <T> T execute(Class<?> entityClass, CollectionCallback<T> action) {
        return this.mongoTemplate.execute(entityClass, action);
    }


    public <T> T execute(String collectionName, CollectionCallback<T> action) {
        return this.mongoTemplate.execute(collectionName, action);
    }

    public <T> CloseableIterator<T> stream(Query query, Class<T> entityType) {
        return this.mongoTemplate.stream(query, entityType);
    }

    public <T> CloseableIterator<T> stream(Query query, Class<T> entityType, String collectionName) {
        return this.mongoTemplate.stream(query, entityType, collectionName);
    }


    public <T> MongoCollection<Document> createCollection(Class<T> entityClass) {
        return this.mongoTemplate.createCollection(entityClass);
    }


    public <T> MongoCollection<Document> createCollection(Class<T> entityClass, CollectionOptions collectionOptions) {
        return this.mongoTemplate.createCollection(entityClass, collectionOptions);
    }


    public MongoCollection<Document> createCollection(String collectionName) {
        return this.mongoTemplate.createCollection(collectionName);
    }


    public MongoCollection<Document> createCollection(String collectionName, CollectionOptions collectionOptions) {
        return this.mongoTemplate.createCollection(collectionName, collectionOptions);
    }


    public Set<String> getCollectionNames() {
        return this.mongoTemplate.getCollectionNames();
    }


    public MongoCollection<Document> getCollection(String collectionName) {
        return this.mongoTemplate.getCollection(collectionName);
    }


    public <T> boolean collectionExists(Class<T> entityClass) {
        return this.mongoTemplate.collectionExists(entityClass);
    }


    public boolean collectionExists(String collectionName) {
        return this.mongoTemplate.collectionExists(collectionName);
    }


    public <T> void dropCollection(Class<T> entityClass) {
        this.mongoTemplate.dropCollection(entityClass);
    }


    public void dropCollection(String collectionName) {
        this.mongoTemplate.dropCollection(collectionName);
    }


    public IndexOperations indexOps(String collectionName) {
        return this.mongoTemplate.indexOps(collectionName);
    }


    public IndexOperations indexOps(Class<?> entityClass) {
        return this.mongoTemplate.indexOps(entityClass);
    }


    public ScriptOperations scriptOps() {
        return this.mongoTemplate.scriptOps();
    }


    public BulkOperations bulkOps(BulkOperations.BulkMode mode, String collectionName) {
        return this.mongoTemplate.bulkOps(mode, collectionName);
    }


    public BulkOperations bulkOps(BulkOperations.BulkMode mode, Class<?> entityType) {
        return this.mongoTemplate.bulkOps(mode, entityType);
    }


    public BulkOperations bulkOps(BulkOperations.BulkMode mode, Class<?> entityType, String collectionName) {
        return this.mongoTemplate.bulkOps(mode, entityType, collectionName);
    }


    public <T> List<T> findAll(Class<T> entityClass) {
        return this.mongoTemplate.findAll(entityClass);
    }


    public <T> List<T> findAll(Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAll(entityClass, collectionName);
    }


    public <T> GroupByResults<T> group(String inputCollectionName, GroupBy groupBy, Class<T> entityClass) {
        return this.mongoTemplate.group(inputCollectionName, groupBy, entityClass);
    }


    public <T> GroupByResults<T> group(Criteria criteria, String inputCollectionName, GroupBy groupBy, Class<T> entityClass) {
        return this.mongoTemplate.group(criteria, inputCollectionName, groupBy, entityClass);
    }


    public <O> AggregationResults<O> aggregate(TypedAggregation<?> aggregation, String collectionName, Class<O> outputType) {
        return this.mongoTemplate.aggregate(aggregation, collectionName, outputType);
    }


    public <O> AggregationResults<O> aggregate(TypedAggregation<?> aggregation, Class<O> outputType) {
        return this.mongoTemplate.aggregate(aggregation, outputType);
    }


    public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<?> inputType, Class<O> outputType) {
        return this.mongoTemplate.aggregate(aggregation, inputType, outputType);
    }


    public <O> AggregationResults<O> aggregate(Aggregation aggregation, String collectionName, Class<O> outputType) {
        return this.mongoTemplate.aggregate(aggregation, collectionName, outputType);
    }


    public <O> CloseableIterator<O> aggregateStream(TypedAggregation<?> aggregation, String collectionName, Class<O> outputType) {
        return this.mongoTemplate.aggregateStream(aggregation, collectionName, outputType);
    }


    public <O> CloseableIterator<O> aggregateStream(TypedAggregation<?> aggregation, Class<O> outputType) {
        return this.mongoTemplate.aggregateStream(aggregation, outputType);
    }


    public <O> CloseableIterator<O> aggregateStream(Aggregation aggregation, Class<?> inputType, Class<O> outputType) {
        return this.mongoTemplate.aggregateStream(aggregation, inputType, outputType);
    }


    public <O> CloseableIterator<O> aggregateStream(Aggregation aggregation, String collectionName, Class<O> outputType) {
        return this.mongoTemplate.aggregateStream(aggregation, collectionName, outputType);
    }


    public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction, Class<T> entityClass) {
        return this.mongoTemplate.mapReduce(inputCollectionName, mapFunction, reduceFunction, entityClass);
    }


    public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction, MapReduceOptions mapReduceOptions, Class<T> entityClass) {
        return this.mongoTemplate.mapReduce(inputCollectionName, mapFunction, reduceFunction, mapReduceOptions, entityClass);
    }


    public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction, String reduceFunction, Class<T> entityClass) {
        return this.mongoTemplate.mapReduce(query, inputCollectionName, mapFunction, reduceFunction, entityClass);
    }


    public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction, String reduceFunction, MapReduceOptions mapReduceOptions, Class<T> entityClass) {
        return this.mongoTemplate.mapReduce(query, inputCollectionName, mapFunction, reduceFunction, mapReduceOptions, entityClass);
    }


    public <T> GeoResults<T> geoNear(NearQuery near, Class<T> entityClass) {
        return this.mongoTemplate.geoNear(near, entityClass);
    }


    public <T> GeoResults<T> geoNear(NearQuery near, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.geoNear(near, entityClass);
    }


    public <T> T findOne(Query query, Class<T> entityClass) {
        return this.mongoTemplate.findOne(query, entityClass);
    }


    public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findOne(query, entityClass);
    }


    public boolean exists(Query query, String collectionName) {
        return this.mongoTemplate.exists(query, collectionName);
    }


    public boolean exists(Query query, Class<?> entityClass) {
        return this.mongoTemplate.exists(query, entityClass);
    }


    public boolean exists(Query query, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.exists(query, entityClass, collectionName);
    }


    public <T> List<T> find(Query query, Class<T> entityClass) {
        return this.mongoTemplate.find(query, entityClass);
    }


    public <T> List<T> find(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.find(query, entityClass, collectionName);
    }


    public <T> T findById(Object id, Class<T> entityClass) {
        return this.mongoTemplate.findById(id, entityClass);
    }


    public <T> T findById(Object id, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findById(id, entityClass, collectionName);
    }


    public <T> List<T> findDistinct(Query query, String field, Class<?> entityClass, Class<T> resultClass) {
        return this.mongoTemplate.findDistinct(query, field, entityClass, resultClass);
    }


    public <T> List<T> findDistinct(Query query, String field, String collectionName, Class<?> entityClass, Class<T> resultClass) {
        return this.mongoTemplate.findDistinct(query, field, collectionName, entityClass, resultClass);
    }


    public <T> T findAndModify(Query query, UpdateDefinition update, Class<T> entityClass) {
        return this.mongoTemplate.findAndModify(query, update, entityClass);
    }


    public <T> T findAndModify(Query query, UpdateDefinition update, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAndModify(query, update, entityClass, collectionName);
    }


    public <T> T findAndModify(Query query, UpdateDefinition update, FindAndModifyOptions options, Class<T> entityClass) {
        return this.mongoTemplate.findAndModify(query, update, options, entityClass);
    }


    public <T> T findAndModify(Query query, UpdateDefinition update, FindAndModifyOptions options, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAndModify(query, update, options, entityClass, collectionName);
    }


    public <S, T> T findAndReplace(Query query, S replacement, FindAndReplaceOptions options, Class<S> entityType, String collectionName, Class<T> resultType) {
        return this.mongoTemplate.findAndReplace(query, replacement, options, entityType, collectionName, resultType);
    }


    public <T> T findAndRemove(Query query, Class<T> entityClass) {
        return this.mongoTemplate.findAndRemove(query, entityClass);
    }


    public <T> T findAndRemove(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAndRemove(query, entityClass, collectionName);
    }


    public long count(Query query, Class<?> entityClass) {
        return this.mongoTemplate.count(query, entityClass);
    }


    public long count(Query query, String collectionName) {
        return this.mongoTemplate.count(query, collectionName);
    }


    public long count(Query query, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.count(query, entityClass, collectionName);
    }


    public <T> T insert(T objectToSave) {
        return this.mongoTemplate.insert(objectToSave);
    }


    public <T> T insert(T objectToSave, String collectionName) {
        return this.mongoTemplate.insert(objectToSave, collectionName);
    }


    public <T> Collection<T> insert(Collection<? extends T> batchToSave, Class<?> entityClass) {
        return this.mongoTemplate.insert(batchToSave, entityClass);
    }


    public <T> Collection<T> insert(Collection<? extends T> batchToSave, String collectionName) {
        return this.mongoTemplate.insert(batchToSave, collectionName);
    }


    public <T> Collection<T> insertAll(Collection<? extends T> objectsToSave) {
        return this.mongoTemplate.insertAll(objectsToSave);
    }


    public <T> T save(T objectToSave) {
        return this.mongoTemplate.save(objectToSave);
    }


    public <T> T save(T objectToSave, String collectionName) {
        return this.mongoTemplate.save(objectToSave, collectionName);
    }


    public UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass) {
        return this.mongoTemplate.upsert(query, update, entityClass);
    }


    public UpdateResult upsert(Query query, UpdateDefinition update, String collectionName) {
        return this.mongoTemplate.upsert(query, update, collectionName);
    }


    public UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.upsert(query, update, collectionName);
    }


    public UpdateResult updateFirst(Query query, UpdateDefinition update, Class<?> entityClass) {
        return this.mongoTemplate.updateFirst(query, update, entityClass);
    }


    public UpdateResult updateFirst(Query query, UpdateDefinition update, String collectionName) {
        return this.mongoTemplate.updateFirst(query, update, collectionName);
    }


    public UpdateResult updateFirst(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.updateFirst(query, update, entityClass, collectionName);
    }


    public UpdateResult updateMulti(Query query, UpdateDefinition update, Class<?> entityClass) {
        return this.mongoTemplate.updateMulti(query, update, entityClass);
    }


    public UpdateResult updateMulti(Query query, UpdateDefinition update, String collectionName) {
        return this.mongoTemplate.updateMulti(query, update, collectionName);
    }


    public UpdateResult updateMulti(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.updateMulti(query, update, entityClass, collectionName);
    }


    public DeleteResult remove(Object object) {
        return this.mongoTemplate.remove(object);
    }


    public DeleteResult remove(Object object, String collectionName) {
        return this.mongoTemplate.remove(object, collectionName);
    }


    public DeleteResult remove(Query query, Class<?> entityClass) {
        return this.mongoTemplate.remove(query, entityClass);
    }


    public DeleteResult remove(Query query, Class<?> entityClass, String collectionName) {
        return this.mongoTemplate.remove(query, entityClass, collectionName);
    }


    public DeleteResult remove(Query query, String collectionName) {
        return this.mongoTemplate.remove(query, collectionName);
    }


    public <T> List<T> findAllAndRemove(Query query, String collectionName) {
        return this.mongoTemplate.findAllAndRemove(query, collectionName);
    }


    public <T> List<T> findAllAndRemove(Query query, Class<T> entityClass) {
        return this.mongoTemplate.findAllAndRemove(query, entityClass);
    }


    public <T> List<T> findAllAndRemove(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findAllAndRemove(query, entityClass, collectionName);
    }


    public MongoConverter getConverter() {
        return this.mongoTemplate.getConverter();
    }


    public <T> ExecutableAggregationOperation.ExecutableAggregation<T> aggregateAndReturn(Class<T> domainType) {
        return this.mongoTemplate.aggregateAndReturn(domainType);
    }


    public <T> ExecutableFindOperation.ExecutableFind<T> query(Class<T> domainType) {
        return this.mongoTemplate.query(domainType);
    }


    public <T> ExecutableInsertOperation.ExecutableInsert<T> insert(Class<T> domainType) {
        return this.mongoTemplate.insert(domainType);
    }


    public <T> ExecutableMapReduceOperation.MapReduceWithMapFunction<T> mapReduce(Class<T> domainType) {
        return this.mongoTemplate.mapReduce(domainType);
    }


    public <T> ExecutableRemoveOperation.ExecutableRemove<T> remove(Class<T> domainType) {
        return this.mongoTemplate.remove(domainType);
    }


    public <T> ExecutableUpdateOperation.ExecutableUpdate<T> update(Class<T> domainType) {
        return this.mongoTemplate.update(domainType);
    }
}
