package com.chua.utils.tools.manager.parser;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.JavassistHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.manager.parser.description.FieldDescription;
import com.chua.utils.tools.manager.parser.description.MethodDescription;
import com.chua.utils.tools.manager.parser.description.ModifyDescription;
import com.chua.utils.tools.named.NamedHelper;
import com.google.common.base.Strings;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.annotation.MemberValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 标准的类修改修饰器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
class StandardClassModifyDescriptionParser<T> implements ClassModifyDescriptionParser<T> {
    /**
     * Ct类
     */
    private final CtClass ctClass;
    /**
     * 父类
     */
    private final Class<? super T> superClass;
    /**
     * 类池
     */
    private final ClassPool classPool;

    /**
     * 待解析的类
     */
    private final Class<T> tClass;
    /**
     * 后缀
     */
    private static final String JAVASSIST_SUFFIX = "$javassist";
    /**
     * 待添加的接口
     */
    private final CopyOnWriteArraySet<Class<?>> interfaceCache = new CopyOnWriteArraySet<>();
    /**
     * 待添加的方法
     */
    private final Map<String, Annotation[]> methodCache = new HashMap<>();
    /**
     * 待添加的方法
     */
    private final Table<String, Class<Annotation>, Map<String, Object>> methodTable = HashBasedTable.create();
    /**
     * 待替换的方法
     */
    private final Map<Method, String> methodReplaceCache = new HashMap<>();
    /**
     * 待添加注解的方法
     */
    private final Map<Method, Annotation[]> methodAnnotationsCache = new HashMap<>();
    /**
     * 待添加注解的字段
     */
    private final Map<Field, Annotation[]> fieldAnnotationsCache = new HashMap<>();
    /**
     * 待添加的字段
     */
    private final Map<String, Annotation[]> fieldCache = new HashMap<>();
    /**
     * 待添加的父类
     */
    private Class<?> addingSuper;
    /**
     * 待添加的注解
     */
    private Set<Annotation> addingAnnotations = new HashSet<>();

    private ClassDescriptionParser classDescriptionParser;
    private String name;

    public StandardClassModifyDescriptionParser(Class<T> tClass, ClassDescriptionParser classDescriptionParser) throws NotFoundException {
        this.tClass = tClass;
        this.classDescriptionParser = classDescriptionParser;
        this.superClass = tClass.getSuperclass();
        this.classPool = JavassistHelper.getClassPool();
        this.ctClass = this.classPool.get(tClass.getName());
        this.ctClass.setName(tClass.getName() + JAVASSIST_SUFFIX);
    }

