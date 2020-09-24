package com.chua.utils.tools.spring.bean;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.StringHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * @author CH
 * @since 1.0
 */
public class BeanDefinitionBeanFactory implements IBeanFactory {
    
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private IBeanFactory applicationContextBeanFactory;

    public BeanDefinitionBeanFactory(ApplicationContext applicationContext, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.applicationContextBeanFactory = null != applicationContext ? new ApplicationContextBeanFactory(applicationContext) : null;
    }


    @Override
    public Environment environment() {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.environment();
    }

    @Override
    public String registerController(@NotNull Object entity, String prefix) {
        return null == applicationContextBeanFactory ? null : applicationContextBeanFactory.registerController(entity, prefix);
    }

    @Override
    public <T> T getBean(String name) throws BeansException {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> classes) throws BeansException {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.getBean(classes);
    }

    @Override
    public <T> Set<T> getBeans(Class<T> tClass) {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.getBeans(tClass);
    }

    @Override
    public Map<String, Object> getBeansFromAnnotation(Class<? extends Annotation> aClass) {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.getBeansFromAnnotation(aClass);
    }

    @Override
    public <T> T getBean(String name, Class<T> tClass) throws BeansException {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.getBean(name, tClass);
    }

    @Override
    public boolean containsBean(Class<?> tClass) {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.containsBean(tClass);
    }

    @Override
    public boolean containsBean(String name) {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.containsBean(name);
    }

    @Override
    public <T> String[] getBeanNamesForType(Class<T> tClass) {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.getBeanNamesForType(tClass);
    }

    @Override
    public <T> Set<T> getBeanForType(Class<T> tClass) throws BeansException {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.getBeanForType(tClass);
    }

    @Override
    public <T> Map<String, T> getBeanMap(Class<T> tClass) {
        return null == applicationContextBeanFactory ? null : this.applicationContextBeanFactory.getBeanMap(tClass);
    }

    @Override
    public String removeBean(@NotNull Class<?> beanClass) {
        String name = beanClass.getName();
        String bean = removeBean(name);
        if (null == bean) {
            bean = removeBean(StringHelper.firstLowerCase(name));
        }
        if (null == bean) {
            name = beanClass.getSimpleName();
            bean = removeBean(name);
        }
        if (null == bean) {
            name = beanClass.getSimpleName();
            bean = removeBean(StringHelper.firstLowerCase(name));
        }
        return bean;
    }

    @Override
    public String registerBean(@Nullable String beanName, @NotNull Class<?> beanClass, @Nullable Map<String, Object> classParams) {
        //获取所有属性名称
        Map<String, Class> fields = doAnalysisBeanFields(beanClass);
        //创建 BeanDefinitionBuilder
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        //从上下文解析属性
        doAnalysisBeanFromApplication(classParams, fields);

        if (null != classParams && null != fields) {
            for (String field : fields.keySet()) {
                beanDefinitionBuilder.addPropertyValue(field, classParams.get(field));
            }
        }

        String name = uniqueBeanName(beanName);

        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinitionBuilder.getBeanDefinition(), name);
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, beanDefinitionRegistry);
        return name;
    }


    /**
     * 从上下文解析属性
     * @param classParams
     * @param fields
     */
    private void doAnalysisBeanFromApplication(Map<String, Object> classParams, Map<String, Class> fields) {
        for (Map.Entry<String, Class> entry : fields.entrySet()) {
            Object o = classParams.get(entry.getKey());
            if(!classParams.containsKey(entry.getKey()) || null == o) {
                Object o1 = doFieldAnalysisFromApplication(entry.getKey(), entry.getValue());
                classParams.put(entry.getKey(), o1);
            }
        }
    }

    /**
     * 通过名称/类型查找注册bean
     * @param name 名称
     * @param type 类型
     * @return
     */
    private Object doFieldAnalysisFromApplication(String name, Class type) {
        if(String.class.getName().equals(type.getName())) {
            return null;
        }
        if(beanDefinitionRegistry.containsBeanDefinition(name)) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(name);
            return ClassHelper.forObject(beanDefinition.getBeanClassName());
        }

        try {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(type.getName());
            return ClassHelper.forObject(beanDefinition.getBeanClassName());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 注销bean
     *
     * @param beanName           bean名称
     * @return bean名称
     */
    public String removeBean(@NotNull String beanName) {
        if (beanDefinitionRegistry.containsBeanDefinition(beanName)) {
            beanDefinitionRegistry.removeBeanDefinition(beanName);
            return beanName;
        }
        return null;
    }

    /**
     * 获取唯一值
     *
     * @param beanName               bean名称
     * @return
     */
    public String uniqueBeanName(@NotBlank String beanName) {
        int cnt = 0;
        while (beanDefinitionRegistry.containsBeanDefinition(beanName)) {
            beanName += "#" + (cnt++);
        }
        return beanName;
    }
}
