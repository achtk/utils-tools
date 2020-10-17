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
     * Alphanumeric characters
     */
    public static final String REG_ALNUM = "\\p{Alnum}";
    /**
     * Alphabetic characters
     */
    public static final String REG_ALPHA = "\\p{Alpha}";
    /**
     * ASCII characters
     */
    public static final String REG_ASCII = "\\p{ASCII}";
    /**
     * Space and tab
     */
    public static final String REG_BLANK = "\\p{Blank}";
    /**
     * Control characters
     */
    public static final String REG_CNTRL = "\\p{Cntrl}";
    /**
     * Digits
     */
    public static final String REG_DIGITS = "\\p{Digit}";
    /**
     * SVisible characters (i.e. anything except spaces, control characters, etc.)
     */
    public static final String REG_GRAPH = "\\p{Graph}";
    /**
     * Lowercase letters
     */
    public static final String REG_LOWER = "\\p{Lower}";
    /**
     * Visible characters and spaces (i.e. anything except control characters, etc.)
     */
    public static final String REG_PRINT = "\\p{Print}";
    /**
     * Punctuation and symbols.
     */
    public static final String REG_PUNCT = "\\p{Punct}";
    /**
     * All whitespace characters, including line breaks
     */
    public static final String REG_SPACE = "\\p{Space}";
    /**
     * Uppercase letters
     */
    public static final String REG_UPPER = "\\p{Upper}";
    /**
     * Hexadecimal digits
     */
    public static final String REG_XDIGIT = "\\p{XDigit}";
    /**
     * 空白行
     */
    public static final String REG_SPACE_LINE = "\\n\\s*\\r";
    /**
     * 首尾空白字符
     */
    public static final String REG_SPACE_POINT = "^\\s*|\\s*$";
    /**
     * HTML
     */
    public static final String REG_HTML = "<(\\S*?)[^>]*>.*?</\\1>|<.*? />";
    /**
     * Email
     */
    public static final String REG_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 国内固定电话
     */
    public static final String REG_FIXED_TELEPHONE = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";
    /**
     * 邮政编码
     */
    public static final String REG_POSTALCODE = "[1-9]\\d{5}(?!\\d)";
    /**
     * 身份证编码
     */
    public static final String REG_IDENTIFICATION_CARD = "\\d{15}|\\d{18}";
    /**
     * URL地址
     */
    public static final String REG_URL = "^http://([w-]+.)+[w-]+(/[w-./?%&=]*)?$";
    /**
     * 移动电话
     */
    public static final String REG_MOBILE_TELEPHONE = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    /**
     * 合法的名字（字母开头，允许5-16字节，允许字母数字下划线）
     */
    public static final String REG_LEGAL_ACCOUNT = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    /**
     * i地址
     */
    public static final String REG_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
    private static Pattern numericStringPattern = Pattern.compile("^[0-9\\-\\-]+$");
    private static Pattern floatNumericPattern = Pattern.compile("^[0-9\\-\\.]+$");
    private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");

    public static final String ARRAY_SUFFIX = "[]";

    private static final String INTERNAL_ARRAY_PREFIX = "[";

    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

    private static final char PACKAGE_SEPARATOR = '.';

    private static final char PATH_SEPARATOR = '/';

    private static final char INNER_CLASS_SEPARATOR = '$';

    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    public static final String CLASS_FILE_SUFFIX = ".class";

    public static final char DIR_SEPARATOR_UNIX = '/';

    public static final char DIR_SEPARATOR_WINDOWS = '\\';

    public static final char DIR_SEPARATOR = File.separatorChar;

    public static final String LINE_SEPARATOR_UNIX = "\n";

    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";

    public final static Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");
    public final static Pattern LINE_PATTERN = Pattern.compile("_(\\w)");


    public static final String SPACE = " ";

    public static final String EMPTY = "";

    public static final String LF = "\n";

    public static final String CR = "\r";

    public static final Pattern PLACE_HOLDER = Pattern.compile("\\{(.*?)\\}");

    //key value pair pattern.
    public static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)");

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.000");

    public static final String PATTERN = "<[^>]*?>";

    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
    /**
     * The Unix separator character.
     */
    public static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    public static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
    public static final char SYSTEM_SEPARATOR = File.separatorChar;
