package com.chua.utils.tools.resource.factory;

import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.common.UrlHelper;
import com.chua.utils.tools.resource.Resource;
import com.chua.utils.tools.resource.matcher.ClassPathMatcher;
import com.chua.utils.tools.resource.matcher.IPathMatcher;
import com.chua.utils.tools.resource.matcher.SubClassMatcher;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 资源工厂
 * @author CH
 * @since 1.0
 */
@Slf4j
public class FastResourceFactory extends UrlHelper implements IResourceFactory {

    private static final Cache<String, Set<Resource>> CACHE_LOCAL_RESOURCE = CacheBuilder.newBuilder()
            .softValues()
            .recordStats()
            .concurrencyLevel(ThreadHelper.processor())
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(100).build();

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private boolean cache = true;
    private ClassLoader classLoader = this.getClass().getClassLoader();

    @Override
    public IResourceFactory classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    @Override
    public IResourceFactory cache(boolean cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public IResourceFactory count() {
        atomicInteger = new AtomicInteger(0);
        return this;
    }

    @Override
    public Set<Resource> getResources(String name, String... excludes) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        if(this.cache) {
            Set<Resource> present = CACHE_LOCAL_RESOURCE.getIfPresent(name);
            if(null != present) {
                return present;
            }
        }

        IPathMatcher pathMatcher = null;
        Set<Resource> result = null;
        //判断是否是classpath:
        try {
            if (name.startsWith(CLASSPATH_URL_PREFIX)) {
                pathMatcher = new ClassPathMatcher(name, excludes, classLoader, atomicInteger);
                result = pathMatcher.matcher();
            } else if(name.startsWith(SUBCLASS_URL_PREFIX)) {
                pathMatcher = new SubClassMatcher(name, excludes, classLoader, atomicInteger);
                result = pathMatcher.matcher();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if(this.cache) {
            if(null == result) {
                result = Collections.emptySet();
            }
            CACHE_LOCAL_RESOURCE.put(name, result);
        }
        return result;
    }

}
