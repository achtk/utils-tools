package com.chua.utils.tools.classes;

import com.chua.utils.tools.classes.callback.FieldCallback;
import com.chua.utils.tools.classes.callback.MethodCallback;
import com.chua.utils.tools.common.*;
import com.chua.utils.tools.entity.*;
import com.chua.utils.tools.filter.MethodFilter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import javassist.*;
import javassist.Modifier;
import javassist.bytecode.*;
import lombok.Getter;
import lombok.Setter;
import net.sf.cglib.beans.BeanMap;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * 类信息处理工厂
 *
 * @author CH
 * @date 2020-09-26
 */
public class ClassExtensionHelper<T> {

    protected static final String GETTER = "get";
    protected static final String SETTER = "set";
    protected static final String JAVASSIST_SUFFIX = "$javassist";

    private static List<Class> primitiveTypes;
    private static List<String> primitiveNames;
    private static List<String> primitiveDescriptors;

    public static final MethodFilter USER_DECLARED_METHODS = (method -> !method.isBridge() && !method.isSynthetic());

    private static final Map<Class<?>, List<MemberInfo>> DECLARED_FIELDS_CACHE = new ConcurrentHashMap<>(256);
    private static final Map<Class<?>, List<MemberInfo>> DECLARED_SUPER_FIELDS_CACHE = new ConcurrentHashMap<>(256);
    private static final Map<Class<?>, List<Method>> DECLARED_METHODS_CACHE = new ConcurrentHashMap<>(256);

    private static final Set<String> DEFAULT_OBJECT_METHODS = Sets.newHashSet("toString", "equals", "hashCode");

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
     *
     * @param ctClass
     * @param classPool
     * @param annotationInfoProperties
     */
    private static void makeClassAnnotationInfo(CtClass ctClass, ClassPool classPool, AnnotationInfoProperties[] annotationInfoProperties) {
        if (!BooleanHelper.hasLength(annotationInfoProperties)) {
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
     *
     * @param ctMethod                 方法
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
     * @param ctField                  字段
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
        int modifiers = ctField.getModifiers();
        if (!Modifier.isPrivate(modifiers)) {
            return;
        }
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
        int modifiers = ctField.getModifiers();
        if (!Modifier.isPrivate(modifiers)) {
            return;
        }
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
            if (BooleanHelper.hasLength(methodInfoProperty.getAnnotations())) {
                makeMethodAnnotationInfo(ctClass, ctMethod, classPool, methodInfoProperty.getAnnotations());
            }
            ctMethod.setBody(methodInfoProperty.getContent());
            ctClass.addMethod(ctMethod);
        }
    }

    /**
     * 方法添加注解
     *
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

    /**
     * Getter and Setter分析
     *
     * @param beanClass 类
     */
    public static GetterSetterProperties doAnalyzeGetterAndSetter(Class<?> beanClass, final boolean autocomplete, final AnnotationInfoProperties... annotationInfoProperties) throws Throwable {
        GetterSetterProperties result = GetterSetterProperties.of();

        ClassPool classPool = getClassPool();
        CtClass ctClass = classPool.get(beanClass.getName());
        //类信息
        ClassInfoProperties classInfoProperties = new ClassInfoProperties();
        //获取缺失的方法
        Set<MethodInfoProperties> methodInfoProperties = Sets.newHashSet();
        //获取缺失属性信息
        Set<FieldInfoProperties> fieldInfoProperties = Sets.newHashSet();

        classInfoProperties.setMethodInfoProperties(methodInfoProperties);
        classInfoProperties.setFieldInfoProperties(fieldInfoProperties);
        //获取所有字段
        CtField[] fields = ctClass.getDeclaredFields();
        for (CtField field : fields) {
            //是否缺失方法
            //AtomicBoolean missMethod = new AtomicBoolean(false);

            GetterSetterProperties.GetterSetterStatus status = GetterSetterProperties.newStatus();

            String fieldName = field.getName();
            String newFieldName = StringHelper.firstUpperCase(fieldName);
            String getterMethod = GETTER + newFieldName;
            String setterMethod = SETTER + newFieldName;
            try {
                ctClass.getDeclaredMethod(getterMethod);
            } catch (NotFoundException e) {
                if (autocomplete) {
                    methodInfoProperties.add(MethodInfoProperties.builder().name(getterMethod).content("{return " + fieldName + ";}").returnType(field.getType().getName()).build());
                }
                status.setGetter(false);
            }
            try {
                ctClass.getDeclaredMethod(setterMethod);
            } catch (NotFoundException e) {
                if (autocomplete) {
                    methodInfoProperties.add(MethodInfoProperties.builder().parameterTypes(new Class[]{ClassHelper.forName(field.getType().getName())}).name(setterMethod).content("{this." + fieldName + " = " + fieldName + ";}").returnType(CtClass.voidType.getName()).build());
                }
                status.setSetter(false);
            }
            result.put(fieldName, status);
        }


        Class<?> aClass = makeClassInfoForClass(beanClass, classInfoProperties, annotationInfoProperties);
        result.record(aClass);
        return result;
    }

