package com.chua.utils.tools.spring.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * spring资源文件查询工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/4/27 9:21
 */
@Slf4j
public class SpringResourceHelper {

    private static final String RESOURCE_PATTERN = "/**/*.class";

    /**
     * 查询spring资源文件
     *
     * @param locationPattern 查询规则
     * @return
     */
    public static Set<Class<?>> findPathMatchingResources(String locationPattern) {
        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(locationPattern) + RESOURCE_PATTERN;
            ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
            Resource[] source = resourceLoader.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourceLoader);
            for (Resource resource : source) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    classes.add(Class.forName(className));
                }
            }
        } catch (Exception e) {
            log.error("寻找符合条件的包失败", e);
        }
        return classes;
    }
}
