package com.chua.utils.tools.constant;

import java.io.File;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 字符串常量
 *
 * @author CH
 */
public class StringConstant {

    /**
     * -------------------------------------格式化-------------------------------------
     */
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.000");
    /**
     * -------------------------------------资源文件-------------------------------------
     */
    public static final String RESOURCE_MESSAGE = "language/message";
    /**-------------------------------------基础常量-------------------------------------*/
    /**
     * Used to build output as Hex
     */
    public static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    public static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * default
     */
    public static final String DEFAULT = "default";
    /**
     * true
     */
    public static final String TRUE = "true";
    /**
     * false
     */
    public static final String FALSE = "false";
    /**
     * 处理占位符对象属性
     */
    public static final String SYSTEM_PLACEHOLDER_PROP = "system.prop.placeholder";
    /**
     * 处理文件优先级
     */
    public static final String SYSTEM_PRIORITY_PROP = "system.prop.priority";
    /**
     * spi默认配置
     */
    public static final String SPI_CONFIG_DEFAULT = "spi-config-default";
    /**
     * spi配置
     */
    public static final String SPI_CONFIG = "spi-config.json";
    /**
     * resource默认配置
     */
    public static final String RESOURCE_CONFIG_DEFAULT = "resource-config-default.json";
    /**
     * host
     */
    public static final String CONFIG_FIELD_HOST = "host";
    /**
     * port
     */
    public static final String CONFIG_FIELD_PORT = "port";
    /**
     * password
     */
    public static final String CONFIG_FIELD_PASSWORD = "password";
    /**
     * username
     */
    public static final String CONFIG_FIELD_USERNAME = "username";
    /**
     * 最大连接数
     */
    public static final String CONFIG_FIELD_MAX_CONNECTION = "maxConnection";
    /**
     * 连接超时
     */
    public static final String CONFIG_FIELD_CONNECTION_TIMEOUT = "connectionTimeout";
    /**
     * 读取超时
     */
    public static final String CONFIG_FIELD_READ_TIMEOUT = "readTimeout";
    /**
     * 写入超时
     */
    public static final String CONFIG_FIELD_WRITE_TIMEOUT = "writeTimeout";
    /**
     * 路径
     */
    public static final String CONFIG_FIELD_PATH = "path";
    /**
     * 超时时间
     */
    public static final String CONFIG_FIELD_SESSION_TIMEOUT = "sessionTimeout";
    /**
     * 重试
     */
    public static final String CONFIG_FIELD_RETRY = "retry";
    /**
     * jar
     */
    public static final String JAR = "jar";
}