    /**
     * 获取字段值
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param fieldType 字段值
     * @return
     */
    public static <T> T getOnlyFieldValue(Object obj, String fieldName, Class<T> fieldType) {
        Map<String, T> fieldValue = getFieldValue(obj, fieldName, fieldType);
        return fieldValue.isEmpty() ? null : FinderHelper.firstElement(fieldValue.values());
    }

    /**
     * 获取字段值
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @return
     */
    public static <T> T getOnlyFieldValue(Object obj, String fieldName) {
        Map<String, T> fieldValue = getFieldValue(obj, fieldName);
        return fieldValue.isEmpty() ? null : FinderHelper.firstElement(fieldValue.values());
    }

    /**
     * 获取字段值
     *
     * @param obj       对象
     * @param fieldType 字段值
     * @return
     */
    public static <T> T getOnlyFieldValue(Object obj, Class<T> fieldType) {
        Map<String, T> fieldValue = getFieldValue(obj, fieldType);
        return fieldValue.isEmpty() ? null : FinderHelper.firstElement(fieldValue.values());
    }

    /**
     * 获取字段值
     *
     * @param obj       对象
     * @param fieldType 字段值
     * @return
     */
    public static <T> Map<String, T> getFieldValue(Object obj, Class<T> fieldType) {
        return getFieldValue(obj, null, fieldType);
    }

    /**
     * 获取字段值
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @return
     */
    public static <T> Map<String, T> getFieldValue(Object obj, String fieldName) {
        return getFieldValue(obj, fieldName, null);
    }

    /**
     * 获取字段值
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param fieldType 字段值
     * @return
     */
    public static <T> Map<String, T> getFieldValue(Object obj, String fieldName, Class<T> fieldType) {
        if (null == obj) {
            return null;
        }
        Map<String, T> stringTMap = doProcessingThroughBeanMap(obj, fieldName, fieldType);
        if (stringTMap.isEmpty()) {
            return doProcessingThroughReflection(obj, fieldName, fieldType);
        }
        return stringTMap;
    }

    /**
     * 通过反射获取字段值
     *
     * @param obj       属性
     * @param fieldName 字段名称
     * @param fieldType 字段值
     * @return
     */
    public static <T> Map<String, T> doProcessingThroughReflection(Object obj, String fieldName, Class<T> fieldType) {
        if (null == obj) {
            return Collections.emptyMap();
        }
        Map<String, T> result = new HashMap<>();
        List<Field> fieldByType = null;
        if (Strings.isNullOrEmpty(fieldName)) {
            fieldByType = getFieldByType(obj, fieldType);
        } else {
            fieldByType = findField(obj, fieldName);
        }
        for (Field field : fieldByType) {
            doDataAnalysis(result, obj, fieldName, field);
        }
        return result;
    }

    /**
     * 字段信息分析
     *
     * @param result    数据结果集合
     * @param obj       原始对象
     * @param fieldName 字段名称
     * @param field     字段
     * @param <T>
     */
    private static <T> void doDataAnalysis(Map<String, T> result, final Object obj, final String fieldName, Field field) {
        field.setAccessible(true);
        try {
            Object o = field.get(obj);
            result.put(field.getName(), (T) o);
        } catch (Exception e) {
        }
    }

