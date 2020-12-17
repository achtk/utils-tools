package com.chua.utils.tools.properties;


import com.chua.utils.tools.common.NetHelper;
import com.chua.utils.tools.constant.SymbolConstant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * net配置项
 *
 * @author CH
 * @since 1.0
 */
@Getter
@Slf4j
public class NetProperties extends Properties implements StatelessProperties {
    public static final String CONFIG_FIELD_RETRY = "retry";
    public static final int DEFAULT_CONFIG_FIELD_RETRY = 3;
    public static final String CONFIG_FIELD_PORT = "port";
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
    private static final String DEFAULT_HOST = "localhost";
    /**
     * 主机
     */
    private String[] host;
    /**
     * 端口
     */
    private int port;
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
    private int connectTimeout = connectionTimeout;
    /**
     * 数据库名称
     */
    private final String databaseName = "db";
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
    private Object t;
    /**
     * 版本
     */
    private String version;

    public static NetProperties newProperty(Properties properties) {
        NetProperties netProperties = new NetProperties();
        netProperties.putAll(properties);
        return netProperties;
    }

    public void setVersion(String version) {
        this.version = version;
        this.put("version", version);
    }

    /**
     * 初始化
     *
     * @param host host
     * @return NetProperties
     */
    public static NetProperties newProperty(String host) {
        NetProperties netProperties = new NetProperties();
        netProperties.setHost(host);
        return netProperties;
    }

    /**
     * 设置host
     *
     * @param host host
     */
    public void setHost(String host) {
        this.host = new String[]{host};
        if (host.contains(SymbolConstant.SYMBOL_COMMA)) {
            int port = NetHelper.getPort(host);
            setPort(port);
        }
        this.put(CONFIG_FIELD_HOST, this.host);
    }

    /**
     * 设置host
     *
     * @param host host
     */
    public void setHost(String[] host) {
        this.host = host;
        this.put(CONFIG_FIELD_HOST, host);
    }

    /**
     * 当唯一值获取数据
     *
     * @return String
     */
    public String getHostIfOnly() {
        if (null == host) {
            return DEFAULT_HOST;
        }
        return host.length == 1 ? host[0] : DEFAULT_HOST;
    }

    /**
     * 添加host
     *
     * @param host host
     */
    @SuppressWarnings("unchecked")
    public void addHost(String host) {
        if (null == host) {
            throw new IllegalArgumentException("[host] cannot be empty");
        }
        if (!this.containsKey(CONFIG_FIELD_HOST)) {
            this.put(CONFIG_FIELD_HOST, host);
            this.host = new String[]{host};
        } else {
            List<String> items = new ArrayList<>();
            Object obj = this.get(CONFIG_FIELD_HOST);
            if (obj instanceof List) {
                items.addAll((Collection<? extends String>) obj);
            } else {
                items.add(obj + "");
                items.add(host);
            }
            this.host = items.toArray(new String[0]);
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

    public int getRetry() {
        return retry < 0 ? DEFAULT_CONFIG_FIELD_RETRY : retry;
    }

    public void setPort(int port) {
        this.port = port;
        this.put(CONFIG_FIELD_PORT, port);
    }

    @Override
    public <T> T getEntity() {
        return (T) t;
    }

    @Override
    public <T> void setEntity(T t) {
        this.t = t;
    }

    /**
     * 获取值不存在获取默认值
     *
     * @param defaultValue 默认值
     * @return
     */
    public int getPort(int defaultValue) {
        return port <= 0 ? defaultValue : port;
    }
}
