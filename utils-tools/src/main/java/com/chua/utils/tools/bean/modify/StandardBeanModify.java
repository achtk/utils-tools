package com.chua.utils.tools.bean.modify;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.JavassistHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.manager.parser.description.ModifyDescription;
import com.chua.utils.tools.named.NamedHelper;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.CollectionUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.annotation.MemberValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * bean 修饰
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/18
 */
public class StandardBeanModify<T> implements BeanModify<T> {
    /**
     * 后缀
     */
    private static final String JAVASSIST_SUFFIX = "$javassist";
    /**
     * 对象
     */
    private final T object;
    private final List<CtField> fields = new ArrayList<>();
    private final List<CtMethod> methods = new ArrayList<>();
    private final ClassPool classPool;
    private final Class<?> classes;
    private final CtClass ctClass;
    private final Map<String, Field> fieldNames;
    private final Multimap<String, Object> fieldAnnotations = HashMultimap.create();
    private final Multimap<String, Object> methodAnnotations = HashMultimap.create();
    private final List<Method> methodsList;
    private final List<Class<?>> interfaces = new ArrayList<>();
    private Class<?> superClass;
    /**
     * 待添加的注解
     */
    private final Set<Annotation> addingAnnotations = new HashSet<>();

    public StandardBeanModify(T object) throws NotFoundException {
        this.object = object;
        this.classPool = ClassUtils.getClassPool();
        this.classes = ClassUtils.getClass(object);
        this.fieldNames = ClassUtils.getFieldsAsMap(object);
        this.methodsList = ClassUtils.getMethods(object);
        this.ctClass = classPool.get(classes.getName());
        this.ctClass.setName(classes.getName() + JAVASSIST_SUFFIX);
    }

    @Override
    public BeanModify<T> addInterface(Class<?>... interfaces) {
        this.interfaces.addAll(Arrays.asList(interfaces));
        return this;
    }

    @Override
    public BeanModify<T> addSuper(Class<?> aClass) {
        this.superClass = aClass;
        return this;
    }

    @Override
    public BeanModify<T> addAnnotation(Annotation... annotations) {
        CollectionUtils.add(addingAnnotations, annotations);
        return this;
    }

