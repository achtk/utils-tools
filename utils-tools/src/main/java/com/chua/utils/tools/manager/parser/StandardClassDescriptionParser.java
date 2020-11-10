package com.chua.utils.tools.manager.parser;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.manager.parser.description.FieldDescription;
import com.chua.utils.tools.manager.parser.description.MethodDescription;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 标准的类解析器
 *
 * @param <T> 解析的类
 * @author CH
 * @version 1.0.0
 * @see com.chua.utils.tools.manager.parser.ClassDescriptionParser
 * @since 2020/11/10
 */
public class StandardClassDescriptionParser<T> implements ClassDescriptionParser<T> {
    /**
     * 待解析的类
     */
    private Class<T> tClass;
    /**
     * 方法替换对象
     */
    private final ConcurrentMap<String, String> methodReplaceCache = new ConcurrentHashMap<>();

    public StandardClassDescriptionParser(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public Collection<FieldDescription<T>> fieldDescription() {
        if (null == tClass) {
            return null;
        }
        List<FieldDescription<T>> fieldDescriptions = new ArrayList<>();
        ClassHelper.doWithLocalFields(tClass, field -> {
            FieldDescription<T> fieldDescription = new FieldDescription<>();
            fieldDescription.setTClass(tClass);
            fieldDescription.setField(field);

            fieldDescriptions.add(fieldDescription);
        });
        return fieldDescriptions;
    }

    @Override
    public Collection<MethodDescription<T>> methodDescription() {
        if (null == tClass) {
            return null;
        }
        List<MethodDescription<T>> methodDescriptions = new ArrayList<>();
        ClassHelper.doWithLocalMethods(tClass, method -> {
            MethodDescription<T> tMethodDescription = new MethodDescription<>();
            tMethodDescription.setTClass(tClass);
            tMethodDescription.setMethod(method);

            methodDescriptions.add(tMethodDescription);
        });
        return methodDescriptions;
    }

    @Override
    public Collection<Class<?>> interfaceDescription() {
        return ClassHelper.getInterfaces(tClass);
    }

    @Override
    public Collection<Class<?>> superDescription() {
        return ClassHelper.getSuperClass(tClass);
    }

    @Override
    public ClassModifyDescriptionParser modify() {
        try {
            return new StandardClassModifyDescriptionParser<>(tClass, this);
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean isSubType(String interfaceName) {
        Class<?> aClass = ClassHelper.forName(interfaceName);
        if (null == aClass) {
            return false;
        }
        return tClass.isAssignableFrom(aClass);
    }
}
