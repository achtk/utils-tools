package com.chua.utils.tools.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 反射工具类
 * @author CH
 */
public class ReflectHelper {


    /**
     * 得到set方法
     *
     * @param clazz         类
     * @param property      属性
     * @param propertyClazz 属性
     * @return Method 方法对象
     */
    public static Method getPropertySetterMethod(Class clazz, String property, Class propertyClazz) {
        String methodName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
        try {
            return clazz.getMethod(methodName, propertyClazz);
        } catch (NoSuchMethodException e) {
            try {
                throw new NoSuchMethodException("No setter method for " + clazz.getName() + "#" + property);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 得到get/is方法
     *
     * @param clazz    类
     * @param property 属性
     * @return Method 方法对象
     */
    public static Method getPropertyGetterMethod(Class clazz, String property) {
        String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
        Method method;
        try {
            method = clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            try {
                methodName = "is" + property.substring(0, 1).toUpperCase() + property.substring(1);
                method = clazz.getMethod(methodName);
            } catch (NoSuchMethodException e1) {
                try {
                    throw new NoSuchMethodException("No getter method for " + clazz.getName() + "#" + property);
                } catch (NoSuchMethodException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取所有属性
     * @param entity 对象
     */
    public static <T> List<Field> getAllFields(T entity) {
        if(null == entity) {
            return null;
        }
        Class<?> aClass = entity.getClass();
        Field[] fields = aClass.getDeclaredFields();
        return null == fields ? null : Arrays.asList(fields);
    }

    /**
     * 获取所有属性名称
     * @param entity 对象
     * @return
     */
    public static <T> List<String> getAllFieldNames(T entity) {
        List<Field> allFields = getAllFields(entity);
        if(null == allFields) {
            return null;
        }

        List<String> fieldNames = new ArrayList<>();
        for (Field field : allFields) {
            fieldNames.add(field.getName());
        }

        return fieldNames;
    }

    /**
     * 获取字段的属性以及值
     * @param from 对象
     * @param <E>
     */
    public static <E> Map<String, Object> getAllFieldNameAndValues(E from) {
        if(null == from) {
            return null;
        }
        Class<?> aClass = from.getClass();
        Field[] fields = aClass.getDeclaredFields();
        Map<String, Object> result = new HashMap<>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                result.put(field.getName(), field.get(from));
            } catch (IllegalAccessException e) {
                continue;
            }
        }

        return result;
    }

    /**
     * 反射设置对象字段值
     * @param obj 对象
     * @param nameAndValues 值
     * @param <E1>
     */
    public static <E1> void setProperties(E1 obj, final Map<String, Object> nameAndValues) {
        if(!BooleanHelper.hasLength(nameAndValues)) {
            return;
        }

        List<Field> allFields = getAllFields(obj);
        if(null == allFields) {
            return;
        }

        for (Field field : allFields) {
            String name = field.getName();
            if(!nameAndValues.containsKey(name)) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(obj, nameAndValues.get(name));
            } catch (IllegalAccessException e) {
                continue;
            }
        }
    }
}
