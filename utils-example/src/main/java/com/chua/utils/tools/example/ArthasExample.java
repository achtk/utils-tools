package com.chua.utils.tools.example;

import com.taobao.arthas.compiler.DynamicCompiler;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/11
 */
public class ArthasExample {

    public static void main(String[] args) throws IOException {
        String jarPath = LoggerFactory.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        File file = new File(jarPath);

        URLClassLoader classLoader = new URLClassLoader(new URL[] { file.toURI().toURL() },
                ClassLoader.getSystemClassLoader().getParent());

        DynamicCompiler dynamicCompiler = new DynamicCompiler(classLoader);

        InputStream logger1Stream = ArthasExample.class.getClassLoader().getResourceAsStream("TestLogger1");
        InputStream logger2Stream = ArthasExample.class.getClassLoader().getResourceAsStream("TestLogger2");

        dynamicCompiler.addSource("TestLogger2", null == logger2Stream ? null : IOUtils.toString(logger2Stream, Charset.defaultCharset()));
        dynamicCompiler.addSource("TestLogger1", null == logger1Stream ? null : IOUtils.toString(logger1Stream, Charset.defaultCharset()));

        Map<String, byte[]> byteCodes = dynamicCompiler.buildByteCodes();

        Assert.assertTrue("TestLogger1", byteCodes.containsKey("com.test.TestLogger1"));
      //  Assert.assertTrue("TestLogger2", byteCodes.containsKey("com.hello.TestLogger2"));
    }
}