    @Override
    public void addAnnotation(Annotation... annotations) {
        for (Annotation annotation : annotations) {
            this.addingAnnotations.add(annotation);
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addMethod(String method, Annotation[] annotations) {
        if (!Strings.isNullOrEmpty(method)) {
            methodCache.put(method, annotations);
        }
    }

    @Override
    public void addMethod(String method, Class<Annotation> annotations, Map<String, Object> params) {
        if (!Strings.isNullOrEmpty(method)) {
            methodTable.put(method, annotations, params);
        }
    }

    @Override
    public void addSuper(Class<?> aClass) {
        this.addingSuper = aClass;
    }

    @Override
    public void addField(String field, Annotation[] annotations) {
        if (!Strings.isNullOrEmpty(field)) {
            fieldCache.put(field, annotations);
        }
    }

    @Override
    public void addFieldGetter(String fieldName, Annotation... annotations) {
        FieldDescription fieldDescription = classDescriptionParser.findFieldDescription(fieldName);
        if (null != fieldDescription) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("get").append(NamedHelper.firstUpperCase(fieldName));

            addMethod(stringBuilder.toString(), fieldDescription.getType(), null, "return " + fieldName + ";", annotations);
        }
    }

    @Override
    public void addFieldSetter(String fieldName, Annotation... annotations) {
        FieldDescription fieldDescription = classDescriptionParser.findFieldDescription(fieldName);
        if (null != fieldDescription) {

            addMethod("set" + NamedHelper.firstUpperCase(fieldName), fieldDescription.getType(), new Class[]{fieldDescription.getType()}, "this." + fieldName + "=" + fieldName + ";", annotations);
        }
    }

    @Override
    public void addFieldsAnnotations(String fieldName, Annotation... annotations) {
        FieldDescription fieldDescription = classDescriptionParser.findFieldDescription(fieldName);
        if (null != fieldDescription) {
            fieldAnnotationsCache.put(fieldDescription.getField(), annotations);
        }
    }

    @Override
    public void addInterface(Class<?>... interfaces) {
        interfaceCache.addAll(Arrays.asList(interfaces));
    }

    @Override
    public void addMethodAnnotations(String methodName, Annotation... annotations) {
        MethodDescription<?> methodDescription = classDescriptionParser.findMethodDescription(methodName);
        if (null != methodDescription) {
            methodAnnotationsCache.put(methodDescription.getMethod(), annotations);
        }
    }

    @Override
    public void replaceMethod(String methodName, String method) {
        MethodDescription<?> methodDescription = classDescriptionParser.findMethodDescription(methodName);
        if (null != methodDescription) {
            this.replaceMethod(methodDescription.getMethod(), method);
        }
    }

    @Override
    public void replaceMethod(Method method, String methodBody) {
        if (null != method && !Strings.isNullOrEmpty(methodBody)) {
            methodReplaceCache.put(method, methodBody);
        }
    }

    @Override
    public ModifyDescription<T> toClass() throws Exception {

        if (!Strings.isNullOrEmpty(name)) {
            this.ctClass.setName(name);
        }
        //修改修饰类
        ModifyDescription<T> modifyDescription = new ModifyDescription<>();
        //添加字段
        this.renderFields(modifyDescription);
        //添加方法
        this.renderMethods(modifyDescription);
        //添加方法
        this.renderMethodsWithClassAnnotation(modifyDescription);
        //添加接口
        this.renderInterfaces(modifyDescription);
        //添加父类(必须不存在父类)
        this.renderSuper(modifyDescription);
        //渲染当前类自身
        this.renderType(modifyDescription);
        //修改方法
        this.modifyMethods(modifyDescription);
        //修改方法注解
        this.modifyMethodsAnnotations(modifyDescription);
        //修改字段注解
        this.modifyFieldsAnnotations(modifyDescription);
        //类加载器
        Loader classLoader = new Loader(classPool);
        classLoader.addTranslator(classPool, new Translator() {
            @Override
            public void start(ClassPool pool) {

            }

            @Override
            public void onLoad(ClassPool pool, String classname) {

            }
        });
        Class<?> aClass = classLoader.loadClass(ctClass.toClass().getName());
        modifyDescription.setAClass((Class<T>) aClass);
        return modifyDescription;
    }

    /**
     * 添加方法
     *
     * @param modifyDescription 修改描述
     */
    private void renderMethodsWithClassAnnotation(ModifyDescription<T> modifyDescription) {
        Set<String> strings = methodTable.rowKeySet();
        for (String string : strings) {

            try {
                CtMethod ctMethod = CtMethod.make(string, ctClass);
                Map<Class<Annotation>, Map<String, Object>> row = methodTable.row(string);
                Set<Class<Annotation>> classes = row.keySet();
                Class<Annotation> annotationClass = FinderHelper.firstElement(classes);

                this.makeMethodAnnotationInfo(annotationClass, row.get(annotationClass), ctMethod);
                ctClass.addMethod(ctMethod);
            } catch (CannotCompileException e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 修改字段注解
     *
     * @param modifyDescription<?>
     */
    private void modifyFieldsAnnotations(ModifyDescription modifyDescription) {
        for (Map.Entry<Field, Annotation[]> entry : fieldAnnotationsCache.entrySet()) {
            try {
                modifyFieldAnnotations(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 修改字段注解
     *
     * @param field       字段
     * @param annotations 注解
     */
    private void modifyFieldAnnotations(Field field, Annotation[] annotations) throws NotFoundException {
        CtField ctField = ctClass.getDeclaredField(field.getName());
        this.makeFieldAnnotationInfo(annotations, ctField);
    }

    /**
     * 修改方法注解
     *
     * @param modifyDescription<?>
     */
    private void modifyMethodsAnnotations(ModifyDescription<?> modifyDescription) {
        for (Map.Entry<Method, Annotation[]> entry : methodAnnotationsCache.entrySet()) {
            try {
                modifyMethodAnnotations(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                modifyDescription.addThrowable(e);
            }
        }
    }


    /**
     * 修改方法注解
     *
     * @param method      方法
     * @param annotations 注解
     */
    private void modifyMethodAnnotations(Method method, Annotation[] annotations) throws NotFoundException {
        //获取方法参数
        Class<?>[] parameterTypes = method.getParameterTypes();
        //方法参数转为Ct参数
        CtClass[] ctClasses = new CtClass[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> aClass = parameterTypes[i];
            ctClasses[i] = classPool.get(aClass.getName());
        }

        CtMethod declaredMethod = this.ctClass.getDeclaredMethod(method.getName(), ctClasses);
        this.makeMethodAnnotationInfo(annotations, declaredMethod);
    }

    /**
     * 渲染当前类自身
     *
     * @param modifyDescription 修改修饰
     */
    private void renderType(ModifyDescription<?> modifyDescription) {
        this.makeTypeAnnotationInfo();
    }

    /**
     * 修改方法
     *
     * @param modifyDescription 修改修饰
     */
    private void modifyMethods(ModifyDescription<?> modifyDescription) {
        for (Map.Entry<Method, String> entry : methodReplaceCache.entrySet()) {
            try {
                modifyMethod(entry, modifyDescription);
            } catch (Exception e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 修改方法
     *
     * @param entry             待修改的方法
     * @param modifyDescription 修改修饰
     */
    private void modifyMethod(Map.Entry<Method, String> entry, ModifyDescription modifyDescription) throws NotFoundException, CannotCompileException {
        Method method = entry.getKey();
        String methodBody = entry.getValue();
        //获取方法参数
        Class<?>[] parameterTypes = method.getParameterTypes();
        //方法参数转为Ct参数
        CtClass[] ctClasses = new CtClass[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> aClass = parameterTypes[i];
            ctClasses[i] = classPool.get(aClass.getName());
        }

        CtMethod declaredMethod = this.ctClass.getDeclaredMethod(method.getName(), ctClasses);
        if (null == declaredMethod) {
            throw new NotFoundException(method.getName() + "not found!");
        }
        declaredMethod.setBody(methodBody);
    }

    /**
     * 添加父类(必须不存在父类)
     *
     * @param modifyDescription 修改修饰
     */
    private void renderSuper(ModifyDescription modifyDescription) {
        if (null == superClass) {
            try {
                ctClass.setSuperclass(classPool.get(this.addingSuper.getName()));
            } catch (Exception e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 添加接口
     *
     * @param modifyDescription 修改修饰
     */
    private void renderInterfaces(ModifyDescription modifyDescription) {
        for (Class<?> aClass : interfaceCache) {
            try {
                ctClass.addInterface(classPool.get(aClass.getName()));
            } catch (NotFoundException e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 添加方法
     *
     * @param modifyDescription 修改修饰
     */
    private void renderMethods(ModifyDescription modifyDescription) {
        for (Map.Entry<String, Annotation[]> entry : methodCache.entrySet()) {
            try {
                CtMethod ctMethod = CtMethod.make(entry.getKey(), ctClass);
                this.makeMethodAnnotationInfo(entry.getValue(), ctMethod);

                ctClass.addMethod(ctMethod);
            } catch (CannotCompileException e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 添加字段
     *
     * @param modifyDescription 修改修饰
     */
    private void renderFields(ModifyDescription modifyDescription) {
        for (Map.Entry<String, Annotation[]> entry : fieldCache.entrySet()) {
            try {
                CtField ctField = CtField.make(entry.getKey(), ctClass);
                this.makeFieldAnnotationInfo(entry.getValue(), ctField);

                ctClass.addField(ctField);
            } catch (CannotCompileException e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 方法添加注解
     *
     * @param ctMethod        方法
     * @param annotationClass 注解
     * @param params          注解参数
     */
    private void makeMethodAnnotationInfo(Class<?> annotationClass, Map<String, Object> params, CtMethod ctMethod) {
        if (null == annotationClass) {
            return;
        }

        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotationClass.getName(), constPool);
        this.addAnnotationValue(annotation1, params, constPool);

        annotationsAttribute.addAnnotation(annotation1);
        methodInfo.addAttribute(annotationsAttribute);
    }

    /**
     * 方法添加注解
     *
     * @param ctMethod    方法
     * @param annotations 注解
     */
    private void makeMethodAnnotationInfo(Annotation[] annotations, CtMethod ctMethod) {
        if (!BooleanHelper.hasLength(annotations)) {
            return;
        }

        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (Annotation annotation : annotations) {
            javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotation.annotationType().getName(), constPool);
            this.addAnnotationValue(annotation1, annotation, constPool);

            annotationsAttribute.addAnnotation(annotation1);
        }
        methodInfo.addAttribute(annotationsAttribute);
    }

    /***
     * 添加字段注解
     * @param annotations 注解
     * @param ctField 字段
     */
    private void makeFieldAnnotationInfo(Annotation[] annotations, CtField ctField) {
        if (!BooleanHelper.hasLength(annotations)) {
            return;
        }

        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        FieldInfo fieldInfo = ctField.getFieldInfo();
        // 属性附上注解
        AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (Annotation annotation : annotations) {
            javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotation.annotationType().getName(), constPool);
            this.addAnnotationValue(annotation1, annotation, constPool);

            fieldAttr.addAnnotation(annotation1);
        }
        fieldInfo.addAttribute(fieldAttr);
    }

    /***
     * 添加类注解
     */
    private void makeTypeAnnotationInfo() {
        if (!BooleanHelper.hasLength(addingAnnotations)) {
            return;
        }
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (Annotation annotation : addingAnnotations) {
            javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotation.annotationType().getName(), constPool);
            this.addAnnotationValue(annotation1, annotation, constPool);

            annotationsAttribute.addAnnotation(annotation1);
        }
        classFile.addAttribute(annotationsAttribute);
    }

    /**
     * 注解赋值
     *
     * @param annotation1 待赋值
     * @param annotationValue  添加的注解
     * @param constPool   对象池
     */
    private void addAnnotationValue(javassist.bytecode.annotation.Annotation annotation1, Map<String, Object> annotationValue, ConstPool constPool) {
        for (Map.Entry<String, Object> entry : annotationValue.entrySet()) {
            MemberValue memberValue = JavassistHelper.getMemberValue(entry.getValue(), constPool);
            if (null == memberValue) {
                continue;
            }
            annotation1.addMemberValue(entry.getKey(), memberValue);
        }
    }
    /**
     * 注解赋值
     *
     * @param annotation1 待赋值
     * @param annotation  添加的注解
     * @param constPool   对象池
     */
    private void addAnnotationValue(javassist.bytecode.annotation.Annotation annotation1, Annotation annotation, ConstPool constPool) {
        Map<String, Object> annotationValue = ClassHelper.getAnnotationValue(annotation);
        this.addAnnotationValue(annotation1, annotationValue, constPool);
    }


}
