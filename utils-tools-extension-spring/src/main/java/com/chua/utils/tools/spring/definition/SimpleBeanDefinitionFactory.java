package com.chua.utils.tools.spring.definition;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SimpleBeanDefinitionFactory 工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/4/28 14:18
 */
@Slf4j
public class SimpleBeanDefinitionFactory<T> implements BeanDefinitionFactory<T> {

    private final Set<String> fieldNames;
    private Object obj;
    private BeanDefinitionRegistry beanDefinitionRegistry;
    private volatile Class<T> tClass;
    protected BeanDefinitionBuilder beanDefinitionBuilder;
    protected String[] aliases;
    private DefaultListableBeanFactory defaultListableBeanFactory;

    private ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Object> lastCacheMap = new ConcurrentHashMap<>();
    private String beanSuffix;
    private String[] aliasName;
    private boolean primary;

    public SimpleBeanDefinitionFactory(Class<T> tClass, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.tClass = tClass;
        this.fieldNames = doAnalyseFields();
        //强制获取DefaultListableBeanFactory, 非DefaultListableBeanFactory直接抛出异常
        this.defaultListableBeanFactory = (DefaultListableBeanFactory) beanDefinitionRegistry;
        this.beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(tClass);
    }

    public SimpleBeanDefinitionFactory(Object obj, Class<T> aClass, BeanDefinitionRegistry registry) {
        this.obj = obj;
        this.tClass = aClass;
        this.fieldNames = doAnalyseFields();
        this.beanDefinitionRegistry = registry;
        //强制获取DefaultListableBeanFactory, 非DefaultListableBeanFactory直接抛出异常
        this.defaultListableBeanFactory = (DefaultListableBeanFactory) beanDefinitionRegistry;
        this.beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(tClass);
    }

    private Set<String> doAnalyseFields() {
        Map<String, Field> fields = ClassHelper.getFieldsAsMap(tClass);
        return fields.keySet();
    }

    public static <T> BeanDefinitionFactory newBeanDefinitionFactory(Class<T> tClass, BeanDefinitionRegistry beanDefinitionRegistry) {
        return new SimpleBeanDefinitionFactory(tClass, beanDefinitionRegistry);
    }

    public static <T> BeanDefinitionFactory newBeanDefinitionFactory(String className, BeanDefinitionRegistry beanDefinitionRegistry) {
        return new SimpleBeanDefinitionFactory(ClassHelper.forName(className), beanDefinitionRegistry);
    }

    @Override
    public BeanDefinitionFactory addPropertyValue(String name, Object value) {
        if (null != value && fieldNames.contains(name)) {
            cacheMap.put(name, value);
        }
        return this;
    }

    @Override
    public BeanDefinitionFactory addLastPropertyValue(String name, Object value) {
        if (null != value && fieldNames.contains(name)) {
            lastCacheMap.put(name, value);
        }
        return this;
    }

    @Override
    public BeanDefinitionFactory addFirstPropertyValue(String name, Object value) {
        if (null != value && fieldNames.contains(name)) {
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
        for (String name : names) {
            addPropertyValue(name, tClass);
        }
        return this;
    }

    @Override
    public void register(String beanName, boolean ignore) {
        AbstractBeanDefinition definition = this.beanDefinitionBuilder.getBeanDefinition();

        if(ignore && !primary) {
            String newName = null == beanName ? tClass.getCanonicalName() : beanName;
            Map<String, ?> ofType = defaultListableBeanFactory.getBeansOfType(tClass);
            if(BooleanHelper.hasLength(ofType)) {
                log.warn("The current bean [{}] already exists, and [primary] is not configured, start to ignore registration", newName);
                log.warn("该类型[{}]已被{}注册", tClass.getName(), ofType.values());
                return;
            }
        }

        if (null == beanName) {
            beanName = uniqueBeanName(beanName);
        }

        if(BooleanHelper.hasLength(cacheMap)) {
            addFirstPropertyValue(cacheMap);
        }

        if(BooleanHelper.hasLength(lastCacheMap)) {
            addFirstPropertyValue(lastCacheMap);
        }

        BeanDefinitionHolder beanDefinitionHolder = null;
        if (BooleanHelper.hasLength(aliases)) {
            beanDefinitionHolder = new BeanDefinitionHolder(definition, beanName, aliases);
        } else {
            beanDefinitionHolder = new BeanDefinitionHolder(definition, beanName);
        }
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, beanDefinitionRegistry);
    }

    /**
     * 获取唯一名称
     *
     * @return
     */
    private String uniqueBeanName(String beanName) {
        String newName = null == beanName ? tClass.getCanonicalName() : beanName;

        int cnt = 0;
        while (beanDefinitionRegistry.containsBeanDefinition(newName)) {
            newName += "#" + (cnt++);
        }
        return newName;
    }

}
