package com.chua.utils.tools.compiler.magaer;

import com.chua.utils.tools.compiler.classloader.CompilerClassLoader;

import javax.tools.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CH
 */
public class ClassFileManager extends ForwardingJavaFileManager {

    private final CompilerClassLoader compilerClassLoader;
    private ClassFileObject classFileObject;

    private static final ConcurrentHashMap<URI, JavaFileObject> URI_JAVA_FILE_OBJECT_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     * @param compilerClassLoader
     */
    public ClassFileManager(JavaFileManager fileManager, CompilerClassLoader compilerClassLoader) {
        super(fileManager);
        this.compilerClassLoader = compilerClassLoader;
    }


    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        URI uri = URI.create(location.getName() + '/' + packageName + '/' + relativeName);
        JavaFileObject javaFileObject = URI_JAVA_FILE_OBJECT_CONCURRENT_HASH_MAP.get(uri);
        if(null != javaFileObject) {
            return javaFileObject;
        }
        return super.getFileForInput(location, packageName, relativeName);
    }

    public ClassFileObject getClassFileObject() {
        return this.classFileObject;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        classFileObject = new ClassFileObject(className, kind);
        compilerClassLoader.addClassFile(className, classFileObject);
        return classFileObject;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return compilerClassLoader;
    }

    public void putFileForInput(StandardLocation location, String pkg, String s, ClassFileObject classFileObject) {
        URI_JAVA_FILE_OBJECT_CONCURRENT_HASH_MAP.put(URI.create(location.getName() + '/' + pkg + '/' + s), classFileObject);
    }
}
