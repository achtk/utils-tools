package com.chua.utils.tools.classes;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.classes.adaptor.AsmAdaptor;
import com.chua.utils.tools.classes.callback.FieldCallback;
import com.chua.utils.tools.classes.callback.MethodCallback;
import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.exceptions.NonUniqueException;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.manager.parser.description.FieldDescription;
import com.chua.utils.tools.named.NamedHelper;
import com.chua.utils.tools.storage.CacheStorage;
import com.google.common.base.Strings;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 反射工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/11
 */
public class ReflectionHelper {

    /**
     * 类-字段关系
     */
    private static final CacheProvider<Class<?>, List<Field>> CLASS_FIELD = new ConcurrentCacheProvider<>(256);
    /**
     * 类-字段关系
     */
    private static final CacheProvider<Class<?>, List<Field>> CLASS_FIELD_LOCAL = new ConcurrentCacheProvider<>(256);
    /**
     * 类-方法关系
     */
    private static final CacheProvider<Class<?>, List<Method>> CLASS_METHOD = new ConcurrentCacheProvider<>(256);
    /**
     * 类-方法关系
     */
    private static final CacheProvider<Class<?>, List<Method>> CLASS_METHOD_LOCAL = new ConcurrentCacheProvider<>(256);
    /**
     * 类-构造关系
     */
    private static final CacheProvider<Class<?>, List<Method>> CLASS_CONSTRUCTOR = new ConcurrentCacheProvider<>(256);
    /**
     * 类-构造关系
     */
    private static final CacheProvider<Class<?>, List<Method>> CLASS_CONSTRUCTOR_LOCAL = new ConcurrentCacheProvider<>(256);
    /**
     * 类-父类关系
     */
    private static final CacheProvider<Class<?>, List<Class<?>>> CLASS_SUPER = new ConcurrentCacheProvider<>(256);
    /**
     * 类-接口关系
     */
    private static final CacheProvider<Class<?>, List<Class<?>>> CLASS_INTERFACES = new ConcurrentCacheProvider<>(256);


