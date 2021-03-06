package com.chua.utils.tools.spring.bean;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.spring.entity.BeanLoader;
import com.chua.utils.tools.spring.environment.EnvironmentFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;

import javax.ws.rs.NotSupportedException;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * BeanDefinitionFactory处理工厂
 *
 * @author CH
 * @since 1.0
 */
@RequiredArgsConstructor
public class BeanDefinitionBeanFactory implements IBeanFactory {

    @NonNull
    private BeanDefinitionRegistry beanDefinitionRegistry;

    @Override
    public EnvironmentFactory environmentFactory() {
        return new EnvironmentFactory(beanDefinitionRegistry);
    }

    @Override
    public String registerController(@NonNull Object entity, String prefix) {
        throw new NotSupportedException("");
    }

    @Override
    public <T> T getBean(String name) throws BeansException {
        throw new NotSupportedException("");
    }

    @Override
    public <T> T getBean(Class<T> classes) throws BeansException {
        throw new NotSupportedException("");
    }

    @Override
    public <T> T getBean(String name, Class<T> tClass) throws BeansException {
        throw new NotSupportedException("");
    }

    @Override
    public boolean containsBean(Class<?> tClass) {
        return beanDefinitionRegistry.containsBeanDefinition(tClass.getName());
    }

    @Override
    public boolean containsBean(String name) {
        return beanDefinitionRegistry.containsBeanDefinition(name);
    }

    @Override
    public <T> String[] getBeanNamesForType(Class<T> tClass) {
        throw new NotSupportedException("");
    }

    @Override
    public <T> BeanLoader<T> getBeanForType(Class<T> tClass) throws BeansException {
        throw new NotSupportedException("");
    }

    @Override
    public <T> BeanLoader<T> getBeans(Class<T> tClass) {
        throw new NotSupportedException("");
    }

    @Override
    public BeanLoader<Object> getBeansFromAnnotation(Class<? extends Annotation> aClass) {
        throw new NotSupportedException("");
    }

    @Override
    public <T> Map<String, T> getBeanMap(Class<T> tClass) {
        throw new NotSupportedException("");
    }

    @Override
    public String unRegisterBean(@NonNull String beanName) {
        if (containsBean(beanName)) {
            beanDefinitionRegistry.removeBeanDefinition(beanName);
            return beanName;
        }
        return null;
    }

    @Override
    public String registerBean(@NonNull String beanName, @NonNull Class<?> beanClass, @NonNull Map<String, Object> classParams) {
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
     *
     * @param classParams
     * @param fields
     */
    private void doAnalysisBeanFromApplication(Map<String, Object> classParams, Map<String, Class> fields) {
        for (Map.Entry<String, Class> entry : fields.entrySet()) {
            Object o = classParams.get(entry.getKey());
            if (!classParams.containsKey(entry.getKey()) || null == o) {
                Object o1 = doFieldAnalysisFromApplication(entry.getKey(), entry.getValue());
                classParams.put(entry.getKey(), o1);
            }
        }
    }

    /**
     * 通过名称/类型查找注册bean
     *
     * @param name 名称
     * @param type 类型
     * @return
     */
    private Object doFieldAnalysisFromApplication(String name, Class type) {
        if (String.class.getName().equals(type.getName())) {
            return null;
        }
        if (containsBean(name)) {
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

}
