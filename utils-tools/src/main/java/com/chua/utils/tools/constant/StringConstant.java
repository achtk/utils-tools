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
     * ------------------------------------ JDK-------------------------------------
     */
    /**
     * path.separator
     */
    public static final String PATH_SEPARATOR = "path.separator";
    /**
     * java.class.path
     */
    public static final String JAVA_CLASS_PATH = "java.class.path";
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
    /**
     * file
     */
    public static final String FILE = "file";
    /**
     * file
     */
    public static final String WAR = "war";
    /**
     * zip
     */
    public static final String ZIP = "zip";
    /**
     * A
     */
    public static final char LETTER_A = 'A';
    /**
     * Z
     */
    public static final char LETTER_Z = 'Z';
    /**
     * 0x
     */
    public static final String HEX_16 = "0x";
    /**
     * 0X
     */
    public static final String HEX_16_UPPER = "0X";


    /**
     * 系统语言环境，默认为中文zh
     */
    public static final String LANGUAGE = "zh";
    /**
     * 系统国家环境，默认为中国CN
     */
    public static final String COUNTRY = "CN";
    /**
     * /jre
     */
    public static final String PATH_JRE = "/jre";
    /**
     * sun
     */
    public static final String SUN = "sun";
    /**
     * 0
     */
    public static final String ZERO = "0";
    /**
     * any
     */
    public static final String ANY = "any";
    /**
     *
     */
    private static final String SYMBOL_FILE = "file";
    /**
     *
     */
    private static final String SYMBOL_WAR = "war";
    /**
     *
     */
    private static final String SYMBOL_JAR = "jar";

    /**
     *
     */
    private static final String EMPTY_URL = "file:.";


    /**
     * Pseudo URL prefix for loading from the class path: "classpath:".
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    /**
     * Pseudo URL prefix for loading from the class path: "subclass:".
     */
    public static final String SUBCLASS_URL_PREFIX = "subclass:";

    /**
     * Pseudo URL prefix for loading from the class path: "class:".
     */
    public static final String CLASS_URL_PREFIX = "class:";

    /**
     * Pseudo URL prefix for loading from the class path: "remote:".
     */
    public static final String REMOTE_URL_PREFIX = "remote:";

    /**
     * content:
     */
    public static final String CONTENT_URL_PREFIX = "content:";
    /**
     * root:
     */
    public static final String ROOT_URL_PREFIX = "root:";

    /**
     * URL prefix for loading from the file system: "file:".
     */
    public static final String FILE_URL_PREFIX = "file:";

    /**
     * URL prefix for loading from a jar file: "jar:".
     */
    public static final String JAR_URL_PREFIX = "jar:";

    /**
     * URL prefix for loading from a war file on Tomcat: "war:".
     */
    public static final String WAR_URL_PREFIX = "war:";

    /**
     * URL protocol for a file in the file system: "file".
     */
    public static final String URL_PROTOCOL_FILE = FILE;

    /**
     * URL protocol for an entry from a jar file: "jar".
     */
    public static final String URL_PROTOCOL_JAR = JAR;

    /**
     * URL protocol for an entry from a war file: "war".
     */
    public static final String URL_PROTOCOL_WAR = WAR;

    /**
     * URL protocol for an entry from a zip file: "zip".
     */
    public static final String URL_PROTOCOL_ZIP = ZIP;

    /**
     * URL protocol for an entry from a WebSphere jar file: "wsjar".
     */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";

    /**
     * URL protocol for an entry from a JBoss jar file: "vfszip".
     */
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";

    /**
     * URL protocol for a JBoss file system resource: "vfsfile".
     */
    public static final String URL_PROTOCOL_VFSFILE = "vfsfile";

    /**
     * URL protocol for a general JBoss VFS resource: "vfs".
     */
    public static final String URL_PROTOCOL_VFS = "vfs";

    /**
     * File extension for a regular jar file: ".jar".
     */
    public static final String JAR_FILE_EXTENSION = ".jar";

    /**
     * Separator between JAR URL and file path within the JAR: "!/".
     */
    public static final String JAR_URL_SEPARATOR = "!/";

    /**
     * Special separator between WAR URL and jar part on Tomcat.
     */
    public static final String WAR_URL_SEPARATOR = "*/";
    /**
     * .class
     */
    public static final String CLASS_FILE_EXTENSION = SuffixConstant.SUFFIX_CLASS;
    /**
     * GET
     */
    public static final String GET = "GET";
    /**
     * POST
     */
    public static final String POST = "POST";
    /**
     * DELETE
     */
    public static final String DELETE = "DELETE";
    /**
     * PUT
     */
    public static final String PUT = "PUT";
}
