package com.chua.utils.tools.resource.matcher;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.matcher.ApachePathMatcher;
import com.chua.utils.tools.matcher.PathMatcher;
import com.chua.utils.tools.resource.Resource;
import com.google.common.collect.HashMultimap;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.chua.utils.tools.constant.StringConstant.CLASS_OBJECT;
import static com.chua.utils.tools.constant.StringConstant.EXTENSION_CLASS_SUFFIX;

/**
 * subclass:匹配
 * @author CH
 * @since 1.0
 */
@Slf4j
public class SubClassMatcher implements IPathMatcher {

    private static final PathMatcher ANT_PATH_MATCHER = new ApachePathMatcher();
    private String name;
    private String[] excludes;
    private ClassLoader classLoader;
    private AtomicInteger atomicInteger;
    private IPathMatcher pathMatcher;
    private static final ConcurrentHashMap<String, Set<Resource>> CACHE = new ConcurrentHashMap<>();
    private static final HashMultimap<Class, Resource> HASH_MULTIMAP = MapHelper.newHashMultimap();

    public SubClassMatcher(String name, String[] excludes, ClassLoader classLoader, AtomicInteger atomicInteger) {
        this.name = name;
        this.excludes = excludes;
        this.classLoader = classLoader;
        this.atomicInteger = atomicInteger;
        this.pathMatcher = new ClassPathMatcher("classpath:**/*.class", excludes, classLoader, atomicInteger);
    }

    @Override
    public Set<Resource> matcher() throws Throwable {
        if(HASH_MULTIMAP.size()  == 0) {
            Set<Resource> matcher =  this.pathMatcher.matcher();
            return findAllSubClassAndInCache(matcher);
        } else {
            Class<?> aClass = ClassHelper.forName(name.replace("subclass:", ""));
            return HASH_MULTIMAP.get(aClass);
        }
    }

    /**
     * 缓存所有子类
     * @param matcher
     * @return
     */
    private Set<Resource> findAllSubClassAndInCache(Set<Resource> matcher) throws InterruptedException {
        Class<?> aClass = ClassHelper.forName(name.replace("subclass:", ""));
        if(null == aClass) {
            return null;
        }
        long startTime = 0L;
        if(log.isDebugEnabled()) {
            startTime = System.currentTimeMillis();
        }
        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();
        CountDownLatch countDownLatch = new CountDownLatch(matcher.size());

        for (Resource resource : matcher) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String name = resource.getName();
                    name = StringHelper.startsWithAndEmpty(name, "/");
                    name = name.replace("/", ".").replace(EXTENSION_CLASS_SUFFIX, "");
                    Class<?> aClass = ClassHelper.forName(name);
                    renderCache(aClass, resource);
                    countDownLatch.countDown();
                }

                /**
                 * 渲染缓存
                 * @param aClass
                 * @param resource
                 */
                private void renderCache(Class<?> aClass, Resource resource) {
                    if(null == aClass) {
                        return;
                    }
                    String name = aClass.getSimpleName().toLowerCase();
                    while (!CLASS_OBJECT.equals(name)) {
                        Class<?> superclass = aClass.getSuperclass();
                        if(null == superclass) {
                            break;
                        }
                        Class[] genericInterfaces = aClass.getInterfaces();
                        for (Class genericInterface : genericInterfaces) {
                            HASH_MULTIMAP.put(genericInterface, resource);
                        }
                        HASH_MULTIMAP.put(superclass, resource);
                        aClass = superclass;
                        name = aClass.getSimpleName().toLowerCase();
                    }
                }
            });
        }

        countDownLatch.await();

        executorService.shutdown();
        if(log.isDebugEnabled()) {
            log.debug("检索子类【{}】耗时: {}ms", aClass.getName(), System.currentTimeMillis() - startTime);
        }
        return HASH_MULTIMAP.get(aClass);
    }


}
