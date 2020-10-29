package com.chua.utils.tools.spring.definition;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * BeanDefinition 处理工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public interface BeanDefinitionFactory<T> {
    /**
     * 添加属性
     *
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    BeanDefinitionFactory addPropertyValue(String name, Object value);

    /**
     * 添加属性
     *
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    BeanDefinitionFactory addLastPropertyValue(String name, Object value);

    /**
     * 添加属性
     *
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    BeanDefinitionFactory addFirstPropertyValue(String name, Object value);

    /**
     * 添加属性
     *
     * @param value 属性值
     * @return
     */
    BeanDefinitionFactory addConstructorArgValue(Object value);

    /**
     * 引用已经定义的bean
     *
     * @param name     属性名称
     * @param beanName 已经定义的bean
     * @return
     */
    BeanDefinitionFactory addPropertyReference(String name, String beanName);

    /**
     * 是否是主要的
     *
     * @return
     */
    BeanDefinitionFactory isPrimary();

    /**
     * autowire类型
     *
     * @param autowireMode
     * @return
     * @see org.springframework.beans.factory.support.AbstractBeanDefinition#AUTOWIRE_NO
     * @see org.springframework.beans.factory.support.AbstractBeanDefinition#AUTOWIRE_BY_NAME
     * @see org.springframework.beans.factory.support.AbstractBeanDefinition#AUTOWIRE_BY_TYPE
     * @see org.springframework.beans.factory.support.AbstractBeanDefinition#AUTOWIRE_CONSTRUCTOR
     * @see org.springframework.beans.factory.support.AbstractBeanDefinition#AUTOWIRE_AUTODETECT
     */
    BeanDefinitionFactory setAutowireMode(int autowireMode);

    /**
     * 初始化方法名称
     *
     * @param initMethodName 方法名称
     * @return
     */
    BeanDefinitionFactory setInitMethodName(String initMethodName);

    /**
     * 销毁方法名称
     *
     * @param destroyMethodName 方法名称
     * @return
     */
    BeanDefinitionFactory setDestroyMethodName(String destroyMethodName);

    /**
     * 添加别名
     *
     * @param aliases 别名
     * @return
     */
    BeanDefinitionFactory aliases(String... aliases);

    /**
     * 设置Bean后缀
     *
     * @param suffix 后缀
     * @return
     */
    BeanDefinitionFactory setBeanSuffix(String suffix);

    /**
     * 设置类别名
     *
     * @param names 别名
     * @return
     */
    BeanDefinitionFactory setClassAlias(String... names);

    /**
     * 注册Bean
     *
     * @param beanName bean名称
     * @param ignore   存在是否忽略注册
     */
    void register(String beanName, boolean ignore);


    /**
     * 添加属性
     *
     * @param params 属性
     * @return
     */
    default BeanDefinitionFactory addFirstPropertyValues(Map<String, Object> params) {
        if (!BooleanHelper.hasLength(params)) {
            return this;
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            addFirstPropertyValue(entry.getKey(), entry.getValue());
        }

        return this;
    }

    /**
     * 添加属性
     *
     * @param params 属性
     * @return
     */
    default BeanDefinitionFactory addFirstPropertyValues(List<Map<String, Object>> params) {
        if (!BooleanHelper.hasLength(params)) {
            return this;
        }
        for (Map<String, Object> param : params) {
            addFirstPropertyValues(param);
        }

        return this;
    }

    /**
     * 添加属性
     *
     * @param params       集合
     * @param defaultValue 默认值
     * @param keys         集合索引
     * @return
     */
    default BeanDefinitionFactory addFirstPropertyValue(Map<String, Object> params, Object defaultValue, String... keys) {
        if (!BooleanHelper.hasLength(keys)) {
            return this;
        }
        if (params == null) {
            params = Collections.emptyMap();
        }
        for (String key : keys) {
            if (!params.containsKey(key)) {
                if (null == defaultValue) {
                    continue;
                }
                addFirstPropertyValue(key, defaultValue);
                continue;
            }
            addFirstPropertyValue(key, params.get(key));
        }
        return this;
    }

    /**
     * 添加属性
     *
     * @param params 集合
     * @param keys   集合索引
     * @return
     */
    default BeanDefinitionFactory addFirstPropertyValue(Map<String, Object> params, String... keys) {
        return addFirstPropertyValue(params, null, keys);
    }

    /**
     * 添加属性
     *
     * @param params 属性
     * @return
     */
    default BeanDefinitionFactory addPropertyValues(Map<String, Object> params) {
        if (!BooleanHelper.hasLength(params)) {
            return this;
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            addPropertyValue(entry.getKey(), entry.getValue());
        }

        return this;
    }

    /**
     * 添加属性
     *
     * @param params 属性
     * @return
     */
    default BeanDefinitionFactory addPropertyValues(List<Map<String, Object>> params) {
        if (!BooleanHelper.hasLength(params)) {
            return this;
        }
        for (Map<String, Object> param : params) {
            addPropertyValues(param);
        }

        return this;
    }

    /**
     * 添加属性
     *
     * @param keys 属性
     * @return
     */
    default BeanDefinitionFactory addFirstPropertyValues(String[] keys, Object value) {
        if (!BooleanHelper.hasLength(keys)) {
            return this;
        }
        for (String key : keys) {
            addFirstPropertyValue(key, value);
        }

        return this;
    }

    /**
     * 添加属性
     *
     * @param params       集合
     * @param defaultValue 默认值
     * @param keys         集合索引
     * @return
     */
    default BeanDefinitionFactory addPropertyValue(Map<String, Object> params, Object defaultValue, String... keys) {
        if (!BooleanHelper.hasLength(keys)) {
            return this;
        }
        if (params == null) {
            params = Collections.emptyMap();
        }
        for (String key : keys) {
            if (!params.containsKey(key)) {
                if (null == defaultValue) {
                    continue;
                }
                addPropertyValue(key, defaultValue);
                continue;
            }
            addPropertyValue(key, params.get(key));
        }
        return this;
    }

    /**
     * 添加属性
     *
     * @param params 集合
     * @param keys   集合索引
     * @return
     */
    default BeanDefinitionFactory addPropertyValue(Map<String, Object> params, String... keys) {
        return addPropertyValue(params, null, keys);
    }

    /**
     * 添加属性
     *
     * @param params 属性
     * @return
     */
    default BeanDefinitionFactory addLastPropertyValues(Map<String, Object> params) {
        if (!BooleanHelper.hasLength(params)) {
            return this;
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            addLastPropertyValue(entry.getKey(), entry.getValue());
        }

        return this;
    }

    /**
     * 添加属性
     *
     * @param params 属性
     * @return
     */
    default BeanDefinitionFactory addLastPropertyValues(List<Map<String, Object>> params) {
        if (!BooleanHelper.hasLength(params)) {
            return this;
        }
        for (Map<String, Object> param : params) {
            addLastPropertyValues(param);
        }

        return this;
    }

    /**
     * 添加属性
     *
     * @param params       集合
     * @param defaultValue 默认值
     * @param keys         集合索引
     * @return
     */
    default BeanDefinitionFactory addLastPropertyValue(Map<String, Object> params, Object defaultValue, String... keys) {
        if (!BooleanHelper.hasLength(keys)) {
            return this;
        }
        if (params == null) {
            params = Collections.emptyMap();
        }
        for (String key : keys) {
            if (!params.containsKey(key)) {
                if (null == defaultValue) {
                    continue;
                }
                addLastPropertyValue(key, defaultValue);
                continue;
            }
            addLastPropertyValue(key, params.get(key));
        }
        return this;
    }

    /**
     * 添加属性
     *
     * @param params 集合
     * @param keys   集合索引
     * @return
     */
    default BeanDefinitionFactory addLastPropertyValue(Map<String, Object> params, String... keys) {
        return addLastPropertyValue(params, null, keys);
    }

    /**
     * autowire类型
     *
     * @return
     * @see org.springframework.beans.factory.support.AbstractBeanDefinition#AUTOWIRE_BY_TYPE
     */
    default BeanDefinitionFactory setAutowireMode() {
        return setAutowireMode(org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
    }

    /**
     * 初始化方法名称(默认：init)
     *
     * @return
     */
    default BeanDefinitionFactory setInitMethodName() {
        return setInitMethodName("init");
    }

    /**
     * 销毁方法名称(默认：destroy)
     *
     * @return
     */
    default BeanDefinitionFactory setDestroyMethodName() {
        return setDestroyMethodName("destroy");
    }

    /**
     * 注册Bean
     */
    default void register() {
        register(null);
    }

    /**
     * 注册Bean
     *
     * @param beanName bean名称
     */
    default void register(String beanName) {
        register(beanName, true);
    }

    /**
     * 解释依赖
     *
     * @param tClass 类
     */
    default void resolveDependence(Class<?> tClass) {
        ClassHelper.doWithFields(tClass, field -> {
            Autowired autowired = field.getDeclaredAnnotation(Autowired.class);
            if (null != autowired) {
                Class<?> type = field.getType();
                Qualifier qualifier = field.getDeclaredAnnotation(Qualifier.class);
                if (null == qualifier) {
                    resolveType(field, type);
                } else {
                    resolveName(field, qualifier.value());
                }
            }
            Resource resource = field.getDeclaredAnnotation(Resource.class);
            if (null != resource) {
                String name = resource.name();
                if (Strings.isNullOrEmpty(name)) {
                    Class<?> type = field.getType();
                    resolveType(field, type);
                } else {
                    resolveName(field, name);
                }
            }
        });
    }

    /**
     * 解释类型
     *
     * @param field 字段
     * @param type  类型
     */
    void resolveType(Field field, Class<?> type);
    /**
     * 解释名称
     *
     * @param field 字段
     * @param name  名称
     */
    void resolveName(Field field, String name);
}
