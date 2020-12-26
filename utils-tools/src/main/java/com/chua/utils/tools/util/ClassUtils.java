package com.chua.utils.tools.util;

import com.chua.utils.tools.classes.ClassHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import javassist.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            CtClass ctClass = classPool.makeClass( name + "_" + System.nanoTime());
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
}
