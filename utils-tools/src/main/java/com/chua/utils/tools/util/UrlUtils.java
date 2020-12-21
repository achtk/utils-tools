package com.chua.utils.tools.util;

import com.chua.utils.tools.common.UrlHelper;
import com.chua.utils.tools.common.codec.UrlEncoder;
import com.google.common.base.Preconditions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_EMPTY;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_LEFT_SLASH;

/**
 * url工具类<br />
 * 部分工具来自于HuTool系列
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class UrlUtils extends UrlHelper {
    /**
     * in the url string,mark the param begin
     */
    private final static String URL_PARAM_STARTING_SYMBOL = "?";

    /**
     * 将URL字符串转换为URL对象，并做必要验证
     *
     * @param urlStr URL字符串
     * @return URL
     * @since 4.1.9
     */
    public static URL parseUrl(final String urlStr) {
        // 编码空白符，防止空格引起的请求异常
        try {
            return new URL(null, encodeBlank(urlStr));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得URL
     *
     * @param path    url路径
     * @param subPath 相对路径
     * @return URL
     */
    public static URL parseUrl(final String path, final String... subPath) {
        if (null == path) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(path);
        if (!path.endsWith(SYMBOL_LEFT_SLASH)) {
            stringBuilder.append(SYMBOL_LEFT_SLASH);
        }
        for (String s : subPath) {
            stringBuilder.append(s);
            if (!s.endsWith(SYMBOL_LEFT_SLASH)) {
                stringBuilder.append(SYMBOL_LEFT_SLASH);
            }
        }
        String preUrl = stringBuilder.toString();
        if(preUrl.endsWith(SYMBOL_LEFT_SLASH)) {
            preUrl = preUrl.substring(0, preUrl.length() - 1);
        }
        return parseUrl(preUrl);
    }

    /**
     * 获得URL
     *
     * @param path 相对给定 class所在的路径
     * @return URL
     */
    public static URL parseFileUrl(final String path) {
        URL url = null;
        File file = new File(path);
        if (file.exists()) {
            try {
                url = file.toURI().toURL();
            } catch (Exception e) {
            }
        }

        if (null == url) {
            ClassLoader classLoader = Optional.ofNullable(Thread.currentThread().getContextClassLoader()).orElse(UrlUtils.class.getClassLoader());
            return classLoader.getResource(path);
        }
        return url;
    }

    /**
     * 获得URL，常用于使用绝对路径时的情况
     *
     * @param file URL对应的文件对象
     * @return URL
     * @throws MalformedURLException MalformedURLException
     */
    public static URL parseFileUrl(File file) {
        Preconditions.checkNotNull(file, "File is null !");
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 标准化URL字符串，包括：
     *
     * <pre>
     * 1. 多个/替换为一个
     * </pre>
     *
     * @param url          URL字符串
     * @param isEncodePath 是否对URL中path部分的中文和特殊字符做转义（不包括 http:, /和域名部分）
     * @return 标准化后的URL字符串
     * @since 4.4.1
     */
    public static String normalize(String url, boolean isEncodePath) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        final int sepIndex = url.indexOf("://");
        String protocol;
        String body;
        if (sepIndex > 0) {
            protocol = StringUtils.subPre(url, sepIndex + 3);
            body = StringUtils.subSuf(url, sepIndex + 3);
        } else {
            protocol = "http://";
            body = url;
        }

        final int paramsSepIndex = StringUtils.indexOf(body, '?');
        String params = null;
        if (paramsSepIndex > 0) {
            params = StringUtils.subSuf(body, paramsSepIndex);
            body = StringUtils.subPre(body, paramsSepIndex);
        }

        if (StringUtils.isNotEmpty(body)) {
            // 去除开头的\或者/
            //noinspection ConstantConditions
            body = body.replaceAll("^[\\\\/]+", SYMBOL_EMPTY);
            // 替换多个\或/为单个/
            body = body.replace("\\", "/").replaceAll("//+", "/");
        }

        final int pathSepIndex = StringUtils.indexOf(body, '/');
        String domain = body;
        String path = null;
        if (pathSepIndex > 0) {
            domain = StringUtils.subPre(body, pathSepIndex);
            path = StringUtils.subSuf(body, pathSepIndex);
        }
        if (isEncodePath) {
            path = encode(path);
        }
        return protocol + domain + StringUtils.nullToEmpty(path) + StringUtils.nullToEmpty(params);
    }

    /**
     * 单独编码URL中的空白符，空白符编码为%20
     *
     * @param urlStr URL字符串
     * @return 编码后的字符串
     * @since 4.5.14
     */
    public static String encodeBlank(CharSequence urlStr) {
        if (urlStr == null) {
            return null;
        }

        int len = urlStr.length();
        final StringBuilder sb = new StringBuilder(len);
        char c;
        for (int i = 0; i < len; i++) {
            c = urlStr.charAt(i);
            if (Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a') {
                sb.append("%20");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 编码URL，默认使用UTF-8编码<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
     *
     * @param url URL
     * @return 编码后的URL
     * @since 3.1.2
     */
    public static String encode(String url) {
        return UrlEncoder.DEFAULT.encode(url);
    }

}
