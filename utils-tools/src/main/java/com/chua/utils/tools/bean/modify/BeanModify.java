package com.chua.utils.tools.bean.modify;

import com.chua.utils.tools.manager.parser.description.ModifyDescription;
import com.chua.utils.tools.util.ClassUtils;
import javassist.NotFoundException;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * bean 修饰
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/18
 */
public interface BeanModify<T> {
    /**
     * 添加接口
     *
     * @param interfaces 接口(不包含泛型)
     * @return this
     */
    BeanModify<T> addInterface(Class<?>... interfaces);

    /**
     * 添加父类
     *
     * @param aClass 父类
     * @return this
     */
    BeanModify<T> addSuper(Class<?> aClass);

    /**
     * 添加注解
     *
     * @param annotations 注解
     * @return this
     */
    BeanModify<T> addAnnotation(Annotation... annotations);

    /**
     * 代理
     *
     * @param object 对象
     * @return 代理
     */
    static <T> BeanModify<T> of(T object) throws NotFoundException {
        return new StandardBeanModify<>(object);
    }

    /**
     * 添加字段
     *
     * @param body 字段内容
     * @return this
     */
    BeanModify<T> addField(String body);

    /**
     * 添加字段
     *
     * @param name           名称
     * @param annotationType 注解类型
     * @return this
     */
    BeanModify<T> addField(String name, List<?> annotationType);

    /**
     * 添加字段
     *
     * @param name           名称
     * @param annotationType 注解类型
     * @return this
     */
    default BeanModify<T> addField(String name, Annotation... annotationType) {
        return this.addField(name, Arrays.asList(annotationType));
    }

    /**
     * 添加字段
     *
     * @param name           名称
     * @param type           类型
     * @param annotationType 注解类型
     * @return this
     */
    default BeanModify<T> addField(String name, Class<?> type, Class<? extends Annotation>... annotationType) {
        this.addField(name, type);
        return this.addField(name, Arrays.asList(annotationType));
    }

    /**
     * 添加字段
     *
     * @param name           名称
     * @param type           类型
     * @param annotationType 注解类型
     * @param params         参数
     * @return this
     */
    default BeanModify<T> addField(String name, Class<?> type, Class<? extends Annotation> annotationType, Map<String, Object> params) {
        this.addField(name, type);
        try {
            Annotation annotation = ClassUtils.makeAnnotation(annotationType, params);
            return this.addField(name, Collections.singletonList(annotation));
        } catch (Exception e) {
            return this.addField(name, Collections.singletonList(annotationType));
        }
    }

    /**
     * 添加字段
     *
     * @param name 名称
     * @param type 类型
     * @return this
     */
    BeanModify<T> addField(String name, Class<?> type);

    /**
     * 添加字段
     *
     * @param name        名称
     * @param type        类型
     * @param annotations 注解
     * @return this
     */
    default BeanModify<T> addField(String name, Class<?> type, List<Annotation> annotations) {
        this.addField(name, type);
        return this.addField(name, annotations);
    }

    /**
     * 添加字段
     *
     * @param name        名称
     * @param type        类型
     * @param annotations 注解
     * @return this
     */
    default BeanModify<T> addField(String name, Class<?> type, Annotation... annotations) {
        this.addField(name, type);
        return this.addField(name, Arrays.asList(annotations));
    }

    /**
     * 添加字段
     *
     * @param body 字段内容
     * @return this
     */
    BeanModify<T> addMethod(String body);

    /**
     * 给方法添加注解
     *
     * @param methodName  方法
     * @param params      参数
     * @param annotations 注解
     * @return this
     */
    BeanModify<T> addMethodAnnotations(String methodName, Class<?>[] params, List<Annotation> annotations);

    /**
     * 添加字段
     *
     * @param name   名称
     * @param body   内容
     * @param type   类型
     * @param params 参数
     * @return this
     */
    BeanModify<T> addMethod(String name, String body, Class<?> type, Class<?>[] params);

    /**
     * 构建对象
     *
     * @return 对象
     * @throws Exception Exception
     */
    ModifyDescription<T> create() throws Exception;
}
