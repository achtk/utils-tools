package com.chua.utils.tools.spring.bean;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.spring.entity.BeanLoader;
import com.chua.utils.tools.spring.entity.MappingEntity;
import com.chua.utils.tools.spring.environment.EnvironmentFactory;
import com.chua.utils.tools.spring.mapping.RequestMappingHandlerMappingFactory;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * applicationContext
 * @author CH
 * @since 1.0
 */
@RequiredArgsConstructor
public class ApplicationContextBeanFactory implements IBeanFactory {

    @NonNull
    private final ApplicationContext applicationContext;


    /**
     * 获取环境变量
     */
    @Override
    public EnvironmentFactory environmentFactory() {
        return new EnvironmentFactory(applicationContext);
    }

    @Override
    public String registerController(@NotNull Object entity, String prefix) {
        if (null == applicationContext || !applicationContext.containsBean("requestMappingHandlerMapping")) {
            return null;
        }

        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        MappingEntity mappingEntity = new MappingEntity();
        mappingEntity.setObj(entity);
        mappingEntity.setApplicationContext(applicationContext);
        if (StringHelper.isNotBlank(prefix)) {
            mappingEntity.paths(new String[]{prefix});
        }
        RequestMappingHandlerMappingFactory requestMappingHandlerMappingFactory = new RequestMappingHandlerMappingFactory(requestMappingHandlerMapping);
        requestMappingHandlerMappingFactory.registerMappings(mappingEntity);
        return entity.getClass().getName();
    }

    @Override
    public <T> T getBean(String name) throws BeansException {
        return applicationContext.containsBean(name) ? (T) applicationContext.getBean(name) : null;
    }

    @Override
    public <T> T getBean(Class<T> classes) throws BeansException {
        return applicationContext.getBean(classes);
    }

    @Override
    public <T> BeanLoader<T> getBeans(Class<T> tClass)  {
        if(null == tClass) {
            return null;
        }
        String[] beanNamesForType = applicationContext.getBeanNamesForType(tClass);
        if(!BooleanHelper.hasLength(beanNamesForType)) {
            return null;
        }
        BeanLoader<T> beanLoader = BeanLoader.newLoader();
        for (String s : beanNamesForType) {
            beanLoader.add(tClass, getBean(s, tClass));
        }
        return beanLoader;
    }

    @Override
    public BeanLoader<Object> getBeansFromAnnotation(Class<? extends Annotation> aClass) {
        if(null == aClass) {
            return null;
        }
        try {
            Map<String, Object> annotation = applicationContext.getBeansWithAnnotation(aClass);
            return BeanLoader.newLoader().addAll(aClass, annotation);
        } catch (Throwable e) {
        }
        return null;
    }

    /**
     * 获取bean
     * @param name 类
     * @param <T>
     * @return
     */
    @Override
    public <T>T getBean(final String name, final Class<T> tClass) throws BeansException  {
        if(null == tClass || StringHelper.isBlank(name)) {
            return null;
        }
        return applicationContext.getBean(name, tClass);
    }

    @Override
    public boolean containsBean(Class<?> tClass) {
        if(null == tClass) {
            return false;
        }
        String[] beanNamesForType = applicationContext.getBeanNamesForType(tClass);
        return BooleanHelper.hasLength(beanNamesForType);
    }

    @Override
    public boolean containsBean(final String name) {
        if(StringHelper.isBlank(name)) {
            return false;
        }
        return applicationContext.containsBean(name);
    }

    @Override
    public <T> String[] getBeanNamesForType(Class<T> tClass) {
        if(null == tClass) {
            return null;
        }
        return applicationContext.getBeanNamesForType(tClass);
    }

