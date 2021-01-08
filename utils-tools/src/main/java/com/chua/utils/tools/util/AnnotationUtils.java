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
import java.util.Map;

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
}
