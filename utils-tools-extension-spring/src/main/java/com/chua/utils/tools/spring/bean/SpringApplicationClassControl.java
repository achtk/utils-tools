package com.chua.utils.tools.spring.bean;

import com.chua.utils.tools.classes.ClassHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * spring class管理器
 *
 * @author CH
 * @since 1.0
 */
@Slf4j
public class SpringApplicationClassControl {

    @Getter
    private IBeanFactory beanFactory;

    public SpringApplicationClassControl(ApplicationContext applicationContext) {
        this.beanFactory = new ApplicationContextBeanFactory(applicationContext);
    }

    public SpringApplicationClassControl(ApplicationContext applicationContext, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanFactory = new BeanDefinitionBeanFactory(applicationContext, beanDefinitionRegistry);
    }

    public SpringApplicationClassControl(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanFactory = new BeanDefinitionBeanFactory(null, beanDefinitionRegistry);
    }

    /**
     * 扫描包下面的类
     * @param packages
     * @return
     */
    public static Set<Class<?>> scanner(final String packages) {
        return scanner(packages, new DefaultResourceLoader());
    }

    /**
     * 扫描包下面的类
     * @param packages
     * @return
     */
    public static Set<Class<?>> scanner(String packages, final ResourceLoader resourceLoader) {
        ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        if(!packages.startsWith("/")) {
            packages = "/" + packages;
        }
        Resource[] resources = null;
        long startTime = 0L;
        if(log.isDebugEnabled()) {
            startTime = System.currentTimeMillis();
        }
        try {
            resources = resourcePatternResolver.getResources("classpath*:" + packages + "/**/*.class");
        } catch (IOException e) {
            return null;
        }

        Set<Class<?>> results = new HashSet<>();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        for (Resource resource : resources) {
            try {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                ClassMetadata classMetadata = metadataReader.getClassMetadata();
                String className = classMetadata.getClassName();
                results.add(ClassHelper.forName(className));
            } catch (IOException e) {
                continue;
            }
        }
        if(log.isDebugEnabled()) {
            log.debug("检索【{}】耗时: {}ms", packages, System.currentTimeMillis() - startTime);
        }
        return results;
    }
}
