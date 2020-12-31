package com.chua.utils.tools.util;

import com.chua.utils.tools.classes.ClassHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import javassist.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.chua.utils.tools.constant.StringConstant.JAVASSIST;

/**
 * 类工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/21
 */
public class ClassUtils extends ClassHelper {

    private static final CharSequence DUPLICATE = "duplicate";

    private static final CtClass[] EMPTY = new CtClass[0];
    private static final Map<String, Class<?>> NAME_PRIMITIVE_MAP = new HashMap<>();
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP;
    private static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP;
    private static final Map<String, String> ABBREVIATION_MAP;
    private static final Map<String, String> REVERSE_ABBREVIATION_MAP;

    static {
        NAME_PRIMITIVE_MAP.put("boolean", Boolean.TYPE);
        NAME_PRIMITIVE_MAP.put("byte", Byte.TYPE);
        NAME_PRIMITIVE_MAP.put("char", Character.TYPE);
        NAME_PRIMITIVE_MAP.put("short", Short.TYPE);
        NAME_PRIMITIVE_MAP.put("int", Integer.TYPE);
        NAME_PRIMITIVE_MAP.put("long", Long.TYPE);
        NAME_PRIMITIVE_MAP.put("double", Double.TYPE);
        NAME_PRIMITIVE_MAP.put("float", Float.TYPE);
        NAME_PRIMITIVE_MAP.put("void", Void.TYPE);

        PRIMITIVE_WRAPPER_MAP = new HashMap<>();
        PRIMITIVE_WRAPPER_MAP.put(Boolean.TYPE, Boolean.class);
        PRIMITIVE_WRAPPER_MAP.put(Byte.TYPE, Byte.class);
        PRIMITIVE_WRAPPER_MAP.put(Character.TYPE, Character.class);
        PRIMITIVE_WRAPPER_MAP.put(Short.TYPE, Short.class);
        PRIMITIVE_WRAPPER_MAP.put(Integer.TYPE, Integer.class);
        PRIMITIVE_WRAPPER_MAP.put(Long.TYPE, Long.class);
        PRIMITIVE_WRAPPER_MAP.put(Double.TYPE, Double.class);
        PRIMITIVE_WRAPPER_MAP.put(Float.TYPE, Float.class);
        PRIMITIVE_WRAPPER_MAP.put(Void.TYPE, Void.TYPE);

        WRAPPER_PRIMITIVE_MAP = new HashMap<>();
        Iterator var0 = PRIMITIVE_WRAPPER_MAP.entrySet().iterator();

        while (var0.hasNext()) {
            Map.Entry<Class<?>, Class<?>> entry = (Map.Entry) var0.next();
            Class<?> primitiveClass = entry.getKey();
            Class<?> wrapperClass = entry.getValue();
            if (!primitiveClass.equals(wrapperClass)) {
                WRAPPER_PRIMITIVE_MAP.put(wrapperClass, primitiveClass);
            }
        }

        Map<String, String> m = new HashMap();
        m.put("int", "I");
        m.put("boolean", "Z");
        m.put("float", "F");
        m.put("long", "J");
        m.put("short", "S");
        m.put("byte", "B");
        m.put("double", "D");
        m.put("char", "C");
        Map<String, String> r = new HashMap();
        Iterator var6 = m.entrySet().iterator();

        while (var6.hasNext()) {
            Map.Entry<String, String> e = (Map.Entry) var6.next();
            r.put(e.getValue(), e.getKey());
        }

        ABBREVIATION_MAP = Collections.unmodifiableMap(m);
        REVERSE_ABBREVIATION_MAP = Collections.unmodifiableMap(r);
    }

    /**
     * 指定行插入代码
     *
     * @param object     对象
     * @param methodName 方法
     * @param line       行数
     * @param code       代码
     * @return 新对象
     */
    public static java.lang.Object insertCode(final java.lang.Object object, final String methodName, final int line, final String code) throws Exception {
        Map<Integer, String> codes = Maps.newHashMap();
        codes.put(line, code);
        return insertCode(object, methodName, codes);
    }

