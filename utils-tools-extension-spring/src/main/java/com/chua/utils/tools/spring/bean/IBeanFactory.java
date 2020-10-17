package com.chua.utils.tools.spring.bean;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.spring.entity.BeanLoader;
import com.chua.utils.tools.spring.environment.EnvironmentFactory;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.core.env.Environment;

import javax.validation.constraints.NotBlank;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * bean工厂
 *
 * @author CH
 * @since 1.0
 */
interface IBeanFactory {
    /**
     * @return
     */
    EnvironmentFactory environmentFactory();

    /**
     * 注册controller
     *
     * @param entity 实体
     * @param prefix 前缀
     * @return
     */
    String registerController(@NonNull Object entity, String prefix);

    /**
     * 获取bean
     *
     * @param name 名称
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T getBean(final String name) throws BeansException;

    /**
     * 获取bean
     *
     * @param classes 名称
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T getBean(final Class<T> classes) throws BeansException;

    /**
     * 获取bean
     *
     * @param name   名称
     * @param tClass 类型
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T getBean(final String name, final Class<T> tClass) throws BeansException;

    /**
     * 是否包含Bean
     *
     * @param tClass
     * @return
     */
    boolean containsBean(final Class<?> tClass);

    /**
     * 是否包含Bean
     *
     * @param name
     * @return
     */
    boolean containsBean(final String name);

    /**
     * 获取bean名称
     *
     * @param tClass
     * @param <T>
     * @return
     */
    <T> String[] getBeanNamesForType(Class<T> tClass);

    /**
     * 查询该类型的所有bean
     *
     * @param tClass 类型
     * @return
     */
    <T> BeanLoader<T> getBeanForType(Class<T> tClass) throws BeansException;

    /**
     * 获取所有bean
     *
     * @param tClass
     * @param <T>
     * @return
     */
    <T> BeanLoader<T> getBeans(Class<T> tClass);

    /**
     * 通过注解获取Bean
     *
     * @param aClass
     * @return
     */
    BeanLoader<Object> getBeansFromAnnotation(Class<? extends Annotation> aClass);

    /**
     * 获取类中的属性
     *
     * @param tClass 类
     * @param <T>
     * @return
     */
    <T> Map<String, T> getBeanMap(Class<T> tClass);

    /**
     * 删除Bean
     *
     * @param beanName bean名称
     * @return
     */
    String unRegisterBean(@NonNull String beanName);

    /**
     * 注册Bean
     *
     * @param beanName    名称
     * @param beanClass   类
     * @param classParams 参数
     * @return
     */
    String registerBean(@NonNull String beanName, @NonNull Class<?> beanClass, @NonNull final Map<String, Object> classParams);

    /**
     * 生成bean名称，防止重名使用
     *
     * @param beanName bean名称
     * @return
     */
    default String uniqueBeanName(@NotBlank String beanName) {
        int cnt = 0;
        while (containsBean(beanName)) {
            beanName += "#" + (cnt++);
        }
        return beanName;
    }

    /**
     * 删除Bean
     *
     * @param beanClass bean类
     * @return
     */
    default String removeBean(@NonNull Class<?> beanClass) {
        String name = beanClass.getName();
        String bean = unRegisterBean(name);
        if (null == bean) {
            bean = unRegisterBean(StringHelper.firstLowerCase(name));
        }
        if (null == bean) {
            name = beanClass.getSimpleName();
            bean = unRegisterBean(name);
        }
        if (null == bean) {
            name = beanClass.getSimpleName();
            bean = unRegisterBean(StringHelper.firstLowerCase(name));
        }
        return bean;
    }

    /**
     * <p>不存在注册Bean</p>
     * <p>存在不注册</p>
     *
     * @param beanName  名称
     * @param beanClass 类
     * @return
     */
    default String ifNotContainerRegisterBean(@NonNull String beanName, @NonNull Class<?> beanClass, @NonNull final Map<String, Object> classParams) {
        if (containsBean(beanName)) {
            return beanName;
        }
        if (containsBean(beanClass)) {
            return getBean(beanClass).getClass().getName();
        }
        return registerBean(beanName, beanClass, classParams);
    }

    /**
     * <p>不存在注册Bean</p>
     * <p>存在不注册</p>
     *
     * @param entity 对象
     * @return
     */
    default <T> String ifNotContainerRegisterBean(@NonNull String beanName, @NonNull T entity, @NonNull final Map<String, Object> classParams) {
        if (null == entity) {
            return null;
        }
        Map<String, Object> maps = ClassHelper.getFieldsValueAsMap(entity);
        if (null != maps) {
            classParams.putAll(maps);
        }
        return ifNotContainerRegisterBean(beanName, entity.getClass(), classParams);
    }

    /**
     * <p>不存在注册Bean</p>
     * <p>存在不注册</p>
     *
     * @param entity 对象
     * @return
     */
    default <T> String ifNotContainerRegisterBean(@NonNull String beanName, @NonNull T entity) {
        if (null == entity) {
            return null;
        }
        Map<String, Object> maps = ClassHelper.getFieldsValueAsMap(entity);
        return ifNotContainerRegisterBean(beanName, entity.getClass(), maps);
    }

    /**
     * <p>不存在注册Bean</p>
     * <p>存在不注册</p>
     *
     * @param entity 对象
     * @return
     */
    default <T> String ifNotContainerRegisterBean(@NonNull T entity) {
        if (null == entity) {
            return null;
        }
        Map<String, Object> maps = ClassHelper.getFieldsValueAsMap(entity);
        return ifNotContainerRegisterBean(entity.getClass().getName(), entity.getClass(), maps);
    }

    /**
     * <p>不存在注册Bean</p>
     * <p>存在不注册</p>
     *
     * @param entity 对象
     * @return
     */
    default <T> String ifNotContainerRegisterBean(@NonNull T entity, @NonNull final Map<String, Object> classParams) {
        if (null == entity) {
            return null;
        }
        Map<String, Object> maps = ClassHelper.getFieldsValueAsMap(entity);
        if (null != maps) {
            classParams.putAll(maps);
        }
        return ifNotContainerRegisterBean(entity.getClass().getName(), entity.getClass(), classParams);
    }

    /**
     * 注册Bean
     *
     * @param beanName  名称
     * @param beanClass 类
     * @return
     */
    default String registerBean(@NonNull String beanName, @NonNull Class<?> beanClass) {
        return registerBean(beanName, beanClass, new HashMap<>());
    }

    /**
     * 注册Bean
     *
     * @param beanClass 类
     * @return
     */
    default String registerBean(@NonNull Class<?> beanClass) {
        return registerBean(beanClass.getName(), beanClass, new HashMap<>());
    }

    /**
     * 分析类属性
     *
     * @param beanClass 类型
     */
    default Map<String, Class> doAnalysisBeanFields(Class<?> beanClass) {
        Map<String, Class> fieldsAndType = new HashMap<>();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (Modifier.isSynchronized(field.getModifiers())) {
                continue;
            }
            fieldsAndType.put(field.getName(), field.getType());
        }
        return fieldsAndType;
    }

    /**
     * 实体校验
     *
     * @param entity 实体
     * @param <T>
     * @return
     */
    default <T> T verificationEntity(T entity) {
        EnvironmentFactory environmentFactory = environmentFactory();
        if (null == environmentFactory) {
            return entity;
        }
        return environmentFactory.automaticAssembly(entity);
    }
}