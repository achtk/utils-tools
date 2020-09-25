package com.chua.utils.netx.mongodb.factory;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.common.FinderHelper;
import com.mongodb.*;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * mongodb工厂
 * @author CH
 */
@NoArgsConstructor
public class MongodbFactory implements INetxFactory<MongoTemplate>  {

    private NetxProperties netxProperties;
    private MongoTemplate mongoTemplate;


    public MongodbFactory(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
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
        MongoClientOptions.Builder mongoBuilder = new MongoClientOptions.Builder();
        mongoBuilder.connectTimeout(netxProperties.getConnectTimeout());
        mongoBuilder.minConnectionsPerHost(1);

        ConnectionString connectionString = new ConnectionString(getMongoUri());
        MongoDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(connectionString);
        return new MongoTemplate(mongoDbFactory);
    }

    /**
     * 获取mongo地址
     * "mongodb://root:root@demo.com:27017/logs?authSource=admin"
     * @return
     */
    private String getMongoUri() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("mongodb://");
        stringBuffer.append(netxProperties.getUsername()).append(":").append(netxProperties.getPassword()).append("@");
        stringBuffer.append(FinderHelper.firstElement(netxProperties.getHost())).append("/");
        stringBuffer.append(netxProperties.getDatabaseName());
        stringBuffer.append("?authSource=admin");

        return stringBuffer.toString();
    }
}