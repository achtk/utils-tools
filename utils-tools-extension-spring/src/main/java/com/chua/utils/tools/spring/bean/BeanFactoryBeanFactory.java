package com.chua.utils.tools.spring.bean;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.entity.AnnotationInfoProperties;
import com.chua.utils.tools.entity.GetterSetterProperties;
import com.chua.utils.tools.spring.entity.BeanLoader;
import com.chua.utils.tools.spring.enums.BeanStatus;
import com.chua.utils.tools.spring.environment.EnvironmentFactory;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.*;
import org.springframework.core.env.Environment;

import javax.validation.constraints.NotNull;
import javax.ws.rs.NotSupportedException;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * BeanFactory处理工厂
 *
 * @author CH
 * @date 2020-09-26
 */
@NoArgsConstructor
@RequiredArgsConstructor
public class BeanFactoryBeanFactory implements IBeanFactory, SingletonBeanRegistry {

    @NonNull
    private BeanFactory beanFactory;

    @Override
    public EnvironmentFactory environmentFactory() {
        EnvironmentFactory environmentFactory = new EnvironmentFactory(beanFactory.getBean(Environment.class));
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
       // beanClass = doAnalysisGetterSetter(beanClass);
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

    /**
     * @param bean
     * @throws BeanDefinitionStoreException
     */
    public void registerSingleton(Object bean) throws BeanDefinitionStoreException {
        if (null == bean) {
            return;
        }
        registerSingleton(bean.getClass().getName(), bean);
    }

    @Override
    public void registerSingleton(String beanName, Object bean) throws BeanDefinitionStoreException {
        if (!(beanFactory instanceof DefaultListableBeanFactory)) {
            return;
        }
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        defaultListableBeanFactory.registerSingleton(beanName, bean);
    }

    @Override
    public Object getSingleton(String beanName) {
        if (!(beanFactory instanceof DefaultListableBeanFactory)) {
            return null;
        }
        return ((DefaultListableBeanFactory) beanFactory).getSingleton(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        if (!(beanFactory instanceof DefaultListableBeanFactory)) {
            return false;
        }
        return ((DefaultListableBeanFactory) beanFactory).containsSingleton(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        if (!(beanFactory instanceof DefaultListableBeanFactory)) {
            return null;
        }
        return ((DefaultListableBeanFactory) beanFactory).getSingletonNames();
    }

    @Override
    public int getSingletonCount() {
        if (!(beanFactory instanceof DefaultListableBeanFactory)) {
            return 0;
        }
        return ((DefaultListableBeanFactory) beanFactory).getSingletonCount();
    }

    @Override
    public Object getSingletonMutex() {
        if (!(beanFactory instanceof DefaultListableBeanFactory)) {
            return null;
        }
        return ((DefaultListableBeanFactory) beanFactory).getSingletonMutex();
    }

}
