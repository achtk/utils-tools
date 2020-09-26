package com.chua.utils.tools.classes;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.entity.*;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * 类信息处理工厂
 *
 * @author CH
 * @date 2020-09-26
 */
public class ClassInfoHelper {

    protected static final String GETTER = "get";
    protected static final String SETTER = "set";
    protected static final String JAVASSIST_SUFFIX = "$javassist";


    private static List<String> primitiveNames;
    private static List<Class> primitiveTypes;
    private static List<String> primitiveDescriptors;


    private static final Method[] NO_METHODS = {};


    public static List<String> getPrimitiveNames() {
        initPrimitives();
        return primitiveNames;
    }

    protected static List<Class> getPrimitiveTypes() {
        initPrimitives();
        return primitiveTypes;
    }

    protected static List<String> getPrimitiveDescriptors() {
        initPrimitives();
        return primitiveDescriptors;
    }

    private static void initPrimitives() {
        if (primitiveNames == null) {
            primitiveNames = Lists.newArrayList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
            primitiveTypes = Lists.<Class>newArrayList(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class);
            primitiveDescriptors = Lists.newArrayList("Z", "C", "B", "S", "I", "J", "F", "D", "V");
        }
    }

    /**
     * 给类添加方法
     *
     * @param aClass                   类
     * @param classInfoProperties      类信息
     * @param annotationInfoProperties 注解补漏
     * @return
     */
    public static Class<?> makeClassInfoForClass(final Class<?> aClass, final ClassInfoProperties classInfoProperties, final AnnotationInfoProperties... annotationInfoProperties) throws Throwable {
        if (null == aClass) {
            return null;
        }
        ClassPool classPool = getClassPool();
        CtClass ctClass = classPool.get(aClass.getName());
        ctClass.setName(aClass.getName() + JAVASSIST_SUFFIX);

        makeInterfaceInfo(ctClass, classPool, classInfoProperties.getInterfaceInfoProperties());
        makeConstructorInfo(ctClass, classPool, classInfoProperties.getConstructorInfoProperties());
        makeFieldInfo(ctClass, classPool, classInfoProperties.getFieldInfoProperties());
        makeMethodInfo(ctClass, classPool, classInfoProperties.getMethodInfoProperties());
        makeAnnotationInfo(ctClass, classPool, annotationInfoProperties);

        return ctClass.toClass();
    }

    /**
     * 注解修饰
     *
     * @param ctClass
     * @param classPool
     * @param annotationInfoProperties
     */
    private static void makeAnnotationInfo(CtClass ctClass, ClassPool classPool, AnnotationInfoProperties[] annotationInfoProperties) {
        if (!BooleanHelper.hasLength(annotationInfoProperties)) {
            return;
        }
        CtField[] declaredFields = ctClass.getDeclaredFields();
        for (CtField ctField : declaredFields) {
            makeFieldAnnotationInfo(ctField, ctClass, classPool, annotationInfoProperties);
        }

        CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
        for (CtMethod ctMethod : declaredMethods) {
            makeMethodAnnotationInfo(ctMethod, ctClass, classPool, annotationInfoProperties);
        }

        makeClassAnnotationInfo(ctClass, classPool, annotationInfoProperties);
    }

