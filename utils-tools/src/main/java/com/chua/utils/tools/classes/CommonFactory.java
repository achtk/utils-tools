package com.chua.utils.tools.classes;

import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 通用工厂
 * @author CH
 * @since 1.0
 */
public class CommonFactory {
    /**
     *
     * @param annotation
     * @return
     */
    public Annotation getAnnnotation(java.lang.annotation.Annotation annotation, ConstPool constPool) {
        Annotation annotation1 = new Annotation(annotation.toString(), constPool);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Field field = ClassHelper.forField(invocationHandler.getClass(), "memberValues");
        try {
            Map<String, Object> map = ClassHelper.forFieldValue(field, invocationHandler, Map.class);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                addMemberValue(entry.getKey(), entry.getValue(), annotation1, constPool);
            }
            return annotation1;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     *
     * @param key
     * @param value
     * @param annotation1
     * @param constPool
     */
    public void addMemberValue(String key, Object value, Annotation annotation1, ConstPool constPool) {
        if(value instanceof Integer) {
            annotation1.addMemberValue(key, new IntegerMemberValue((Integer) value, constPool));
        } else if(value instanceof String) {
            annotation1.addMemberValue(key, new StringMemberValue((String) value, constPool));
        } else if(value instanceof Short) {
            annotation1.addMemberValue(key, new ShortMemberValue((Short) value, constPool));
        }  else if(value instanceof Class) {
            annotation1.addMemberValue(key, new ClassMemberValue(((Class) value).getName(), constPool));
        }  else if(value instanceof Byte) {
            annotation1.addMemberValue(key, new ByteMemberValue((Byte) value, constPool));
        }  else if(value instanceof Character) {
            annotation1.addMemberValue(key, new CharMemberValue((Character) value, constPool));
        }  else if(value instanceof Boolean) {
            annotation1.addMemberValue(key, new BooleanMemberValue((boolean) value, constPool));
        }  else if(value instanceof Long) {
            annotation1.addMemberValue(key, new LongMemberValue((Long) value, constPool));
        }  else if(value instanceof Float) {
            annotation1.addMemberValue(key, new FloatMemberValue((Float) value, constPool));
        }
    }
}
