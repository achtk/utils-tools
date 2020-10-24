package com.chua.utils.tools.spring.scanner;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.spring.definition.ResourceScannerFactoryBeanDefinitionFactory;
import com.google.common.base.Splitter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 资源类路径扫描提供程序
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
@AllArgsConstructor
public class ResourceClassPathScanningProvider {

    private ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider;

    public ResourceClassPathScanningProvider(ResourceLoader resourceLoader, Set<TypeFilter> includeFilter) {
        this(resourceLoader, includeFilter, Collections.emptySet());
    }

    public ResourceClassPathScanningProvider(ResourceLoader resourceLoader, Set<TypeFilter> includeFilter, Set<TypeFilter> excludeFilter) {
        ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new ResourceScannerFactoryBeanDefinitionFactory.InnerClassPathScanningCandidateComponentProvider();
        classPathScanningCandidateComponentProvider.setResourceLoader(resourceLoader);
        for (TypeFilter typeFilter : includeFilter) {
            classPathScanningCandidateComponentProvider.addIncludeFilter(typeFilter);
        }
        for (TypeFilter typeFilter : excludeFilter) {
            classPathScanningCandidateComponentProvider.addExcludeFilter(typeFilter);
        }
        this.classPathScanningCandidateComponentProvider = classPathScanningCandidateComponentProvider;
    }

    /**
     * 设置环境变量
     *
     * @param environment 环境
     * @return
     */
    public ResourceClassPathScanningProvider setEnvironment(Environment environment) {
        this.classPathScanningCandidateComponentProvider.setEnvironment(environment);
        return this;
    }

    /**
     * 设置在扫描类路径时要使用的资源模式。
     *
     * @param resourcePattern 资源模式
     * @return
     */
    public ResourceClassPathScanningProvider setResourcePattern(String resourcePattern) {
        this.classPathScanningCandidateComponentProvider.setResourcePattern(resourcePattern);
        return this;
    }

    /**
     * 设置{@link MetadataReaderFactory}以使用.
     *
     * @param metadataReaderFactory 元数据阅读器工厂
     * @return
     */
    public ResourceClassPathScanningProvider setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
        this.classPathScanningCandidateComponentProvider.setMetadataReaderFactory(metadataReaderFactory);
        return this;
    }

    /**
     * 扫描数据
     *
     * @param mapperLocations 映射器位置
     */
    public void doWithScanner(String mapperLocations, final Matcher<BeanDefinition> matcher) {
        Iterable<String> iterable = Splitter.on(",").trimResults().omitEmptyStrings().split(mapperLocations);
        for (String basePackage : iterable) {
            Set<BeanDefinition> components = classPathScanningCandidateComponentProvider.findCandidateComponents(basePackage);
            components.parallelStream().forEach(new Consumer<BeanDefinition>() {
                @Override
                public void accept(BeanDefinition beanDefinition) {
                    try {
                        matcher.doWith(beanDefinition);
                    } catch (Throwable throwable) {
                        return;
                    }
                }
            });
        }
    }
}
