package com.chua.utils.tools.util;

import com.chua.utils.tools.classes.ClassHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import javassist.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * 指定行插入代码
     *
     * @param object     对象
     * @param methodName 方法
     * @param line       行数
     * @param code       代码
     * @return 新对象
     */
    public static Object insertCode(final Object object, final String methodName, final int line, final String code) throws Exception {
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
    public static Object insertCode(final Object object, final int line, final String code) throws Exception {
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
    public static Object insertCode(final Object object, final String methodName, final Map<Integer, String> codes) throws Exception {
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
    public static Object toUniqueEntity(CtClass ctClass, ClassPool classPool) {
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
                Object entity = null;
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

}
