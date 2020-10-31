package com.chua.utils.netx.mysql.factory;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.common.ObjectHelper;
import com.chua.utils.tools.properties.NetProperties;
import com.google.common.base.Strings;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.AdditionalConfig;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.distribution.Version;
import lombok.extern.slf4j.Slf4j;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.wix.mysql.config.Charset.UTF8;

/**
 * mysql
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
@Slf4j
public class MysqlFactory implements INetFactory<EmbeddedMysql> {
    private NetProperties netProperties;
    private EmbeddedMysql.Builder embeddedMysqlBuilder;
    private EmbeddedMysql mysql;

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public EmbeddedMysql client() {
        return mysql;
    }

    @Override
    public void start() {
        Object version = netProperties.get("version");
        Version version1 = Version.v5_5_51;
        if (null != version && !"".equals(version)) {
            try {
                version1 = Version.valueOf(version.toString());
            } catch (IllegalArgumentException e) {
                log.error("version: [{}]无法解析", version);
            }
        }
        String username = netProperties.getUsername();
        String password = netProperties.getPassword();
        MysqldConfig.Builder config = MysqldConfig.aMysqldConfig(version1)
                .withCharset(UTF8)
                .withPort(netProperties.getPort(2215))
                .withTimeZone(TimeZone.getDefault())
                .withTimeout(2, TimeUnit.MINUTES)
                .withServerVariable("max_connect_errors", 666);

        if(!Strings.isNullOrEmpty(username)) {
            config.withUser(username, ObjectHelper.nullToEmpty(password));
        }

        this.embeddedMysqlBuilder = EmbeddedMysql.anEmbeddedMysql(config.build());
        Object schema = netProperties.get("schema");
        if (null != schema) {
            log.info("Schema detected: [{}]", schema);
            try {
                embeddedMysqlBuilder.addSchema("aschema", ScriptResolver.classPathScript(schema.toString()));
            } catch (Exception e) {
            }
        }
        this.mysql = this.embeddedMysqlBuilder.start();

    }

    @Override
    public boolean isStart() {
        return null != mysql;
    }

    @Override
    public void close() throws Exception {
        mysql.stop();
        mysql = null;
    }
}
