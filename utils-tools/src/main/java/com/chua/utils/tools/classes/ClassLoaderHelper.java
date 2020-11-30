package com.chua.utils.tools.classes;

import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.common.skip.SkipPatterns;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * 类加载器工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/11
 */
public class ClassLoaderHelper extends ReflectionHelper {

    /**
     * 获取类的类加载器
     *
     * @param caller 类
     * @return ClassLoader
     */
    public static ClassLoader getCallerClassLoader(Class<?> caller) {
        Preconditions.checkNotNull(caller);
        return caller.getClassLoader();
    }

    /**
     * 获取默认类加载器
     * <p>
     * 默认获取当前线程的类加载器<code>Thread.currentThread().getContextClassLoader()</code>
     * <p>{@link Thread#currentThread()#getDefaultClassLoader()}</p>
     * </p>
     *
     * @return 类加载器
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        if (cl == null) {
            cl = ClassHelper.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignored) {
                }
            }
        }
        return cl;
    }

    /**
     * 获取默认类加载器
     *
     * @param classLoaders 类加载器
     * @return 如果请求参数为空返回默认类加载器, 反之返回请求参数
     */
    public static List<ClassLoader> getOrDefault(final ClassLoader[] classLoaders) {
        return BooleanHelper.hasLength(classLoaders) ? Arrays.asList(classLoaders) : Collections.singletonList(getDefaultClassLoader());
    }

    /**
     * 获取类加载器下的URL非JDK
     *
     * @param classLoader 类加载器
     * @return URL[]
     */
    public static URL[] getUrlsByClassLoaderExcludeJdk(ClassLoader classLoader) {
        URL[] urlsByClassLoader = getUrlsByClassLoader(classLoader);
        List<URL> cache = new ArrayList<>(urlsByClassLoader.length);
        Arrays.stream(urlsByClassLoader).parallel().forEach(url -> {
            String form = url.toExternalForm();
            for (String item : SkipPatterns.JDK_LIB) {
                if (StringHelper.wildcardMatch(FileHelper.getName(form), item)) {
                    return;
                }
            }
            cache.add(url);
        });
        return cache.toArray(new URL[0]);
    }

    /**
     * 获取类加载器下的URL
     *
     * @param classLoader 类加载器
     * @return URL[]
     */
    public static URL[] getUrlsByClassLoader(ClassLoader classLoader) {
        if (!(classLoader instanceof URLClassLoader)) {
            return ArraysHelper.emptyArray(URL.class);
        }
        URLClassPath urlClassPath = SharedSecrets.getJavaNetAccess().getURLClassPath((URLClassLoader) classLoader);
        URL[] urls = urlClassPath.getURLs();
        if (BooleanHelper.hasLength(urls)) {
            return urls;
        }

        return Arrays.stream(System.getProperty("java.class.path").split(";")).map(new Function<String, URL>() {
            @Override
            public @Nullable
            URL apply(@Nullable String input) {
                if (null == input) {
                    return null;
                }
                try {
                    return new URL(input);
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        }).filter((Predicate<URL>) Objects::nonNull).toArray(value -> new URL[0]);
    }
}