/*****************************************FILE: START*******************************************/
    /**
     *
     */
    public static final String FILE_COMMA = ",";
    /**
     *
     */
    public static final String FILE_LEFT_SLASH = "/";
    /**
     *
     */
    public static final String FILE_RIGHT_SLASH = "\\";
    /**
     * .
     */
    public static final String FILE_DOT = ".";
    /*****************************************FILE: END*******************************************/
    /*****************************************HOLDER: START*******************************************/
    /**
     * //\\{[^>]*?\\}
     */
    public static final String HOLDER_PLACE_S = "\\{[^>]*?\\}";
    /**
     * //\\$\\{[^>]*?\\}
     */
    public static final String HOLDER_PLACE_$S = "\\$\\{[^>]*?\\}";
    /**
     * //\\$\\{[^>]*?\\}
     */
    public static final String HOLDER_PLACE = "\\$\\{[^>]*?\\}";
    /**
     *
     */
    public static final String HOLDER_CODE = "<[^>]*?>";
    /*****************************************HOLDER: END*******************************************/
    /*****************************************SQL: START*******************************************/
    /*****************************************SQL: END*******************************************/
    /*****************************************EXTENSION: START*******************************************/
    /**
     * ""
     */
    public static final String EXTENSION_EMPTY = "";
    /**
     * null
     */
    public static final String EXTENSION_NULL = null;
    /**
     * .
     */
    public static final String EXTENSION_DOT = ".";
    /**
     * ,
     */
    public static final String EXTENSION_COMMA = ",";
    /**
     * ;
     */
    public static final String EXTENSION_SEMICOLON = ";";
    /**
     * %
     */
    public static final String EXTENSION_PER = "%";
    /**
     * :
     */
    public static final String EXTENSION_COLON = ":";
    /**
     * \\s
     */
    public static final String EXTENSION_REG_S = "\\s";
    /**
     * \\
     */
    public static final String EXTENSION_REG_RIGHT_SLASH = "\\";
    /**
     * \\\\
     */
    public static final String EXTENSION_DOUBLE_REG_RIGHT_SLASH = "\\\\";
    /**
     * script
     */
    public static final String EXTENSION_REG_SCRIPT = "<script[^>]*?>[\\\\s\\\\S]*?</script>";
    /**
     * style
     */
    public static final String EXTENSION_REG_STYLE = "<style[^>]*?>[\\\\s\\\\S]*?</style>";
    /**
     * html
     */
    public static final String EXTENSION_REG_HTML = "<[^>]+>";
    /**
     * _
     */
    public static final String EXTENSION_UNDERLINE = "_";
    /**
     * _
     */
    public static final String EXTENSION_DOUBLE_UNDERLINE = "__";
    /**
     * "$", "{", "}"
     * 特殊字符
     */
    public static final String[] EXTENSION_ESCAPED = new String[]{"$", "{", "}"};
    /**
     * /
     */
    public static final String EXTENSION_LEFT_SLASH = "/";
    /**
     * .properties
     */
    public static final String EXTENSION_PROPERTIES = ".properties";
    /**
     * 字节B
     */
    public static final String EXTENSION_UNIT_B = "B";

    /**
     * 字节K
     */
    public static final String EXTENSION_UNIT_K = "K";

    /**
     * 字节M
     */
    public static final String EXTENSION_UNIT_M = "M";

    /**
     * 字节G
     */
    public static final String EXTENSION_UNIT_G = "G";

    /**
     * "new"
     */
    public static final String EXTENSION_NEW = "new";
    /**
     * -
     */
    public static final String EXTENSION_MINS = "-";
    /**
     * +
     */
    public static final String EXTENSION_PLUS = "+";
    /**
     * @
     */
    public static final String EXTENSION_AT = "@";
    /**
     * \n
     */
    public static final String EXTENSION_N = "\n";
    /**
     * *
     */
    public static final String EXTENSION_ASTERISK = "*";
    /**
     * **
     */
    public static final String EXTENSION_ASTERISKS = "**";
    /**
     *
     */
    public static final String EXTENSION_SEPARATOR_ASTERISK = "*******************************************************************************************";
    /**
     * \n\r
     */
    public static final String EXTENSION_NR = "\n\r";
    /**
     * \r\n
     */
    public static final String EXTENSION_RN = "\r\n";
    /**
     * \r
     */
    public static final String EXTENSION_R = "\r";
    /**
     * \t
     */
    public static final String EXTENSION_T = "\t";
    /**
     * \b
     */
    public static final String EXTENSION_B = "\b";
    /**
     *
     */
    public static final String EXTENSION_WELL = "#";
    /**
     *
     */
    public static final String EXTENSION_QUESTION = "?";
    /**
     * |
     */
    public static final String EXTENSION_OR = "|";
    /**
     * file
     */
    public static final String EXTENSION_PREFIX_FILE = "file";
    /**
     * jar
     */
    public static final String EXTENSION_PREFIX_JAR = "jar";
    /**
     * .jar
     */
    public static final String EXTENSION_JAR_SUFFIX = ".jar";
    /**
     * .class
     */
    public final static String EXTENSION_CLASS_SUFFIX = ".class";
    /**
     * class
     */
    public final static String EXTENSION_CLASS = "class";
    /**
     * .class
     */
    public final static String EXTENSION_JAVA_SUFFIX = ".java";
    /**
     * class
     */
    public final static String EXTENSION_JAVA = "java";

    /*****************************************EXTENSION: END*******************************************/
    /*****************************************COMMON: START*******************************************/
    /**
     *
     */
    public static final String BLANK = " ";
    /**
     * ;
     */
    public static final String SEMICOLON = ";";
    /*****************************************COMMON: END*******************************************/
    /*****************************************REG: START*******************************************/
    /**
     * *
     */
    public static final String REGEXP_ALL = "(.*?)";
    /**
     * *
     */
    public static final String REGEXP_OR = "(*|*)";
    /**
     * ?
     */
    public static final String REGEXP_ONE = "?";

    /**
     * \\s+
     */
    public static final String REGEXP_EMPTY = "(\\s+)";
    /**
     * (.*?)
     */
    public static final String REGEXP_ALL_1 = "(.*?)";
    /*****************************************REG: END*******************************************/
    /*****************************************CHARSET: START*******************************************/
    /**
     *
     */
    public static final String CHARSET_UTF_8 = "UTF-8";
    /**
     *
     */
    public static final String CHARSET_GB2312 = "GB2312";
    /**
     *
     */
    public static final String CHARSET_UTF_16 = "UTF-16";
    /**
     *
     */
    public static final String CHARSET_GBK = "GBK";
    /**
     * gbk
     */
    public static final String GBK = "gbk";
    /**
     *
     */
    public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    /**
     *
     */
    public static final String CHARSET_US_ASCII = "US-ASCII";
    /**
     * file://
     */
    public static final String EXTENSION_URL_PROTOCOL_FILE = "file://";
    /**
     * jar://
     */
    public static final String EXTENSION_URL_PROTOCOL_JAR = "jar://";
    /**
     * war://
     */
    public static final String EXTENSION_URL_PROTOCOL_WAR = "war://";
    /**
     * ear://
     */
    public static final String EXTENSION_URL_PROTOCOL_EAR = "ear://";
    /**
     * rar://
     */
    public static final String EXTENSION_URL_PROTOCOL_RAR = "rar://";
    /**
     * zip://
     */
    public static final String EXTENSION_URL_PROTOCOL_ZIP = "ZIP://";
    /**
     * ftp://
     */
    public static final String EXTENSION_URL_PROTOCOL_FTP = "ftp://";
    /**
     * sftp://
     */
    public static final String EXTENSION_URL_PROTOCOL_SFTP = "sftp://";
    /**
     * ssh://
     */
    public static final String EXTENSION_URL_PROTOCOL_SSH = "ssh://";
    /**
     * scp://
     */
    public static final String EXTENSION_URL_PROTOCOL_SCP = "scp://";

    /**
     * -------------------------------------资源文件-------------------------------------
     */
    public static final String RESOURCE_MESSAGE = "language/message";
    /**-------------------------------------基础类型-------------------------------------*/
    /**
     * void
     */
    public static final String CLASS_VOID = "void";
    /**
     * string
     */
    public static final String CLASS_STRING = "string";
    /**
     * long
     */
    public static final String CLASS_LONG = "long";
    /**
     * short
     */
    public static final String CLASS_SHORT = "short";
    /**
     * double
     */
    public static final String CLASS_DOUBLE = "double";
    /**
     * float
     */
    public static final String CLASS_FLOAT = "float";
    /**
     * char
     */
    public static final String CLASS_CHAR = "char";
    /**
     * boolean
     */
    public static final String CLASS_BOOLEAN = "boolean";
    /**
     * object
     */
    public static final String CLASS_OBJECT = "object";
    /**
     * byte
     */
    public static final String CLASS_BYTE = "byte";
    /**
     * int
     */
    public static final String CLASS_INT = "int";
    /**
     * integer
     */
    public static final String CLASS_INTEGER = "integer";
    /**
     * date
     */
    public static final String CLASS_DATE = "date";
    /**
     * string[]
     */
    public static final String CLASS_STRING_ARRAY = "string[]";
    /**
     * long[]
     */
    public static final String CLASS_LONG_ARRAY = "long[]";
    /**
     * short[]
     */
    public static final String CLASS_SHORT_ARRAY = "short[]";
    /**
     * double[]
     */
    public static final String CLASS_DOUBLE_ARRAY = "double[]";
    /**
     * float[]
     */
    public static final String CLASS_FLOAT_ARRAY = "float[]";
    /**
     * char[]
     */
    public static final String CLASS_CHAR_ARRAY = "char[]";
    /**
     * boolean[]
     */
    public static final String CLASS_BOOLEAN_ARRAY = "boolean[]";
    /**
     * object[]
     */
    public static final String CLASS_OBJECT_ARRAY = "object[]";
    /**
     * byte[]
     */
    public static final String CLASS_BYTE_ARRAY = "byte[]";
    /**
     * int[]
     */
    public static final String CLASS_INT_ARRAY = "int[]";
    /**
     * integer[]
     */
    public static final String CLASS_INTEGER_ARRAY = "integer[]";

    /**-------------------------------------基础方法-------------------------------------*/
    /**
     * equals
     */
    public static final String METHOD_EQUALS = "equals";
    /**
     * tostring
     */
    public static final String METHOD_TO_STRING = "tostring";
    /**
     * hashcode
     */
    public static final String METHOD_HASCODE = "hashcode";
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
     * MD5
     */
    public static final String DIGEST_MD5 = "MD5";
    /**
     * MD2
     */
    public static final String DIGEST_MD2 = "MD2";
    /**
     * SHA-224
     */
    public static final String DIGEST_SHA_224 = "SHA-224";
    /**
     * SHA-256
     */
    public static final String DIGEST_SHA_256 = "SHA-256";
    /**
     * SHA-384
     */
    public static final String DIGEST_SHA_384 = "SHA-384";
    /**
     * SHA-512
     */
    public static final String DIGEST_SHA_512 = "SHA-512";
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
     * {
     */
    public static final String EXTENSION_LEFT_BIG_PARANTHESES = "{";
    /**
     * }
     */
    public static final String EXTENSION_RIGHT_BIG_PARANTHESES = "}";
}
