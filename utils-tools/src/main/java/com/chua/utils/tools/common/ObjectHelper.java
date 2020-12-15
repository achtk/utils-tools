package com.chua.utils.tools.common;


import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * object 工具类
 *
 * @author CHTK
 */
public class ObjectHelper {

    /**
     * 获取对象标识
     *
     * @param obj 对象
     * @return
     */
    public static String identityToString(Object obj) {
        return obj == null ? "" : obj.getClass().getName() + "@" + getIdentityHexString(obj);
    }

    /**
     * 获取hex特征值
     *
     * @param obj 对象
     * @return
     */
    public static String getIdentityHexString(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    /**
     * 检测异常
     *
     * @param ex
     * @return
     */
    public static boolean isCheckedException(Throwable ex) {
        return !(ex instanceof RuntimeException || ex instanceof Error);
    }

    /**
     * 检测是否是数组
     *
     * @param obj
     * @return
     */
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    /**
     * 是否为空
     *
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * object转String
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return null != object ? object.toString() : "";
    }

    /**
     * object转float
     *
     * @param object obj
     * @return
     */
    public static Float toFloat(Object object) {
        return NumberHelper.toFloat(toString(object));
    }

    /**
     * object转long
     *
     * @param object obj
     * @return
     */
    public static Long toLong(Object object) {
        return NumberHelper.toLong(toString(object));
    }

    /**
     * object转 byte
     *
     * @param object obj
     * @return
     */
    public static Byte toByte(Object object) {
        return NumberHelper.toByte(toString(object));
    }

    /**
     * object转 int
     *
     * @param object obj
     * @return
     */
    public static Integer toInt(Object object) {
        return NumberHelper.toInt(toString(object));
    }

    /**
     * object转 short
     *
     * @param object obj
     * @return
     */
    public static Short toShort(Object object) {
        return NumberHelper.toShort(toString(object));
    }

    /**
     * object转 double
     *
     * @param object obj
     * @return
     */
    public static Double toDouble(Object object) {
        return NumberHelper.toDouble(toString(object));
    }

    /**
     * object转 boolean
     *
     * @param object obj
     * @return
     */
    public static Boolean toBoolean(Object object) {
        return BooleanHelper.toBoolean(null != object ? object.toString() : "false");
    }


    /**
     * 当对象为空返回默认值
     *
     * @param object       obj
     * @param defaultValue 默认值
     * @return
     */
    public static String defaultIfNull(Object object, String defaultValue) {
        return null == object ? defaultValue : object.toString();
    }

    /**
     * 获取长度
     *
     * @param o1
     * @return
     */
    public static int length(Object o1) {
        return null == o1 ? 0 : o1.toString().length();
    }

    /**
     * 是否为空
     *
     * @param object 对象
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        } else if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else {
            return object instanceof Map ? ((Map) object).isEmpty() : false;
        }
    }

    /**
     * 拷贝对象
     *
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> T dCopy(T entity) {
        if (null == entity) {
            return entity;
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);) {
            outputStream.writeObject(entity);

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                return (T) objectInputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 对象转JSON字符串
     *
     * @param obj 对象
     * @return
     */
    public static String toFormat(Object obj) {
        return JsonHelper.toFormatJson(obj);
    }

    /**
     * 是否有一个为空
     *
     * @param source
     * @param sources
     * @return
     */
    public static boolean isAnyNull(Object source, Object... sources) {
        if (Objects.isNull(source) || !BooleanHelper.hasLength(sources)) {
            return true;
        }
        for (Object o : sources) {
            if (Objects.isNull(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果数据为空
     *
     * @param source
     * @param source1
     * @return
     */
    public static <T> T firstNonNull(T source, T source1) {
        if (null != source) {
            return source;
        }
        if (null != source1) {
            return source1;
        }
        throw new IllegalArgumentException("The both parameters cannot be empty!");
    }

    /**
     * 默认值
     *
     * @param predicate
     * @param instance
     * @return
     */
    public static <T> T defaultNoneNull(T predicate, T instance) {
        return null == predicate ? instance : predicate;
    }

    /**
     * 执行方法
     *
     * @param obj  对象
     * @param name 方法
     * @param args 参数
     */
    public static void invoke(Object obj, String name, Object... args) {
        if (null == obj) {
            return;
        }

        Class<?> aClass = obj.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        Method method = null;
        for (Method item : methods) {
            if (!item.getName().equals(name)) {
                continue;
            }
            method = item;
        }

        if (null == method) {
            return;
        }

        try {
            method.setAccessible(true);
            method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 空转空字符串
     *
     * @param source 数据
     * @return String
     */
    public static String nullToEmpty(String source) {
        return null == source ? "" : source.toString();
    }

    /**
     * 类型是否一致
     *
     * @param source 元数据
     * @param target 目标数据
     * @return 一致返回true
     */
    public static boolean isTypeEquals(Class<?> source, Class<?> target) {
        if (null == source && null == target) {
            return true;
        }
        if (null == source || null == target) {
            return false;
        }
        return source.isAssignableFrom(target);
    }

    /**
     * 类型是否一致
     *
     * @param source 元数据
     * @param target 目标数据
     * @return 一致返回true
     */
    public static boolean isTypeEquals(Class<?>[] source, Class<?>[] target) {
        if (null == source && null == target) {
            return true;
        }
        if (null == source || null == target) {
            return false;
        }

        if (source.length != target.length) {
            return false;
        }

        for (int i = 0; i < source.length; i++) {
            Class<?> sourceClass = source[i];
            Class<?> targetClass = target[i];
            if (!isTypeEquals(sourceClass, targetClass)) {
                return false;
            }
        }

        return true;
    }


}
