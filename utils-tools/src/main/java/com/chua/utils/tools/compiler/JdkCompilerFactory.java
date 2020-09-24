package com.chua.utils.tools.compiler;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.compiler.classloader.CompilerClassLoader;
import com.chua.utils.tools.compiler.magaer.ClassFileManager;
import com.chua.utils.tools.compiler.magaer.ClassFileObject;
import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * javassist 编译器
 *
 * @author CHTK
 */
@Slf4j
public class JdkCompilerFactory extends AbstractCompiler {

    private static final JavaCompiler JAVA_COMPILER = ToolProvider.getSystemJavaCompiler();

    private static final DiagnosticCollector<JavaFileObject> JAVA_FILE_OBJECT_DIAGNOSTIC_COLLECTOR = new DiagnosticCollector<JavaFileObject>();

    private static final StandardJavaFileManager STANDARD_FILE_MANAGER = JAVA_COMPILER.getStandardFileManager(JAVA_FILE_OBJECT_DIAGNOSTIC_COLLECTOR, null, null);

    private static final ConcurrentHashMap<String, JavaCompiler.CompilationTask> TASK_CACHE = new ConcurrentHashMap<>();

    private static final DiagnosticCollector<JavaFileObject> DIAGNOSTIC_COLLECTOR = new DiagnosticCollector<>();

    private static final String TEMP = System.getProperty("user.home") + File.separator + "sources";

    public JdkCompilerFactory() {
        addEnvClassOrJar();
        FileHelper.create(TEMP);
    }

    private CompilerClassLoader compilerClassLoader;

    @Override
    public Class<?> compiler(String code, final ClassLoader classLoader) {

        String pkg = getPkg(code);
        String classesName = getClassName(code);
        String name = null == pkg ? classesName : pkg + "." + classesName;

        this.compilerClassLoader = AccessController.doPrivileged(new PrivilegedAction<CompilerClassLoader>() {
            @Override
            public CompilerClassLoader run() {
                return new CompilerClassLoader(classLoader);
            }
        });


        ClassFileManager classFileManager = new ClassFileManager(STANDARD_FILE_MANAGER, compilerClassLoader);

        ClassFileObject classFileObject = null;
        try {
            classFileObject = new ClassFileObject(new URI(classesName + ".java"), JavaFileObject.Kind.SOURCE, code);
            classFileManager.putFileForInput(StandardLocation.SOURCE_PATH, pkg, classesName + ".java", classFileObject);

            JavaCompiler.CompilationTask task = JAVA_COMPILER.getTask(null, classFileManager, DIAGNOSTIC_COLLECTOR, null, null, Arrays.asList(classFileObject));
            if(task.call()) {
                try {
                    return compilerClassLoader.loadClass(name);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        List<Diagnostic<? extends JavaFileObject>> diagnostics = DIAGNOSTIC_COLLECTOR.getDiagnostics();
        if(BooleanHelper.hasLength(diagnostics)) {
            StandardJavaFileManager errorStandardFileManager = JAVA_COMPILER.getStandardFileManager(null, null, null);
            List<File> list = analysisStandardFileManager();

            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
                String message = diagnostic.getCode();
                if(message.indexOf("compiler.err.doesnt.exist") > -1) {
                    try {
                        //不存在类
                        CharSequence charSequence = diagnostic.getSource().getCharContent(true).subSequence((int)diagnostic.getStartPosition(), (int)diagnostic.getEndPosition());
                        Class<?> aClass = Class.forName(charSequence.toString(), false, ClassLoader.getSystemClassLoader());
                        list.add(new File(aClass.getResource("").getFile()));
                    } catch (Throwable e) {
                        continue;
                    }
                }
            }

            classFileManager = new ClassFileManager(errorStandardFileManager, compilerClassLoader);
            classFileManager.putFileForInput(StandardLocation.SOURCE_PATH, pkg, classesName + ".java", classFileObject);

            Iterable<String> options = Arrays.asList("-d", TEMP);
            JavaCompiler.CompilationTask task = JAVA_COMPILER.getTask(null, classFileManager, DIAGNOSTIC_COLLECTOR, options, null, Arrays.asList(classFileObject));
            if(task.call()) {
                try {
                    return compilerClassLoader.loadClass(name);
                } catch (ClassNotFoundException e) {
                }
            }
        }
        diagnostics = DIAGNOSTIC_COLLECTOR.getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            log.error(diagnostic.getMessage(Locale.getDefault()));
        }
        return null;
    }

    @Override
    protected Class<?> doCompile(String name, String source) throws Throwable {
        return null;
    }

    private List<File> analysisStandardFileManager() {
        List<File> list = new ArrayList<>();
        Iterable<? extends File> location = STANDARD_FILE_MANAGER.getLocation(StandardLocation.CLASS_PATH);
        for (File file : location) {
            list.add(file);
        }
        return list;
    }

    /**
     * 添加jar或者class
     */
    public static void addEnvClassOrJar() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader instanceof URLClassLoader) {
            List<File> files = new ArrayList<>();
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            URL[] urls = urlClassLoader.getURLs();
            for (URL url : urls) {
                files.add(new File(url.getFile()));
            }
//            if(!BooleanHelper.hasLength(urls)) {
//                Collection<URL> classLoaderUrls = UrlHelper.classLoaderUrls();
//                for (URL classLoaderUrl : classLoaderUrls) {
//                    files.add(new File(classLoaderUrl.getFile()));
//                }
//            }
            try {
                STANDARD_FILE_MANAGER.setLocation(StandardLocation.CLASS_PATH, files);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
