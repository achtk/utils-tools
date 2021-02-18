package com.chua.utils.tools.common;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.charset.CharsetHelper;
import com.chua.utils.tools.resource.entity.Resource;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.chua.utils.tools.constant.StringConstant.*;
import static com.chua.utils.tools.constant.SuffixConstant.SUFFIX_JAR_SUFFIX;
import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * @author CH
 */
@Slf4j
public class UrlHelper {

    private static final ConcurrentHashMap<String, Resource> ALL_FILES = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<String> ALL_NAMES = new ConcurrentLinkedQueue<>();
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    /**
     * 拼接url
     *
     * @param url url
     * @return url
     */
    public static URL toUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            try {
                return new File(url).toURI().toURL();
            } catch (MalformedURLException e1) {
                return null;
            }
        }
    }

    /**
     * 拼接url
     *
     * @param url url
     * @return
     */
    public static URL toWar(String url) {
        try {
            return null == url ? null : new URL(WAR_URL_PREFIX + "/" + url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * 拼接url
     *
     * @param url url
     * @return
     */
    public static URL toJar(String url) {
        try {
            return null == url ? null : new URL(JAR_URL_PREFIX + "/" + url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * 拼接url
     *
     * @param url url
     * @return
     */
    public static URL toFile(String url) {
        try {
            return null == url ? null : new URL(FILE_URL_PREFIX + "/" + url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * 拼接url
     *
     * @param protocol 协议
     * @param host     地址
     * @param port     端口
     * @param path     地址
     * @return String
     */
    public static String toUrl(String protocol, String host, int port, String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append("://");
        sb.append(host).append(':').append(port);
        if (path.charAt(0) != SYMBOL_LEFT_SLASH_CHAR) {
            sb.append(SYMBOL_LEFT_SLASH_CHAR);
        }
        sb.append(path);
        return sb.toString();
    }

    /**
     * 重置计数器
     */
    public static void resetCount() {
        ATOMIC_INTEGER.set(0);
    }


    /**
     * file://
     *
     * @param url url
     * @return boolean
     */
    public static boolean isFileUrl(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_FILE.equals(protocol) || URL_PROTOCOL_VFSFILE.equals(protocol) ||
                URL_PROTOCOL_VFS.equals(protocol));
    }

    /**
     * 是否是jar://
     *
     * @param url url
     * @return boolean
     */
    public static boolean isJarUrl(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_WAR.equals(protocol) ||
                URL_PROTOCOL_ZIP.equals(protocol) || URL_PROTOCOL_VFSZIP.equals(protocol) ||
                URL_PROTOCOL_WSJAR.equals(protocol));
    }

    /**
     * 是否是jar url
     *
     * @param url url
     * @return boolean
     */
    public static boolean isJarFileUrl(URL url) {
        return (URL_PROTOCOL_FILE.equals(url.getProtocol()) &&
                url.getPath().toLowerCase().endsWith(JAR_FILE_EXTENSION));
    }

    /**
     * url转url
     *
     * @param jarUrl jar url
     * @return URL
     * @throws MalformedURLException
     */
    public static URL extractJarFileUrl(URL jarUrl) throws MalformedURLException {
        String urlFile = jarUrl.getFile();
        int separatorIndex = urlFile.indexOf("!/");
        if (separatorIndex != -1) {
            String jarFile = urlFile.substring(0, separatorIndex);

            try {
                return new URL(jarFile);
            } catch (MalformedURLException var5) {
                if (!jarFile.startsWith(SYMBOL_LEFT_SLASH)) {
                    jarFile = SYMBOL_LEFT_SLASH + jarFile;
                }

                return new URL("file:" + jarFile);
            }
        } else {
            return jarUrl;
        }
    }

    /**
     * url转url
     *
     * @param jarUrl jar url
     * @return url
     * @throws MalformedURLException
     */
    public static URL extractArchiveUrl(URL jarUrl) throws MalformedURLException {
        String urlFile = jarUrl.getFile();
        int endIndex = urlFile.indexOf(WAR_URL_SEPARATOR);
        if (endIndex != -1) {
            String warFile = urlFile.substring(0, endIndex);
            if (WAR.equals(jarUrl.getProtocol())) {
                return new URL(warFile);
            }

            int startIndex = warFile.indexOf(WAR_URL_PREFIX);
            if (startIndex != -1) {
                return new URL(warFile.substring(startIndex + WAR_URL_PREFIX.length()));
            }
        }

        return extractJarFileUrl(jarUrl);
    }

    /**
     * url转uri
     *
     * @param url url
     * @return URI
     * @throws URISyntaxException
     */
    public static URI toUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转uri
     *
     * @param location 字符串
     * @return URI
     * @throws URISyntaxException
     */
    public static URI toUri(String location) {
        try {
            return new URI(StringHelper.replace(location, " ", "%20"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @param con
     */
    public static void useCachesIfNecessary(URLConnection con) {
        con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
    }

    protected static boolean isResourceEmpty() {
        return ALL_NAMES.isEmpty();
    }


    /**
     * 更新缓存
     *
     * @param resource1
     */
    protected static void setFileResource(Resource resource1) {
        ALL_FILES.put(resource1.toString(), resource1);
    }


    /**
     * 获取file://文件
     *
     * @param name
     * @param path
     * @return
     */
    public static void getFileFiles(String name, final String path, List<URL> fileList) {
        File[] files = new File(path).listFiles(pathname -> false);
        if (BooleanHelper.hasLength(files)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileFiles(name, file.getPath(), fileList);
                } else {
                    try {
                        fileList.add(file.toURI().toURL());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 是否包含
     *
     * @param url
     * @param excludes
     * @return
     */
    protected static boolean containUrl(URL url, String[] excludes) {
        return null != url && containUrl(url.toExternalForm(), excludes);
    }

    /**
     * 是否包含
     *
     * @param urlPath
     * @param excludes
     * @return
     */
    protected static boolean containUrl(String urlPath, String[] excludes) {
        if (null == excludes || excludes.length == 0 || StringHelper.isEmpty(urlPath)) {
            return false;
        }

        if (!urlPath.endsWith(SUFFIX_JAR_SUFFIX)) {
            urlPath = urlPath.replace(JAR_URL_SEPARATOR, SYMBOL_EMPTY);
            int index = urlPath.lastIndexOf(SYMBOL_LEFT_SLASH);
            urlPath = urlPath.substring(index > -1 ? index + 1 : 0);
        }

        for (String exclude : excludes) {
            boolean b = StringHelper.wildcardMatch(urlPath, exclude);
            if (b) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取jar://文件
     *
     * @param name
     * @return
     */
    public static void getJarFiles(ClassLoader classLoader, LinkedBlockingQueue<String> queue, final AtomicInteger count) {
        if (classLoader instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            URL[] urLs = urlClassLoader.getURLs();
            for (URL urL : urLs) {
                String protocol = urL.getProtocol();
                String path = urL.getPath();
                try {
                    path = URLDecoder.decode(path, CharsetHelper.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    //ignore
                }
                if (protocol.equals(URL_PROTOCOL_FILE) && path.endsWith(JAR_FILE_EXTENSION)) {
                    String path1 = JAR_URL_PREFIX + protocol + ":" + path + JAR_URL_SEPARATOR;
                    if (!queue.contains(path1)) {
                        queue.add(path1);
                        count.incrementAndGet();
                    }
                }
            }
        }
    }

    /**
     * 获取url
     *
     * @param resourceLocation 资源文件地址
     * @return URL
     * @throws FileNotFoundException
     */
    public static URL getUrl(String resourceLocation) throws FileNotFoundException {
        Assert.notNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring("classpath:".length());
            ClassLoader cl = ClassHelper.getDefaultClassLoader();
            URL url = cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path);
            if (url == null) {
                String description = "class path resource [" + path + "]";
                throw new FileNotFoundException(description + " cannot be resolved to URL because it does not exist");
            } else {
                return url;
            }
        } else {
            try {
                return new URL(resourceLocation);
            } catch (MalformedURLException var6) {
                try {
                    return (new File(resourceLocation)).toURI().toURL();
                } catch (MalformedURLException var5) {
                    throw new FileNotFoundException("Resource location [" + resourceLocation + "] is neither a URL not a well-formed file path");
                }
            }
        }
    }

    public static File getFile(String resourceLocation) throws FileNotFoundException {
        Assert.notNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring("classpath:".length());
            String description = "class path resource [" + path + "]";
            ClassLoader cl = ClassHelper.getDefaultClassLoader();
            URL url = cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path);
            if (url == null) {
                throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not exist");
            } else {
                return getFile(url, description);
            }
        } else {
            try {
                return getFile(new URL(resourceLocation));
            } catch (MalformedURLException var5) {
                return new File(resourceLocation);
            }
        }
    }

    /**
     * 从url获取文件
     *
     * @param resourceUrl url
     * @return
     * @throws FileNotFoundException
     */
    public static File getFile(URL resourceUrl) throws FileNotFoundException {
        return getFile(resourceUrl, "URL");
    }

    /**
     * file://
     *
     * @param resourceUrl url
     * @param description 文件描述
     * @return
     * @throws FileNotFoundException
     */
    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException, NullPointerException {
        Assert.notNull(resourceUrl, "Resource URL must not be null");
        if (!FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUrl);
        } else {
            try {
                return new File(toUri(resourceUrl).getSchemeSpecificPart());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取uri中的文件
     *
     * @param resourceUri uri
     * @return
     * @throws FileNotFoundException
     */
    public static File getFile(URI resourceUri) throws FileNotFoundException {
        return getFile(resourceUri, "URI");
    }

    /**
     * file://
     *
     * @param resourceUri uri
     * @param description 文件描述
     * @return
     * @throws FileNotFoundException
     */
    public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
        Assert.notNull(resourceUri, "Resource URI must not be null");
        if (!FILE.equals(resourceUri.getScheme())) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUri);
        } else {
            return new File(resourceUri.getSchemeSpecificPart());
        }
    }

    /**
     * 是否是file
     *
     * @param url url
     * @return
     */
    public static boolean isFile(URL url) {
        return null != url && URL_PROTOCOL_FILE.equals(url.getProtocol());
    }

    /**
     * 是否是file
     *
     * @param url url
     * @return
     */
    public static boolean isJar(URL url) {
        return null != url && URL_PROTOCOL_JAR.equals(url.getProtocol());
    }

    /**
     * 是否是 jar
     *
     * @param url url
     * @return 是jar类压缩包返回true
     */
    public static boolean isAllJar(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_WAR.equals(protocol) ||
                URL_PROTOCOL_ZIP.equals(protocol) || URL_PROTOCOL_VFSZIP.equals(protocol) ||
                URL_PROTOCOL_WSJAR.equals(protocol));
    }

    /**
     * 是否是 jar
     *
     * @param path url
     * @return
     */
    public static boolean isAllJar(String path) {
        String extension = FileHelper.getExtension(path);
        return (URL_PROTOCOL_JAR.equals(extension) || URL_PROTOCOL_WAR.equals(extension) ||
                URL_PROTOCOL_ZIP.equals(extension) || URL_PROTOCOL_VFSZIP.equals(extension) ||
                URL_PROTOCOL_WSJAR.equals(extension));
    }

    /**
     * 是否是 war
     *
     * @param url url
     * @return
     */
    public static boolean isWar(URL url) {
        return null != url && URL_PROTOCOL_WAR.equals(url.getProtocol());
    }

    /**
     * 是否是 wsjar
     *
     * @param url url
     * @return
     */
    public static boolean isWsJar(URL url) {
        return null != url && URL_PROTOCOL_WSJAR.equals(url.getProtocol());
    }

    /**
     * 是否是file jar
     *
     * @param url url
     * @return
     */
    public static boolean isFileJar(URL url) {
        return isFile(url) && url.toExternalForm().endsWith(JAR_FILE_EXTENSION);
    }

    /**
     * 获取 jar url
     *
     * @param url
     * @return
     */
    public static URL toJarFile(URL url) {
        if (isFileJar(url)) {
            try {
                return new URL(JAR_URL_PREFIX + url.toExternalForm() + JAR_URL_SEPARATOR);
            } catch (MalformedURLException e) {
                return null;
            }
        } else if (isJar(url)) {
            return url;
        } else if (isWar(url)) {
            return url;
        }
        return url;
    }

    /**
     * 批量转化字符串为URL
     *
     * @param strings 批量字符串
     * @return URL[]
     */
    public static URL[] toUrls(String[] strings) {
        List<URL> urls = new ArrayList<>();
        for (String s : strings) {
            try {
                urls.add(new File(s).toURI().toURL());
            } catch (MalformedURLException e) {
            }
        }
        return urls.toArray(new URL[urls.size()]);
    }

    /**
     * file转url
     *
     * @param file 文件
     * @return url
     */
    public static URL[] toUrls(File[] file) {
        List<URL> urls = new ArrayList<>();
        for (File file1 : file) {
            try {
                urls.add(file1.toURI().toURL());
            } catch (MalformedURLException e) {
            }
        }
        return urls.toArray(new URL[urls.size()]);
    }

    /**
     * 字符串转 url
     *
     * @param strings 字符串
     * @return List<URL> URL集合
     */
    public static List<URL> toUrl(String[] strings) {
        if (null == strings) {
            return Collections.emptyList();
        }
        return toUrl(Arrays.asList(strings));
    }

    /**
     * 字符串转 url
     *
     * @param strings 字符串
     * @return List<URL> URL集合
     */
    public static List<URL> toUrl(List<String> strings) {
        if (!BooleanHelper.hasLength(strings)) {
            return Collections.emptyList();
        }
        List<URL> urls = new ArrayList<>(strings.size());
        for (String s : strings) {
            try {
                urls.add(new URL(s));
            } catch (MalformedURLException e) {
                try {
                    urls.add(new File(s).toURI().toURL());
                } catch (MalformedURLException ignore) {
                }
            }
        }
        return urls;
    }

    /**
     * List<URL> -> List<String>
     *
     * @param urlList url集合
     * @return 字符串集合
     */
    public static List<String> toStrings(List<URL> urlList) {
        if (!BooleanHelper.hasLength(urlList)) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>(urlList.size());
        for (URL url : urlList) {
            result.add(url.getFile());
        }
        return result;
    }
}
