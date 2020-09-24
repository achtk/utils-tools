package com.chua.utils.tools.classes;

import com.chua.utils.tools.common.BooleanHelper;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import lombok.Getter;

import java.lang.annotation.Annotation;

/**
 * class工厂
 *
 * @author CH
 * @since 1.0
 */
public class ClassFactory extends CommonFactory{

    private static final String JAVASSIST_SUFFIX = "$javassist";
    @Getter
    private Class<?> aClass;

    public ClassFactory(Class<?> aClass) {
        this.aClass = aClass;
    }

    /**
     * 虚拟化类实现
     *
     * @return
     */
    public CtClass forClass() throws NotFoundException {
        //类名
        String name = aClass.getName() + JAVASSIST_SUFFIX;
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get(aClass.getName());
        ctClass.setName(name);
        return ctClass;
    }

    /**
     * 虚拟化接口实现
     *
     * @return
     */
    public CtClass forInterface() throws NotFoundException {
        //类名
        String name = aClass.getName() + JAVASSIST_SUFFIX;
        //类注解
        Annotation[] annotations = aClass.getDeclaredAnnotations();
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(name);
        ctClass.setInterfaces(new CtClass[]{classPool.get(aClass.getName())});

        if (!BooleanHelper.hasLength(annotations)) {
            return ctClass;
        }
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();

        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (Annotation annotation : annotations) {
            annotationsAttribute.addAnnotation(getAnnnotation(annotation, constPool));
        }
        classFile.addAttribute(annotationsAttribute);

        return ctClass;
    }
}
