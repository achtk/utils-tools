package com.chua.utils.tools.bean.copy;

import com.chua.utils.tools.named.NamedHelper;
import com.chua.utils.tools.util.ClassUtils;
import javassist.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 标准的bean拷贝
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class ClassBeanCopy<T> extends StandardBeanCopy<T> implements BeanCopy<T> {

    private final String className;

    private final Class<T> tClass;

    private final Map<String, Object> params = new HashMap<>();

    public ClassBeanCopy(String className, Class<T> tClass) {
        super((Class<T>) ClassUtils.forName(className));
        this.className = className;
        this.tClass = tClass;
    }

    public static <T> BeanCopy<T> of(String t, Class<T> tClass) {
        return new ClassBeanCopy<>(t, tClass);
    }

    @Override
    public T create() {
        if (ClassUtils.isPresent(className)) {
            return super.create();
        }

        try {
            ClassPool classPool = ClassUtils.getClassPool();
            CtClass ctClass = classPool.makeClass(className);
            CtClass ctClass1 = classPool.get(tClass.getName());
            if (tClass.isInterface()) {
                ctClass.addInterface(ctClass1);
            } else {
                ctClass.setSuperclass(ctClass1);
            }

            for (Map.Entry<String, Object> entry : withParams.entrySet()) {
                Object value = entry.getValue();
                String key = entry.getKey();
                CtField field = null;
                try {
                    field = ctClass1.getDeclaredField(key);
                } catch (NotFoundException e) {
                }
                if (null != field) {
                    continue;
                }
                String typeName = null;
                if (null == value) {
                    typeName = Object.class.getName();
                } else {
                    typeName = value.getClass().getName();
                }
                CtClass ctClass2 = classPool.get(typeName);
                CtField ctField = new CtField(ctClass2, key, ctClass);
                ctClass.addField(ctField);
                ctClass.addMethod(CtNewMethod.getter("get" + NamedHelper.firstUpperCase(key), ctField));
                ctClass.addMethod(CtNewMethod.setter("set" + NamedHelper.firstUpperCase(key), ctField));
            }
            return (T) BeanCopy.of(ClassUtils.toEntity(ctClass, classPool)).with(withParams).create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
