package com.chua.utils.tools.spring.bean;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.CollectionHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.entity.AnnotationInfoProperties;
import com.chua.utils.tools.entity.GetterSetterProperties;
import com.chua.utils.tools.spring.entity.BeanLoader;
import com.chua.utils.tools.spring.enums.BeanStatus;
import com.chua.utils.tools.spring.environment.EnvironmentFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.support.ResourceEditorRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.web.context.support.StandardServletEnvironment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.NotSupportedException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * BeanFactory处理工厂
 *
 * @author CH
 * @date 2020-09-26
 */
@RequiredArgsConstructor
public class BeanFactoryBeanFactory implements IBeanFactory {

    @NonNull
    private BeanFactory beanFactory;

    @Override
    public EnvironmentFactory environmentFactory() {
        EnvironmentFactory environmentFactory = new EnvironmentFactory();
        if(!(beanFactory instanceof DefaultListableBeanFactory)) {
            return environmentFactory;
        }
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        Set<PropertyEditorRegistrar> propertyEditorRegistrars = defaultListableBeanFactory.getPropertyEditorRegistrars();
        Set<ResourceEditorRegistrar> resourceEditorRegistrars = CollectionHelper.filter(propertyEditorRegistrars, ResourceEditorRegistrar.class);
        for (ResourceEditorRegistrar resourceEditorRegistrar : resourceEditorRegistrars) {
            PropertyResolver propertyResolver = ClassHelper.getOnlyFieldValue(resourceEditorRegistrar, PropertyResolver.class);
            if(null == propertyResolver) {
                return environmentFactory;
            }
            if(!(propertyResolver instanceof StandardServletEnvironment)) {
                return environmentFactory;
            }
            environmentFactory.absorb((StandardServletEnvironment) propertyResolver);
        }
        return environmentFactory;
    }

    @Override
    public String registerController(@NotNull Object entity, String prefix) {
        throw new NotSupportedException("");
    }

    @Override
    public <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> classes) throws BeansException {
        return beanFactory.getBean(classes);
    }

    @Override
    public <T> T getBean(String name, Class<T> tClass) throws BeansException {
        return beanFactory.getBean(name, tClass);
    }

    @Override
    public boolean containsBean(Class<?> tClass) {
        return beanFactory.containsBean(tClass.getName());
    }

    @Override
    public boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    @Override
    public <T> String[] getBeanNamesForType(Class<T> tClass) {
        return beanFactory instanceof DefaultListableBeanFactory ? ((DefaultListableBeanFactory) beanFactory).getBeanNamesForType(tClass) : null;
    }

    @Override
    public <T> BeanLoader<T> getBeanForType(Class<T> tClass) throws BeansException {
        BeanLoader<T> beanLoader = BeanLoader.newLoader();

        String[] namesForType = getBeanNamesForType(tClass);
        if (!BooleanHelper.hasLength(namesForType)) {
            return null;
        }
        for (String s : namesForType) {
            T bean = null;
            try {
                bean = getBean(s, tClass);
            } catch (BeansException e) {
                beanLoader.throwable(s, BeanStatus.OTHER_ABNORMAL_INFORMATION);
                continue;
            }
            if (null == bean) {
                beanLoader.throwable(s, BeanStatus.BEAN_NOT_EXIST);
                continue;
            }
            beanLoader.add(tClass, bean);
        }
        return beanLoader;
    }

    @Override
    public <T> BeanLoader<T> getBeans(Class<T> tClass) {
        return getBeanForType(tClass);
    }

    @Override
    public BeanLoader<Object> getBeansFromAnnotation(Class<? extends Annotation> aClass) {
        Map<String, Object> annotation = beanFactory instanceof DefaultListableBeanFactory ? ((DefaultListableBeanFactory) beanFactory).getBeansWithAnnotation(aClass) : null;
        BeanLoader<Object> beanLoader = BeanLoader.newLoader();
        beanLoader.addAll(aClass, annotation);
        return beanLoader;
    }

    @Override
    public <T> Map<String, T> getBeanMap(Class<T> tClass) {
        return beanFactory instanceof DefaultListableBeanFactory ? ((DefaultListableBeanFactory) beanFactory).getBeansOfType(tClass) : null;
    }

    @Override
    public String unRegisterBean(@NonNull String beanName) {
        if (containsBean(beanName)) {
            if (beanFactory instanceof DefaultListableBeanFactory) {
                ((DefaultListableBeanFactory) beanFactory).removeBeanDefinition(beanName);
                return beanName;
            }
        }
        return null;
    }

    @Override
    public String registerBean(String beanName, @NotNull Class<?> beanClass, @NonNull Map<String, Object> classParams) {
        //获取所有属性名称
        Map<String, Class> fields = doAnalysisBeanFields(beanClass);
        //Getter and Setter分析
        beanClass = doAnalysisGetterSetter(beanClass);
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
        if (beanFactory instanceof DefaultListableBeanFactory) {
            BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, (BeanDefinitionRegistry) beanFactory);
            return name;
        }
        return null;
    }

    /**
     * Getter and Setter分析
     *
     * @param beanClass 类
     */
    private Class<?> doAnalysisGetterSetter(Class<?> beanClass) {
        try {
            GetterSetterProperties getterAndSetter = ClassHelper.doAnalyzeGetterAndSetter(beanClass, true, new AnnotationInfoProperties(Autowired.class.getName(), 1, "*"));
            if (getterAndSetter.isEmpty()) {
                return beanClass;
            }
            return getterAndSetter.getRecordClass();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return beanClass;
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
        if (containsBean(name) && beanFactory instanceof DefaultListableBeanFactory) {
            BeanDefinition beanDefinition = ((DefaultListableBeanFactory) beanFactory).getBeanDefinition(name);
            return ClassHelper.forObject(beanDefinition.getBeanClassName());
        }

        if (beanFactory instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
            try {
                BeanDefinition beanDefinition = defaultListableBeanFactory.getBeanDefinition(type.getName());
                return ClassHelper.forObject(beanDefinition.getBeanClassName());
            } catch (Throwable e) {
                Map beans = defaultListableBeanFactory.getBeansOfType(type);
                for (Object value : beans.values()) {
                    if (value.getClass().isAssignableFrom(type)) {
                        return value;
                    }
                }
            }
        }

        return null;
    }

}