    /**
     * 指定行插入代码
     *
     * @param object 对象
     * @param line   行数
     * @param code   代码
     * @return 新对象
     */
    public static java.lang.Object insertCode(final java.lang.Object object, final int line, final String code) throws Exception {
        Map<Integer, String> codes = Maps.newHashMap();
        codes.put(line, code);
        return insertCode(object, null, codes);
    }

    /**
     * 指定行插入代码
     *
     * @param object     对象
     * @param methodName 方法
     * @param codes      代码
     * @return 新对象
     */
    public static java.lang.Object insertCode(final java.lang.Object object, final String methodName, final Map<Integer, String> codes) throws Exception {
        if (null == object || null == codes || codes.isEmpty()) {
            return object;
        }

        String name = object.getClass().getName();

        ClassPool classPool = getClassPool();
        CtClass ctClass = classPool.makeClass(name + JAVASSIST);
        CtClass oldCtClass = classPool.get(name);

        ctClass.setSuperclass(oldCtClass);
        ctClass.setInterfaces(oldCtClass.getInterfaces());
        ctClass.setModifiers(Modifier.PUBLIC);

        for (CtField field : oldCtClass.getDeclaredFields()) {
            CtField ctField = new CtField(field, ctClass);
            ctField.setModifiers(Modifier.PUBLIC);
            ctClass.addField(ctField);
        }

        for (CtConstructor constructor : oldCtClass.getDeclaredConstructors()) {
            CtConstructor ctConstructor = new CtConstructor(constructor.getParameterTypes(), ctClass);
            if (ctConstructor.getParameterTypes().length == 0) {
                continue;
            }
            ctConstructor.setModifiers(Modifier.PUBLIC);
            ctClass.addConstructor(ctConstructor);
        }

        CtConstructor ctConstructor = new CtConstructor(new CtClass[0], ctClass);
        ctConstructor.setBody("{}");
        ctClass.addConstructor(ctConstructor);


        CtMethod[] methods;
        if (Strings.isNullOrEmpty(methodName)) {
            methods = oldCtClass.getDeclaredMethods();
        } else {
            methods = new CtMethod[]{oldCtClass.getDeclaredMethod(methodName)};
        }
        for (CtMethod method : methods) {
            for (Map.Entry<Integer, String> entry : codes.entrySet()) {
                if (entry.getKey() == 0) {
                    method.insertBefore(entry.getValue());
                } else if (entry.getKey() == -1) {
                    method.insertAfter(entry.getValue());
                } else {
                    method.insertAt(entry.getKey(), true, entry.getValue());
                }
            }
            ClassMap classMap = new ClassMap();

            CtMethod ctMethod = CtNewMethod.copy(method, ctClass, classMap);
            ctMethod.setModifiers(Modifier.PUBLIC);
            ctClass.addMethod(ctMethod);
        }

        ctClass.setModifiers(Modifier.PUBLIC);
        ctClass.writeFile();
        return toUniqueEntity(ctClass, classPool);
    }

    /**
     * 获取唯一性的对象
     *
     * @param ctClass   对象
     * @param classPool 类池
     * @return 唯一性对象
     */
    public static java.lang.Object toUniqueEntity(CtClass ctClass, ClassPool classPool) {
        try {
            return toEntity(ctClass, classPool);
        } catch (Exception e) {
            if (!e.getMessage().contains(DUPLICATE)) {
                e.printStackTrace();
                return null;
            }
            String name = ctClass.getName();
            AtomicInteger atomicInteger = new AtomicInteger();
            while (true) {
                ctClass.defrost();
                ctClass.setName(name + "#" + atomicInteger.getAndIncrement());
                java.lang.Object entity = null;
                try {
                    entity = toEntity(ctClass, classPool);
                } catch (Throwable exception) {
                    if (!exception.getMessage().contains(DUPLICATE)) {
                        exception.printStackTrace();
                        return null;
                    }
                    continue;
                }
                return entity;
            }
        }
    }

    /**
     * 简单添加接口
     *
     * @param bean       对象
     * @param interfaces 接口
     */
    public static Class<?> doAddInterfaceForClass(Object bean, Class<?>... interfaces) {
        return doAddInterfaceForClass(bean, null, null, interfaces);
    }

