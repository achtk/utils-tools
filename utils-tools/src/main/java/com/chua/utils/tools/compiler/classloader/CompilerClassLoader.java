package com.chua.utils.tools.compiler.classloader;

import com.chua.utils.tools.compiler.magaer.ClassFileObject;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CH
 */
public class CompilerClassLoader extends ClassLoader {

    private ClassLoader classLoader;

    private static final ConcurrentHashMap<String, ClassFileObject> CACHE_CLASS = new ConcurrentHashMap<>();

    public CompilerClassLoader(ClassLoader classLoader) {
        super(classLoader);
        this.classLoader = classLoader;
    }

    public ClassLoader classLoader() {
        return this.classLoader;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        ClassFileObject classFileObject = CACHE_CLASS.get(name);
        if (null != classFileObject) {
            byte[] bytes = classFileObject.getBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            return contextClassLoader.loadClass(name);
        } catch (Throwable e) {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            try {
                return systemClassLoader.loadClass(name);
            } catch (Throwable ex) {
                return super.findClass(name);
            }
        }
    }

    @Override
    public synchronized Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }


    public void addClassFile(String className, ClassFileObject classFileObject) {
        CACHE_CLASS.put(className, classFileObject);
    }
}
