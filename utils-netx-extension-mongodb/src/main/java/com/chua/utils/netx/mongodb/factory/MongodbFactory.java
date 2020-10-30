package com.chua.utils.netx.mongodb.factory;

import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.common.FinderHelper;
import com.mongodb.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * mongodb工厂
 * @author CH
 */
@Slf4j
@NoArgsConstructor
public class MongodbFactory implements INetFactory<MongoTemplate> {

    private NetProperties netProperties;
    private MongoTemplate mongoTemplate;


    public MongodbFactory(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public MongoTemplate client() {
        return mongoTemplate;
    }

    @Override
    public void start() {
        this.mongoTemplate = this.startMongoTemplate();
    }


    @Override
    public boolean isStart() {
        return null != mongoTemplate;
    }

    @Override
    public void close() throws Exception {
    }

    /**
     * 创建mongo模板
     * @return MongoTemplate
     */
    private MongoTemplate startMongoTemplate() {
        log.info(">>>>>>>>>>> MongodbFactory Starting to connect");
        try {
            MongoClientOptions.Builder mongoBuilder = new MongoClientOptions.Builder();
            mongoBuilder.connectTimeout(netProperties.getConnectTimeout());
            mongoBuilder.minConnectionsPerHost(1);

            ConnectionString connectionString = new ConnectionString(getMongoUri());
            MongoDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(connectionString);
            log.info(">>>>>>>>>>> MongodbFactory connection complete.");
            return new MongoTemplate(mongoDbFactory);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(">>>>>>>>>>> MongodbFactory connection activation failed.");
        }
        return null;
    }

    /**
     * 获取mongo地址
     * "mongodb://root:root@demo.com:27017/logs?authSource=admin"
     * @return
     */
    private String getMongoUri() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("mongodb://");
        stringBuffer.append(netProperties.getUsername()).append(":").append(netProperties.getPassword()).append("@");
        stringBuffer.append(FinderHelper.firstElement(netProperties.getHost())).append("/");
        stringBuffer.append(netProperties.getDatabaseName());
        stringBuffer.append("?authSource=admin");

        return stringBuffer.toString();
    }
}
