package com.chua.utils.tools.resource.matcher;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.*;
import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.matcher.AntPathMatcher;
import com.chua.utils.tools.resource.entity.Resource;
import com.chua.utils.tools.resource.parser.compress.dir.ZipParserDir;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import static com.chua.utils.tools.constant.StringConstant.*;
import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * Classpath:匹配
 *
 * @author CH
 * @since 1.0
 */
@Slf4j
public class ClassPathMatcher extends UrlHelper implements PathMatcher {

    private static final com.chua.utils.tools.matcher.PathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private final Matcher<Resource> matcher;
    private final String name;
    private final String[] excludes;
    private ClassLoader classLoader;
    private final AtomicInteger atomicInteger;

    public ClassPathMatcher(String name, String[] excludes, ClassLoader classLoader, AtomicInteger atomicInteger, Matcher<Resource> matcher) {
        this.name = name;
        this.excludes = excludes;
        this.classLoader = classLoader;
        this.atomicInteger = atomicInteger;
        this.matcher = matcher;
    }

    /**
     * @param path
     * @return
     */
    private static boolean getPathMatcher(String path) {
        return ANT_PATH_MATCHER.isPattern(path);
    }

    @Override
    public Set<Resource> matcher() throws IOException {
        if (null != atomicInteger) {
            atomicInteger.set(0);
        }
        return getResources(name, excludes);
    }

    /**
     * 查询路径匹配资源
     *
     * @param name
     * @param excludes
     * @return
     */
    private Set<Resource> findPathMatchingResources(String name, String[] excludes) throws IOException {
        //获取根目录
        String classPathRoot = findPathRootPath(name);
        //查询根目录
        String rootPath = classPathRoot.substring(CLASSPATH_URL_PREFIX.length()).trim();
        //待匹配的文件
        String subPath = name.substring(classPathRoot.length());
        //获取目录文件
        Set<Resource> resources = getResources(classPathRoot, excludes);
        return analysisResources(resources, name, rootPath, subPath);
    }

    /**
     * @param name
     * @param excludes
     * @return
     * @throws IOException
     */
    private Set<Resource> getResources(String name, String[] excludes) throws IOException {
        Set<Resource> result = null;
        //是否存在路径通配符
        if (getPathMatcher(name.substring(CLASSPATH_URL_PREFIX.length()))) {
            result = findPathMatchingResources(name, excludes);
        } else {
            result = findAllClassPathResources(name, excludes);
        }
        return result;
    }


    /**
     * 便利数据
     *
     * @param resources
     * @param name
     * @param rootPath
     * @param subPath
     */
    private Set<Resource> analysisResources(Set<Resource> resources, String name, String rootPath, String subPath) {
        Set<Resource> result = new HashSet<>();

        long startTime = System.currentTimeMillis();

        int size = resources.size();
        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();
        List<Future> futures = new ArrayList<>();

        resources.forEach(resource -> {
            futures.add(executorService.submit((Callable<Object>) () -> {
                URL url = resource.getUrl();
                //如果是jar, war, zip, wsjar, vfszip文件
                long startTime1 = 0L;
                if (log.isDebugEnabled()) {
                    startTime1 = System.currentTimeMillis();
                }
                try {
                    if (isAllJar(url)) {
                        try {
                            doFindPathMatchingJarResources(url, subPath, atomicInteger, result);
                        } catch (IOException e) {
                            return null;
                        }
                    } else {
                        doFindPathMatchingFileResources(url, subPath, rootPath, atomicInteger, result);
                    }
                } finally {
                    if (log.isDebugEnabled()) {
                        log.debug("处理目录: {}, 耗时: {}ms", url, System.currentTimeMillis() - startTime1);
                    }
                }
                return "";
            }));
        });
        for (Future future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                continue;
            }
        }
        executorService.shutdown();

