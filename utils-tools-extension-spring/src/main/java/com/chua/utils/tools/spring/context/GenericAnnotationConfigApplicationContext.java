package com.chua.utils.tools.spring.context;

import com.chua.utils.tools.spring.definition.BeanDefinitionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Properties;

/**
 * @author CH
 * @version 1.0.0
 * @className GenericAnnotationConfigApplicationContext
 * @since 2020/8/4 15:35
 */
public class GenericAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext {
    /**
     *
     * @param properties
     */
    public GenericAnnotationConfigApplicationContext(Properties properties) {
        Properties springProperties = (properties == null) ? new Properties() : properties;
        BeanDefinitionFactory.registerModulePropertiesPlaceHolderConfigurer(this, springProperties);
    }
}
