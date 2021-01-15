package com.chua.utils.tools.util;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.manager.parser.description.FieldDescription;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 注解工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/8
 */
public class AnnotationUtils {

    private static final String MEMBER_VALUES = "memberValues";
    private static final Class<?>[] IGNORED = new Class<?>[]{
            Documented.class,
            Retention.class,
            Target.class,
            Repeatable.class,
            Inherited.class,
            Native.class,
            Inherited.class
    };

    /**
     * 注解转Map
     *
     * @param obj            对象
     * @param annotationType 注解
     * @return 注解值
     */
    public static OperateHashMap findValuesByType(Object obj, Class<? extends Annotation> annotationType) {
        if (null == obj || null == annotationType) {
            return OperateHashMap.emptyMap();
        }

        OperateHashMap annotationMap = new OperateHashMap();
        Class<?> aClass = obj.getClass();
        Annotation[] annotations = aClass.getDeclaredAnnotationsByType(annotationType);
        for (Annotation annotation : annotations) {
            annotationMap.putAll(asMap(annotation));
        }
        return annotationMap;
    }

    /**
     * 注解下的注解
     *
     * @param annotation 注解
     * @return Map
     */
    private static OperateHashMap asAnnotationMap(Annotation annotation) {
        OperateHashMap result = new OperateHashMap();

        Class<? extends Annotation> aClass = annotation.annotationType();
        Annotation[] annotations = aClass.getDeclaredAnnotations();
        for (Annotation annotation1 : annotations) {
            if (ArrayUtils.contains(IGNORED, annotation1.annotationType())) {
                continue;
            }
            result.putAll(asMap(annotation1));
        }
        return result;
    }

    /**
     * 注解转Map
     *
     * @param obj            对象
     * @param annotationType 注解
     * @return 注解值
     */
    public static OperateHashMap findValuesByMethod(Object obj, Class<? extends Annotation> annotationType) {
        if (null == obj || null == annotationType) {
            return OperateHashMap.emptyMap();
        }

        OperateHashMap annotationMap = new OperateHashMap();
        Class<?> aClass = obj.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotationsByType(annotationType);
            for (Annotation annotation : annotations) {
                annotationMap.putAll(asMap(annotation));
            }
        }
        return annotationMap;
    }

    /**
     * 注解转Map
     *
     * @param obj            对象
     * @param annotationType 注解
     * @return 注解值
     */
    public static OperateHashMap findValuesByField(Object obj, Class<? extends Annotation> annotationType) {
        if (null == obj || null == annotationType) {
            return OperateHashMap.emptyMap();
        }

        OperateHashMap annotationMap = new OperateHashMap();
        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotationsByType(annotationType);
            for (Annotation annotation : annotations) {
                annotationMap.putAll(asMap(annotation));
            }
        }
        return annotationMap;
    }

    /**
     * 注解转Map
     *
     * @param annotation 注解
     * @return 注解值
     */
    public static Map<String, Object> asMap(Annotation annotation) {
        if (null == annotation) {
            return Collections.emptyMap();
        }

        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Field field = ClassUtils.getLocalField(invocationHandler, MEMBER_VALUES);
        if (null == field) {
            return Collections.emptyMap();
        }
        Map<String, Object> memberValues = FieldDescription.get(field, invocationHandler, EmptyOrBase.MAP_STRING_OBJECT);
        Map<String, Object> stringObjectMap = asAnnotationMap(annotation);
        return MapUtils.merge(memberValues, stringObjectMap);
    }

    /**
     * 是否包含注解
     *
     * @param compiler       类
     * @param componentClass 注解类
     * @return 包含返回true
     */
    public static boolean isAnnotationPresent(Class<?> compiler, Class<? extends Annotation> componentClass) {
        if (null != compiler.getDeclaredAnnotation(componentClass)) {
            return true;
        }
        Set<Annotation> allAnnotation = findAllAnnotation(compiler, annotation -> annotation.getClass().getName().equals(componentClass.getName()));
        return !allAnnotation.isEmpty();
    }


    /**
     * 获取注解
     *
     * @param source         类
     * @param componentClass 注解类
     * @param <A>            类型
     * @return 注解
     */
    public static <A> A get(Class<?> source, Class<? extends A> componentClass) {
        A annotation = (A) source.getDeclaredAnnotation((Class<? extends Annotation>) componentClass);
        if (null != annotation) {
            return annotation;
        }
        Set<Annotation> allAnnotation = findAllAnnotation(source, annotation1 -> componentClass.isAssignableFrom(annotation1.annotationType()));
        return (A) CollectionUtils.findOnly(allAnnotation);
    }

    /**
     * 获取所有注解
     *
     * @param source    类
     * @param predicate 拦截器
     * @return 注解
     */
    public static Set<Annotation> findAllAnnotation(final Class<?> source, final Predicate<Annotation> predicate) {
        //获取所有注解
        Set<Annotation> annotations = new HashSet<>();
        for (Annotation declaredAnnotation : source.getDeclaredAnnotations()) {
            if (predicate.test(declaredAnnotation)) {
                annotations.add(declaredAnnotation);
            }
            findAllAnnotation(declaredAnnotation, annotations, predicate);
        }
        return annotations;
    }

    /**
     * 获取所有注解
     *
     * @param annotation 注解
     * @param result     结果集
     */
    private static void findAllAnnotation(final Annotation annotation, final Set<Annotation> result, final Predicate<Annotation> predicate) {
        Class<? extends Annotation> aClass = annotation.annotationType();
        Annotation[] declaredAnnotations = aClass.getDeclaredAnnotations();
        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (ArrayUtils.contains(IGNORED, declaredAnnotation.annotationType())) {
                continue;
            }
            if (result.contains(declaredAnnotation)) {
                continue;
            }

            if (predicate.test(declaredAnnotation)) {
                result.add(declaredAnnotation);
            }
            findAllAnnotation(declaredAnnotation, result, predicate);
        }

    }


}
