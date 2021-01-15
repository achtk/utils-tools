package com.chua.tools.groovy.script;

import com.chua.utils.tools.script.Script;
import com.chua.utils.tools.util.CollectionUtils;
import com.chua.utils.tools.util.FileUtils;
import com.chua.utils.tools.util.IoUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * groovy脚本
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/11
 */
public class GroovyScript implements Script {

    private static final Map<String, GroovyClassLoader> CACHE = new ConcurrentHashMap<>();


    public static CompilerConfiguration config = new CompilerConfiguration();

    {
        config.setSourceEncoding("UTF-8");
    }

    @Override
    public Object eval(String script, Map<String, Object> params) throws Exception {
        // 调用带参数的groovy shell时，使用bind绑定数据
        Binding binding = new Binding();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            binding.setProperty(entry.getKey(), entry.getValue());
        }
        GroovyShell groovyShell = new GroovyShell(binding);
        return groovyShell.evaluate(script);
    }

    @Override
    public Class<?> compiler(String script, ClassLoader classLoader) throws Exception {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(classLoader, config);
        return groovyClassLoader.parseClass(script);
    }

    @Override
    public Class<?> compiler(File script, ClassLoader classLoader) throws Exception {
        String absolutePath = script.getAbsolutePath();

        GroovyClassLoader groovyClassLoader = CollectionUtils.get(absolutePath, () -> new GroovyClassLoader(classLoader, config), CACHE);
        return groovyClassLoader.parseClass(script);
    }

    @Override
    public Class<?> compiler(URL script, ClassLoader classLoader) throws Exception {
        String name = FileUtils.getName(script.getFile());
        GroovyClassLoader groovyClassLoader = CollectionUtils.get(script.toExternalForm(), () -> new GroovyClassLoader(classLoader, config), CACHE);
        return groovyClassLoader.parseClass(IoUtils.toUtf8InputStreamReader(script), name);
    }

}
