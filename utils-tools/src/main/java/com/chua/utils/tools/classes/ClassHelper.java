package com.chua.utils.tools.classes;

import com.chua.utils.tools.classes.reflections.ReflectionsHelper;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.Assert;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.exceptions.NotSupportedException;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.proxy.CglibProxyAgent;
import com.chua.utils.tools.proxy.ProxyAgent;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import javassist.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Modifier;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

import static com.chua.utils.tools.constant.ClassConstant.*;
import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * class工具
 *
 * @author CH
 */
@Slf4j
public class ClassHelper extends ReflectionsHelper {

    public static final Map<Class<?>, Object> DEFAULT_TYPE_VALUES;

    static {
        Map<Class<?>, Object> values = new HashMap<>();
        values.put(boolean.class, false);
        values.put(byte.class, (byte) 0);
        values.put(float.class, (float) 0);
        values.put(double.class, (double) 0);
        values.put(char.class, (char) 0);
        values.put(short.class, (short) 0);
        values.put(int.class, 0);
        values.put(long.class, (long) 0);
        DEFAULT_TYPE_VALUES = Collections.unmodifiableMap(values);
    }

    /**
     * 得到当前ClassLoader
     *
     * @param clazz 某个类
     * @return ClassLoader
     */
    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            return loader;
        }
        if (clazz != null) {
            loader = clazz.getClassLoader();
            if (loader != null) {
                return loader;
            }
        }
        return ClassLoader.getSystemClassLoader();
    }

    /**
     * 获取类的类加载器
     *
     * @param caller 类
     * @return ClassLoader
     */
    public static ClassLoader getCallerClassLoader(Class<?> caller) {
        Preconditions.checkNotNull(caller);
        return caller.getClassLoader();
    }

    /**
     * 根据方法名, 指定类，将方法名转化为指定类类加载器下的类
     *
     * @param qualifiedClassName 方法名
     * @param aClass             指定类
     * @return
     */
    public static Class<?> forNameWithCallerClassLoader(String qualifiedClassName, Class<?> aClass) throws ClassNotFoundException {
        ClassLoader classLoader = aClass.getClassLoader();
        return forName(qualifiedClassName, classLoader);
    }

    /**
     * 字符串转类对象
     *
     * @param typeName     类名
     * @param classLoaders 类加载器
     * @return
     */
    public static Class<?> forName(final String typeName, ClassLoader... classLoaders) {
        if (StringHelper.isNotEmpty(typeName)) {
            if (getPrimitiveNames().contains(typeName)) {
                return getPrimitiveTypes().get(getPrimitiveNames().indexOf(typeName));
            } else {
                String type;
                if (typeName.contains(SYMBOL_LEFT_SQUARE_BRACKET)) {
                    int i = typeName.indexOf(SYMBOL_LEFT_SQUARE_BRACKET);
                    type = typeName.substring(0, i);
                    String array = typeName.substring(i).replace(SYMBOL_RIGHT_SQUARE_BRACKET, SYMBOL_EMPTY);

                    if (getPrimitiveNames().contains(type)) {
                        type = getPrimitiveDescriptors().get(getPrimitiveNames().indexOf(type));
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
     * <p>ClassHelper.getSimpleClassName("java.lang.String") = String</p>
     *
     * @param qualifiedName 方法名
     * @return
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
     * @return
     */
    public static boolean isPresent(final String className) {
        return isPresent(className, null);
    }

    /**
     * 是否包含类
     *
     * @param className   类名
     * @param classLoader 类加载器
     * @return
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
     * @return
     */
    public static <T> boolean isAssignableFrom(final T value, final String parentClass) {
        return null == value ? false : isAssignableFrom(value.getClass(), parentClass);
    }

    /**
     * 是否为父类子类
     *
     * @param value       子类
     * @param parentClass 父类
     * @return boolean
     */
    public static boolean isAssignableFrom(final Class<?> value, final String parentClass) {
        return null == value || Strings.isNullOrEmpty(parentClass) ? false : isAssignableFrom(value, ClassHelper.forName(parentClass));
    }

    /**
     * 是否为父类子类
     *
     * @param value       子类
     * @param parentClass 父类
     * @return
     */
    public static boolean isAssignableFrom(final Class<?> value, final Class<?> parentClass) {
        if (null == value || null == parentClass) {
            return false;
        }
        if (value.isAssignableFrom(parentClass)) {
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
     * @return
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
                } catch (ClassNotFoundException e2) {
                }
            }
        }
        return null;
    }

    /**
     * 通过默认类加载器转化类名为类
     *
     * @param className 类名
     * @return
     * @throws ClassNotFoundException
     * @see #getDefaultClassLoader()
     */
    private static Class<?> forNameNoClassLoader(String className) throws ClassNotFoundException {
        if (CLASS_BOOLEAN.equals(className)) {
            return boolean.class;
        }
        if (CLASS_BYTE.equals(className)) {
            return byte.class;
        }
        if (CLASS_CHAR.equals(className)) {
            return char.class;
        }
        if (CLASS_SHORT.equals(className)) {
            return short.class;
        }
        if (CLASS_INT.equals(className)) {
            return int.class;
        }
        if (CLASS_LONG.equals(className)) {
            return long.class;
        }
        if (CLASS_FLOAT.equals(className)) {
            return float.class;
        }
        if (CLASS_DOUBLE.equals(className)) {
            return double.class;
        }
        if (CLASS_BOOLEANS.equals(className)) {
            return boolean[].class;
        }
        if (CLASS_BYTES.equals(className)) {
            return byte[].class;
        }
        if (CLASS_CHARS.equals(className)) {
            return char[].class;
        }
        if (CLASS_SHORTS.equals(className)) {
            return short[].class;
        }
        if (CLASS_INTS.equals(className)) {
            return int[].class;
        }
        if (CLASS_LONGS.equals(className)) {
            return long[].class;
        }
        if (CLASS_FLOATS.equals(className)) {
            return float[].class;
        }
        if (CLASS_DOUBLES.equals(className)) {
            return double[].class;
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
     * @return
     * @throws ClassNotFoundException
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
     * @return
     */
    public static Class<?> forName(final String typeName, List<ClassLoader> classLoaders) {
        return forName(typeName, classLoaders.toArray(new ClassLoader[classLoaders.size()]));
    }

    /**
     * 实例化类
     *
     * @param className    类名
     * @param tClass       类型
     * @param classLoaders 类加载器(默认当前线程类加载器)
     * @return
     */
    public static <T> T forObject(String className, Class<T> tClass, ClassLoader... classLoaders) {
        if (!BooleanHelper.hasLength(classLoaders)) {
            classLoaders = new ClassLoader[]{getDefaultClassLoader()};
        }
        Class<?> aClass = forName(className, classLoaders);
        return (T) forObject(aClass);
    }

    /**
     * 实例化类
     *
     * @param className      类名
     * @param parameterTypes 构造参数
     * @return
     */
    public static <T> T forConstructor(String className, Object... parameterTypes) {
        Class<?> aClass = forName(className);
        try {
            Constructor constructor = null;
            Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
            for (Constructor<?> declaredConstructor : declaredConstructors) {
                Class<?>[] parameterTypes1 = declaredConstructor.getParameterTypes();
                if (parameterTypes1.length != parameterTypes.length) {
                    continue;
                }
                boolean isSimilar = true;
                for (int i = 0; i < parameterTypes1.length; i++) {
                    Class<?> aClass1 = parameterTypes1[i];
                    if (!aClass1.isAssignableFrom(parameterTypes[i].getClass())) {
                        isSimilar = false;
                        break;
                    }
                }

                if (isSimilar) {
                    constructor = declaredConstructor;
                }
            }
            if (null == constructor) {
                return null;
            }
            constructor.setAccessible(true);
            return (T) constructor.newInstance(parameterTypes);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 实例化类
     *
     * @param className      类名
     * @param parameterTypes 构造参数
     * @return
     */
    public static <T> T forConstructor(Class<?> className, Object... parameterTypes) {
        if (null == className) {
            return null;
        }
        return forConstructor(className.getName(), parameterTypes);
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
     * @return
     */
    public static <T> T forObject(Class<? extends T> tClass) {
        if (null == tClass) {
            return null;
        }

        if (tClass.isInterface() || Modifier.isAbstract(tClass.getModifiers())) {
            ProxyAgent abstractProxy = new CglibProxyAgent();
            return (T) abstractProxy.newProxy(tClass);
        }


        boolean hasNoParamConstructor = false;
        boolean isPublic = false;
        try {
            return tClass.newInstance();
        } catch (Throwable e) {
            try {
                Constructor<?>[] constructors = tClass.getDeclaredConstructors();
                for (Constructor<?> constructor : constructors) {
                    if (constructor.getParameterCount() == 0) {
                        hasNoParamConstructor = true;
                        isPublic = Modifier.isPublic(constructor.getModifiers());
                        break;
                    }
                }

                if (!hasNoParamConstructor) {
                    Class<? extends T> aClass = addNoParamConstructor(tClass);
                    try {
                        return aClass.newInstance();
                    } catch (InstantiationException instantiationException) {
                        Loader loader = new Loader();
                        return (T) loader.loadClass(aClass.getName()).newInstance();
                    }
                }

                if (isPublic) {
                    return tClass.newInstance();
                }

                Constructor<? extends T> declaredConstructor = tClass.getDeclaredConstructor();
                return declaredConstructor.newInstance();
            } catch (Exception e1) {
            }
        }

        return null;
    }

    /**
     * 当类没有无参构造，创建无参构造，反之直接返回与<b>原类</b>相同的<b>新类</b>
     * <b>异常</b>则返回<b>原类</b>
     *
     * @param <T>
     * @param aClass 原类
     * @return
     */
    private static <T> Class<? extends T> addNoParamConstructor(Class<? extends T> aClass) {
        String name = aClass.getName();
        String newName = name + JAVASSIST_SUFFIX;
        try {
            ClassPool classPool = getClassPool();
            CtClass oldClass = classPool.get(name);
            try {
                CtClass ctClass = classPool.get(name);
                ctClass.setName(newName);
                oldClass = ctClass;
                oldClass.setSuperclass(classPool.get(name));
                setNewName(oldClass, classPool, newName);
            } catch (NotFoundException e) {
                oldClass.setName(newName);
            }

            boolean hasNoParamsContructor = false;
            CtConstructor[] constructors = oldClass.getConstructors();
            for (CtConstructor constructor : constructors) {
                if (constructor.getParameterTypes().length == 0) {
                    hasNoParamsContructor = true;
                    break;
                }

            }

            if (!hasNoParamsContructor) {
                oldClass.addConstructor(CtNewConstructor.defaultConstructor(oldClass));
            }
            return (Class<? extends T>) oldClass.toClass();
        } catch (Exception e) {
            try {
                return (Class<? extends T>) new Loader().loadClass(newName);
            } catch (ClassNotFoundException classNotFoundException) {
            }
        }
        return aClass;
    }

    /**
     * 获取类设置唯一的新名称
     *
     * @param oldClass          原类
     * @param classPool         类池
     * @param tryCheckClassName 类名称
     * @return
     */
    private static String setNewName(CtClass oldClass, ClassPool classPool, String tryCheckClassName) {
        int i = 0;
        tryCheckClassName = tryCheckClassName + SYMBOL_WELL + i;
        while (true) {
            try {
                classPool.get(tryCheckClassName);
            } catch (NotFoundException e) {
                oldClass.setName(tryCheckClassName);
                break;
            }
            tryCheckClassName = tryCheckClassName + SYMBOL_WELL + (++i);
        }
        return tryCheckClassName;
    }

    /**
     * 实例化类
     *
     * @param clazz        类
     * @param classLoaders 类加载器(默认当前线程类加载器)
     * @return
     */
    public static <T> T forObject(Class<?> clazz, ClassLoader... classLoaders) {
        if (null == clazz) {
            return null;
        }
        if (!BooleanHelper.hasLength(classLoaders)) {
            classLoaders = new ClassLoader[]{getDefaultClassLoader()};
        }
        Class<?> aClass = forName(clazz.getName(), classLoaders);
        return (T) forObject(aClass);
    }

    /**
     * 实例化类
     *
     * @param className 类名
     * @return
     */
    public static <T> T forObject(String className) {
        return forObject(className, getDefaultClassLoader());
    }

    /**
     * 实例化类
     *
     * @param className    类名
     * @param classLoaders 类加载器(默认当前线程类加载器)
     * @return
     */
    public static <T> T forObject(String className, ClassLoader... classLoaders) {
        if (!BooleanHelper.hasLength(classLoaders)) {
            classLoaders = new ClassLoader[]{getDefaultClassLoader()};
        }
        Class<?> aClass = forName(className, classLoaders);
        return (T) forObject(aClass);
    }

    /**
     * 获取类对象
     *
     * @param value  数据
     * @param tClass 类
     * @param <T>
     * @return
     */
    public static <T> T forDefaultObject(final Object value, final Class<T> tClass) {
        T forObject = forObject(value, tClass);
        if (null == forObject) {
            return forObject(tClass);
        }
        return forObject;
    }

    /**
     * 获取类对象
     *
     * @param value  数据
     * @param tClass 类
     * @param <T>
     * @return
     */
    public static <T> T forObject(final Object value, final Class<T> tClass) {
        if (null == value) {
            return null;
        }
        if (null == tClass) {
            return (T) value.toString();
        }

        String simpleName = tClass.getSimpleName().toLowerCase();
        switch (simpleName) {
            case "string":
                return (T) value.toString();
            case "long":
                return (T) Long.valueOf(value.toString());
            case "int":
                return (T) Integer.valueOf(value.toString());
            case "integer":
                return (T) Integer.valueOf(value.toString());
            case "float":
                return (T) Float.valueOf(value.toString());
            case "boolean":
                return (T) Boolean.valueOf(value.toString());
            case "double":
                return (T) Double.valueOf(value.toString());
            case "byte":
                return (T) Byte.valueOf(value.toString());
            case "char":
                return (T) value;
            case "short":
                return (T) Short.valueOf(value.toString());
            case "bigdecimal":
                return (T) new BigDecimal(value.toString());
            default:
                return null;
        }
    }

    /**
     * 将字符串集合转为类集合
     *
     * @param namesOfAllClasses 类名称集合
     * @return
     */
    private static Set<Class<?>> listStringToSetClass(List<String> namesOfAllClasses) {
        Set<Class<?>> sets = new HashSet<>();
        for (String namesOfAllClass : namesOfAllClasses) {
            Class<?> aClass = forName(namesOfAllClass);
            if (null == aClass) {
                continue;
            }
            sets.add(aClass);
        }
        return sets;
    }

    /**
     * 判断接口实现或者子类
     *
     * @param interfaceName  接口名
     * @param implementClass 接口实现/子类
     * @return
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
     * 解释类型
     *
     * @param declaredClass
     * @param <T>
     * @return
     */
    public static <T> Class<T> resolveGenericType(Class<?> declaredClass) {
        ParameterizedType parameterizedType = (ParameterizedType) declaredClass.getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return (Class<T>) actualTypeArguments[0];
    }

    /**
     * 实例化
     *
     * @param clazz 类
     * @param <T>
     * @return
     * @throws Throwable
     */
    public static <T> T instantiateClass(Class<T> clazz) throws Throwable {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            throw new NotSupportedException(clazz.getName());
        }
        try {
            return instantiateClass(clazz.getDeclaredConstructor());
        } catch (NoSuchMethodException ex) {
            throw new NotSupportedException("No default constructor found", ex);
        } catch (LinkageError err) {
            throw new NotSupportedException("Unresolvable class definition", err);
        }
    }

    /**
     * 实例化
     *
     * @param ctor 构造
     * @param args 参数
     * @param <T>
     * @return
     * @throws Throwable
     */
    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws Throwable {
        Assert.notNull(ctor, "Constructor must not be null");
        try {
            makeAccessible(ctor);
            Class<?>[] parameterTypes = ctor.getParameterTypes();
            Assert.isTrue(args.length <= parameterTypes.length, "Can't specify more arguments than constructor parameters");
            Object[] argsWithDefaultValues = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    Class<?> parameterType = parameterTypes[i];
                    argsWithDefaultValues[i] = (parameterType.isPrimitive() ? DEFAULT_TYPE_VALUES.get(parameterType) : null);
                } else {
                    argsWithDefaultValues[i] = args[i];
                }
                return ctor.newInstance(argsWithDefaultValues);
            }
        } catch (InstantiationException ex) {
            throw new NotSupportedException("Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new NotSupportedException("Is the constructor accessible?", ex);
        } catch (IllegalArgumentException ex) {
            throw new NotSupportedException("Illegal arguments for constructor", ex);
        } catch (InvocationTargetException ex) {
            throw new NotSupportedException("Constructor threw exception", ex.getTargetException());
        }
        return null;
    }

    /**
     * 获取类
     *
     * @param object 对象
     * @return
     */
    public static Class<?> getClass(Object object) {
        return BooleanHelper.isClass(object) ? (Class<?>) object : object.getClass();
    }

    /**
     * 是否基础类
     *
     * @param type
     * @return
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
        classNamesList.stream().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Class<?> aClass = forName(s);
                boolean matcher = filter.matcher(aClass);
                if (matcher) {
                    verifyList.add(s);
                }
            }
        });
        return verifyList;
    }

    /**
     * 获取参数类型
     *
     * @param args
     * @return
     */
    public static Class<?>[] toParamType(Object[] args) {
        if (!BooleanHelper.hasLength(args)) {
            return new Class[0];
        }
        Class<?>[] classes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
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
        return null == clazz || Object.class.isAssignableFrom(clazz);
    }
}
