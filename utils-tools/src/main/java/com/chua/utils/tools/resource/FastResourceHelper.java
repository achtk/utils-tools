package com.chua.utils.tools.resource;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.common.UrlHelper;
import com.chua.utils.tools.common.skip.SkipPatterns;
import com.chua.utils.tools.resource.factory.FastResourceFactory;
import com.chua.utils.tools.resource.factory.IResourceFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 快速资源查找器
 * @author CH
 * @since 1.0
 */
@Slf4j
public class FastResourceHelper extends UrlHelper {

    private static final IResourceFactory FAST_RESOURCE_FACTORY = new FastResourceFactory();

    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     * @param names 检索的资源
     * @param excludes 除外的资源(支持通配符)
     * @return
     */
    public static Set<Resource> getResources(String[] names, final String... excludes) {
       return getResources(Lists.newArrayList(names), excludes);
    }
    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     * @param names 检索的资源
     * @param excludes 除外的资源(支持通配符)
     * @return
     */
    public static Set<Resource> getResources(String names, final String... excludes) {
       return getResources(names, null, excludes);
    }
    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     * @param names 检索的资源
     * @param excludes 除外的资源(支持通配符)
     * @return
     */
    public static Set<Resource> getResources(List<String> names, final String... excludes) {
        if(!BooleanHelper.hasLength(names)) {
            return Collections.emptySet();
        }

        Set<Resource> result = new HashSet<>();

        int size = names.size();
        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();
        CountDownLatch countDownLatch = new CountDownLatch(size);

        for (String name : names) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Set<Resource> resources = getResources(name);
                    try {
                        if(null == resources) {
                            return;
                        }
                        result.addAll(resources);
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        return result;
    }
    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     * @param name 检索的资源
     * @return
     */
    public static Set<Resource> getResources(String name) {
        return getResources(name, null, SkipPatterns.JDK_LIB.toArray(new String[SkipPatterns.JDK_LIB.size()]));
    }
    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     * @param name 检索的资源
     * @return
     */
    public static Set<Resource> getResources(String name, ClassLoader classLoader) {
        return getResources(name, classLoader, SkipPatterns.JDK_LIB.toArray(new String[SkipPatterns.JDK_LIB.size()]));
    }
    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     * @param name 检索的资源
     * @param classLoader 检索的类加载器
     * @param excludes 除外的资源(支持通配符)
     * @return
     */
    public static Set<Resource> getResources(String name, ClassLoader classLoader, final String... excludes) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        synchronized (FastResourceHelper.FAST_RESOURCE_FACTORY) {
            if(null != classLoader) {
                FAST_RESOURCE_FACTORY.classLoader(classLoader);
            }
            return FAST_RESOURCE_FACTORY.getResources(name.trim(), excludes);
        }
    }
}
