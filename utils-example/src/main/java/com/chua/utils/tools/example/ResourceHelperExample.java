package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.RewriteReflections;
import com.chua.utils.tools.classes.reflections.configuration.RewriteConfiguration;
import com.chua.utils.tools.classes.reflections.scanner.RewriteResourcesScanner;
import com.chua.utils.tools.classes.reflections.scanner.RewriteSubTypesScanner;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.resource.entity.Resource;
import com.chua.utils.tools.resource.factory.FastResourceFactory;
import com.chua.utils.tools.resource.factory.ReflectionFactory;
import com.chua.utils.tools.resource.template.ResourceTemplate;

import java.util.List;
import java.util.Set;

/**
 * @author CH
 */
public class ResourceHelperExample {

    private static final String EXP = "classpath:**/*.class";
    private static final Class<Encrypt> ENCRYPT_CLASS = Encrypt.class;


    public static void main(String[] args) throws InterruptedException {
        //资源查找
        Set<Resource> resources = testFastGetResources();
        //资源查找
        Set<Resource> resources1 = testReflectGetResources();
        //资源查找
        long s1 = System.currentTimeMillis();
        testFastSubGetResources();
        System.out.println("FastResource: " + (System.currentTimeMillis() - s1));
        Thread.sleep(2000);
        //子类查找
        long s2 = System.currentTimeMillis();
        testSubGetResources();
        System.out.println("ReflectResource: " + (System.currentTimeMillis() - s2));
        Thread.sleep(3500);
        System.out.println();
    }

    private static void testSubGetResources() {
        RewriteConfiguration rewriteConfiguration = new RewriteConfiguration();
        rewriteConfiguration.setUrls(ClassHelper.getUrlsByClassLoaderExcludeJdk(ClassHelper.getDefaultClassLoader()));
        rewriteConfiguration.setRewriteScanners(new RewriteSubTypesScanner(false), new RewriteResourcesScanner());
        rewriteConfiguration.setExecutorService(ThreadHelper.newProcessorThreadExecutor());
        rewriteConfiguration.setClassLoaders(new ClassLoader[]{ClassHelper.getDefaultClassLoader()});

        RewriteReflections rewriteReflections = new RewriteReflections(rewriteConfiguration);
        Set<Class<? extends Encrypt>> typesOf = rewriteReflections.getSubTypesOf(ENCRYPT_CLASS);
        System.out.println(typesOf);
    }

    private static void testReflectSubGetResources() {
        ResourceTemplate resourceTemplate = new ResourceTemplate(new ReflectionFactory());
        List<Class<?>> subOfType = resourceTemplate.getSubOfType(ENCRYPT_CLASS);
        System.out.println(subOfType);
    }

    private static void testFastSubGetResources() {
        ResourceTemplate resourceTemplate = new ResourceTemplate(new FastResourceFactory());
        List<Class<?>> subOfType = resourceTemplate.getSubOfType(ENCRYPT_CLASS);
        System.out.println(subOfType);
    }

    public static Set<Resource> testFastGetResources() {
        ResourceTemplate resourceTemplate = new ResourceTemplate(new FastResourceFactory());
        Set<Resource> resources = resourceTemplate.getResources(EXP);
        System.out.println(resources.size());
        return resources;
    }

    public static Set<Resource> testReflectGetResources() {
        ResourceTemplate resourceTemplate = new ResourceTemplate(new ReflectionFactory());
        Set<Resource> resources = resourceTemplate.getResources(EXP);
        System.out.println(resources.size());
        return resources;
    }
}