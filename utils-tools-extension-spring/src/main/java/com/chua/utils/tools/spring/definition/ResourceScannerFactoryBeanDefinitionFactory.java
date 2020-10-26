package com.chua.utils.tools.spring.definition;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.spring.scanner.ResourceClassPathScanningProvider;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 资源Bean定义工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/23
 */
@Slf4j
public class ResourceScannerFactoryBeanDefinitionFactory implements BeanDefinitionFactory<FactoryBean> {

    private DefaultListableBeanFactory defaultListableBeanFactory;
    private BeanDefinitionRegistry beanDefinitionRegistry;
    private BeanDefinitionBuilder beanDefinitionBuilder;
    private ResourceClassPathScanningProvider resourceClassPathScanningProvider;
    private Class<? extends FactoryBean> factoryBean;
    private String packages;
    private ClassPathScanningCandidateComponentProvider componentProvider;
    private String[] aliases;

    private ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Object> lastCacheMap = new ConcurrentHashMap<>();
    private String beanSuffix;
    private String[] aliasNames;
    private Map<String, Field> fields;
    private boolean primary;

    public ResourceScannerFactoryBeanDefinitionFactory(Class<? extends FactoryBean> factoryBean, String packages, BeanDefinitionRegistry beanDefinitionRegistry, ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider) {
        this.factoryBean = factoryBean;
        this.packages = packages;
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        //强制获取DefaultListableBeanFactory, 非DefaultListableBeanFactory直接抛出异常
        this.defaultListableBeanFactory = (DefaultListableBeanFactory) beanDefinitionRegistry;
        this.fields = ClassHelper.getFieldsAsMap(factoryBean);
        this.componentProvider = classPathScanningCandidateComponentProvider;
        this.resourceClassPathScanningProvider = new ResourceClassPathScanningProvider(componentProvider);
        this.beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(factoryBean);
    }

    public ResourceScannerFactoryBeanDefinitionFactory(Class<? extends FactoryBean> factoryBean, String packages, BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader, TypeFilter includeFilter) {
        this.factoryBean = factoryBean;
        this.packages = packages;
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.fields = ClassHelper.getFieldsAsMap(factoryBean);
        this.defaultListableBeanFactory = (DefaultListableBeanFactory) beanDefinitionRegistry;
        this.componentProvider = newClassPathScanningCandidateComponentProvider(resourceLoader, includeFilter);
        this.resourceClassPathScanningProvider = new ResourceClassPathScanningProvider(componentProvider);
        this.beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(factoryBean);
    }

    @Override
    public BeanDefinitionFactory addPropertyValue(String name, Object value) {
        if (null != value && fields.containsKey(name)) {
            Field field = fields.get(name);
            if(!field.getType().isAssignableFrom(value.getClass())) {
                return this;
            }
            cacheMap.put(name, value);
        }
        return this;
    }

    @Override
    public BeanDefinitionFactory addLastPropertyValue(String name, Object value) {
        if (null != value && fields.containsKey(name)) {
            Field field = fields.get(name);
            if(!field.getType().isAssignableFrom(value.getClass())) {
                return this;
            }
            lastCacheMap.put(name, value);
        }
        return this;
    }

    @Override
    public BeanDefinitionFactory addFirstPropertyValue(String name, Object value) {
        if (null != value && fields.containsKey(name)) {
            Field field = fields.get(name);
            if(!field.getType().isAssignableFrom(value.getClass())) {
                return this;
            }
            if(String.class.isAssignableFrom(field.getType()) && "".equals(value)) {
                return this;
            }
            this.beanDefinitionBuilder.addPropertyValue(name, value);
        }
        return this;
    }

    @Override
    public BeanDefinitionFactory addConstructorArgValue(Object value) {
        this.beanDefinitionBuilder.addConstructorArgValue(value);
        return this;
    }

    @Override
    public BeanDefinitionFactory addPropertyReference(String name, String beanName) {
        this.beanDefinitionBuilder.addPropertyReference(name, beanName);
        return this;
    }

    @Override
    public BeanDefinitionFactory isPrimary() {
        this.primary = true;
        this.beanDefinitionBuilder.setPrimary(true);
        return this;
    }

    @Override
    public BeanDefinitionFactory setAutowireMode(int autowireMode) {
        this.beanDefinitionBuilder.setAutowireMode(autowireMode);
        return this;
    }

    @Override
    public BeanDefinitionFactory setInitMethodName(String initMethodName) {
        if (!Strings.isNullOrEmpty(initMethodName)) {
            beanDefinitionBuilder.setInitMethodName(initMethodName);
        }
        return this;
    }

    @Override
    public BeanDefinitionFactory setDestroyMethodName(String destroyMethodName) {
        if (!Strings.isNullOrEmpty(destroyMethodName)) {
            beanDefinitionBuilder.setDestroyMethodName(destroyMethodName);
        }
        return this;
    }

