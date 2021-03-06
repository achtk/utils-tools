package com.chua.utils.tools.classes.adaptor;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.entity.FieldDescription;
import com.chua.utils.tools.classes.entity.MethodDescription;
import com.chua.utils.tools.entity.ClassNode;
import com.google.common.base.Strings;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.chua.utils.tools.constant.SymbolConstant.*;
import static javassist.Modifier.isPublic;
import static javassist.bytecode.AccessFlag.isPrivate;
import static javassist.bytecode.AccessFlag.isProtected;

/**
 * 元数据适配器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/28
 */
public class AsmAdaptor implements MetadataAdapter {

    private static final String L = "L";
    private static final String RETURN_L = "()L";
    private ClassReader classReader = null;
    private ClassNode classNode;

    public AsmAdaptor(InputStream inputStream) {
        try {
            this.classReader = new ClassReader(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.classNode = new ClassNode();
        this.classReader.accept(classNode, 0);
    }

    /**
     * 解析名称
     *
     * @param descriptor 名称
     * @return 名称
     */
    public static String resolveNewName(String descriptor) {
        if (Strings.isNullOrEmpty(descriptor)) {
            return null;
        }
        String newDescriptor = descriptor;
        if (descriptor.startsWith(L)) {
            newDescriptor = descriptor.substring(1);
        }

        if (descriptor.startsWith(RETURN_L)) {
            newDescriptor = descriptor.substring(3);
        }
        newDescriptor = newDescriptor.replace(SYMBOL_LEFT_SLASH, SYMBOL_DOT);

        if (newDescriptor.endsWith(SYMBOL_SEMICOLON)) {
            newDescriptor = newDescriptor.substring(0, newDescriptor.length() - 1);
        }
        return newDescriptor;
    }

    @Override
    public boolean isEmpty() {
        return null != classReader;
    }

    @Override
    public int getAccessFlags() {
        return this.classReader.getAccess();
    }

    @Override
    public String getSuperClass() {
        return resolveNewName(this.classReader.getSuperName());
    }

    @Override
    public List<String> getAnnotations() {
        return classNode.visibleAnnotations.stream().map(annotationNode -> resolveNewName(annotationNode.desc)).collect(Collectors.toList());
    }

    @Override
    public List<String> getInterfaceName() {
        return this.classNode.interfaces.stream().map(AsmAdaptor::resolveNewName).collect(Collectors.toList());
    }

    @Override
    public String getClassName() {
        return resolveNewName(this.classReader.getClassName());
    }

    @Override
    public List<FieldDescription> getFields() {
        return classNode.fields.stream().map(fieldNode -> {
            FieldDescription fieldDescription = new FieldDescription();
            fieldDescription.setName(resolveNewName(fieldNode.name));
            fieldDescription.setType(ClassHelper.forName(resolveNewName(fieldNode.desc)));
            fieldDescription.setModifier(getModifier(fieldNode.access));
            fieldDescription.setAnnotations(Optional.ofNullable(fieldNode.visibleAnnotations).orElse(Collections.emptyList()).stream().map(annotationNode -> resolveNewName(annotationNode.desc)).collect(Collectors.toList()));
            return fieldDescription;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MethodDescription> getMethods() {
        return classNode.methods.stream().map(methodNode -> {
            MethodDescription methodDescription = new MethodDescription();
            methodDescription.setName(resolveNewName(methodNode.name));
            methodDescription.setType(ClassHelper.forName(resolveNewName(methodNode.desc)));
            methodDescription.setModifier(getModifier(methodNode.access));
            methodDescription.setAnnotations(Optional.ofNullable(methodNode.visibleAnnotations).orElse(Collections.emptyList()).stream().map(annotationNode -> resolveNewName(annotationNode.desc)).collect(Collectors.toList()));
            return methodDescription;
        }).collect(Collectors.toList());
    }

    public String getModifier(int accessFlags) {
        if (isPrivate(accessFlags)) {
            return "private";
        }
        if (isProtected(accessFlags)) {
            return "protected";
        }
        return isPublic(accessFlags) ? "public" : "";
    }
}
