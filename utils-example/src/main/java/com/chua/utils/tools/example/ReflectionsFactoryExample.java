package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.reflections.RewriteReflections;
import com.chua.utils.tools.classes.reflections.configuration.RewriteConfiguration;
import com.chua.utils.tools.common.ThreadHelper;
import org.reflections.Configuration;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/11
 */
public class ReflectionsFactoryExample {

    public static void main(String[] args) throws MalformedURLException {
        RewriteConfiguration configuration = new RewriteConfiguration();
        configuration.setExecutorService(ThreadHelper.newProcessorThreadExecutor());
        configuration.setUrls(new URL("file:\\D:\\soft\\apache\\apache-maven-3.6.1-bin\\repo\\commons-codec\\commons-codec\\1.15\\commons-codec-1.15.jar"));
        configuration.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false));

        RewriteReflections reflectionsFactory = new RewriteReflections(configuration);
        System.out.println(reflectionsFactory.getAllTypes().size());
        reflectionsFactory.scanUrl(new URL("file:\\D:\\soft\\apache\\apache-maven-3.6.1-bin\\repo\\commons-collections\\commons-collections\\3.2.2\\commons-collections-3.2.2.jar"));
        System.out.println(reflectionsFactory.getAllTypes().size());
    }
}
