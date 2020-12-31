package com.chua.tools.example;

import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.compiler.Compiler;
import com.chua.utils.tools.compiler.JdkCompiler;

import java.io.IOException;

import static com.chua.utils.tools.common.charset.CharsetHelper.UTF_8;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class CompilerExample extends BaseExample {

    public static void main(String[] args) throws IOException {
        Compiler compiler = new JdkCompiler();
        Class<?> aClass = compiler.compiler(IoHelper.toString(ClassLoader.getSystemResource("TestLogger1"), UTF_8));
        log.info("字符串生成类: " + aClass);
    }
}
