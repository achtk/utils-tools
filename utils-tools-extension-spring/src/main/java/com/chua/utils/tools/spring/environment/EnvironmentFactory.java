package com.chua.utils.tools.spring.environment;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.callback.FieldCallback;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.constant.BeanConstant;
import com.chua.utils.tools.prop.placeholder.PropertyPlaceholder;
import com.chua.utils.tools.spring.placeholder.SpringPropertyPlaceholder;
import com.google.common.base.Strings;
import lombok.NoArgsConstructor;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import static com.chua.utils.tools.constant.BeanConstant.BEAN_CONFIGURATION_BEAN_BINDING_POST_PROCESSOR;
import static com.chua.utils.tools.constant.BeanConstant.BEAN_CONFIGURATION_PROPERTIES;

/**
 * 环境变量读取
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/3/24 15:37
 */
@NoArgsConstructor
public class EnvironmentFactory {

    private PropertyPlaceholder placeholderResolver;
    protected BeanFactory beanFactory;
    private TypeConverter typeConverter;
    /**
     * PropertyResolver 集合
     */
    private Environment environment;
    private static String CONFIGURATION_PROPERTIES = BEAN_CONFIGURATION_PROPERTIES;

    public EnvironmentFactory(ApplicationContext applicationContext) {
        this.environment = null == applicationContext ? null : applicationContext.getEnvironment();
        this.beanFactory = null == applicationContext ? null : applicationContext.getAutowireCapableBeanFactory();
        this.placeholderResolver = new SpringPropertyPlaceholder(environment);
    }

    public EnvironmentFactory(ConfigurableListableBeanFactory beanFactory) {
        this.environment = null == beanFactory ? null : beanFactory.getBean(Environment.class);
        this.beanFactory = beanFactory;
        this.placeholderResolver = new SpringPropertyPlaceholder(environment);
    }

    public EnvironmentFactory(Environment environment) {
        this.environment = environment;
        this.placeholderResolver = new SpringPropertyPlaceholder(environment);
    }

    public EnvironmentFactory(BeanDefinitionRegistry beanDefinitionRegistry) {
        if (null != beanDefinitionRegistry && beanDefinitionRegistry instanceof DefaultListableBeanFactory) {
            this.beanFactory = ((DefaultListableBeanFactory) beanDefinitionRegistry);
            this.environment = beanFactory.getBean(Environment.class);
            this.typeConverter = ((DefaultListableBeanFactory) beanDefinitionRegistry).getTypeConverter();
            this.placeholderResolver = new SpringPropertyPlaceholder(environment);
        }
    }

    /**
     * 自动配置
     *
     * @param obj 对象
     * @param <T>
     * @return
     */
    public <T> T autoConfiguration(final T obj) {
        if (null == obj) {
            return null;
        }
        Map<String, Object> annotation = getConfigurationPropertiesAttribute(obj.getClass());
        if (null == annotation) {
            return autoConfigurationByAttribute(obj);
        }
        return autoConfigurationAnnotation(obj, annotation);
    }

    /**
     * 自动配置
     *
     * @param tClass 对象
     * @param <T>
     * @return
     */
    public <T> T autoConfiguration(final Class<T> tClass) {
        if (null == tClass) {
            return null;
        }
        return autoConfiguration(ClassHelper.forObject(tClass));
    }

    /**
     * 通过属性自动装配
     *
     * @param obj        对象
     * @param <T>
     * @param annotation 注解
     * @return
     */
    private <T> T autoConfigurationAnnotation(Object obj, Map<String, Object> annotation) {
        Class<?> aClass = ClassHelper.getClass(obj);
        T newObject = (T) ClassHelper.forObject(aClass);
        String prefix = getPrefix(annotation);

        ClassHelper.doWithFields(aClass, new FieldCallback() {
            @Override
            public void doWith(Field item) throws Throwable {
                setField(item);
            }

            private void setField(Field item) {
                String name = item.getName();
                Class<?> type = item.getType();
                String property = environment.getProperty(prefix + "." + name);
                if (Strings.isNullOrEmpty(property)) {
                    property = environment.getProperty(prefix + "." + StringHelper.humpToLine2(name, "-"));
                }

                if (Strings.isNullOrEmpty(property)) {
                    return;
                }
                ClassHelper.makeAccessible(item);
                try {
                    item.set(newObject, converter(property, item.getType()));
                } catch (Throwable e) {
                }
            }
        });
        return newObject;
    }

