package com.chua.utils.tools.classes;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.chua.utils.tools.util.ArrayUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.chua.utils.tools.constant.SymbolConstant.*;
import static com.chua.utils.tools.empty.EmptyOrBase.*;

/**
 * class工具
 *
 * @author CH
 */
@Slf4j
public class ClassHelper extends ClassLoaderHelper {

    /**
     * 字符串转类对象
     *
     * @param typeName     类名
     * @param classLoaders 类加载器
     * @return 类
     */
    public static Class<?> forName(final String typeName, ClassLoader... classLoaders) {
        if (StringHelper.isNotEmpty(typeName)) {
            if (BASE_NAME_LIST.contains(typeName)) {
                return BASE_TYPE_LIST.get(BASE_NAME_LIST.indexOf(typeName));
            } else {
                String type;
                if (typeName.contains(SYMBOL_LEFT_SQUARE_BRACKET)) {
                    int i = typeName.indexOf(SYMBOL_LEFT_SQUARE_BRACKET);
                    type = typeName.substring(0, i);
                    String array = typeName.substring(i).replace(SYMBOL_RIGHT_SQUARE_BRACKET, SYMBOL_EMPTY);

                    if (BASE_NAME_LIST.contains(type)) {
                        type = BASE_DESCRIPT_LIST.get(BASE_NAME_LIST.indexOf(type));
                    } else {
                        type = "L" + type + ";";
                    }
                    type = array + type;
                } else {
                    type = typeName;
                }

                if (!BooleanHelper.hasLength(classLoaders)) {
                    classLoaders = ArraysHelper.add(classLoaders, getDefaultClassLoader());
                }

                List<Throwable> reflectionsExceptions = Lists.newArrayList();
                for (ClassLoader classLoader : classLoaders) {
                    try {
                        return Class.forName(type, false, classLoader);
                    } catch (Throwable e) {
                        reflectionsExceptions.add(e);
                        String message = e.getMessage();
                        String errorName = "wrong name: ";
                        int index = message.indexOf(errorName);
                        if (index == -1) {
                            return null;
                        }
                        String newName = message.substring(index + errorName.length(), message.length() - 1).replace("/", ".");
                        try {
                            return Class.forName(newName, false, classLoader);
                        } catch (ClassNotFoundException classNotFoundException) {
                            reflectionsExceptions.add(classNotFoundException);
                            return null;
                        }
                    }
                }

                if (log.isTraceEnabled()) {
                    for (Throwable reflectionsException : reflectionsExceptions) {
                        log.trace("could not get type for name " + typeName + " from any class loader", reflectionsException);
                    }
                }
                return null;
            }
        }
        return null;
    }

    /**
     * 获取简单名字
     * <pre>
     *     ClassHelper.getSimpleClassName("java.lang.String") = String
     * </pre>
     *
     * @param qualifiedName 方法名
     * @return 简单名字
     */
    public static String getSimpleClassName(String qualifiedName) {
        if (null == qualifiedName) {
            return null;
        }

        int i = qualifiedName.lastIndexOf('.');
        return i < 0 ? qualifiedName : qualifiedName.substring(i + 1);
    }

    /**
     * 是否包含类
     *
     * @param className 类名
     * @return 类加载器是否包含该类, 包含返回true
     */
    public static boolean isPresent(final String className) {
        return isPresent(className, null);
    }

    /**
     * 是否包含类
     *
     * @param className   类名
     * @param classLoader 类加载器
     * @return 类加载器是否包含该类, 包含返回true
     */
    public static boolean isPresent(final String className, final ClassLoader classLoader) {
        try {
            final Class<?> aClass = forName(className, null == classLoader ? new ClassLoader[0] : new ClassLoader[]{classLoader});
            return null != aClass;
        } catch (Throwable ex) {
            return false;
        }
    }

    /**
     * 是否为父类子类
     *
     * @param value       子类
     * @param parentClass 父类
     * @return 子类是父类的子类返回true
     */
    public static <T> boolean isAssignableFrom(final T value, final String parentClass) {
        return null != value && isAssignableFrom(value.getClass(), parentClass);
    }

