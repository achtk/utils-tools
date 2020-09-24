package com.chua.utils.tools.classes;

import com.google.common.base.Joiner;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ParameterAnnotationsAttribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.chua.utils.tools.constant.StringConstant.*;

/**
 * 方法工厂
 * @author CH
 * @since 1.0
 */
public class MethodFactory extends CommonFactory {

    private CtClass ctClass;
    private Class<?> aClass;

    private static final Pattern ZX_PATTERN = Pattern.compile("byte|short|char|int");

    public MethodFactory(CtClass ctClass, Class<?> aClass) {
        this.ctClass = ctClass;
        this.aClass = aClass;
    }

    public MethodFactory(ClassFactory classFactory) {
        this.afterProperties(classFactory);
    }

    /**
     * 生成虚拟方法
     * @return
     */
    public Class<?> makeClass() throws Exception {
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            makeMethodCtClass(method);
        }
        return ctClass.toClass();
    }

    /**
     * 绘制方法
     * @param method
     * @return
     */
    private void makeMethodCtClass(Method method) throws Exception {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getModifierName(method));
        stringBuffer.append(getReturnString(method));
        stringBuffer.append(method.getName()).append(" ");
        stringBuffer.append(getParameter(method));
        stringBuffer.append(" {");
        stringBuffer.append(getBody(method));
        stringBuffer.append(" }");

        AnnotationsAttribute methodAnnotation = getMethodAnnotation(method);
        ParameterAnnotationsAttribute parameterAnnotation = getParameterAnnotation(method);

        CtMethod ctMethod = CtMethod.make(stringBuffer.toString(), ctClass);
        ctClass.addMethod(ctMethod);

        if(null != methodAnnotation) {
            ctMethod.getMethodInfo().addAttribute(methodAnnotation);
        }

        if(null != parameterAnnotation) {
            ctMethod.getMethodInfo().addAttribute(parameterAnnotation);
        }
    }
    /**
     * 获取注解
     * @param method
     * @return
     */
    private ParameterAnnotationsAttribute getParameterAnnotation(Method method) {
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        ParameterAnnotationsAttribute parameterAnnotationsAttribute = new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag);
        javassist.bytecode.annotation.Annotation[][] annotationsAttribute = new javassist.bytecode.annotation.Annotation[parameterAnnotations.length][];

        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            annotationsAttribute[i] = getAnnotations(parameterAnnotation);
        }

        parameterAnnotationsAttribute.setAnnotations(annotationsAttribute);

        return parameterAnnotationsAttribute;
    }

    /**
     * 获取注解
     * @param parameterAnnotation
     * @return
     */
    private javassist.bytecode.annotation.Annotation[] getAnnotations(Annotation[] parameterAnnotation) {
        javassist.bytecode.annotation.Annotation[] annotations = new javassist.bytecode.annotation.Annotation[parameterAnnotation.length];
        for (int i = 0; i < parameterAnnotation.length; i++) {
            Annotation annotation = parameterAnnotation[i];
            annotations[i] = getAnnnotation(annotation, ctClass.getClassFile().getConstPool());
        }
        return annotations;
    }

    /**
     * 获取注解
     * @param method
     * @return
     */
    private AnnotationsAttribute getMethodAnnotation(Method method) {
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();

        Annotation[] annotations = method.getDeclaredAnnotations();

        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (Annotation annotation : annotations) {
            annotationsAttribute.addAnnotation(getAnnnotation(annotation, constPool));
        }

        return annotationsAttribute;
    }

    /**
     * 内容
     *
     * @param method
     * @return
     */
    private String getBody(Method method) {
        String simpleName = method.getReturnType().getSimpleName();
        if (ZX_PATTERN.matcher(simpleName).find()) {
            return "return -1;";
        }
        if (CLASS_FLOAT.equals(simpleName)) {
            return "return -1f;";
        }

        if (CLASS_DOUBLE.equals(simpleName)) {
            return "return -1D;";
        }

        if (CLASS_LONG.equals(simpleName)) {
            return "return -1L;";
        }

        if (CLASS_BOOLEAN.equals(simpleName)) {
            return "return false;";
        }

        String lowerCase = method.getReturnType().getSimpleName().toLowerCase();
        return CLASS_VOID.equals(lowerCase) ? "" : "return null;";
    }
    /**
     * 参数
     * @param method
     * @return
     */
    private StringBuffer getParameter(Method method) {
        Parameter[] parameters = method.getParameters();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        stringBuffer.append(getParams(parameters));
        stringBuffer.append(")");
        return stringBuffer;
    }
    /**
     * 请求参数
     *
     * @param parameters
     * @return
     */
    private String getParams(Parameter[] parameters) {
        int size = parameters.length;
        List<String> strings = new ArrayList<>(size);

        for (Parameter parameter : parameters) {
            strings.add(parameter.getType().getName() + " " + parameter.getName());
        }
        return Joiner.on(",").join(strings);
    }
    /**
     * 获取修饰符
     * @param method
     * @return
     */
    private String getModifierName(Method method) {
        int modifiers = method.getModifiers();
        if(Modifier.isProtected(modifiers)) {
            return "protected ";
        } else if(Modifier.isPrivate(modifiers)) {
            return "private ";
        }
        return "public ";
    }

    /**
     * 获取返回类型
     * @param method
     * @return
     */
    private String getReturnString(Method method) {
        return method.getReturnType().getSimpleName() + " ";
    }

    /**
     * @return
     * @param classFactory
     */
    private void afterProperties(ClassFactory classFactory) {
        Class<?> aClass = classFactory.getAClass();
        this.aClass = aClass;
        try {
            if(aClass.isInterface()) {
                this.ctClass = classFactory.forInterface();
            } else {
                this.ctClass = classFactory.forClass();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }



}