    /**
     * @param name
     * @return
     */
    private boolean containsBeanDefinition(String name) {
        if (null != beanFactory) {
            return beanFactory.containsBean(name);
        }
        return false;
    }


    /**
     * @param annotation
     * @return
     */
    private String getPrefix(Map<String, Object> annotation) {
        return annotation.containsKey("prefix") ? annotation.get("prefix").toString() : "";
    }

    /**
     * 通过属性自动装配
     *
     * @param object 类
     * @param <T>
     * @return
     */
    private <T> T autoConfigurationByAttribute(final T object) {
        Class<?> tClass = ClassHelper.getClass(object);
        T newObject = (T) ClassHelper.forObject(tClass);
        ClassHelper.doWithFields(tClass, new FieldCallback() {
            @Override
            public void doWith(Field item) throws Throwable {
                String name = item.getName();
                String property = environment.getProperty(name);
                if (Strings.isNullOrEmpty(property)) {
                    property = environment.getProperty(StringHelper.humpToLine2(name, "-"));
                }

                if (Strings.isNullOrEmpty(property)) {
                    return;
                }
                ClassHelper.makeAccessible(item);
                try {
                    item.set(newObject, converter(property, item.getType()));
                } catch (Throwable e) {
                }
            }
        });
        return newObject;
    }

    /**
     * 数据转化
     *
     * @param property
     * @param type
     * @return
     */
    private Object converter(String property, Class<?> type) {
        if (null == typeConverter) {
            return property;
        }
        return typeConverter.convertIfNecessary(property, type);
    }

    /**
     * 获取注解
     *
     * @param tClass
     * @param <T>
     * @return
     */
    private <T> Map<String, Object> getConfigurationPropertiesAttribute(Class<T> tClass) {
        Annotation[] annotations = tClass.getDeclaredAnnotations();
        Annotation annotationItem = null;
        for (Annotation annotation : annotations) {
            if (!CONFIGURATION_PROPERTIES.equals(annotation.annotationType().getName())) {
                continue;
            }
            annotationItem = annotation;
            break;
        }

        if (null == annotationItem) {
            return null;
        }

        Map<String, Object> map = ClassHelper.getAnnotationValue(annotationItem);
        return map;
    }

    /**
     * 尝试获取参数是否存在
     * <p>1、默认值</p>
     * <p>2、'-'</p>
     *
     * @param name
     * @return
     */
    public boolean tryContainsProperty(String name) {
        return environment.containsProperty(name) ? true : environment.containsProperty(StringHelper.humpToLine2(name, "-"));
    }

    /**
     * 获取值
     * <p>1、默认值</p>
     * <p>2、'-'</p>
     *
     * @param name
     * @return
     */
    public Object tryGetProperty(String name) {
        if (environment.containsProperty(name)) {
            return placeholderResolver.placeholder(environment.getProperty(name));
        }
        String newName = StringHelper.humpToLine2(name, "-");
        if (environment.containsProperty(newName)) {
            return placeholderResolver.placeholder(environment.getProperty(newName));
        }
        return null;
    }

    /**
     * 获取值
     * <p>1、默认值</p>
     * <p>2、'-'</p>
     *
     * @param name
     * @return
     */
    public String tryGetStringProperty(String name) {
        if (environment.containsProperty(name)) {
            Object o = placeholderResolver.placeholder(environment.getProperty(name));
            return o instanceof String ? o.toString() : "";
        }
        String newName = StringHelper.humpToLine2(name, "-");
        if (environment.containsProperty(newName)) {
            Object o = placeholderResolver.placeholder(environment.getProperty(newName));
            return o instanceof String ? o.toString() : "";
        }
        return "";
    }
}
