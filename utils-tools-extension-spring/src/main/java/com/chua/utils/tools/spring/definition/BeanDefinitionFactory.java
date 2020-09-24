package com.chua.utils.tools.spring.definition;

import com.chua.utils.tools.common.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.Map;
import java.util.Properties;

/**
 * BeanDefinitionF 工具类
 * @author CH
 * @version 1.0.0
 * @since 2020/4/28 14:18
 */
@Slf4j
public class BeanDefinitionFactory {

    private static final String PROPERTY_PLACEHOLDER_CONFIGURER = "propertyPlaceholderConfigurer";

    private Class<?> aClass;
    private Map<String, Object> params;
    private ApplicationContext applicationContext;
    private String uniqueName;

    public BeanDefinitionFactory(Class<?> aClass) {
        this.aClass = aClass;
    }

    public BeanDefinitionFactory(Class<?> aClass, Map<String, Object> params) {
        this.aClass = aClass;
        this.params = params;
    }

    public BeanDefinitionFactory(Class<?> aClass, Map<String, Object> params, ApplicationContext applicationContext) {
        this.aClass = aClass;
        this.params = params;
        this.applicationContext = applicationContext;
    }

    /**
     * 获取唯一名称
     * @return
     */
    public String uniqueName(String name) {
        if(null == aClass) {
            return null;
        }

        if(StringHelper.isBlank(name)) {
            name = aClass.getName();
        }
        if(null != applicationContext) {
            int count = 0;
            while (applicationContext.containsBean(name)) {
                name += "#" + count;
            }
        } else {
            name += "#" + System.currentTimeMillis();
        }
        this.uniqueName = name;
        return name;
    }

    /**
     * 获取 BeanDefinitionBuilder
     * @return
     */
    public BeanDefinitionBuilder genericBeanDefinitionBuilder() {
        return null == params ?
                BeanDefinitionBuilder.genericBeanDefinition(aClass):
                genericBeanDefinitionBuilder(params);
    }

    /**
     * 获取BeanDefinitionBuilder
     * @param params 属性
     * @return
     */
    public BeanDefinitionBuilder genericBeanDefinitionBuilder(Map<String, Object> params) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
        if(null != params) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                beanDefinitionBuilder.addPropertyValue(entry.getKey(), entry.getValue());
            }
        }
        return beanDefinitionBuilder;
    }

    /**
     * 获取 BeanDefinition
     * @return
     */
    public AbstractBeanDefinition genericBeanDefinition() {
        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinitionBuilder();
        if(null == beanDefinitionBuilder) {
            return null;
        }
        return beanDefinitionBuilder.getBeanDefinition();
    }
    /**
     * 获取 BeanDefinition
     * @return
     */
    public BeanDefinitionHolder genericBeanDefinitionHolder(String name) {
        AbstractBeanDefinition abstractBeanDefinition = genericBeanDefinition();
        if(null == abstractBeanDefinition) {
            return null;
        }
        return new BeanDefinitionHolder(abstractBeanDefinition, uniqueName(StringHelper.defaultIfBlank(name, aClass.getName())));
    }
    /**
     * 注册
     * @return
     */
    public String register() {
        return register(null);
    }
    /**
     * 注册
     * @param name 名称
     * @return
     */
    public String register(String name) {
        if(null == applicationContext) {
            log.warn("Application不存在忽略注册");
            return null;
        }
        BeanDefinitionHolder beanDefinitionHolder = genericBeanDefinitionHolder(name);
        if(null == beanDefinitionHolder) {
            return null;
        }

        if(applicationContext instanceof GenericWebApplicationContext) {
            GenericWebApplicationContext genericWebApplicationContext = (GenericWebApplicationContext) applicationContext;
            BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, genericWebApplicationContext);
            return this.uniqueName;
        }
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, defaultListableBeanFactory);
        return this.uniqueName;
    }

    public static BeanDefinitionRegistry registerModulePropertiesPlaceHolderConfigurer(BeanDefinitionRegistry beanFactory,
                                                                                       Properties properties) {
        //通过GenericBeanDefinition创建bean
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        //设置bean资源属性的配置器
        beanDefinition.setBeanClass(PropertyPlaceholderConfigurer.class);
        //配置PropertyPlaceholderConfigurer的properties属性
        beanDefinition.getPropertyValues().add("properties", properties);
        if (log.isInfoEnabled()) {
            log.info("Register: {}", beanDefinition);
        }
        //注册bean
        beanFactory.registerBeanDefinition(PROPERTY_PLACEHOLDER_CONFIGURER, beanDefinition);
        return beanFactory;
    }
}