    @Override
    public BeanModify<T> addField(String body) {
        try {
            CtField ctField = CtField.make(body, ctClass);
            if (!fieldNames.containsKey(ctField.getName())) {
                fields.add(ctField);
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public BeanModify<T> addField(String name, List<?> annotationType) {
        fieldAnnotations.putAll(name, annotationType);
        return this;
    }

    @Override
    public BeanModify<T> addField(String name, Class<?> type) {
        try {
            if (!fieldNames.containsKey(name)) {
                CtField ctField = new CtField(classPool.get(type.getName()), name, ctClass);
                fields.add(ctField);
            }
        } catch (CannotCompileException | NotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public BeanModify<T> addMethod(String body) {
        try {
            CtMethod ctMethod = CtNewMethod.make(body, ctClass);
            if (!exist(ctMethod)) {
                methods.add(ctMethod);
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public BeanModify<T> addMethodAnnotations(String methodName, Class<?>[] params, List<Annotation> annotations) {
        methodAnnotations.putAll(methodName + (null == params ? "" : Arrays.stream(params).map(Class::getName).collect(Collectors.joining())), annotations);
        return this;
    }

    /**
     * 方法是否存在
     *
     * @param ctMethod 方法
     * @return 存在返回true
     */
    private boolean exist(CtMethod ctMethod) {
        return methodsList.stream().filter(method -> {
            if (!method.getName().equals(ctMethod.getName())) {
                return false;
            }
            Class<?>[] paramTypes = method.getParameterTypes();
            CtClass[] ctParamTypes;
            try {
                ctParamTypes = ctMethod.getParameterTypes();
            } catch (NotFoundException e) {
                return false;
            }
            return ClassUtils.isPresent(paramTypes, ctParamTypes);
        }).count() != 0;
    }

    @Override
    public BeanModify<T> addMethod(String name, String body, Class<?> type, Class<?>[] params) {
        try {
            CtMethod ctMethod = CtNewMethod.make(Modifier.PUBLIC, classPool.get(type.getName()), name, ClassUtils.toCtClass(params), new CtClass[]{classPool.get(Exception.class.getName())}, body, ctClass);
            if (!exist(ctMethod)) {
                methods.add(ctMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public ModifyDescription<T> create() throws Exception {
        //修改修饰类
        ModifyDescription<T> modifyDescription = new ModifyDescription<>();
        //添加字段
        this.renderFields(modifyDescription);
        //添加方法
        this.renderMethods(modifyDescription);
        //添加接口
        this.renderInterfaces(modifyDescription);
        //添加父类(必须不存在父类)
        this.renderSuper(modifyDescription);
        //渲染当前类自身
        this.renderType();
        //修改方法注解
        this.modifyMethodsAnnotations(modifyDescription);
        //修改字段注解
        this.modifyFieldsAnnotations(modifyDescription);
        //类加载器
        ClassLoader callerClassLoader = ClassUtils.getCallerClassLoader(this.classes);
        Class<?> aClass = null;
        try {
            aClass = callerClassLoader.loadClass(ctClass.toClass().getName());
        } catch (Exception e) {
            Loader classLoader = new Loader(callerClassLoader, classPool);
            classLoader.addTranslator(classPool, new Translator() {
                @Override
                public void start(ClassPool pool) {

                }

                @Override
                public void onLoad(ClassPool pool, String classname) {

                }
            });
            aClass = classLoader.loadClass(ctClass.toClass().getName());
        }
        modifyDescription.setModifyType((Class<T>) aClass);
        return modifyDescription;
    }

    /**
     * 修改方法注解
     *
     * @param modifyDescription<?> 修改修饰
     */
    private void modifyMethodsAnnotations(ModifyDescription<?> modifyDescription) {
        for (Method method : methodsList) {
            try {
                String name = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                String key = name + (null == parameterTypes ? "" : Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining()));
                if (methodAnnotations.containsKey(key)) {
                    CtMethod declaredMethod = ctClass.getDeclaredMethod(name, ClassUtils.toCtClass(parameterTypes));
                    this.makeMethodAnnotationInfo(methodAnnotations.get(key), declaredMethod);
                }
            } catch (NotFoundException e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 修改字段注解
     *
     * @param modifyDescription<?> 修改修饰
     */
    private void modifyFieldsAnnotations(ModifyDescription<T> modifyDescription) {
        for (Map.Entry<String, Field> entry : fieldNames.entrySet()) {
            try {
                if (fieldAnnotations.containsKey(entry.getKey())) {
                    CtField declaredField = ctClass.getDeclaredField(entry.getKey());
                    this.makeFieldAnnotationInfo(fieldAnnotations.get(entry.getKey()), declaredField);
                }
            } catch (NotFoundException e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 渲染当前类自身
     */
    private void renderType() {
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
     * 添加父类(必须不存在父类)
     *
     * @param modifyDescription 修改修饰
     */
    private void renderSuper(ModifyDescription<T> modifyDescription) {
        if (null != superClass) {
            try {
                ctClass.setSuperclass(classPool.get(this.superClass.getName()));
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
    private void renderInterfaces(ModifyDescription<T> modifyDescription) {
        CtClass[] interfaces = new CtClass[0];
        try {
            interfaces = ctClass.getInterfaces();
        } catch (NotFoundException e) {
        }
        List<String> collect = Arrays.stream(interfaces).map(CtClass::getName).collect(Collectors.toList());
        for (Class<?> aClass : this.interfaces) {
            if (!collect.contains(aClass.getName())) {
                try {
                    ctClass.addInterface(classPool.get(aClass.getName()));
                } catch (NotFoundException e) {
                    modifyDescription.addThrowable(e);
                }
            }

        }
    }

    /**
     * 添加方法
     *
     * @param modifyDescription 修改修饰
     */
    private void renderMethods(ModifyDescription<T> modifyDescription) {
        for (CtMethod ctMethod : methods) {
            try {
                String name = ctMethod.getName();
                CtClass[] parameterTypes = ctMethod.getParameterTypes();
                String key = name + (null == parameterTypes ? "" : Arrays.stream(parameterTypes).map(CtClass::getName).collect(Collectors.joining()));
                if (methodAnnotations.containsKey(key)) {
                    this.makeMethodAnnotationInfo(methodAnnotations.get(key), ctMethod);
                }
                ctClass.addMethod(ctMethod);
            } catch (NotFoundException | CannotCompileException e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 添加字段
     *
     * @param modifyDescription 修改修饰类
     */
    private void renderFields(ModifyDescription<T> modifyDescription) {
        for (CtField ctField : fields) {
            String fieldName = ctField.getName();
            if (fieldAnnotations.containsKey(fieldName)) {
                this.makeFieldAnnotationInfo(fieldAnnotations.get(fieldName), ctField);
            }
            try {
                CtMethod getter = CtNewMethod.getter("get" + NamedHelper.firstUpperCase(fieldName), ctField);
                CtMethod setter = CtNewMethod.getter("set" + NamedHelper.firstUpperCase(fieldName), ctField);
                ctClass.addField(ctField);
                ctClass.addMethod(getter);
                ctClass.addMethod(setter);
            } catch (CannotCompileException e) {
                modifyDescription.addThrowable(e);
            }
        }
    }

    /**
     * 方法添加注解
     *
     * @param ctMethod    方法
     * @param annotations 注解
     */
    private void makeMethodAnnotationInfo(Collection<Object> annotations, CtMethod ctMethod) {
        if (CollectionUtils.isEmpty(annotations)) {
            return;
        }

        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (Object annotation : annotations) {
            if (annotation instanceof Annotation) {
                Annotation a = (Annotation) annotation;
                javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(a.annotationType().getName(), constPool);
                this.addAnnotationValue(annotation1, a, constPool);
                annotationsAttribute.addAnnotation(annotation1);
            } else if (annotation instanceof Class && Annotation.class.isAssignableFrom((Class<?>) annotation)) {
                Class<? extends Annotation> a = (Class<? extends Annotation>) annotation;
                javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(a.getName(), constPool);
                this.addAnnotationValue(annotation1, a, constPool);
                annotationsAttribute.addAnnotation(annotation1);
            }

        }
        methodInfo.addAttribute(annotationsAttribute);
    }

    /***
     * 添加字段注解
     * @param annotations 注解
     * @param ctField 字段
     */
    private void makeFieldAnnotationInfo(Collection<Object> annotations, CtField ctField) {
        if (CollectionUtils.isEmpty(annotations)) {
            return;
        }

        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        FieldInfo fieldInfo = ctField.getFieldInfo();
        // 属性附上注解
        AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (Object obj : annotations) {
            if (obj instanceof Annotation) {
                Annotation annotation = (Annotation) obj;
                javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotation.annotationType().getName(), constPool);
                this.addAnnotationValue(annotation1, annotation, constPool);

                fieldAttr.addAnnotation(annotation1);
            } else if (obj instanceof Class && Annotation.class.isAssignableFrom((Class<?>) obj)) {
                Class<? extends Annotation> annotation = (Class<? extends Annotation>) obj;
                javassist.bytecode.annotation.Annotation annotation1 = new javassist.bytecode.annotation.Annotation(annotation.getName(), constPool);
                this.addAnnotationValue(annotation1, annotation, constPool);

                fieldAttr.addAnnotation(annotation1);
            }
        }
        fieldInfo.addAttribute(fieldAttr);
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

    /**
     * 注解赋值
     *
     * @param annotation1 待赋值
     * @param annotation  添加的注解
     * @param constPool   对象池
     */
    private void addAnnotationValue(javassist.bytecode.annotation.Annotation annotation1, Class<? extends Annotation> annotation, ConstPool constPool) {
        this.addAnnotationValue(annotation1, Collections.emptyMap(), constPool);
    }

    /**
     * 注解赋值
     *
     * @param annotation1     待赋值
     * @param annotationValue 添加的注解
     * @param constPool       对象池
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
}
