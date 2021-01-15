package com.chua.utils.tools.example;

import com.chua.tools.groovy.script.GroovyScript;
import com.chua.utils.tools.example.interfaces.ITestInterface;
import com.chua.utils.tools.script.Script;
import com.chua.utils.tools.util.ClassUtils;

import java.io.File;

/**
 * @author CH
 * @version 1.0.0
 * @since 2021/1/11
 */
public class GroovyUtilsExample {

    public static void main(String[] args) throws Exception {
        Script script = new GroovyScript();
        Class<?> compiler = script.compiler(new File("D:\\work\\utils-tools-parent\\utils-example\\src\\main\\java\\com\\chua\\utils\\tools\\example\\groovy\\Demo.groovy"));
        ITestInterface testInterface = ClassUtils.forObject(compiler, ITestInterface.class);
        System.out.println(testInterface.getName());
    }
}
