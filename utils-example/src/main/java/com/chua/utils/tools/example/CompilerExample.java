package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.compiler.Compiler;
import com.chua.utils.tools.compiler.JavassistCompiler;
import com.chua.utils.tools.compiler.JdkCompiler;
import org.slf4j.Logger;

import java.io.IOException;


import static com.chua.utils.tools.common.charset.CharsetHelper.UTF_8;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class CompilerExample {

    public static void main(String[] args) throws IOException {
        Compiler compiler = new JdkCompiler();
        Class<?> aClass = compiler.compiler(IoHelper.toString(ClassLoader.getSystemResource("TestLogger1"), UTF_8));
        Logger logger = ClassHelper.forObject(aClass, Logger.class);
        System.out.println("字符串生成类: " + aClass);
    }
}