    /**
     * 通过类型获取字段
     *
     * @param obj       对象
     * @param fieldType 类型
     * @param <T>
     * @return
     */
    public static <T> List<Field> getFieldByType(final Object obj, final Class<T> fieldType) {
        if (null == obj || null == fieldType) {
            return null;
        }
        List<Field> result = new ArrayList<>();
        Class<?> aClass = obj.getClass();
        while (null != aClass && !Object.class.getName().equals(aClass.getName())) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (!field.getType().isAssignableFrom(fieldType)) {
                    continue;
                }
                result.add(field);
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }

    /**
     * 获取所有字段
     *
     * @param obj 对象
     * @return
     */
    public static List<Field> getFields(final Object obj) {
        return findFields(getMembers(obj));
    }
    /**
     * 获取所有字段
     *
     * @param obj 对象
     * @return
     */
    public static Map<String, Field> getFieldsAsMap(final Object obj) {
        Map<String, Field> result = new HashMap<>();
        List<MemberInfo> members = getMembers(obj);
        for (MemberInfo member : members) {
            if(!member.isField()) {
                continue;
            }
            result.put(member.getName(), (Field) member.getMember());
        }
        return result;
    }
    /**
     * 获取所有字段值
     *
     * @param obj 对象
     * @return
     */
    public static Map<String, Object> getFieldsValueAsMap(final Object obj) {
        Map<String, Object> result = new HashMap<>();
        List<MemberInfo> members = getMembers(obj);
        for (MemberInfo member : members) {
            if(!member.isField()) {
                continue;
            }
            try {
                result.put(member.getName(), member.tryGetFieldValue());
            } catch (Throwable throwable) {
                continue;
            }
        }
        return result;
    }
    /**
     * 获取MemberInfo
     *
     * @param obj 对象
     * @return
     */
    public static List<MemberInfo> getMembers(final Object obj) {
        if (null == obj) {
            return Collections.emptyList();
        }
        Class<?> aClass = obj.getClass();
        if (obj instanceof Class) {
            aClass = (Class<?>) obj;
        }
        if (DECLARED_SUPER_FIELDS_CACHE.containsKey(aClass)) {
            return DECLARED_SUPER_FIELDS_CACHE.get(aClass);
        }
        List<MemberInfo> result = new ArrayList<>();
        while (null != aClass && !Object.class.getName().equals(aClass.getName())) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setName(field.getName());
                memberInfo.setObj(obj);
                memberInfo.setType(aClass);
                memberInfo.setMember(field);

                result.add(memberInfo);
            }
            aClass = aClass.getSuperclass();
        }
        DECLARED_SUPER_FIELDS_CACHE.put(aClass, result);
        return getMembers(obj);
    }

    /**
     * 获取MemberInfo
     *
     * @param obj 对象
     * @return
     */
    public static List<Field> getLocalFields(final Object obj) {
        return findFields(getLocalMembers(obj));
    }

    /**
     * 获取所有字段
     *
     * @param obj              对象
     * @return
     */
    public static List<MemberInfo> getLocalMembers(final Object obj) {
        if (null == obj) {
            return Collections.emptyList();
        }
        Class<?> tClass = obj.getClass();
        if (DECLARED_FIELDS_CACHE.containsKey(tClass)) {
            return DECLARED_FIELDS_CACHE.get(tClass);
        }

        Field[] fields = tClass.getDeclaredFields();
        DECLARED_FIELDS_CACHE.put(tClass, findMembers(fields, obj));
        return getLocalMembers(obj);
    }

    /**
     * 通过类型获取字段
     *
     * @param obj      对象
     * @param name     对象名称
     * @param wildcard 是否通配符匹配
     * @return
     */
    public static List<Field> findField(final Object obj, final String name, boolean wildcard) {
        List<MemberInfo> members = getMembers(obj);
        return MemberInfo.findField(members, name, wildcard);
    }

    /**
     * 通过类型获取字段
     *
     * @param obj  对象
     * @param name 对象名称
     * @return
     */
    public static List<Field> findField(final Object obj, final String name) {
        return findField(obj, name, StringHelper.isWildcardMatch(name));
    }

    /**
     * 通过cglib获取字段值
     *
     * @param obj       属性
     * @param fieldName 字段名称
     * @param fieldType 字段值
     * @return
     */
    public static <T> Map<String, T> doProcessingThroughBeanMap(Object obj, String fieldName, Class<T> fieldType) {
        if (null == obj) {
            return Collections.emptyMap();
        }
        Map<String, T> result = new HashMap<>();
        BeanMap beanMap = BeanMap.create(obj);

        if (Strings.isNullOrEmpty(fieldName)) {
            beanMap.forEach(new BiConsumer() {
                @Override
                public void accept(Object o, Object o2) {
                    if (null == o2) {
                        return;
                    }
                    try {
                        result.put(o.toString(), (T) o2);
                    } catch (Exception e) {
                    }
                }
            });
            return result;
        }

        if (!beanMap.containsKey(fieldName)) {
            return result;
        }
        Object o = beanMap.get(fieldName);
        try {
            result.put(fieldName, (T) o);
        } catch (Exception e) {
        }
        return result;
    }


    /**
     * 获取所有方法名称
     *
     * @param tClass 方法
     * @param <T>
     * @return
     */
    public static <T> List<String> getAllMethodNames(Class<T> tClass) {
        if (null == tClass) {
            return Collections.emptyList();
        }
        List<String> methods = new ArrayList<>();
        Method[] declaredMethods = tClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            methods.add(method.getName());
        }

        return methods;
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
        Map<String, Object> result = new HashMap<>();
        //获取方法名
        result.put("method.name", method.getName());
        //获取方法返回值
        result.put("method.return", method.getReturnType().getName());
        //获取所有注解
        Annotation[] annotations = method.getDeclaredAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            Annotation annotation = annotations[i];
            result.put("method.annotation[" + i + "].name", annotation.annotationType().getName());
        }
        //获取方法参数
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            result.put("method.parameter[" + i + "].name", parameter.getName());
            result.put("method.parameter[" + i + "].type", parameter.getType().getName());
        }

        return result;
    }

    /**
     * 获取注解的值
     *
     * @param annotation 直接
     * @param fieldName  字段
     */
    public static <T> T getAnnotationValue(final Annotation annotation, final String fieldName, final Class<T> type) {
        if (null == annotation) {
            return null;
        }
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Map<String, Map> memberValues = getFieldValue(invocationHandler, "memberValues", Map.class);
        Map map = memberValues.get("memberValues");
        return (T) map.get(fieldName);
    }

    /**
     * 添加接口
     *
     * @param obj        对象
     * @param interfaces 接口
     */
    public static <T> T addInterface(T obj, Class... interfaces) throws Throwable {
        if (null == obj || !BooleanHelper.hasLength(interfaces)) {
            return obj;
        }
        ClassPool classPool = getClassPool();
        List<CtClass> newInterfaceSet = new ArrayList<>();
        Class<?> aClass = obj.getClass();
        for (Class anInterface : interfaces) {
            if (null == anInterface || anInterface.isAssignableFrom(aClass)) {
                continue;
            }
            newInterfaceSet.add(classPool.get(anInterface.getName()));
        }
        if (!BooleanHelper.hasLength(newInterfaceSet)) {
            return obj;
        }
        CtClass ctClass = classPool.get(aClass.getName());
        ctClass.setName(aClass.getName() + JAVASSIST_SUFFIX);
        for (CtClass ctClass1 : newInterfaceSet) {
            ctClass.addInterface(ctClass1);
        }
        return (T) ClassHelper.forObject(ctClass.toClass());
    }

    /**
     * 字段查询
     *
     * @param clazz
     * @param fc
     */
    public static void doWithLocalFields(Class<?> clazz, FieldCallback fc) {
        for (Field field : getLocalFields(clazz)) {
            try {
                fc.doWith(field);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
            }
        }
    }

    /**
     * 方法回调
     *
     * @param clazz 类
     * @param mc    方法回调
     */
    public static void doWithLocalMethods(Class<?> clazz, MethodCallback mc) {
        List<Method> methods = getDeclaredMethods(clazz);
        for (Method method : methods) {
            try {
                mc.doWith(method);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
            }
        }
    }

    /**
     * 方法回调
     *
     * @param clazz 类
     * @param mc    方法回调
     */
    public static void doWithMethods(Class<?> clazz, MethodCallback mc) {
        doWithMethods(clazz, mc, null);
    }

    /**
     * 方法回调
     *
     * @param clazz 类
     * @param mc    方法回调
     * @param mf    过滤回调
     */
    public static void doWithMethods(Class<?> clazz, MethodCallback mc, @Nullable MethodFilter mf) {
        // Keep backing up the inheritance hierarchy.
        List<Method> methods = getDeclaredMethods(clazz);
        for (Method method : methods) {
            if (mf != null && !mf.matches(method)) {
                continue;
            }
            try {
                mc.doWith(method);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
            }
        }
        if (clazz.getSuperclass() != null && (mf != USER_DECLARED_METHODS || clazz.getSuperclass() != Object.class)) {
            doWithMethods(clazz.getSuperclass(), mc, mf);
        } else if (clazz.isInterface()) {
            for (Class<?> superIfc : clazz.getInterfaces()) {
                doWithMethods(superIfc, mc, mf);
            }
        }
    }


    /**
     * 获取默认类加载器
     * <p>
     * 默认获取当前线程的类加载器<code>Thread.currentThread().getContextClassLoader()</code>
     * </p>
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
        }
        if (cl == null) {
            cl = ClassHelper.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                }
            }
        }
        return cl;
    }

    /**
     * 方法 Accessible
     *
     * @param method 方法
     */
    public static void methodAccessible(Method method) {
        if (null == method) {
            return;
        }
        method.setAccessible(true);
    }

    /**
     * 获取所有方法
     *
     * @param clazz 类
     * @return
     */
    public static List<Method> getDeclaredMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        List<Method> result = DECLARED_METHODS_CACHE.get(clazz);
        if (result == null) {
            try {
                Method[] declaredMethods = clazz.getDeclaredMethods();
                List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
                if (defaultMethods != null) {
                    result = new ArrayList<>(declaredMethods.length + defaultMethods.size());
                    System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                    int index = declaredMethods.length;
                    for (Method defaultMethod : defaultMethods) {
                        result.add(defaultMethod);
                        index++;
                    }
                } else {
                    result = Arrays.asList(declaredMethods);
                }
                DECLARED_METHODS_CACHE.put(clazz, (result.size() == 0 ? Collections.emptyList() : result));
            } catch (Throwable ex) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
                        "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
            }
        }
        return result;
    }

    @Nullable
    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        for (Class<?> ifc : clazz.getInterfaces()) {
            for (Method ifcMethod : ifc.getMethods()) {
                if (!java.lang.reflect.Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(ifcMethod);
                }
            }
        }
        return result;
    }

    /**
     * 查找属性
     *
     * @param type 类
     * @param name 字段名称
     * @return
     */
    public static Field findField(Class<?> type, String name) {
        if (null == type || Strings.isNullOrEmpty(name)) {
            return null;
        }
        return ClassExtensionHelper.findField(type, name);
    }

    /**
     * 获取字段
     *
     * @param memberInfos MemberInfo数据
     * @return
     */
    private static List<Field> findFields(final List<MemberInfo> memberInfos) {
        if (!BooleanHelper.hasLength(memberInfos)) {
            return Collections.emptyList();
        }
        List<Field> fieldList = new ArrayList<>(memberInfos.size());
        for (MemberInfo memberInfo : memberInfos) {
            if (!memberInfo.isField()) {
                continue;
            }
            fieldList.add((Field) memberInfo.getMember());
        }
        return fieldList;
    }

    /**
     * 获取Member
     *
     * @param members          Member数据
     * @param obj              对象
     * @return
     */
    public static List<MemberInfo> findMembers(final Member[] members, Object obj) {
        return findMembers(Arrays.asList(members), obj);
    }

    /**
     * 获取Member
     *
     * @param members          Member数据
     * @param obj              对象
     * @return
     */
    public static List<MemberInfo> findMembers(final List<Member> members, Object obj) {
        if (!BooleanHelper.hasLength(members)) {
            return Collections.emptyList();
        }
        List<MemberInfo> fieldList = new ArrayList<>(members.size());
        for (Member member : members) {
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setMember(member);
            memberInfo.setType(member instanceof Field ? ((Field) member).getType() : ((Method) member).getReturnType());
            memberInfo.setName(member.getName());

            fieldList.add(memberInfo);
        }
        return fieldList;
    }

    /**
     * 获取字段值
     *
     * @param field 字段
     * @param obj   对象
     * @return
     */
    public static Object getFieldValue(final Field field, final Object obj) throws Throwable {
        if (null == field || null == obj) {
            return null;
        }
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 执行方法
     *
     * @param method 字段
     * @param obj    对象
     * @return
     */
    public static Object getMethodValue(final Method method, final Object obj, final Object... args) throws Throwable {
        if (null == method || null == obj) {
            return null;
        }
        method.setAccessible(true);
        return method.invoke(obj, args);
    }


    /**
     * 获取默认方法
     * @return
     */
    public static Set<String> getDefaultMethods() {
        return DEFAULT_OBJECT_METHODS;
    }
    /**
     * 设置字段值
     *
     * @param field 字段
     * @param value 数据
     * @param obj   对象
     * @return
     */
    public static void setFieldValue(final Field field, final Object value, final Object obj) throws Throwable {
        if (null == field || null == obj) {
            return;
        }
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 字段信息
     */
    @Getter
    @Setter
    public static class MemberInfo {
        private String name;
        private Class<?> type;
        private Object obj;
        private Member member;

        /**
         * 查找字符串
         *
         * @param members MemberInfo
         * @param name    名称
         * @return
         */
        public static List<Field> findField(List<MemberInfo> members, String name, boolean wildcard) {
            if (!BooleanHelper.hasLength(members) || Strings.isNullOrEmpty(name)) {
                return null;
            }
            List<Field> result = new ArrayList<>();
            for (MemberInfo member : members) {
                if (!member.isField()) {
                    continue;
                }
                if (!equals(member, name, wildcard)) {
                    continue;
                }
                result.add((Field) member.getMember());
            }
            return result;
        }

        private static boolean equals(MemberInfo member, String name, boolean wildcard) {
            return wildcard ? StringHelper.wildcardMatch(member.getName(), name) : member.getName().equals(name);
        }

        public boolean isEqualsByName(final String name) {
            return Strings.isNullOrEmpty(name) ? false : this.name.equals(name);
        }

        public boolean isEquals(final String name, final Class<?> type) {
            return Strings.isNullOrEmpty(name) || null == type ? false : this.name.equals(name) && this.type.isAssignableFrom(type);
        }

        public boolean isEqualsIgnoreCase(final String name) {
            return Strings.isNullOrEmpty(name) ? false : this.name.equalsIgnoreCase(name);
        }

        public boolean isEqualsIgnoreCase(final String name, final Class<?> type) {
            return Strings.isNullOrEmpty(name) || null == type ? false : this.name.equalsIgnoreCase(name) && this.type.isAssignableFrom(type);
        }

        public boolean isField() {
            return member instanceof Field;
        }

        public boolean isMethod() {
            return member instanceof Method;
        }

        public boolean isConstructor() {
            return member instanceof Constructor;
        }

        /**
         * 尝试获取字段数据
         * @return
         */
        public Object tryGetFieldValue() {
            if(!isField() || null == obj) {
                return null;
            }
            try {
                return getFieldValue((Field) member, obj);
            } catch (Throwable throwable) {
                return null;
            }
        }
    }
}
