package com.chua.utils.tools.resource.factory;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.RewriteReflections;
import com.chua.utils.tools.classes.reflections.configuration.RewriteConfiguration;
import com.chua.utils.tools.classes.reflections.scanner.RewriteResourcesScanner;
import com.chua.utils.tools.classes.reflections.scanner.RewriteSubTypesScanner;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.common.skip.SkipPatterns;
import com.chua.utils.tools.constant.SuffixConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.matcher.AntPathMatcher;
import com.chua.utils.tools.matcher.PathMatcher;
import com.chua.utils.tools.resource.entity.Resource;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashMultimap;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.chua.utils.tools.constant.StringConstant.CLASSPATH_URL_PREFIX;

/**
 * 反射获取资源
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/18
 */
@Slf4j
public class ReflectionFactory implements ResourceFactory {

    private static final Cache<String, Set<Resource>> CACHE_LOCAL_RESOURCE = CacheBuilder.newBuilder()
            .softValues()
            .recordStats()
            .concurrencyLevel(ThreadHelper.processor())
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(100).build();
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private ClassLoader classLoader;
    private Matcher<Resource> matcher;
    private boolean cache;
    private AtomicInteger atomicInteger;

    @Override
    public ResourceFactory classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    @Override
    public ResourceFactory matcher(Matcher<Resource> matcher) {
        this.matcher = matcher;
        return this;
    }

    @Override
    public ResourceFactory cache(boolean cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public ResourceFactory count() {
        this.atomicInteger = new AtomicInteger(0);
        return this;
    }

    @Override
    public Set<Resource> getResources(String name, String... excludes) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        if (this.cache) {
            Set<Resource> present = CACHE_LOCAL_RESOURCE.getIfPresent(name);
            if (null != present) {
                return present;
            }
        }
        URL[] urls = ClassHelper.getUrlsByClassLoader(ClassHelper.getDefaultClassLoader());
        URL[] newUrls = excludeUrls(urls, excludes);

        RewriteConfiguration rewriteConfiguration = new RewriteConfiguration();
        rewriteConfiguration.setUrls(newUrls);
        rewriteConfiguration.setRewriteScanners(new RewriteSubTypesScanner(true), new RewriteResourcesScanner());
        rewriteConfiguration.setExecutorService(ThreadHelper.newProcessorThreadExecutor());
        rewriteConfiguration.setClassLoaders(new ClassLoader[]{ClassHelper.getDefaultClassLoader()});

        long startTime = System.currentTimeMillis();

        RewriteReflections rewriteReflections = new RewriteReflections(rewriteConfiguration);

        final Set<Resource> result = new HashSet<>();
        //判断是否是classpath:
        try {
            if (name.startsWith(CLASSPATH_URL_PREFIX)) {
                final String newName = name.substring(CLASSPATH_URL_PREFIX.length()).trim();

                Map<String, String> resourceMaps = rewriteReflections.getResourceMaps(resource -> StringHelper.wildcardMatch(resource, newName));
                resourceMaps.keySet().parallelStream().forEach(resource -> {
                    try {
                        Resource resource1 = Resource.create(new URL(resource + "!/" + resourceMaps.get(resource)));
                        result.add(resource1);
                    } catch (MalformedURLException e) {
                    }
                });
                HashMultimap<String, String> classes = rewriteReflections.getAllMaps();
                if (null != classes) {
                    classes.keySet().parallelStream().forEach(resource -> {
                        Set<String> strings = classes.get(resource);
                        strings.parallelStream().forEach(file -> {
                            file = file.replace(".", "/") + SuffixConstant.SUFFIX_CLASS;
                            if (pathMatcher.match(newName, file)) {
                                try {
                                    result.add(Resource.create(new URL(resource + "!/" + file.replace(".", "/") + SuffixConstant.SUFFIX_CLASS)));
                                } catch (MalformedURLException ignore) {

                                }
                            }
                        });
                    });
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (null == atomicInteger) {
                log.info("表达式: {}, 共检索到[{}], 耗时: {}ms", name, null == result ? 0 : result.size(), System.currentTimeMillis() - startTime);
            } else {
                log.info("表达式: {}, 共检索到[{}/{}], 耗时: {}ms", name, null == result ? 0 : result.size(), atomicInteger.get(), System.currentTimeMillis() - startTime);
            }
        }
        if (this.cache) {
            CACHE_LOCAL_RESOURCE.put(name, result);
        }

        return result;
    }

    /**
     * 过滤除外的urls
     *
     * @param urls     urls
     * @param excludes 除外集合
     * @return
     */
    private URL[] excludeUrls(URL[] urls, String[] excludes) {
        Set<URL> result = new HashSet<>();
        for (URL url : urls) {
            if (isSkip(url, excludes)) {
                continue;
            }

            if (isSkip(url, SkipPatterns.JDK_LIB)) {
                continue;
            }

            result.add(url);
        }

        return result.toArray(new URL[0]);
    }

    /**
     * 是否跳过url
     *
     * @param url      url
     * @param excludes 除外
     * @return 跳过返回true
     */
    private boolean isSkip(URL url, String[] excludes) {
        for (String exclude : excludes) {
            if (!StringHelper.wildcardMatch(url.toExternalForm(), exclude)) {
                continue;
            }
            return true;
        }

        return false;
    }

    /**
     * 是否跳过url
     *
     * @param url      url
     * @param excludes 除外
     * @return 跳过返回true
     */
    private boolean isSkip(URL url, Set<String> excludes) {
        for (String exclude : excludes) {
            if (!StringHelper.wildcardMatch(url.toExternalForm(), exclude)) {
                continue;
            }
            return true;
        }

        return false;
    }

}