    /**
     * 是否有无参构造
     *
     * @param aClass 类
     * @return 有无参构造返回true, 如果为null返回false
     */
    public static boolean hasNoArgsConstructor(final Class<?> aClass) {
        if (null == aClass) {
            return false;
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        doWithLocalConstructors(aClass, constructor -> {
            if (constructor.getParameters().length == 0) {
                atomicBoolean.set(true);
            }
        });

        return atomicBoolean.get();
    }

    /**
     * 获取本地所有构造
     *
     * @param aClass  类
     * @param matcher 比较器
     * @return 所有构造
     */
    public static void doWithLocalConstructors(final Class<?> aClass, final Matcher<Constructor<?>> matcher) {
        for (Constructor<?> constructor : getLocalConstructors(aClass)) {
            try {
                matcher.doWith(constructor);
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * 获取本地所有构造
     *
     * @param object 对象
     * @return 所有构造
     */
    public static List<Constructor<?>> getLocalConstructors(final Object object) {
        if (null == object) {
            return Collections.emptyList();
        }
        final Class<?> aClass = ClassHelper.getClass(object);

        return CacheStorage.doWith(() -> {
            Constructor<?>[] constructors = aClass.getDeclaredConstructors();
            return Arrays.asList(constructors);
        }, aClass, CLASS_CONSTRUCTOR_LOCAL);
    }

    /**
     * 获取所有构造
     *
     * @param object 对象
     * @return 所有构造
     */
    public static List<Constructor<?>> getConstructors(final Object object) {
        if (null == object) {
            return Collections.emptyList();
        }
        final Class<?> aClass = ClassHelper.getClass(object);

        return CacheStorage.doWith(() -> {
            List<Constructor<?>> result = new ArrayList<>();
            Class<?> newClass = aClass;
            while (!ClassHelper.isObject(newClass)) {
                Constructor<?>[] constructors = newClass.getDeclaredConstructors();
                result.addAll(Arrays.asList(constructors));
                newClass = newClass.getSuperclass();
            }
            return result;
        }, aClass, CLASS_CONSTRUCTOR);
    }

    /**
     * 获取对象总的属性
     *
     * @param object    对象
     * @param fieldName 字段名称
     * @return 字段值
     */
    public static Object getOnlyFieldValue(final Object object, final String fieldName) {
        return getOnlyFieldValue(object, fieldName, Object.class);
    }

    /**
     * 获取对象总的属性
     *
     * @param object     对象
     * @param fieldName  字段名称
     * @param returnType 返回类型(及字段类型)
     * @param <T>        返回类型
     * @return 字段值
     */
    public static <T> T getOnlyFieldValue(final Object object, final String fieldName, final Class<T> returnType) {
        if (null == object || Strings.isNullOrEmpty(fieldName)) {
            return null;
        }

        final Class<?> fieldType = null == returnType ? Object.class : returnType;
        final List<Field> items = new ArrayList<>();

        doWithFields(object.getClass(), field -> {
            if (fieldName.equals(field.getName()) && fieldType.isAssignableFrom(field.getType())) {
                items.add(field);
            }
        });

        if (items.size() != 1) {
            throw new NonUniqueException();
        }

        Field field = items.get(0);
        field.setAccessible(true);
        try {
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有字段值
     *
     * @param obj 对象
     * @return
     */
    public static Map<String, Object> getFieldsValueAsMap(final Object obj) {
        List<Field> fieldList = getFields(obj);
        Map<String, Object> result = new HashMap<>(fieldList.size());
        for (Field field : fieldList) {
            try {
                result.put(field.getName(), FieldDescription.get(field, obj));
            } catch (Throwable ignored) {
            }
        }
        return result;
    }

    /**
     * 获取字段值
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param fieldType 字段类型
     * @return 值
     */
    public static Object getFieldValue(final Object obj, final String fieldName, final String fieldType) {
        if (null == obj || null == fieldName) {
            return null;
        }

        ClassReader classReader = null;
        try {
            classReader = new ClassReader(obj.getClass().getName());
        } catch (IOException ignore) {
        }
        if (null == classReader) {
            List<Field> field = getField(obj, fieldName);
            Optional<Field> first = field.stream().filter(field1 -> {
                return field1.getType().getName().equals(fieldType);
            }).findFirst();
            if (first.isPresent()) {
                return getFieldValue(obj, first.get());
            }
            return null;
        }
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        MethodVisitor mv = classNode.visitMethod(Opcodes.AALOAD, "get" + NamedHelper.firstUpperCase(fieldName), fieldType, null, null);
        mv.visitFieldInsn(Opcodes.GETFIELD, Type.getInternalName(ClassHelper.forName(fieldType)), "str", fieldType);
        Optional<FieldNode> first = classNode.fields.stream().filter(fieldNode -> {
            return fieldNode.name.equals(fieldName) && AsmAdaptor.resolveNewName(fieldNode.desc).equals(fieldType);
        }).findFirst();

        if (first.isPresent()) {
            return first.get().value;
        }
        return null;
    }

    /**
     * 获取字段值
     *
     * @param obj   对象
     * @param field 字段
     * @return 值
     */
    public static Object getFieldValue(final Object obj, final Field field) {
        if (null == obj || null == field) {
            return null;
        }

        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 获取所有字段
     *
     * @param obj 对象
     * @return 字段名-字段
     */
    public static Map<String, Field> getFieldsAsMap(final Object obj) {
        List<Field> members = getFields(obj);
        Map<String, Field> result = new HashMap<>(members.size());
        for (Field member : members) {
            result.put(member.getName(), member);
        }
        return result;
    }

    /**
     * 字段查询
     *
     * @param object    处理对象
     * @param fieldName 字段名称
     * @return 字段
     */
    public static Field getFieldIfOnly(final Object object, final String fieldName) {
        List<Field> fieldList = getField(object, fieldName);
        return null == fieldList || fieldList.size() != 1 ? null : fieldList.get(0);
    }

    /**
     * 字段查询
     *
     * @param object    处理对象
     * @param fieldName 字段名称
     * @return 字段
     */
    public static List<Field> getField(final Object object, final String fieldName) {
        if (null == fieldName) {
            return null;
        }
        return getFields(ClassHelper.getClass(object)).stream().filter(field -> {
            return fieldName.equals(field.getName());
        }).collect(Collectors.toList());
    }

    /**
     * 字段查询
     *
     * @param object    处理对象
     * @param fieldName 字段名称
     * @return 字段
     */
    public static Field getLocalField(final Object object, final String fieldName) {
        if (null == fieldName) {
            return null;
        }

        for (Field field : getFields(ClassHelper.getClass(object))) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    /**
     * 字段查询
     *
     * @param aClass   处理类
     * @param callback 回调
     */
    public static void doWithFields(final Class<?> aClass, final FieldCallback callback) {
        for (Field field : getFields(aClass)) {
            try {
                callback.doWith(field);
            } catch (Throwable ex) {
                throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
            }
        }
    }

    /**
     * 字段查询
     *
     * @param aClass   处理类
     * @param callback 回调
     */
    public static void doWithLocalFields(final Class<?> aClass, final FieldCallback callback) {
        for (Field field : getLocalFields(aClass)) {
            try {
                callback.doWith(field);
            } catch (Throwable ex) {
                throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
            }
        }
    }

    /**
     * 获取所有字段
     *
     * @param obj            对象
     * @param annotationType 注解
     * @return 所有字段
     */
    public static List<Field> getAnnotationFields(final Object obj, final Class<? extends Annotation>... annotationType) {
        return getFields(obj, annotationType);
    }

    /**
     * 获取所有字段
     *
     * @param obj            对象
     * @param annotationType 注解
     * @return 所有字段
     */
    public static List<Field> getFields(final Object obj, final Class<? extends Annotation>[] annotationType) {
        if (null == obj) {
            return Collections.emptyList();
        }
        final Class<?> aClass = ClassHelper.getClass(obj);

        if (!BooleanHelper.hasLength(annotationType)) {
            return CacheStorage.doWith(() -> {
                List<Field> result = new ArrayList<>();
                Class<?> newClass = aClass;
                while (!ClassHelper.isObject(newClass)) {
                    Field[] fields = newClass.getDeclaredFields();
                    result.addAll(Arrays.asList(fields));
                    newClass = newClass.getSuperclass();
                }
                return result;
            }, aClass, CLASS_FIELD_LOCAL);
        }

        List<Field> result = new ArrayList<>();
        Class<?> newClass = aClass;
        while (!ClassHelper.isObject(newClass)) {
            Field[] fields = newClass.getDeclaredFields();
            result.addAll(Arrays.stream(fields).parallel().filter(method -> {
                for (Class<? extends Annotation> aClass1 : annotationType) {
                    Annotation annotation = method.getDeclaredAnnotation(aClass1);
                    if (null == annotation) {
                        continue;
                    }
                    return true;
                }
                return false;
            }).collect(Collectors.toList()));
            newClass = newClass.getSuperclass();
        }
        return result;
    }

    /**
     * 获取所有字段
     *
     * @param obj 对象
     * @return 所有字段
     */
    public static List<Field> getFields(final Object obj) {
        return getFields(obj, null);
    }

    /**
     * 获取当前类所有字段
     *
     * @param obj 对象
     * @return 所有字段
     */
    public static List<Field> getLocalFields(final Object obj) {
        if (null == obj) {
            return Collections.emptyList();
        }
        final Class<?> aClass = ClassHelper.getClass(obj);

        return CacheStorage.doWith(() -> {
            Field[] fields = aClass.getDeclaredFields();
            return Arrays.asList(fields);
        }, aClass, CLASS_FIELD);
    }

    /**
     * 方法回调
     *
     * @param aClass   类
     * @param callback 方法回调
     */
    public static void doWithMethods(final Class<?> aClass, final MethodCallback callback) {
        if (null == aClass || null == callback) {
            return;
        }
        for (Method method : getMethods(aClass)) {
            try {
                callback.doWith(method);
            } catch (Throwable throwable) {
                throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + throwable);
            }
        }
    }

    /**
     * 方法回调
     *
     * @param aClass   类
     * @param callback 方法回调
     */
    public static void doWithLocalMethods(final Class<?> aClass, final MethodCallback callback) {
        if (null == aClass || null == callback) {
            return;
        }
        for (Method method : getLocalMethods(aClass)) {
            try {
                callback.doWith(method);
            } catch (Throwable throwable) {
                throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + throwable);
            }
        }
    }

    /**
     * 获取所有方法
     *
     * @param obj 对象
     * @return 所有字段
     */
    public static List<Method> getMethods(final Object obj) {
        return getMethods(obj, null);
    }

    /**
     * 获取所有方法
     *
     * @param obj 对象
     * @return 所有字段
     */
    public static List<Method> getAnnotationMethods(final Object obj, final Class<? extends Annotation>... annotationType) {
        return getMethods(obj, annotationType);
    }

    /**
     * 获取所有方法
     *
     * @param obj            对象
     * @param annotationType 注解
     * @return 所有字段
     */
    public static List<Method> getMethods(final Object obj, final Class<? extends Annotation>[] annotationType) {
        if (null == obj) {
            return Collections.emptyList();
        }
        final Class<?> aClass = ClassHelper.getClass(obj);

        if (!BooleanHelper.hasLength(annotationType)) {
            return CacheStorage.doWith(() -> {
                List<Method> result = new ArrayList<>();
                Class<?> newClass = aClass;
                while (!ClassHelper.isObject(newClass)) {
                    Method[] methods = newClass.getDeclaredMethods();
                    result.addAll(Arrays.asList(methods));
                    newClass = newClass.getSuperclass();
                }
                return result;
            }, aClass, CLASS_METHOD);
        }
        List<Method> result = new ArrayList<>();
        Class<?> newClass = aClass;
        while (!ClassHelper.isObject(newClass)) {
            Method[] methods = newClass.getDeclaredMethods();
            result.addAll(Arrays.stream(methods).parallel().filter(method -> {
                for (Class<? extends Annotation> aClass1 : annotationType) {
                    Annotation annotation = method.getDeclaredAnnotation(aClass1);
                    if (null == annotation) {
                        continue;
                    }
                    return true;
                }
                return false;
            }).collect(Collectors.toList()));
            newClass = newClass.getSuperclass();
        }
        return result;
    }

    /**
     * 获取所有方法
     *
     * @param obj 对象
     * @return 所有字段
     */
    public static List<Method> getLocalMethods(final Object obj) {
        if (null == obj) {
            return Collections.emptyList();
        }
        final Class<?> aClass = ClassHelper.getClass(obj);

        return CacheStorage.doWith(() -> {
            Method[] methods = aClass.getDeclaredMethods();
            return Arrays.asList(methods);
        }, aClass, CLASS_METHOD_LOCAL);
    }

    /**
     * 获取注解的值
     *
     * @param annotation 注解
     * @return 注解参数值
     */
    public static Map<String, Object> getAnnotationValue(final Annotation annotation) {
        if (null == annotation) {
            return null;
        }
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Field field = getLocalField(invocationHandler, "memberValues");
        if (null == field) {
            return Collections.emptyMap();
        }
        Map<String, Map<String, Object>> memberValues = FieldDescription.get(field, invocationHandler, EmptyOrBase.MAP_MAP_OBJECT);
        return null != memberValues ? memberValues.get("memberValues") : Collections.emptyMap();
    }

    /**
     * 接口
     *
     * @param clazz   类
     * @param matcher 回调
     */
    public static void doWithInterface(Class<?> clazz, Matcher<Class<?>> matcher) {
        for (Class<?> anInterface : getInterfaces(clazz)) {
            try {
                matcher.doWith(anInterface);
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * 获取所有接口
     *
     * @param aClass 类
     */
    public static Collection<Class<?>> getInterfaces(final Class<?> aClass) {
        if (null == aClass) {
            return null;
        }
        return CacheStorage.doWith(() -> {
            List<Class<?>> result = new ArrayList<>();
            Class<?>[] interfaces = aClass.getInterfaces();
            Class<?> superClass = aClass.getSuperclass();

            CollectionHelper.add(result, interfaces);

            while (!ClassHelper.isObject(superClass)) {
                Class<?>[] interfaces1 = superClass.getInterfaces();
                CollectionHelper.add(result, interfaces1);

                superClass = superClass.getSuperclass();

            }
            return result;
        }, aClass, CLASS_INTERFACES);
    }

    /**
     * 获取超类接口
     *
     * @param aClass 类
     */
    public static Collection<Class<?>> getSuperClass(final Class<?> aClass) {
        if (null == aClass) {
            return null;
        }
        return CacheStorage.doWith(() -> {
            List<Class<?>> result = new ArrayList<>();
            Class<?> superClass = aClass.getSuperclass();

            CollectionHelper.add(result, superClass);

            while (!ClassHelper.isObject(superClass)) {
                superClass = superClass.getSuperclass();
                CollectionHelper.add(result, superClass);
            }
            return result;
        }, aClass, CLASS_SUPER);
    }


    /**
     * 方法拆解
     * <p>
     * 拆解方法中的注解，参数，基础信息等
     * </p>
     * 返回信息包含下列信息: <br />
     * <table border=1 cellpadding=5>
     *     <thead>
     *         <tr>
     *             <td>字段</td>
     *             <td>描述</td>
     *         </tr>
     *     </thead>
     *     <tbody>
     *         <tr>
     *             <td>method.name</td>
     *             <td>方法名</td>
     *         </tr>
     *         <tr>
     *             <td>method.return</td>
     *             <td>返回值类型</td>
     *         </tr>
     *         <tr>
     *             <td>method.annotation[index]</td>
     *             <td>注解名称(类型)</td>
     *         </tr>
     *         <tr>
     *             <td>method.parameter[index].name</td>
     *             <td>参数名称</td>
     *         </tr>
     *         <tr>
     *             <td>method.parameter[index].type</td>
     *             <td>参数类型</td>
     *         </tr>
     *     </tbody>
     * </table>
     *
     * @param method 方法
     * @return
     */
    public static Map<String, Object> disassembleForMethod(final Method method) {
        if (null == method) {
            return null;
        }
        //获取所有注解
        Annotation[] annotations = method.getDeclaredAnnotations();
        //获取方法参数
        Parameter[] parameters = method.getParameters();

        Map<String, Object> result = new HashMap<>(annotations.length + parameters.length + 3);

        //获取方法名
        result.put("method.name", method.getName());
        //获取方法返回值
        result.put("method.return", method.getReturnType().getName());

        for (int i = 0; i < annotations.length; i++) {
            Annotation annotation = annotations[i];
            result.put("method.annotation[" + i + "].name", annotation.annotationType().getName());
        }

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            result.put("method.parameter[" + i + "].name", parameter.getName());
            result.put("method.parameter[" + i + "].type", parameter.getType().getName());
        }

        return result;
    }


    /**
     * 获取指定注解, 没有返回空
     *
     * @param aClass         类
     * @param annotationType 注解
     * @return
     */
    public static <T> T getAnnotation(Class<?> aClass, Class<T> annotationType) {
        if (null == aClass || null == annotationType) {
            return null;
        }
        String annotationTypeName = annotationType.getName();
        Annotation[] annotations = aClass.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (!annotationTypeName.equals(annotation.annotationType().getName())) {
                continue;
            }
            return (T) annotation;
        }
        return null;
    }
}
