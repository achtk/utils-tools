package com.chua.utils.tools.spring.context;

import com.chua.utils.tools.spring.definition.BeanDefinitionFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;

/**
 * @author CH
 * @version 1.0.0
 * @className GenericClassPathXmlApplicationContext
 * @since 2020/8/4 15:39
 */
public class GenericClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

    /**
     * 模块属性
     */
    private Properties properties = new Properties();

    @Override
    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
        BeanDefinitionFactory.registerModulePropertiesPlaceHolderConfigurer(beanFactory, properties);
        super.customizeBeanFactory(beanFactory);
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