    /**
     * 类添加注解
     * @param ctClass
     * @param classPool
     * @param annotationInfoProperties
     */
    private static void makeClassAnnotationInfo(CtClass ctClass, ClassPool classPool, AnnotationInfoProperties[] annotationInfoProperties) {
        if(!BooleanHelper.hasLength(annotationInfoProperties)) {
            return;
        }

        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        // 属性附上注解
        AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (AnnotationInfoProperties annotationInfoProperty : annotationInfoProperties) {
            String name = ctClass.getName();
            if (Strings.isNullOrEmpty(annotationInfoProperty.getAccess()) ||
                    !StringHelper.wildcardMatch(name, annotationInfoProperty.getAccess()) ||
                    Strings.isNullOrEmpty(annotationInfoProperty.getName()) ||
                    0 != annotationInfoProperty.getType()
            ) {
                continue;
            }
            javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotationInfoProperty.getName(), constPool);
            fieldAttr.addAnnotation(annotation1);
        }
        classFile.addAttribute(fieldAttr);
    }

    /**
     * 修饰注解
     * @param ctMethod 方法
     * @param ctClass
     * @param classPool
     * @param annotationInfoProperties
     */
    private static void makeMethodAnnotationInfo(CtMethod ctMethod, CtClass ctClass, ClassPool classPool, AnnotationInfoProperties[] annotationInfoProperties) {
        if (!BooleanHelper.hasLength(annotationInfoProperties)) {
            return;
        }
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        // 属性附上注解
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (AnnotationInfoProperties annotationInfoProperty : annotationInfoProperties) {
            String name = ctMethod.getName();
            if (Strings.isNullOrEmpty(annotationInfoProperty.getAccess()) ||
                    !StringHelper.wildcardMatch(name, annotationInfoProperty.getAccess()) ||
                    Strings.isNullOrEmpty(annotationInfoProperty.getName()) ||
                    2 != annotationInfoProperty.getType()
            ) {
                continue;
            }
            javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotationInfoProperty.getName(), constPool);
            annotationsAttribute.addAnnotation(annotation1);
        }
        methodInfo.addAttribute(annotationsAttribute);
    }

    /**
     * 修饰注解
     *
     * @param ctField 字段
     * @param ctClass
     * @param classPool
     * @param annotationInfoProperties
     */
    private static void makeFieldAnnotationInfo(CtField ctField, CtClass ctClass, ClassPool classPool, AnnotationInfoProperties[] annotationInfoProperties) {
        if (!BooleanHelper.hasLength(annotationInfoProperties)) {
            return;
        }
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        FieldInfo fieldInfo = ctField.getFieldInfo();
        // 属性附上注解
        AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (AnnotationInfoProperties annotationInfoProperty : annotationInfoProperties) {
            String name = ctField.getName();
            if (Strings.isNullOrEmpty(annotationInfoProperty.getAccess()) ||
                    !StringHelper.wildcardMatch(name, annotationInfoProperty.getAccess()) ||
                    Strings.isNullOrEmpty(annotationInfoProperty.getName()) ||
                    1 != annotationInfoProperty.getType()
            ) {
                continue;
            }
            javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotationInfoProperty.getName(), constPool);
            fieldAttr.addAnnotation(annotation1);
        }
        fieldInfo.addAttribute(fieldAttr);
    }

    /**
     * 接口信息创建
     *
     * @param ctClass
     * @param classPool
     * @param interfaceInfoProperties 接口信息
     */
    private static void makeInterfaceInfo(CtClass ctClass, ClassPool classPool, InterfaceInfoProperties interfaceInfoProperties) throws Throwable {
        if (null == interfaceInfoProperties || !BooleanHelper.hasLength(interfaceInfoProperties.getClassSet())) {
            return;
        }
        ctClass.setInterfaces(transferCtClass(interfaceInfoProperties.getClassSet().toArray(new Class[0]), classPool));
    }

    /**
     * 构造信息创建
     *
     * @param ctClass
     * @param classPool
     * @param constructorInfoProperties 构造信息
     */
    private static void makeConstructorInfo(CtClass ctClass, ClassPool classPool, Set<ConstructorInfoProperties> constructorInfoProperties) {
        if (!BooleanHelper.hasLength(constructorInfoProperties)) {
            return;
        }
        for (ConstructorInfoProperties constructorInfoProperty : constructorInfoProperties) {
            try {
                CtConstructor ctConstructor = new CtConstructor(transferCtClass(constructorInfoProperty.getParameterType(), classPool), ctClass);
                ctClass.addConstructor(ctConstructor);
            } catch (Throwable throwable) {
                continue;
            }
        }
    }

    /**
     * 字段信息创建
     *
     * @param ctClass
     * @param classPool
     * @param fieldInfoProperties 字段信息
     */
    private static void makeFieldInfo(CtClass ctClass, ClassPool classPool, Set<FieldInfoProperties> fieldInfoProperties) throws Throwable {
        if (!BooleanHelper.hasLength(fieldInfoProperties)) {
            return;
        }
        for (FieldInfoProperties fieldInfoProperty : fieldInfoProperties) {
            CtField ctField = new CtField(
                    classPool.get(fieldInfoProperty.getReturnType()),
                    fieldInfoProperty.getName(),
                    ctClass);
            if (BooleanHelper.hasLength(fieldInfoProperty.getAnnotations())) {
                makeFieldAnnotationInfo(fieldInfoProperty.getAnnotations(), ctClass, ctField);
            }

            ctClass.addField(ctField);
        }
    }

    /***
     * 添加字段注解
     * @param annotations 注解
     * @param ctClass
     * @param ctField
     */
    private static void makeFieldAnnotationInfo(Set<String> annotations, CtClass ctClass, CtField ctField) {
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        FieldInfo fieldInfo = ctField.getFieldInfo();
        // 属性附上注解
        AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        if (BooleanHelper.hasLength(annotations)) {
            for (String annotation : annotations) {
                javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotation, constPool);
                fieldAttr.addAnnotation(annotation1);
            }
        }
        fieldInfo.addAttribute(fieldAttr);
    }

    /**
     * 方法信息创建
     *
     * @param ctClass
     * @param classPool
     * @param methodInfoProperties 方法信息
     */
    private static void makeMethodInfo(CtClass ctClass, ClassPool classPool, Set<MethodInfoProperties> methodInfoProperties) throws Throwable {
        if (!BooleanHelper.hasLength(methodInfoProperties)) {
            return;
        }
        for (MethodInfoProperties methodInfoProperty : methodInfoProperties) {
            CtMethod ctMethod = new CtMethod(
                    classPool.get(methodInfoProperty.getReturnType()),
                    methodInfoProperty.getName(),
                    transferCtClass(methodInfoProperty.getParameterTypes(), classPool), ctClass);
            if(BooleanHelper.hasLength(methodInfoProperty.getAnnotations())) {
                makeMethodAnnotationInfo(ctClass, ctMethod, classPool, methodInfoProperty.getAnnotations());
            }
            ctMethod.setBody(methodInfoProperty.getContent());
            ctClass.addMethod(ctMethod);
        }
    }

    /**
     * 方法添加注解
     * @param ctClass
     * @param ctMethod
     * @param classPool
     * @param annotations
     */
    private static void makeMethodAnnotationInfo(CtClass ctClass, CtMethod ctMethod, ClassPool classPool, Set<String> annotations) {
        if (BooleanHelper.hasLength(annotations)) {
            ClassFile classFile = ctClass.getClassFile();
            ConstPool constPool = classFile.getConstPool();
            MethodInfo methodInfo = ctMethod.getMethodInfo();
            // 属性附上注解
            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            for (String annotation : annotations) {
                javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotation, constPool);
                annotationsAttribute.addAnnotation(annotation1);
            }
            methodInfo.addAttribute(annotationsAttribute);
        }
    }

    /**
     * 获取classpool
     *
     * @return
     */
    protected static ClassPool getClassPool() {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        return classPool;
    }

    /**
     * 返回值类型转化
     *
     * @param parameterTypes 参数类型
     * @param classPool      类池
     * @return
     */
    private static CtClass[] transferCtClass(Class[] parameterTypes, ClassPool classPool) throws Throwable {
        if (!BooleanHelper.hasLength(parameterTypes)) {
            return new CtClass[0];
        }
        CtClass[] ctClasses = new CtClass[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class type = parameterTypes[i];
            CtClass aClass = classPool.get(type.getName());
            ctClasses[i] = aClass;
        }

        return ctClasses;
    }
}