    /**
     * 查询该类型的所有bean
     * @param tClass 类型
     * @return
     */
    @Override
    public <T> BeanLoader<T> getBeanForType(Class<T> tClass) throws BeansException {
        String[] beanNamesForType =  getBeanNamesForType(tClass);
        if(null == beanNamesForType) {
            return BeanLoader.newLoader();
        }
        BeanLoader<T> beanLoader = BeanLoader.newLoader();
        for (String s : beanNamesForType) {
            T bean = applicationContext.getBean(s, tClass);
            if(null == bean) {
                continue;
            }
            beanLoader.add(tClass, bean);
        }
        return beanLoader;
    }
    @Override
    public <T> Map<String, T> getBeanMap(Class<T> tClass) {
        if(null == tClass) {
            return null;
        }
        String[] beanNamesForType = applicationContext.getBeanNamesForType(tClass);
        if(!BooleanHelper.hasLength(beanNamesForType)) {
            return null;
        }
        Map<String, T> result = Maps.newHashMap();
        for (String s : beanNamesForType) {
            result.put(s, getBean(s, tClass));
        }
        return result;
    }

    @Override
    public String unRegisterBean(@NonNull String beanName) {
        if(!containsBean(beanName)) {
            return null;
        }
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        if(!(autowireCapableBeanFactory instanceof DefaultListableBeanFactory)) {
            return null;
        }
        ((DefaultListableBeanFactory) autowireCapableBeanFactory).removeBeanDefinition(beanName);
        return beanName;
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

        String[] beanNamesForType = getBeanNamesForType(beanClass);
        if(!BooleanHelper.hasLength(beanNamesForType)) {
            for (String s : beanNamesForType) {
                removeBean(s);
            }
        }

        return bean;
    }

    @Override
    public String registerBean(@Nullable String beanName, @NotNull Class<?> beanClass, @Nullable final Map<String, Object> classParams) {
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

        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        if(autowireCapableBeanFactory instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) autowireCapableBeanFactory;
            BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinitionBuilder.getBeanDefinition(), name);
            BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, defaultListableBeanFactory);
        }
        return null;
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
        if(applicationContext.containsBean(name)) {
            return applicationContext.getBean(name);
        }

        try {
            Object bean = applicationContext.getBean(type);
            return bean;
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
        DefaultListableBeanFactory autowireCapableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        if (autowireCapableBeanFactory.containsBean(beanName)) {
            autowireCapableBeanFactory.removeBeanDefinition(beanName);
            return beanName;
        }
        return null;
    }

    /**
     * 获取带有注解的方法
     * @param aClass 注解
     * @return
     */
    public HashMultimap<String, Method> getMethodByAnnotation(Class<? extends Annotation> aClass) {
        HashMultimap<String, Method> result = HashMultimap.create();

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Component.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            Object value = entry.getValue();
            Method[] declaredMethods = value.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                if(method.isAnnotationPresent(aClass)) {
                    result.put(entry.getKey(), method);
                }
            }
        }
        return result;
    }

    /**
     * 方法拆解获取详细方法信息
     * @param aClass 注解
     * @return
     */
    public Map<String, Map<String, Object>> getMethodDisassembleByAnnotation(Class<? extends Annotation> aClass) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Component.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            Object value = entry.getValue();
            //String mapping = getMapping(value);
            Method[] declaredMethods = value.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                if(method.isAnnotationPresent(aClass)) {
                    Map<String, Object> stringObjectMap = ClassHelper.disassembleForMethod(method);
                    stringObjectMap.put("method.mapping", "" + getMapping(method));
                    result.put(entry.getKey(), stringObjectMap);
                }
            }
        }
        return result;
    }

    /**
     * 获取对象上的URL映射
     * @param obj 方法
     * @return
     */
    public String[] getMapping(Object obj) {
        if(null == obj) {
            return new String[0];
        }
        if(obj instanceof Method) {
            return getMethodMapping((Method) obj);
        }

        RequestMapping requestMapping = obj.getClass().getDeclaredAnnotation(RequestMapping.class);
        if(null != requestMapping) {
            return requestMapping.path();
        }

        return new String[0];
    }

    /**
     * 获取方法上的URL映射
     * @param method 方法
     * @return
     */
    private String[] getMethodMapping(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        if(null != requestMapping) {
            return requestMapping.path();
        }

        return new String[0];
    }
}
