package com.chua.unified.properties;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * netx配置项
 * @author CH
 * @since 1.0
 */
@Getter
public class NetxProperties extends Properties {
    public static final String CONFIG_FIELD_RETRY = "retry";
    public static final String CONFIG_FIELD_HOST = "host";
    public static final String CONFIG_FIELD_PASSWORD = "password";
    public static final String CONFIG_FIELD_USERNAME = "username";
    public static final String CONFIG_FIELD_READ_TIMEOUT = "readTimeout";
    public static final String CONFIG_FIELD_WRITE_TIMEOUT = "writeTimeout";
    public static final String CONFIG_FIELD_CONNECTION_TIMEOUT = "connectTimeout";
    public static final String CONFIG_FIELD_MAX_CONNECTION = "maxConnection";
    public static final String CONFIG_FIELD_PATH = "path";
    public static final String CONFIG_FIELD_SESSION_TIMEOUT = "sessionTimeout";
    public static final String CONFIG_FIELD_DATABASE_NAME = "databaseName";
    /**
     * 主机
     */
    private String[] host;
    /**
     * 读取超时
     */
    private int readTimeout = 6000;
    /**
     * 链接超时
     */
    private int connectionTimeout = 10000;
    /**
     * 重试次数
     */
    private int retry = 3;
    /**
     * 写入超时
     */
    private int writeTimeout;
    /**
     * 连接超时
     */
    private int connectTimeout = 10000;
    /**
     * 数据库名称
     */
    private String databaseName = "db";
    /**
     * 最大连接数
     */
    private int maxConnection;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 路径
     */
    private String path;

    public void setHost(String host) {
        this.host = new String[] {host};
        this.put(CONFIG_FIELD_HOST, this.host);
    }

    public void setHost(String[] host) {
        this.host = host;
        this.put(CONFIG_FIELD_HOST, host);
    }

    /**
     *
     * @param host
     */
    public void addHost(String host) {
        if(!this.containsKey(CONFIG_FIELD_HOST)) {
            this.put(CONFIG_FIELD_HOST, host);
            this.host = new String[] {host};
        } else {
            List<String> items = new ArrayList<>();
            Object o = this.get(CONFIG_FIELD_HOST);
            if(o instanceof List) {
                items.addAll((Collection<? extends String>) o);
            } else {
                items.add(o + "");
                items.add(host);
            }
            this.host = items.toArray(new String[items.size()]);
            this.put(CONFIG_FIELD_HOST, items);
        }
    }

    public void setUsername(String username) {
        this.username = username;
        this.put(CONFIG_FIELD_USERNAME, username);
    }

    public void setPassword(String password) {
        this.password = password;
        this.put(CONFIG_FIELD_PASSWORD, password);
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        this.put(CONFIG_FIELD_READ_TIMEOUT, readTimeout);

    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        this.put(CONFIG_FIELD_WRITE_TIMEOUT, writeTimeout);
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        this.put(CONFIG_FIELD_CONNECTION_TIMEOUT, connectTimeout);
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
        this.put(CONFIG_FIELD_MAX_CONNECTION, maxConnection);
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.put(CONFIG_FIELD_CONNECTION_TIMEOUT, connectionTimeout);
    }

    public void setPath(String path) {
        this.path = path;
        this.put(CONFIG_FIELD_PATH, path);
    }

    public void setRetry(int retry) {
        this.retry = retry;
        this.put(CONFIG_FIELD_RETRY, retry);
    }




}
