package com.chua.utils.tools.compiler;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.constant.SymbolConstant;
import javassist.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @className CtClassBuilder
 * @since 2020/6/8 22:17
 */
public class CtClassBuilder {

    private String className;

    private String superClassName = "java.lang.Object";

    private final List<String> imports = new ArrayList<>();

    private final Map<String, String> fullNames = new HashMap<>();

    private final List<String> ifaces = new ArrayList<>();

    private final List<String> constructors = new ArrayList<>();

    private final List<String> fields = new ArrayList<>();

    private final List<String> methods = new ArrayList<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public void setSuperClassName(String superClassName) {
        this.superClassName = getQualifiedClassName(superClassName);
    }

    public List<String> getImports() {
        return imports;
    }

    public void addImports(String pkg) {
        int pi = pkg.lastIndexOf('.');
        if (pi > 0) {
            String pkgName = pkg.substring(0, pi);
            this.imports.add(pkgName);
            if (!pkg.endsWith(SymbolConstant.SYMBOL_DOT_ASTERISK)) {
                fullNames.put(pkg.substring(pi + 1), pkg);
            }
        }
    }

    public List<String> getInterfaces() {
        return ifaces;
    }

    public void addInterface(String iface) {
        this.ifaces.add(getQualifiedClassName(iface));
    }

    public List<String> getConstructors() {
        return constructors;
    }

    public void addConstructor(String constructor) {
        this.constructors.add(constructor);
    }

    public List<String> getFields() {
        return fields;
    }

    public void addField(String field) {
        this.fields.add(field);
    }

    public List<String> getMethods() {
        return methods;
    }

    public void addMethod(String method) {
        this.methods.add(method);
    }

    /**
     * get full qualified class name
     *
     * @param className super class name, maybe qualified or not
     */
    protected String getQualifiedClassName(String className) {
        if (className.contains(SymbolConstant.SYMBOL_DOT)) {
            return className;
        }

        if (fullNames.containsKey(className)) {
            return fullNames.get(className);
        }

        return ClassHelper.forName(imports.toArray(new String[0]), className).getName();
    }

    /**
     * build CtClass object
     */
    public CtClass build(ClassLoader classLoader) throws NotFoundException, CannotCompileException {
        ClassPool pool = new ClassPool(true);
        pool.appendClassPath(new LoaderClassPath(classLoader));

        // create class
        CtClass ctClass = pool.makeClass(className, pool.get(superClassName));

        // add imported packages
        imports.stream().forEach(pool::importPackage);

        // add implemented interfaces
        for (String iface : ifaces) {
            ctClass.addInterface(pool.get(iface));
        }

        // add constructors
        for (String constructor : constructors) {
            ctClass.addConstructor(CtNewConstructor.make(constructor, ctClass));
        }

        // add fields
        for (String field : fields) {
            try {
                ctClass.addField(CtField.make(field, ctClass));
            } catch (CannotCompileException e) {
                int before = field.indexOf("<");
                int after = field.indexOf(">");
                field = field.substring(0, before) + field.substring(after + 1);
                ctClass.addField(CtField.make(field, ctClass));
            }
        }

        // add methods
        for (String method : methods) {
            try {
                ctClass.addMethod(CtNewMethod.make(method, ctClass));
            } catch (CannotCompileException e) {
                method = method.replace("...", "[]");
                ctClass.addMethod(CtNewMethod.make(method, ctClass));
            }
        }

        return ctClass;
    }

}