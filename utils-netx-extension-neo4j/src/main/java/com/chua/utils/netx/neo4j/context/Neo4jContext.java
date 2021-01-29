package com.chua.utils.netx.neo4j.context;

import com.chua.utils.tools.properties.NetProperties;
import org.neo4j.cypherdsl.core.Statement;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jOperations;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.PreparedQuery;
import org.springframework.data.neo4j.core.mapping.Neo4jPersistentProperty;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Neo4jContext
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class Neo4jContext implements Neo4jOperations, AutoCloseable {

    private Neo4jTemplate neo4jTemplate;
    private NetProperties netProperties;
    private Driver driver;

    public Neo4jContext(NetProperties netProperties) {
        this.netProperties = netProperties;
        this.neo4jTemplate = createTemplate();
    }

    private Neo4jTemplate createTemplate() {
        Config config = Config.builder()
                .withConnectionTimeout(netProperties.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .withEncryption()
                .withMaxConnectionPoolSize(netProperties.getMaxConnection())
                .build();
        this.driver = GraphDatabase.driver(netProperties.getHostIfOnly(), AuthTokens.basic(netProperties.getUsername(), netProperties.getPassword()), config);
        return new Neo4jTemplate(Neo4jClient.create(driver));
    }

    public Neo4jTemplate getTemplate() {
        return neo4jTemplate;
    }

    @Override
    public void close() throws Exception {
        if (null != driver) {
            driver.close();
        }
    }


    @Override
    public long count(Class<?> domainType) {
        return neo4jTemplate.count(domainType);
    }

    @Override
    public long count(Statement statement) {
        return neo4jTemplate.count(statement);
    }

    @Override
    public long count(Statement statement, Map<String, Object> parameters) {
        return neo4jTemplate.count(statement, parameters);
    }

    @Override
    public long count(String cypherQuery) {
        return neo4jTemplate.count(cypherQuery);
    }

    @Override
    public long count(String cypherQuery, Map<String, Object> parameters) {
        return neo4jTemplate.count(cypherQuery, parameters);
    }

    @Override
    public <T> List<T> findAll(Class<T> domainType) {
        return neo4jTemplate.findAll(domainType);
    }

    @Override
    public <T> List<T> findAll(Statement statement, Class<T> domainType) {
        return neo4jTemplate.findAll(statement, domainType);
    }

    @Override
    public <T> List<T> findAll(Statement statement, Map<String, Object> parameters, Class<T> domainType) {
        return neo4jTemplate.findAll(statement, parameters, domainType);
    }

    @Override
    public <T> Optional<T> findOne(Statement statement, Map<String, Object> parameters, Class<T> domainType) {
        return neo4jTemplate.findOne(statement, parameters, domainType);
    }

    @Override
    public <T> List<T> findAll(String cypherQuery, Class<T> domainType) {
        return neo4jTemplate.findAll(cypherQuery, domainType);
    }

    @Override
    public <T> List<T> findAll(String cypherQuery, Map<String, Object> parameters, Class<T> domainType) {
        return neo4jTemplate.findAll(cypherQuery, parameters, domainType);
    }

    @Override
    public <T> Optional<T> findOne(String cypherQuery, Map<String, Object> parameters, Class<T> domainType) {
        return neo4jTemplate.findOne(cypherQuery, parameters, domainType);
    }

    @Override
    public <T> Optional<T> findById(Object id, Class<T> domainType) {
        return neo4jTemplate.findById(id, domainType);
    }

    @Override
    public <T> List<T> findAllById(Iterable<?> ids, Class<T> domainType) {
        return neo4jTemplate.findAllById(ids, domainType);
    }

    @Override
    public <T> T save(T instance) {
        return neo4jTemplate.save(instance);
    }

    @Override
    public <T> List<T> saveAll(Iterable<T> instances) {
        return neo4jTemplate.saveAll(instances);
    }

    @Override
    public <T> void deleteById(Object id, Class<T> domainType) {
        neo4jTemplate.deleteById(id, domainType);
    }

    @Override
    public <T> void deleteByIdWithVersion(Object id, Class<T> domainType, Neo4jPersistentProperty versionProperty, Object versionValue) {
        neo4jTemplate.deleteByIdWithVersion(id, domainType, versionProperty, versionValue);
    }

    @Override
    public <T> void deleteAllById(Iterable<?> ids, Class<T> domainType) {
        neo4jTemplate.deleteAllById(ids, domainType);
    }

    @Override
    public void deleteAll(Class<?> domainType) {
        neo4jTemplate.deleteAll(domainType);
    }

    @Override
    public <T> ExecutableQuery<T> toExecutableQuery(PreparedQuery<T> preparedQuery) {
        return neo4jTemplate.toExecutableQuery(preparedQuery);
    }
}
