package com.chua.utils.tools.spring.util;

import com.chua.utils.tools.classes.ClassHelper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * spring工具类
 *
 * @author CH
 */
public class SpringUtil {
    /**
     * 获取所有Bean
     *
     * @param packages 包
     * @return
     */
    public static List<Object> findAllBean(final String packages) {
        return findAllBean(new DefaultResourceLoader(), packages);
    }

    /**
     * 获取所有Bean
     *
     * @param packages       包
     * @param resourceLoader 资源加载器
     * @return
     */
    public static List<Object> findAllBean(ResourceLoader resourceLoader, final String packages) {
        List<Object> result = new ArrayList<>();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        Resource[] resources = new Resource[0];
        try {
            resources = resolver.getResources("classpath*:" + packages);
            for (Resource r : resources) {
                MetadataReader reader = metaReader.getMetadataReader(r);
                String className = reader.getClassMetadata().getClassName();
                Object object = ClassHelper.forObject(className);
                if (null == object) {
                    continue;
                }
                result.add(object);
            }
        } catch (IOException e) {
        }


        return result;
    }
}
