package com.chua.utils.tools.classes;

import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.function.able.InitializingCacheable;
import javassist.*;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static com.chua.utils.tools.constant.StringConstant.JAVASSIST;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_WELL;

/**
 * javassist工具类
 *
 * @author CH
 * @date 2020-09-26
 */
public class JavassistHelper extends InitializingCacheable {


    /**
     * 获取 classPool
     *
     * @return classPool
     */
    public static ClassPool getClassPool() {
        ClassPool classPool = new ClassPool(true);
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        return classPool;
    }

    /**
     * 当类没有无参构造，创建无参构造，反之直接返回与<b>原类</b>相同的<b>新类</b>
     * <b>异常</b>则返回<b>原类</b>
     *
     * @param <T>    类型
     * @param aClass 原类
     * @return 类
     */
    @SuppressWarnings("all")
    protected static <T> Class<? extends T> addNoParamConstructor(Class<? extends T> aClass) {
        String name = aClass.getName();
        String newName = name + JAVASSIST;
        CtClass oldClass = null;
        try {
            ClassPool classPool = getClassPool();
            oldClass = classPool.get(name);
            try {
                CtClass ctClass = classPool.get(name);
                ctClass.setName(newName);
                oldClass = ctClass;
                oldClass.setModifiers(Modifier.PUBLIC);
                oldClass.setSuperclass(classPool.get(name));
                setNewName(oldClass, classPool, newName);
            } catch (NotFoundException e) {
                oldClass.setName(newName);
            }

            boolean hasNoParamsContructor = ReflectionHelper.hasNoArgsConstructor(aClass);

            if (!hasNoParamsContructor) {
                oldClass.addConstructor(CtNewConstructor.defaultConstructor(oldClass));
            }
            return (Class<? extends T>) oldClass.toClass();
        } catch (Exception e1) {
            if (e1.getMessage().contains("Cannot inherit from final class")) {
                return overrideTheFinalModifiedClass(aClass);
            }
            try {
                return (Class<? extends T>) oldClass.toClass(ClassHelper.getDefaultClassLoader(), aClass.getProtectionDomain());
            } catch (CannotCompileException e) {
                try {
                    return (Class<? extends T>) new Loader().loadClass(newName);
                } catch (ClassNotFoundException classNotFoundException) {
                }
            }
        }
        return aClass;
    }

    /**
     * 重写final修饰的类
     *
     * @param aClass 类
     * @param <T>    类型
     * @return
     */
    private static <T> Class<? extends T> overrideTheFinalModifiedClass(Class<? extends T> aClass) {
        String name = aClass.getName();
        String newName = name + JAVASSIST;
        try {
            ClassPool classPool = getClassPool();
            CtClass oldClass = classPool.get(name);

            CtClass newClass = classPool.makeClass(newName);

            newClass.setModifiers(Modifier.PUBLIC);

            for (CtField field : oldClass.getDeclaredFields()) {
                CtField ctField = new CtField(field, newClass);
                ctField.setModifiers(field.getModifiers());
                newClass.addField(ctField);
            }

            for (CtMethod declaredMethod : oldClass.getDeclaredMethods()) {
                CtMethod ctMethod = new CtMethod(declaredMethod.getReturnType(), declaredMethod.getName(), declaredMethod.getParameterTypes(), newClass);
                ctMethod.setModifiers(declaredMethod.getModifiers());
                newClass.addMethod(ctMethod);
            }

            newClass.addConstructor(CtNewConstructor.defaultConstructor(newClass));

            for (CtClass anInterface : oldClass.getInterfaces()) {
                newClass.addInterface(anInterface);
            }
            return (Class<? extends T>) newClass.toClass();

        } catch (Exception e) {
        }
        return aClass;
    }

    /**
     * 获取类设置唯一的新名称
     *
     * @param oldClass          原类
     * @param classPool         类池
     * @param tryCheckClassName 类名称
     * @return 新名称
     */
    private static String setNewName(final CtClass oldClass, final ClassPool classPool, String tryCheckClassName) {
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
     * 获取MemberValue
     *
     * @param value     原始值
     * @param constPool 对象池
     * @return MemberValue
     */
    @SuppressWarnings("all")
    public static MemberValue getMemberValue(Object value, ConstPool constPool) {

        if (null == value) {
            return null;
        }
        if (value instanceof Integer) {
            return new IntegerMemberValue(constPool, (Integer) value);
        }

        if (value instanceof Long) {
            return new LongMemberValue((Long) value, constPool);
        }

        if (value instanceof Double) {
            return new DoubleMemberValue((Double) value, constPool);
        }

        if (value instanceof Float) {
            return new FloatMemberValue((Float) value, constPool);
        }

        if (value instanceof Boolean) {
            return new BooleanMemberValue((Boolean) value, constPool);
        }

        if (value instanceof Byte) {
            return new ByteMemberValue((Byte) value, constPool);
        }

        if (value instanceof Class) {
            return new ClassMemberValue(((Class) value).getName(), constPool);
        }

        if (value instanceof Short) {
            return new ShortMemberValue((Short) value, constPool);
        }

        if (value instanceof Character) {
            return new CharMemberValue((Character) value, constPool);
        }
        if (value instanceof String) {
            return new StringMemberValue(value.toString(), constPool);
        }

        if (value instanceof Enum) {
            Enum enumType = (Enum) value;
            return new EnumMemberValue(constPool);
        }

        if (value.getClass().isArray()) {
            Object[] objects = (Object[]) value;
            if (objects instanceof String[]) {
                return new ArrayMemberValue(new StringMemberValue(value.toString(), constPool), constPool);
            }

            if (objects instanceof Long[]) {
                return new ArrayMemberValue(new LongMemberValue((Long) value, constPool), constPool);
            }

            if (objects instanceof Integer[]) {
                return new ArrayMemberValue(new IntegerMemberValue((Integer) value, constPool), constPool);
            }

            if (objects instanceof Float[]) {
                return new ArrayMemberValue(new FloatMemberValue((Float) value, constPool), constPool);
            }

            if (objects instanceof Double[]) {
                return new ArrayMemberValue(new DoubleMemberValue((Double) value, constPool), constPool);
            }

            if (objects instanceof Boolean[]) {
                return new ArrayMemberValue(new BooleanMemberValue((Boolean) value, constPool), constPool);
            }

            if (objects instanceof Class[]) {
                return new ArrayMemberValue(new ClassMemberValue(((Class) value).getName(), constPool), constPool);
            }

            if (objects instanceof Byte[]) {
                return new ArrayMemberValue(new ByteMemberValue((Byte) value, constPool), constPool);
            }

            if (objects instanceof Character[]) {
                return new ArrayMemberValue(new CharMemberValue((Character) value, constPool), constPool);
            }
        }
        return null;
    }

    /**
     * 流转类
     *
     * @param inputStream 流
     * @return 类
     */
    public static Class<?> toClass(InputStream inputStream) {
        try {
            ClassPool classPool = JavassistHelper.getClassPool();
            ClassFile classFile = new ClassFile(new DataInputStream(inputStream));
            CtClass ctClass = classPool.makeClass(classFile);
            Loader loader = new Loader(classPool);
            loader.addTranslator(classPool, createTranslator());

            return loader.loadClass(ctClass.toClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoHelper.closeQuietly(inputStream);
        }
        return null;
    }

    /**
     * @return Translator
     */
    private static Translator createTranslator() {
        return new Translator() {

            @Override
            public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

            }

            @Override
            public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {

            }
        };
    }
}
