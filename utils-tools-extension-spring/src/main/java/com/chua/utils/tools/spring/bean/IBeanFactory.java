package com.chua.utils.tools.spring.bean;

import com.chua.utils.tools.classes.ClassHelper;
import org.springframework.beans.BeansException;
import org.springframework.core.env.Environment;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * bean工厂
 *
 * @author CH
 * @since 1.0
 */
public interface IBeanFactory {
    /**
     *
     * @return
     */
    public Environment environment();
    /**
     * 注册controller
     *
     * @param entity 实体
     * @param prefix 前缀
     * @return
     */
    public String registerController(@NotNull Object entity, String prefix);
    /**
     * 获取bean
     * @param name 名称
     * @param <T>
     * @return
     * @throws BeansException
     */
    public <T>T getBean(final String name) throws BeansException;
    /**
     * 获取bean
     * @param classes 名称
     * @param <T>
     * @return
     * @throws BeansException
     */
    public <T>T getBean(final Class<T> classes) throws BeansException;

    /**
     * 获取bean
     * @param name 名称
     * @param tClass 类型
     * @param <T>
     * @return
     * @throws BeansException
     */
    public <T>T getBean(final String name, final Class<T> tClass) throws BeansException;

    /**
     * 是否包含Bean
     * @param tClass
     * @return
     */
    public boolean containsBean(final Class<?> tClass);
    /**
     * 是否包含Bean
     * @param name
     * @return
     */
    public boolean containsBean(final String name);

    /**
     * 获取bean名称
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> String[] getBeanNamesForType(Class<T> tClass);
    /**
     * 查询该类型的所有bean
     * @param tClass 类型
     * @return
     */
    public <T> Set<T> getBeanForType(Class<T> tClass) throws BeansException;
    /**
     * 获取所有bean
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> Set<T> getBeans(Class<T> tClass);

    /**
     * 通过注解获取Bean
     * @param aClass
     * @return
     */
    public Map<String, Object> getBeansFromAnnotation(Class<? extends Annotation> aClass);
    /**
     *
     * 获取类中的属性
     * @param tClass 类
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getBeanMap(Class<T> tClass);

    /**
     * 删除Bean
     *
     * @param beanClass
     * @return
     */
    public String removeBean(@NotNull Class<?> beanClass);

    /**
     * 注册Bean
     *
     * @param beanName  名称
     * @param beanClass 类
     * @return
     */
    public String registerBean(@Nullable String beanName, @NotNull Class<?> beanClass, @Nullable final Map<String, Object> classParams);
    /**
     * <p>不存在注册Bean</p>
     * <p>存在不注册</p>
     *
     * @param beanName  名称
     * @param beanClass 类
     * @return
     */
    default public String ifNotContainerRegisterBean(@Nullable String beanName, @NotNull Class<?> beanClass, @Nullable final Map<String, Object> classParams) {
        if(containsBean(beanName)) {
            return beanName;
        }
        if(containsBean(beanClass)) {
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
    default public <T> String ifNotContainerRegisterBean(@NotNull String beanName, @NotNull T entity, @Nullable final Map<String, Object> classParams) {
        if(null == entity) {
            return null;
        }
        Map<String, Object> maps = ClassHelper.fieldsSuperMaps(entity);
        if(null != maps) {
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
    default public <T> String ifNotContainerRegisterBean(@NotNull String beanName, @NotNull T entity) {
        if(null == entity) {
            return null;
        }
        Map<String, Object> maps = ClassHelper.fieldsSuperMaps(entity);
        return ifNotContainerRegisterBean(beanName, entity.getClass(), maps);
    }
    /**
     * <p>不存在注册Bean</p>
     * <p>存在不注册</p>
     *
     * @param entity 对象
     * @return
     */
    default public <T> String ifNotContainerRegisterBean(@NotNull T entity) {
        if(null == entity) {
            return null;
        }
        Map<String, Object> maps = ClassHelper.fieldsSuperMaps(entity);
        return ifNotContainerRegisterBean(entity.getClass().getName(), entity.getClass(), maps);
    }
    /**
     * <p>不存在注册Bean</p>
     * <p>存在不注册</p>
     *
     * @param entity 对象
     * @return
     */
    default public <T> String ifNotContainerRegisterBean(@NotNull T entity, @Nullable final Map<String, Object> classParams) {
        if(null == entity) {
            return null;
        }
        Map<String, Object> maps = ClassHelper.fieldsSuperMaps(entity);
        if(null != maps) {
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
    default public String registerBean(@Nullable String beanName, @NotNull Class<?> beanClass) {
        return registerBean(beanName, beanClass, new HashMap<>());
    }
    /**
     * 注册Bean
     *
     * @param beanClass 类
     * @return
     */
    default public String registerBean(@NotNull Class<?> beanClass) {
        return registerBean(beanClass.getName(), beanClass, new HashMap<>());
    }
    /**
     * 分析类属性
     *
     * @param beanClass
     */
    default public Map<String, Class> doAnalysisBeanFields(Class<?> beanClass) {
        Map<String, Class> fieldsAndType = new HashMap<>();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            if(Modifier.isFinal(field.getModifiers())) {
               continue;
            }

            if(Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if(Modifier.isSynchronized(field.getModifiers())) {
                continue;
            }
            fieldsAndType.put(field.getName(), field.getType());
        }
        return fieldsAndType;
    }
}