    /**
     * 是否为父类子类
     *
     * @param value       子类
     * @param parentClass 父类
     * @return 子类是父类的子类返回true
     */
    public static boolean isAssignableFrom(final Class<?> value, final String parentClass) {
        return null != value && !Strings.isNullOrEmpty(parentClass) && isAssignableFrom(value, ClassHelper.forName(parentClass));
    }

    /**
     * 是否为父类子类
     *
     * @param value       子类
     * @param parentClass 父类
     * @return 子类是父类的子类返回true
     */
    public static boolean isAssignableFrom(final Class<?> value, final Class<?> parentClass) {
        if (null == value || null == parentClass) {
            return false;
        }
        if (parentClass.isAssignableFrom(value)) {
            return true;
        }
        // 跨ClassLoader的情况
        String interfaceName = value.getCanonicalName();
        return parentClass.getCanonicalName().equals(interfaceName) || isImplementOrSubclass(interfaceName, parentClass);
    }

    /**
     * 根据类名加载Class, 异常返回null
     *
     * @param className  类名
     * @param initialize 是否初始化
     * @return Class
     */
    public static Class forName(String className, boolean initialize) {
        try {
            return Class.forName(className, initialize, getDefaultClassLoader());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过包+类名获取类
     *
     * @param packages  包
     * @param className 类名
     * @return 类
     */
    public static Class<?> forName(String[] packages, String className) {
        try {
            return forNameNoClassLoader(className);
        } catch (ClassNotFoundException e) {
            if (!BooleanHelper.hasLength(packages)) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            for (String pkg : packages) {
                try {
                    return forNameNoClassLoader(pkg + "." + className);
                } catch (ClassNotFoundException ignore) {
                }
            }
        }
        return null;
    }

    /**
     * 通过默认类加载器转化类名为类
     *
     * @param className 类名
     * @return 类
     * @throws ClassNotFoundException ClassNotFoundException
     * @see #getDefaultClassLoader()
     */
    private static Class<?> forNameNoClassLoader(String className) throws ClassNotFoundException {
        Class<?> aClass = BASE_NAME_TYPE.get(className);
        if (null != aClass) {
            return aClass;
        }

        try {
            return arrayForName(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className, false, getDefaultClassLoader());
            } catch (Exception e1) {
                if (className.indexOf(SYMBOL_DOT_CHAR) != -1) {
                    throw e;
                }
                try {
                    return arrayForName("java.lang." + className);
                } catch (ClassNotFoundException e2) {
                }
            }
        }
        return null;
    }

    /**
     * 获取数组类类
     *
     * @param className 数组类类名
     * @return 类
     * @throws ClassNotFoundException ClassNotFoundException
     */
    private static Class<?> arrayForName(String className) throws ClassNotFoundException {
        return Class.forName(className.endsWith("[]")
                ? "[L" + className.substring(0, className.length() - 2) + ";"
                : className, true, Thread.currentThread().getContextClassLoader());
    }

    /**
     * 字符串转类对象
     *
     * @param typeName     类名
     * @param classLoaders 类加载器
     * @return 类
     */
    public static Class<?> forName(final String typeName, List<ClassLoader> classLoaders) {
        return forName(typeName, classLoaders.toArray(new ClassLoader[classLoaders.size()]));
    }

    /**
     * 实例化类
     * <p>
     * <p>类加载过程:</p>
     * <p>1、首次尝试对类实例化<code>newInstance</code></p>
     * <p>2、当失败时, 检测类是否包含无参构造，不存在则创建无参构造，存在但是无参构造不为公共修饰符则构造初始化</p>
     * <p>3、当创建了无参构造再次尝试实例化，再次失败返回null</p>
     *
     * <p>接口/抽象类加载过程：</p>
     * <p>1、判断类为接口， 采用代理生成空的实现类，并返回</p>
     * </p>
     *
     * @param tClass 类
     * @return T
     */
    public static <T> T safeForObject(Class<? extends T> tClass) {
        if (null == tClass) {
            return null;
        }
        try {
            return forObject(tClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 实例化类
     * <p>
     * <p>类加载过程:</p>
     * <p>1、首次尝试对类实例化<code>newInstance</code></p>
     * <p>2、当失败时, 检测类是否包含无参构造，不存在则创建无参构造，存在但是无参构造不为公共修饰符则构造初始化</p>
     * <p>3、当创建了无参构造再次尝试实例化，再次失败返回null</p>
     *
     * <p>接口/抽象类加载过程：</p>
     * <p>1、判断类为接口， 采用代理生成空的实现类，并返回</p>
     * </p>
     *
     * @param tClass 类
     * @return 对象
     */
    public static <T> T forObject(Class<? extends T> tClass) {
        try {
            return forObject(tClass, Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 实例化类
     * <p>
     * <p>类加载过程:</p>
     * <p>1、首次尝试对类实例化<code>newInstance</code></p>
     * <p>2、当失败时, 检测类是否包含无参构造，不存在则创建无参构造，存在但是无参构造不为公共修饰符则构造初始化</p>
     * <p>3、当创建了无参构造再次尝试实例化，再次失败返回null</p>
     *
     * <p>接口/抽象类加载过程：</p>
     * <p>1、判断类为接口， 采用代理生成空的实现类，并返回</p>
     * </p>
     *
     * @param tClass  类
     * @param loaders 类加载器
     * @return 对象
     */
    public static <T> T forObject(Class<? extends T> tClass, List<ClassLoader> loaders) throws Exception {
        if (null == tClass) {
            return null;
        }
        if (!BooleanHelper.hasLength(loaders)) {
            try {
                return tClass.newInstance();
            } catch (Throwable e) {
                return null;
            }
        }

        for (ClassLoader loader : loaders) {
            try {
                return (T) Class.forName(tClass.getName(), false, loader).newInstance();
            } catch (ClassNotFoundException e) {
                continue;
            }
        }
        return null;
    }

    /**
     * 实例化类
     *
     * @param clazz        类
     * @param classLoaders 类加载器(默认当前线程类加载器)
     * @param <T>          类型
     * @return 对象
     */
    public static <T> T forObject(Class<?> clazz, ClassLoader... classLoaders) {
        if (null == clazz) {
            return null;
        }
        Class<?> aClass = forName(clazz.getName(), ClassLoaderHelper.getOrDefault(classLoaders));
        try {
            return (T) forObject(aClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 实例化对象
     *
     * @param className 类名
     * @return 对象
     */
    public static <T> T forObject(String className) {
        return forObject(className, getDefaultClassLoader());
    }

    /**
     * 实例化类
     *
     * @param className  类名
     * @param returnType 类类型
     * @param <T>        类类型
     * @return 对象
     */
    @SuppressWarnings("Unchecked")
    public static <T> T forObject(String className, Class<T> returnType) {
        Object object = forObject(className);
        return null == object || returnType.isAssignableFrom(object.getClass()) ? (T) object : null;
    }

    /**
     * 实例化类
     *
     * @param aClass     类名
     * @param returnType 类类型
     * @param <T>        类类型
     * @return 对象
     */
    public static <T> T forObject(Class<?> aClass, Class<T> returnType) {
        if (null == aClass) {
            return null;
        }
        return forObject(aClass.getName(), returnType);
    }

    /**
     * 实例化类
     *
     * @param className    类名
     * @param classLoaders 类加载器(默认当前线程类加载器)
     * @return 对象
     */
    public static <T> T forObject(String className, ClassLoader... classLoaders) {
        List<ClassLoader> loaders = ClassLoaderHelper.getOrDefault(classLoaders);
        try {
            Class<?> aClass = forName(className, loaders);
            return (T) forObject(aClass, loaders);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断接口实现或者子类
     *
     * @param interfaceName  接口名
     * @param implementClass 接口实现/子类
     * @return 接口实现或者子类返回true
     */
    private static boolean isImplementOrSubclass(String interfaceName, Class<?> implementClass) {
        Class<?>[] interfaces = implementClass.getInterfaces();
        if (interfaces.length > 0) {
            for (Class<?> oneInterface : interfaces) {
                if (interfaceName.equals(oneInterface.getCanonicalName())) {
                    return true;
                }
                if (isImplementOrSubclass(interfaceName, oneInterface)) {
                    return true;
                }
            }
        }
        while (!Object.class.equals(implementClass)) {
            Class<?> superClass = implementClass.getSuperclass();
            if (superClass == null) {
                break;
            }
            implementClass = superClass;
            if (isImplementOrSubclass(interfaceName, implementClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取参数实际类型
     *
     * @param declaredClass 类
     * @param <T>           类型
     * @return 参数实际类型
     */
    public static <T> Class<T> resolveGenericType(Class<?> declaredClass) {
        ParameterizedType parameterizedType = (ParameterizedType) declaredClass.getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return (Class<T>) actualTypeArguments[0];
    }

    /**
     * 获取类
     *
     * @param object 对象
     * @return 类
     */
    public static Class<?> getClass(Object object) {
        return BooleanHelper.isClass(object) ? (Class<?>) object : object.getClass();
    }

    /**
     * 是否基础类
     *
     * @param type 类型
     * @return boolean
     */
    public static boolean isBasicClass(Class<?> type) {
        return null == type ||
                type.isPrimitive() || type.isArray() ||
                String.class.isAssignableFrom(type) ||
                Integer.class.isAssignableFrom(type) ||
                Long.class.isAssignableFrom(type) ||
                Short.class.isAssignableFrom(type) ||
                Double.class.isAssignableFrom(type) ||
                Float.class.isAssignableFrom(type) ||
                Boolean.class.isAssignableFrom(type) ||
                BigDecimal.class.isAssignableFrom(type) ||
                Character.class.isAssignableFrom(type) ||
                CharSequence.class.isAssignableFrom(type) ||
                Byte.class.isAssignableFrom(type);
    }

    /**
     * 校验类名是否合法
     *
     * @param classNamesList 类名集合
     * @param filter         是否合法
     * @return
     */
    public static List<String> doWithVerify(List<String> classNamesList, Filter<Class> filter) {
        if (!BooleanHelper.hasLength(classNamesList) || null == filter) {
            return Collections.emptyList();
        }
        List<String> verifyList = new ArrayList<>(classNamesList.size());
        classNamesList.stream().forEach(s -> {
            Class<?> aClass = forName(s);
            boolean matcher = filter.matcher(aClass);
            if (matcher) {
                verifyList.add(s);
            }
        });
        return verifyList;
    }

    /**
     * 数据转类型
     *
     * @param objects 请求数据
     * @return 请求数据类型
     */
    public static Class<?>[] paramsToType(Object[] objects) {
        if (!BooleanHelper.hasLength(objects)) {
            return new Class[0];
        }
        Class<?>[] classes = new Class[objects.length];
        for (int i = 0; i < objects.length; i++) {
            Object arg = objects[i];
            classes[i] = arg.getClass();
        }
        return classes;
    }

    /**
     * 获取枚举类
     *
     * @param value     值
     * @param enumClass 枚举类类型
     * @return 枚举类
     */
    public static <T> T getEnum(String value, Class<T> enumClass) {
        if (null == enumClass || Strings.isNullOrEmpty(value)) {
            return null;
        }
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (!value.equalsIgnoreCase(enumConstant.toString())) {
                continue;
            }
            return enumConstant;
        }
        return null;
    }

    /**
     * 类是否是Object.class
     *
     * @param clazz 类
     * @return 类是 Object.class 或者null 返回true
     */
    public static boolean isObject(Class<?> clazz) {
        return null == clazz || Object.class.getName().equals(clazz.getName());
    }

    /**
     * 参数和数据类型一致处理
     * <p>当需要的参数为空, 则直接返回</p>
     *
     * @param paramTypes 需要的参数类型
     * @param params     传递的数据
     * @return 参数和数据类型可以一致化返回指定数据, 反之返回null
     */
    public static Object[] doTypeConsistent(final Class<?>[] paramTypes, final Object[] params) {
        if (null == paramTypes || paramTypes.length == 0) {
            return new Object[]{};
        }
        int dataLength = paramTypes.length;
        Object[] newParams = new Object[dataLength];
        Object[] result = new Object[dataLength];
        //计数器
        AtomicInteger atomicInteger = new AtomicInteger(dataLength);

        for (int i = 0; i < params.length; i++) {
            newParams[i] = params[i];

        }
        for (int i = 0; i < dataLength; i++) {
            Class<?> paramType = paramTypes[i];
            Object data = newParams[i];
            atomicInteger.decrementAndGet();
            TypeConverter typeConverter = getTypeConverter(paramType);
            if(null == typeConverter) {
                result[i] = data;
                continue;
            }
            result[i]  = typeConverter.convert(data);
        }
        return atomicInteger.get() == 0 ? result : null;
    }

    /**
     * 获取类所在的资源文件
     *
     * @param aClass 类
     * @return 资源文件, 当类不存在返回null
     */
    public static URL getUrlByClass(Class<?> aClass) {
        if (null == aClass) {
            return null;
        }
        try {
            return aClass.getProtectionDomain().getCodeSource().getLocation().toURI().toURL();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量处理
     *
     * @param classes      类名称
     * @param classLoaders 类加载器
     * @return 类
     */
    public static <T> Set<Class<? extends T>> forNames(Set<String> classes, ClassLoader... classLoaders) {
        return classes.parallelStream()
                .filter(item -> item.indexOf("console") == -1 || !item.endsWith("Util"))
                .map(className -> {
                    try {
                        return (Class<? extends T>) forName(className, classLoaders);
                    } catch (Throwable e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * 获取方法
     *
     * @param object     对象
     * @param methodName 方法名
     * @param params     参数
     * @return
     */
    public static Method getMethodByName(Object object, String methodName, Class[] params) {
        Class<?> aClass = getClass(object);
        try {
            return aClass.getDeclaredMethod(methodName, params);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 获取方法值
     *
     * @param object 对象
     * @param method 方法
     * @param params 参数
     * @param <T>    类型
     * @return 值
     */
    public static <T> T getMethodValue(Object object, Method method, Object[] params) {
        if (null == method) {
            return null;
        }

        method.setAccessible(true);
        try {
            return (T) method.invoke(object, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取方法值
     *
     * @param object     对象
     * @param methodName 方法名
     * @param params     参数
     * @return 值
     */
    public static Object getMethodValue(Object object, String methodName, Object[] params) {
        return getMethodValue(object, methodName, ArrayUtils.toClass(params), params);
    }

    /**
     * 获取方法值
     *
     * @param object     对象
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @param paramTypes 参数
     * @param <T>        类型
     * @return 值
     */
    public static <T> T getMethodValue(Object object, String methodName, Class<?>[] paramTypes, Object[] params) {
        Method method = getMethodByName(object, methodName, paramTypes);
        return getMethodValue(object, method, params);
    }

    /**
     * 类型转化
     *
     * @param item 数据
     * @param type 类型
     * @param <T>  类型
     * @return 类型数据
     */
    public static <T> T converter(Object item, Class<T> type) {
        if (null == item || null == type || item instanceof Class) {
            return null;
        }

        if (type.isAssignableFrom(item.getClass())) {
            return (T) item;
        }

        TypeConverter<T> typeConverter = getTypeConverter(type);
        if (null != typeConverter) {
            return typeConverter.convert(item);
        }

        if (item instanceof Map) {
            return BeanCopy.of(ClassHelper.forObject(type)).with(item).create();
        }

        if (item instanceof String) {
            try {
                return JsonHelper.fromJson((String) item, type);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return BeanCopy.converter(item, ClassHelper.forObject(type));
    }

    /**
     * 类型转化
     *
     * @param item 数据
     * @param type 类型
     * @param <T>  类型
     * @return 类型数据
     */
    public static <T> T converter(Object item, String type) {
        return (T) converter(item, ClassHelper.forName(type));
    }


}