    /**
     * 简单添加接口并创建接口方法
     *
     * @param bean                 对象
     * @param methodStringFunction 方法拦截器
     * @param interfaces           接口
     */
    public static Class<?> doAddInterfaceForClass(final Object bean, final Function<CtMethod, String> methodStringFunction, final Class<?>... interfaces) {
        return doAddInterfaceForClass(bean, methodStringFunction, null, interfaces);
    }

    /**
     * 简单添加接口并创建接口方法
     *
     * @param bean                 对象
     * @param methodStringFunction 方法拦截器
     * @param preProcessing        预处理处理器
     * @param interfaces           接口
     */
    public static Class<?> doAddInterfaceForClass(final Object bean, final Function<CtMethod, String> methodStringFunction, final BiConsumer<CtClass, ClassPool> preProcessing, final Class<?>... interfaces) {
        if (null == bean || null == interfaces) {
            return null;
        }

        return doAddInterfaceForClass(bean, (ctClass, classPool) -> {
            try {
                preProcessing.accept(ctClass, classPool);

                for (Class<?> anInterface : interfaces) {
                    final CtClass interfaceCtClass = classPool.get(anInterface.getName());
                    ctClass.addInterface(interfaceCtClass);

                    if (null != methodStringFunction) {

                        for (CtMethod method : interfaceCtClass.getDeclaredMethods()) {
                            String apply = methodStringFunction.apply(method);
                            if (Strings.isNullOrEmpty(apply)) {
                                continue;
                            }
                            CtMethod ctMethod = CtNewMethod.make(
                                    Modifier.PUBLIC,
                                    method.getReturnType(),
                                    method.getName(),
                                    method.getParameterTypes(),
                                    method.getExceptionTypes(),
                                    apply, ctClass);

                            ctMethod.setModifiers(Modifier.PUBLIC);
                            ctClass.addMethod(ctMethod);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 简单添加接口并创建接口方法
     *
     * @param bean      对象
     * @param processor 处理器
     */
    public static Class<?> doAddInterfaceForClass(final Object bean, final BiConsumer<CtClass, ClassPool> processor) {
        if (null == bean) {
            return null;
        }

        ClassPool classPool = getClassPool();
        try {

            String name = bean.getClass().getName();
            name = name.replace("/", "$");
            CtClass ctClass = classPool.makeClass(name + "_" + System.nanoTime());
            if (null != processor) {
                processor.accept(ctClass, classPool);
            }
            ctClass.setModifiers(Modifier.PUBLIC);
            return ctClass.toClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 类转Ct类
     *
     * @param classes 类
     * @return ct类
     */
    public static CtClass toCtClass(Class<?> classes) throws Exception {
        if (null == classes) {
            return null;
        }
        ClassPool classPool = getClassPool();

        return classPool.get(classes.getName());
    }

    /**
     * 类转Ct类
     *
     * @param classes 类
     * @return ct类
     */
    public static CtClass[] toCtClass(Class<?>[] classes) {
        if (BooleanUtils.isEmpty(classes)) {
            return EMPTY;
        }
        ClassPool classPool = getClassPool();

        List<CtClass> ctClassList = new ArrayList<>(classes.length);
        for (Class<?> aClass : classes) {
            try {
                ctClassList.add(toCtClass(aClass));
            } catch (Exception e) {
                return EMPTY;
            }
        }
        return ctClassList.toArray(EMPTY);
    }

    /**
     * 设置字段权限
     *
     * @param field 字段
     */
    public static void setAccessible(Field field) {
        field.setAccessible(true);
    }

    /**
     * 设置方法权限
     *
     * @param method 方法
     */
    public static void setAccessible(Method method) {
        method.setAccessible(true);
    }

    /**
     * 包裹类
     *
     * @param cls 类
     * @return 包裹类
     */
    public static Class<?> wrapperToPrimitive(Class<?> cls) {
        return WRAPPER_PRIMITIVE_MAP.get(cls);
    }

    /**
     * 原始包裹类
     *
     * @param cls 类
     * @return 原始包裹类
     */
    public static Class<?> primitiveToWrapper(Class<?> cls) {
        Class<?> convertedClass = cls;
        if (cls != null && cls.isPrimitive()) {
            convertedClass = PRIMITIVE_WRAPPER_MAP.get(cls);
        }

        return convertedClass;
    }
}
