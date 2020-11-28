package com.chua.utils.tools.resource.template;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.cache.GuavaMultiValueCacheProvider;
import com.chua.utils.tools.cache.MultiValueCacheProvider;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.entity.ClassDescription;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.skip.SkipPatterns;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.entity.Resource;
import com.chua.utils.tools.resource.factory.FastResourceFactory;
import com.chua.utils.tools.resource.factory.ResourceFactory;
import com.chua.utils.tools.storage.CacheStorage;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_DOLLAR;

/**
 * 资源模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/18
 */
@Slf4j
@NoArgsConstructor
public class ResourceTemplate {

    private ResourceFactory resourceFactory = new FastResourceFactory();

    private static final String ANY = "classpath:**/*.class";
    /**
     * 子类缓存
     */
    private static final CacheProvider<Class<?>, List<Class<?>>> SUB_TYPE = new ConcurrentCacheProvider<>();
    /**
     * 资源缓存
     */
    private static final CacheProvider<String, Set<Resource>> RESOURCE = new ConcurrentCacheProvider<>();
    /**
     * 子类关系缓存
     */
    private static final MultiValueCacheProvider<String, String> SUB_CACHE = new GuavaMultiValueCacheProvider();
    @Setter
    private ClassLoader classLoader;

    @Setter
    private Matcher<Resource> matcher = resource -> {
    };

    @Setter
    private List<String> excludes = Lists.newArrayList(SkipPatterns.JDK_LIB);


    public ResourceTemplate(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     *
     * @param name 检索的资源
     * @return 资源
     */
    public Set<Resource> getResources(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        synchronized (resourceFactory) {
            if (null != classLoader) {
                resourceFactory.classLoader(classLoader);
            }
            resourceFactory.matcher(matcher);
            return CacheStorage.doWith(() -> resourceFactory.getResources(name.trim(), excludes.toArray(EmptyOrBase.EMPTY_STRING)), name + SYMBOL_DOLLAR + resourceFactory.getClass().getName(), RESOURCE);
        }
    }

    /**
     * 获取子类
     *
     * @param encryptClass 类
     * @return 子类
     */
    public List<Class<?>> getSubOfType(Class<?> encryptClass) {
        return CacheStorage.doWith(() -> {
            long startTime = System.currentTimeMillis();
            Set<Resource> resources = getResources(ANY);
            List<Class<?>> subType = new ArrayList<>();
            if (SUB_CACHE.size() == 0) {
                resources.forEach(resource -> {

                    ClassDescription classDescription = resource.makeClassDescription();
                    if (null == classDescription) {
                        return;
                    }

                    String name = classDescription.getName();
                    String superClass = classDescription.getSuperClass();
                    if (!Strings.isNullOrEmpty(superClass)) {
                        SUB_CACHE.add(superClass, name);
                    }

                    List<String> interfaceNames = classDescription.getInterfaceNames();
                    if (BooleanHelper.hasLength(interfaceNames)) {
                        for (String interfaceName : interfaceNames) {
                            SUB_CACHE.add(interfaceName, name);
                        }
                    }
                });
            }
            List<String> subs = new ArrayList<>();
            subs.add(encryptClass.getName());

            for (int i = 0; i < subs.size(); i++) {
                String sub = subs.get(i);
                Set<String> strings = SUB_CACHE.get(sub);
                if (null != strings) {
                    subs.addAll(strings);
                }
            }
            subs.remove(encryptClass.getName());
            for (String sub : subs) {
                Class<?> aClass = ClassHelper.forName(sub);
                if (null == aClass) {
                    continue;
                }
                subType.add(aClass);
            }
            log.info("Query class [{}] subclass time-consuming: {} ms", encryptClass.getName(), (System.currentTimeMillis() - startTime));
            return subType;
        }, encryptClass, SUB_TYPE);
    }
}
