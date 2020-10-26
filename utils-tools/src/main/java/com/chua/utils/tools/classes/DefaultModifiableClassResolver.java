package com.chua.utils.tools.classes;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.compiler.AbstractCompiler;
import com.chua.utils.tools.compiler.JavassistCompilerFactory;
import com.chua.utils.tools.entity.FieldInfoProperties;
import com.chua.utils.tools.entity.MethodInfoProperties;
import javassist.*;
import javassist.bytecode.*;

import java.util.Set;

/**
 * 可被修改类解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
public class DefaultModifiableClassResolver implements ModifiableClassResolver {

    private final ClassPool classPool;
    private CtClass ctClass;
    private final Class source;
    private Object sourceObject;
    protected static final String JAVASSIST_SUFFIX = "$javassist";

    public DefaultModifiableClassResolver(Object source) throws NotFoundException {
        this.sourceObject = source;
        this.source = source.getClass();
        this.classPool = ClassHelper.getClassPool();
        reset();
    }

    public DefaultModifiableClassResolver(Class source) throws NotFoundException {
        this.source = source;
        this.classPool = ClassHelper.getClassPool();
        reset();
    }

    @Override
    public void reset() throws NotFoundException {
        this.ctClass = null;
        this.ctClass = this.classPool.get(source.getName());
        this.ctClass.setName(source.getName() + JAVASSIST_SUFFIX);
    }

    @Override
    public ModifiableClassResolver addMethod(String methodFunction) throws Exception {
        this.ctClass.addMethod(CtMethod.make(methodFunction, ctClass));
        return this;
    }

    @Override
    public ModifiableClassResolver addMethod(MethodInfoProperties methodInfoProperties) throws Exception {
        CtMethod ctMethod = new CtMethod(
                classPool.get(methodInfoProperties.getReturnType()),
                methodInfoProperties.getName(),
                transferCtClass(methodInfoProperties.getParameterTypes(), classPool), ctClass);

        if (BooleanHelper.hasLength(methodInfoProperties.getAnnotations())) {
            makeMethodAnnotationInfo(ctMethod, methodInfoProperties.getAnnotations());
        }
        ctMethod.setBody(methodInfoProperties.getContent());
        ctClass.addMethod(ctMethod);
        return this;
    }


    @Override
    public ModifiableClassResolver addField(String fieldFunction) throws Exception {
        this.ctClass.addField(CtField.make(fieldFunction, ctClass));
        return this;
    }

    @Override
    public ModifiableClassResolver addField(FieldInfoProperties fieldInfoProperties) throws Exception {
        CtField ctField = new CtField(
                classPool.get(fieldInfoProperties.getReturnType()),
                fieldInfoProperties.getName(),
                ctClass);

        if (BooleanHelper.hasLength(fieldInfoProperties.getAnnotations())) {
            makeFieldAnnotationInfo(fieldInfoProperties.getAnnotations(), ctField);
        }

        ctClass.addField(ctField);
        return this;
    }

    @Override
    public ModifiableClassResolver addConstruct(Class<?>[] parameters) throws Exception {
        CtClass[] ctClasses = new CtClass[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> parameter = parameters[i];
            ctClasses[i] = getCtClassByClass(parameter);
        }
        this.ctClass.addConstructor(new CtConstructor(ctClasses, ctClass));
        return this;
    }

    @Override
    public ModifiableClassResolver addInterface(String... interfaceNames) {
        for (String interfaceName : interfaceNames) {
            CtClass ctClassByName = getCtClassByName(interfaceName);
            if (null == ctClassByName) {
                continue;
            }
            this.ctClass.addInterface(ctClassByName);
        }
        return this;
    }

    @Override
    public ModifiableClassResolver setSuperClass(String superClass) throws Exception {
        CtClass ctClassByName = getCtClassByName(superClass);
        if (null != ctClassByName) {
            this.ctClass.setSuperclass(ctClassByName);
        }
        return this;
    }

    @Override
    public Class<?> toClass() throws Exception {
        return this.ctClass.toClass();
    }

    @Override
    public Object toObject() throws Exception {
        Class<?> aClass = toClass();
        if (null == aClass) {
            return null;
        }
        try {
            return aClass.newInstance();
        } catch (Throwable e) {
            Loader classLoader = new Loader(classPool);
            classLoader.addTranslator(classPool, new Translator() {
                @Override
                public void start(ClassPool pool) {

                }

                @Override
                public void onLoad(ClassPool pool, String classname) {

                }
            });

            return classLoader.loadClass(aClass.getName()).newInstance();
        }
    }

    /**
     * 方法添加注解
     *
     * @param ctMethod    方法
     * @param annotations 注解
     */
    private void makeMethodAnnotationInfo(CtMethod ctMethod, Set<String> annotations) {
        if (BooleanHelper.hasLength(annotations)) {
            ClassFile classFile = ctClass.getClassFile();
            ConstPool constPool = classFile.getConstPool();
            MethodInfo methodInfo = ctMethod.getMethodInfo();
            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            for (String annotation : annotations) {
                javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotation, constPool);
                annotationsAttribute.addAnnotation(annotation1);
            }
            methodInfo.addAttribute(annotationsAttribute);
        }
    }

    /***
     * 添加字段注解
     * @param annotations 注解
     * @param ctField 字段
     */
    private void makeFieldAnnotationInfo(Set<String> annotations, CtField ctField) {
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

    private CtClass getCtClassByName(String name) {
        try {
            return this.classPool.get(name);
        } catch (NotFoundException e) {
            return null;
        }
    }

    private CtClass getCtClassByClass(Class aClass) {
        try {
            return this.classPool.get(aClass.getName());
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * 返回值类型转化
     *
     * @param parameterTypes 参数类型
     * @param classPool      类池
     * @return
     */
    private static CtClass[] transferCtClass(Class[] parameterTypes, ClassPool classPool) throws Exception {
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
     * 编译字符串
     *
     * @param javaCode
     * @return
     */
    public Object compile(String javaCode) {
        AbstractCompiler compiler = new JavassistCompilerFactory();
        return compiler.compiler(javaCode, ClassHelper.getDefaultClassLoader());
    }
}