        if (null == atomicInteger || atomicInteger.get() == 0) {
            log.info("Expression: {}, [{}] was retrieved, time-consuming: {}ms, to scan {} urls", name, null == result ? 0 : result.size(), System.currentTimeMillis() - startTime, size);
        } else {
            log.info("Expression: {}, [{}/{}] was retrieved in total, time-consuming: {}ms, to scan {} urls", name, null == result ? 0 : result.size(), atomicInteger.get(), System.currentTimeMillis() - startTime, size);
        }
        return result;
    }

    /**
     * 查询file:下的文件
     *
     * @param url     fileURL
     * @param subPath 文件
     * @param path
     * @param count
     * @param result
     * @return
     */
    private void doFindPathMatchingFileResources(URL url, String subPath, String path, AtomicInteger count, Set<Resource> result) {
        doFindPathMatchingLocalFileResources(url.getFile(), new File(url.getFile()).getAbsolutePath(), path, subPath, count, result);
    }

    /**
     * 解析文件夹下的文件
     *
     * @param path
     * @param matcherPath
     * @param count
     * @param result
     * @return
     */
    private void doFindPathMatchingLocalFileResources(String path, String root, String rootPath, String matcherPath, AtomicInteger count, Set<Resource> result) {
        File file = new File(path);
        if (null == file && file.isFile()) {
            return;
        }

        Path path1 = Paths.get(file.getAbsolutePath());
        try {
            Files.walkFileTree(path1, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (log.isDebugEnabled() && null != count) {
                        count.incrementAndGet();
                    }
                    if (file.toFile().isDirectory()) {
                        return FileVisitResult.CONTINUE;
                    }

                    String absolutePath = file.toString();
                    int length = absolutePath.length();
                    int length1 = root.length();
                    if (length < length1 + 1) {
                        return FileVisitResult.CONTINUE;
                    }
                    String embeddedSubPath = absolutePath.substring(root.length() + 1);
                    if (embeddedSubPath.startsWith(SYMBOL_LEFT_SLASH)) {
                        embeddedSubPath = embeddedSubPath.substring(1);
                    }

                    embeddedSubPath = embeddedSubPath.replace(SYMBOL_RIGHT_SLASH, SYMBOL_LEFT_SLASH);

                    if ((SYMBOL_ASTERISK.equals(matcherPath) || ANT_PATH_MATCHER.match(matcherPath, embeddedSubPath))) {
                        Resource resource = new Resource();
                        try {
                            resource.setUrl(file.toUri().toURL());
                            matcher.doWith(resource);
                        } catch (Exception e) {
                        }
                        result.add(resource);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查找jar中的文件
     *
     * @param url     jarURL
     * @param subPath 文件
     * @param count
     * @param result
     * @return
     */
    private void doFindPathMatchingJarResources(URL url, String subPath, AtomicInteger count, Set<Resource> result) throws IOException {
        final Class<?> aClass = ClassHelper.forName(subPath);
        if (null == aClass) {
            //return result;
        }
        //jar文件
        ZipFile jarFile = null;
        try {
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                urlConnection.setUseCaches(false);
                jarFile = ((JarURLConnection) urlConnection).getJarFile();
            }
        } catch (Throwable ignore) {
        }

        if(null == jarFile) {
            return;
        }
        //url根目录
        final String rootEntryPath = "";

        try {
            jarFile.stream().parallel().forEach(jarEntry -> {
                if (log.isDebugEnabled() && null != count) {
                    count.incrementAndGet();
                }

                String jarEntryName = jarEntry.getName();
                if (jarEntryName.startsWith(rootEntryPath)) {
                    String relativePath = jarEntryName.substring(rootEntryPath.length());
                    if ("*".equals(relativePath) || ANT_PATH_MATCHER.match(subPath, relativePath)) {
                        try {
                            Resource resource = Resource.create(new URL(url.toExternalForm() + jarEntryName));
                            matcher.doWith(resource);
                            result.add(resource);
                        } catch (Exception e) {
                        }
                    }
                }
            });
        } finally {
            if (null != jarFile) {
                jarFile.close();
            }
        }

    }

    /**
     * 获取jarFile
     *
     * @param jarFileUrl
     * @return
     * @throws IOException
     */
    protected JarFile getJarFile(String jarFileUrl) throws IOException {
        if (jarFileUrl.startsWith(FILE_URL_PREFIX)) {
            try {
                return new JarFile(toUri(jarFileUrl).getSchemeSpecificPart());
            } catch (IOException ex) {
                return new JarFile(jarFileUrl.substring(FILE_URL_PREFIX.length()));
            }
        } else {
            return new JarFile(jarFileUrl);
        }
    }

    /**
     * 获取根目录
     *
     * @param name
     * @return
     */
    private String findPathRootPath(String name) {
        int prefixEnd = name.indexOf(':') + 1;
        int rootDirEnd = name.length();
        while (rootDirEnd > prefixEnd && getPathMatcher(name.substring(prefixEnd, rootDirEnd))) {
            rootDirEnd = name.lastIndexOf('/', rootDirEnd - 2) + 1;
        }
        if (rootDirEnd == 0) {
            rootDirEnd = prefixEnd;
        }
        return name.substring(0, rootDirEnd);
    }

    /**
     * 忽略文件
     *
     * @param excludes 忽略列表
     * @param name     当前文件
     * @return
     */
    private boolean ignoreJar(String[] excludes, String name) {
        if (!BooleanHelper.hasLength(excludes) || Strings.isNullOrEmpty(name)) {
            return false;
        }
        for (String exclude : excludes) {
            if (FileHelper.wildcardMatch(name, exclude)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 解析文件夹下的文件
     *
     * @param name     文件夹
     * @param excludes
     * @return
     */
    protected Set<Resource> findAllClassPathResources(String name, String[] excludes) {
        Set<Resource> result = new HashSet<>();
        Set<String> additionalCollections = new HashSet<>();
        //获取路径
        String path = name.substring(CLASSPATH_URL_PREFIX.length()).trim();

        if (path.startsWith(SYMBOL_LEFT_SLASH)) {
            path = path.substring(1);
        }
        //获取类加载器
        this.classLoader = null == this.classLoader ? this.classLoader : ClassLoader.getSystemClassLoader();
        //获取资源文件
        Enumeration<URL> enumeration = null;

        try {
            enumeration = classLoader.getResources(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (enumeration.hasMoreElements()) {
            if (log.isDebugEnabled() && null != atomicInteger) {
                atomicInteger.incrementAndGet();
            }
            URL url = enumeration.nextElement();

            if (ignoreJar(excludes, getUrlName(url.toExternalForm(), path))) {
                continue;
            }
            additionalCollections.add(url.toExternalForm());
            result.add(new Resource(url));
        }

        if ("".equals(path)) {
            addAllClassLoaderJarRoots(classLoader, result, additionalCollections, excludes);
        }
        return result;
    }

    /**
     * 获取名称
     *
     * @param toExternalForm
     * @param path
     * @return
     */
    private String getUrlName(String toExternalForm, String path) {
        int index = toExternalForm.indexOf(JAR_URL_SEPARATOR);
        if (index == -1) {
            return toExternalForm;
        }
        String substring = toExternalForm.substring(0, index);
        return FileHelper.getName(substring);
    }

    /**
     * 添加jar
     *
     * @param classLoader
     * @param result
     * @param additionalCollections
     * @param excludes
     */
    private void addAllClassLoaderJarRoots(ClassLoader classLoader, Set<Resource> result, Set<String> additionalCollections, String[] excludes) {
        if (classLoader instanceof URLClassLoader) {
            URL[] urls = ((URLClassLoader) classLoader).getURLs();
            try {
                Arrays.stream(urls).parallel().forEach(url -> {
                    if (log.isDebugEnabled() && null != atomicInteger) {
                        atomicInteger.incrementAndGet();
                    }

                    if (ignoreJar(excludes, FileHelper.getName(url.toExternalForm()))) {
                        return;
                    }

                    Resource resource = null;
                    if (StringConstant.JAR.equals(url.getProtocol())) {
                        resource = Resource.create(url);
                    } else {
                        resource = Resource.create(toUrl(JAR_URL_PREFIX + url + JAR_URL_SEPARATOR));
                    }

                    if (additionalCollections.contains(resource.getUrl().toExternalForm())) {
                        return;
                    }
                    additionalCollections.add(resource.getUrl().toExternalForm());
                    result.add(resource);
                });
            } catch (Exception ex) {
                if (log.isDebugEnabled()) {
                    log.debug("Cannot introspect jar files since ClassLoader [" + classLoader + "] does not support 'getURLs()': " + ex);
                }
            }
        }

        if (classLoader == ClassLoader.getSystemClassLoader()) {
            addClassPathManifestEntries(result, additionalCollections, excludes);
        }

        if (classLoader != null) {
            try {
                addAllClassLoaderJarRoots(classLoader.getParent(), result, additionalCollections, excludes);
            } catch (Exception ex) {
                if (log.isDebugEnabled()) {
                    log.debug("Cannot introspect jar files in parent ClassLoader since [" + classLoader + "] does not support 'getParent()': " + ex);
                }
            }
        }
    }

    /**
     * @param result
     * @param additionalCollections
     * @param excludes
     */
    protected void addClassPathManifestEntries(Set<Resource> result, Set<String> additionalCollections, String[] excludes) {
        try {
            String javaClassPathProperty = System.getProperty(JAVA_CLASS_PATH);
            for (String path : StringHelper.delimitedListToStringArray(javaClassPathProperty, System.getProperty(PATH_SEPARATOR))) {
                if (log.isDebugEnabled() && null != atomicInteger) {
                    atomicInteger.incrementAndGet();
                }

                String filePath = new File(path).getAbsolutePath();
                int prefixIndex = filePath.indexOf(':');
                if (prefixIndex == 1) {
                    filePath = StringHelper.capitalize(filePath);
                }

                if (ignoreJar(excludes, FileHelper.getName(filePath))) {
                    continue;
                }

                if (!isAllJar(filePath)) {
                    continue;
                }

                Resource resource = Resource.create(toUrl(JAR_URL_PREFIX + FILE_URL_PREFIX + SYMBOL_LEFT_SLASH + filePath.replace("\\", SYMBOL_LEFT_SLASH) + JAR_URL_SEPARATOR));

                if (additionalCollections.contains(resource.getUrl().toExternalForm())) {
                    continue;
                }

                additionalCollections.add(resource.getUrl().toExternalForm());
                result.add(resource);
            }
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to evaluate 'java.class.path' manifest entries: " + ex);
            }
        }
    }


}
