package com.chua.utils.tools.classes.adaptor;

import com.chua.utils.tools.classes.entity.FieldDescription;
import com.chua.utils.tools.classes.entity.MethodDescription;
import com.chua.utils.tools.common.IoHelper;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static javassist.Modifier.isPublic;
import static javassist.bytecode.AccessFlag.isPrivate;
import static javassist.bytecode.AccessFlag.isProtected;

/**
 * 元数据适配器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/18
 */
public class JavassistAdaptor implements MetadataAdapter {

    private final ClassFile classFile;
    private final InputStream inputStream;

    public JavassistAdaptor(InputStream inputStream) {
        this.inputStream = inputStream;
        this.classFile = this.getOrCreateClassObject();
    }

    /**
     * 创建ClassFile
     *
     * @return ClassFile
     */
    public ClassFile getOrCreateClassObject() {
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
            return new ClassFile(dis);
        } catch (IOException e) {
            return null;
        } finally {
            IoHelper.closeQuietly(inputStream);
        }
    }

    @Override
    public boolean isEmpty() {
        return null == classFile;
    }

    @Override
    public int getAccessFlags() {
        return classFile.getAccessFlags();
    }

    @Override
    public String getSuperClass() {
        return classFile.getSuperclass();
    }

    @Override
    public List<String> getAnnotations() {
        AnnotationsAttribute attribute = (AnnotationsAttribute) classFile.getAttribute(AnnotationsAttribute.visibleTag);
        AnnotationsAttribute[] annotationsAttributes = new AnnotationsAttribute[]{attribute};
        if (annotationsAttributes != null) {
            return Arrays.stream(annotationsAttributes)
                    .filter(Objects::nonNull)
                    .flatMap(annotationsAttribute -> Arrays.stream(annotationsAttribute.getAnnotations()))
                    .map(Annotation::getTypeName)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getInterfaceName() {
        return Arrays.asList(classFile.getInterfaces());
    }

    @Override
    public String getClassName() {
        return classFile.getName();
    }

    @Override
    public List<FieldDescription> getFields() {
        List<FieldInfo> fileFields = classFile.getFields();
        List<FieldDescription> result = new ArrayList<>(fileFields.size());

        fileFields.parallelStream().forEach(fieldInfo -> {
            AnnotationsAttribute attribute = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.visibleTag);

            FieldDescription fieldDescription = new FieldDescription();
            fieldDescription.setAnnotations(Arrays.stream(new AnnotationsAttribute[]{attribute})
                    .filter(Objects::nonNull)
                    .flatMap(annotationsAttribute -> Arrays.stream(annotationsAttribute.getAnnotations()))
                    .map(Annotation::getTypeName)
                    .collect(Collectors.toList()));
            fieldDescription.setName(fieldInfo.getName());
            fieldDescription.setModifier(getModifier(fieldInfo.getAccessFlags()));

            result.add(fieldDescription);
        });
        return result;
    }

    @Override
    public List<MethodDescription> getMethods() {

        List<MethodInfo> methodInfos = classFile.getMethods();
        List<MethodDescription> result = new ArrayList<>(methodInfos.size());

        methodInfos.parallelStream().forEach(methodInfo -> {
            AnnotationsAttribute attribute = (AnnotationsAttribute) methodInfo.getAttribute(AnnotationsAttribute.visibleTag);

            MethodDescription methodDescription = new MethodDescription();
            if (null != attribute) {
                methodDescription.setAnnotations(Arrays.stream(new AnnotationsAttribute[]{attribute})
                        .filter(Objects::nonNull)
                        .flatMap(annotationsAttribute -> Arrays.stream(annotationsAttribute.getAnnotations()))
                        .map(Annotation::getTypeName)
                        .collect(Collectors.toList()));
            }
            methodDescription.setName(methodInfo.getName());
            methodDescription.setModifier(getModifier(methodInfo.getAccessFlags()));
            if (null != methodInfo.getExceptionsAttribute()) {
                methodDescription.setExceptions(Arrays.asList(methodInfo.getExceptionsAttribute().getExceptions()));
            }

            result.add(methodDescription);
        });
        return result;
    }

    public String getModifier(int accessFlags) {
        return isPrivate(accessFlags) ? "private" :
                isProtected(accessFlags) ? "protected" :
                        isPublic(accessFlags) ? "public" : "";
    }

}
