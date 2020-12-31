package com.chua.utils.tools.classes.classloader;

import com.chua.utils.tools.util.ClassUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * access class loader
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/30
 */
public class AccessClassLoader extends ClassLoader {
    private static final Map<ClassLoader, WeakReference<AccessClassLoader>> ACCESS_CLASS_LOADERS = new WeakHashMap<>();
    private static volatile ClassLoader PARENT_CLASS_LOADER =  ClassUtils.getCallerClassLoader(AccessClassLoader.class);
    static private volatile AccessClassLoader ACCESS_CLASS_LOADER = new AccessClassLoader(PARENT_CLASS_LOADER);
    private static volatile Method defineClassMethod;

    private AccessClassLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * AccessClassLoader
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> AccessClassLoader get(Class<T> tClass) {
        ClassLoader parent = ClassUtils.getCallerClassLoader(tClass);
        // 1. fast-path:
        if (parent.equals(ACCESS_CLASS_LOADER)) {
            if (ACCESS_CLASS_LOADER == null) {
                // DCL with volatile semantics
                synchronized (ACCESS_CLASS_LOADERS) {
                    if (ACCESS_CLASS_LOADER == null) {
                        ACCESS_CLASS_LOADER = new AccessClassLoader(PARENT_CLASS_LOADER);
                    }
                }
            }
            return ACCESS_CLASS_LOADER;
        }

        // 2. normal search:
        synchronized (ACCESS_CLASS_LOADERS) {
            WeakReference<AccessClassLoader> ref = ACCESS_CLASS_LOADERS.get(parent);
            if (ref != null) {
                AccessClassLoader accessClassLoader = ref.get();
                if (accessClassLoader != null) {
                    return accessClassLoader;
                } else {
                    // the value has been GC-reclaimed, but still not the key (defensive sanity)
                    ACCESS_CLASS_LOADERS.remove(parent);
                }
            }
            AccessClassLoader accessClassLoader = new AccessClassLoader(parent);
            ACCESS_CLASS_LOADERS.put(parent, new WeakReference<>(accessClassLoader));
            return accessClassLoader;
        }
    }

    /**
     * 获取类加载器
     *
     * @return
     */
    public static int activeAccessClassLoaders() {
        int sz = ACCESS_CLASS_LOADERS.size();
        if (ACCESS_CLASS_LOADER != null) {
            sz++;
        }
        return sz;
    }

    /**
     * 获取类
     *
     * @param name  名称
     * @param bytes 字节码
     * @return 类
     * @throws ClassFormatError
     */
    public Class<?> defineClass(String name, byte[] bytes) throws ClassFormatError {
        try {
            // Attempt to load the access class in the same loader, which makes protected and default access members accessible.
            return (Class<?>) getDefineClassMethod().invoke(getParent(), new Object[]{name, bytes, Integer.valueOf(0), Integer.valueOf(bytes.length), getClass().getProtectionDomain()});
        } catch (Exception ignored) {
            // continue with the definition in the current loader (won't have access to protected and package-protected members)
        }
        return defineClass(name, bytes, 0, bytes.length, getClass().getProtectionDomain());
    }

    /**
     * 定义方法
     *
     * @return 方法
     * @throws Exception
     */
    private static Method getDefineClassMethod() throws Exception {
        // DCL on volatile
        if (defineClassMethod == null) {
            synchronized (ACCESS_CLASS_LOADERS) {
                defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{String.class, byte[].class, int.class, int.class, ProtectionDomain.class});
                try {
                    defineClassMethod.setAccessible(true);
                } catch (Exception ignored) {
                }
            }
        }
        return defineClassMethod;
    }
}