    @Override
    public BeanDefinitionFactory aliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }

    @Override
    public BeanDefinitionFactory setBeanSuffix(String suffix) {
        this.beanSuffix = null == suffix ? "" : suffix;
        return this;
    }

    @Override
    public BeanDefinitionFactory setClassAlias(String... names) {
        this.aliasNames = names;
        return this;
    }

    @Override
    public void register(String beanName, boolean ignore) {
        resourceClassPathScanningProvider.doWithScanner(packages, new Matcher<BeanDefinition>() {
            @Override
            public void doWith(BeanDefinition beanDefinition) throws Throwable {
                if (beanDefinition instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
                    AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();

                    Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
                    List<Map<String, Object>> mapList = new ArrayList<>(annotationTypes.size());

                    for (String annotationType : annotationTypes) {
                        mapList.add(annotationMetadata.getAnnotationAttributes(annotationType));
                    }
                    registerBean(annotationMetadata, mapList, ignore);
                }
            }
        });
    }

    /**
     * 注册Bean
     *  @param annotationMetadata
     * @param mapList
     * @param ignore
     */
    protected void registerBean(AnnotationMetadata annotationMetadata, List<Map<String, Object>> mapList, boolean ignore) {
        String className = annotationMetadata.getClassName();
        Class<?> aClass = ClassHelper.forName(className);
        if(log.isDebugEnabled()) {
            log.debug("扫描类[{}]开始处理类", className);
        }
        addPropertyValue("type", aClass);
        addPropertyValue("className", className);

        AbstractBeanDefinition definition = this.beanDefinitionBuilder.getBeanDefinition();

        if(ignore && !primary) {
            Map<String, ?> ofType = defaultListableBeanFactory.getBeansOfType(aClass);
            if(BooleanHelper.hasLength(ofType)) {
                log.warn("The current bean [{}] already exists, and [primary] is not configured, start to ignore registration", className);
                log.warn("该类型[{}]已被{}注册", aClass.getName(), ofType.values());
                return;
            }
        }

        String beanName = className;
        if (null == className) {
            beanName = uniqueBeanName(className);
        }
        beanName += beanSuffix;

        if (BooleanHelper.hasLength(cacheMap)) {
            addFirstPropertyValues(cacheMap);
        }

        if (BooleanHelper.hasLength(lastCacheMap)) {
            addFirstPropertyValues(lastCacheMap);
        }

        if (BooleanHelper.hasLength(aliasNames)) {
            addFirstPropertyValues(aliasNames, aClass);
        }

        addFirstPropertyValues(mapList);

        BeanDefinitionHolder beanDefinitionHolder = null;
        if (BooleanHelper.hasLength(aliases)) {
            beanDefinitionHolder = new BeanDefinitionHolder(definition, beanName, aliases);
        } else {
            beanDefinitionHolder = new BeanDefinitionHolder(definition, beanName);
        }
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, beanDefinitionRegistry);
    }

    /**
     * @param resourceLoader
     * @param includeFilter
     * @return
     */
    private ClassPathScanningCandidateComponentProvider newClassPathScanningCandidateComponentProvider(ResourceLoader resourceLoader, TypeFilter includeFilter) {
        ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new InnerClassPathScanningCandidateComponentProvider();
        classPathScanningCandidateComponentProvider.setResourceLoader(resourceLoader);
        classPathScanningCandidateComponentProvider.addIncludeFilter(includeFilter);
        return classPathScanningCandidateComponentProvider;
    }

    /**
     * 获取唯一名称
     *
     * @return
     */
    private String uniqueBeanName(String beanName) {
        String newName = null == beanName ? factoryBean.getCanonicalName() : beanName;

        int cnt = 0;
        while (beanDefinitionRegistry.containsBeanDefinition(newName)) {
            newName += "_" + (cnt++);
        }
        return newName;
    }


    /**
     * 内部类路径扫描候选组件提供者
     */
    @Slf4j
    public static class InnerClassPathScanningCandidateComponentProvider extends ClassPathScanningCandidateComponentProvider {

        public List<TypeFilter> getTypeFilters() {
            return MapHelper.getIfOnly(ClassHelper.getFieldValue(this, "includeFilters"), List.class);
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            if (beanDefinition.getMetadata().isIndependent()) {
                if (beanDefinition.getMetadata().isInterface()
                        && beanDefinition.getMetadata().getInterfaceNames().length == 1
                        && Annotation.class.getName().equals(beanDefinition.getMetadata().getInterfaceNames()[0])) {

                    try {
                        Class<?> target = ClassUtils.forName(
                                beanDefinition.getMetadata().getClassName(),
                                ClassHelper.getDefaultClassLoader());
                        return !target.isAnnotation();
                    } catch (Exception ex) {
                        log.error("Could not load target class: {}", beanDefinition.getMetadata().getClassName(), ex);

                    }

                }
                return true;
            }
            return false;
        }

    }
}
