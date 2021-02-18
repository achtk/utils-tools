package com.chua.utils.tools.entity;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * class节点
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/21
 */
public class ClassNode extends ClassVisitor {
    public int version;
    public int access;
    public String name;
    public String signature;
    public String superName;
    public List<String> interfaces;
    public String sourceFile;
    public String sourceDebug;
    public String outerClass;
    public String outerMethod;
    public String outerMethodDesc;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<Attribute> attrs;
    public List<InnerClassNode> innerClasses;
    public List<FieldNode> fields;
    public List<MethodNode> methods;

    public ClassNode() {
        this(327680);
        if (this.getClass() != ClassNode.class) {
            throw new IllegalStateException();
        }
    }

    public ClassNode(int var1) {
        super(var1);
        this.interfaces = new ArrayList();
        this.innerClasses = new ArrayList();
        this.fields = new ArrayList();
        this.methods = new ArrayList();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        if (interfaces != null) {
            this.interfaces.addAll(Arrays.asList(interfaces));
        }

    }

    @Override
    public void visitSource(String source, String debug) {
        this.sourceFile = source;
        this.sourceDebug = debug;
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        this.outerClass = owner;
        this.outerMethod = name;
        this.outerMethodDesc = descriptor;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationNode annotationNode = new AnnotationNode(descriptor);
        if (visible) {
            if (this.visibleAnnotations == null) {
                this.visibleAnnotations = new ArrayList(1);
            }

            this.visibleAnnotations.add(annotationNode);
        } else {
            if (this.invisibleAnnotations == null) {
                this.invisibleAnnotations = new ArrayList(1);
            }

            this.invisibleAnnotations.add(annotationNode);
        }

        return annotationNode;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(typeRef, typePath, descriptor);
        if (visible) {
            if (this.visibleTypeAnnotations == null) {
                this.visibleTypeAnnotations = new ArrayList(1);
            }

            this.visibleTypeAnnotations.add(typeAnnotationNode);
        } else {
            if (this.invisibleTypeAnnotations == null) {
                this.invisibleTypeAnnotations = new ArrayList(1);
            }

            this.invisibleTypeAnnotations.add(typeAnnotationNode);
        }

        return typeAnnotationNode;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        if (this.attrs == null) {
            this.attrs = new ArrayList(1);
        }

        this.attrs.add(attribute);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        InnerClassNode innerClassNode = new InnerClassNode(name, outerName, innerName, access);
        this.innerClasses.add(innerClassNode);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldNode fieldNode = new FieldNode(access, name, descriptor, signature, value);
        this.fields.add(fieldNode);
        return fieldNode;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodNode methodNode = new MethodNode(access, name, descriptor, signature, exceptions);
        this.methods.add(methodNode);
        return methodNode;
    }

    @Override
    public void visitEnd() {
    }

    public void check(int i) {
        int type = 262144;
        if (i == type) {
            if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations.size() > 0) {
                throw new RuntimeException();
            }

            if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations.size() > 0) {
                throw new RuntimeException();
            }

            Iterator iterator = this.fields.iterator();

            while (iterator.hasNext()) {
                FieldNode var3 = (FieldNode) iterator.next();
                var3.check(i);
            }

            iterator = this.methods.iterator();

            while (iterator.hasNext()) {
                MethodNode var4 = (MethodNode) iterator.next();
                var4.check(i);
            }
        }

    }

    public void accept(ClassVisitor classVisitor) {
        String[] strings = new String[this.interfaces.size()];
        this.interfaces.toArray(strings);
        classVisitor.visit(this.version, this.access, this.name, this.signature, this.superName, strings);
        if (this.sourceFile != null || this.sourceDebug != null) {
            classVisitor.visitSource(this.sourceFile, this.sourceDebug);
        }

        if (this.outerClass != null) {
            classVisitor.visitOuterClass(this.outerClass, this.outerMethod, this.outerMethodDesc);
        }

        int var4 = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();

        int var3;
        AnnotationNode var5;
        for (var3 = 0; var3 < var4; ++var3) {
            var5 = this.visibleAnnotations.get(var3);
            var5.accept(classVisitor.visitAnnotation(var5.desc, true));
        }

        var4 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();

        for (var3 = 0; var3 < var4; ++var3) {
            var5 = this.invisibleAnnotations.get(var3);
            var5.accept(classVisitor.visitAnnotation(var5.desc, false));
        }

        var4 = this.visibleTypeAnnotations == null ? 0 : this.visibleTypeAnnotations.size();

        TypeAnnotationNode var6;
        for (var3 = 0; var3 < var4; ++var3) {
            var6 = this.visibleTypeAnnotations.get(var3);
            var6.accept(classVisitor.visitTypeAnnotation(var6.typeRef, var6.typePath, var6.desc, true));
        }

        var4 = this.invisibleTypeAnnotations == null ? 0 : this.invisibleTypeAnnotations.size();

        for (var3 = 0; var3 < var4; ++var3) {
            var6 = this.invisibleTypeAnnotations.get(var3);
            var6.accept(classVisitor.visitTypeAnnotation(var6.typeRef, var6.typePath, var6.desc, false));
        }

        var4 = this.attrs == null ? 0 : this.attrs.size();

        for (var3 = 0; var3 < var4; ++var3) {
            classVisitor.visitAttribute(this.attrs.get(var3));
        }

        for (var3 = 0; var3 < this.innerClasses.size(); ++var3) {
            this.innerClasses.get(var3).accept(classVisitor);
        }

        for (var3 = 0; var3 < this.fields.size(); ++var3) {
            this.fields.get(var3).accept(classVisitor);
        }

        for (var3 = 0; var3 < this.methods.size(); ++var3) {
            this.methods.get(var3).accept(classVisitor);
        }

        classVisitor.visitEnd();
    }
}
