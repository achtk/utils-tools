package com.chua.utils.tools.script;

import bsh.EvalError;
import bsh.Interpreter;
import com.chua.utils.tools.compiler.Compiler;
import com.chua.utils.tools.compiler.JavassistCompiler;
import com.chua.utils.tools.compiler.JdkCompiler;
import com.chua.utils.tools.util.IoUtils;

import java.io.File;
import java.net.URL;
import java.util.Map;

/**
 * java脚本
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/11
 */
public class JavaScript implements Script {

    private static final Compiler JAVASSIST_COMPILER = new JavassistCompiler();
    private static final Compiler JDK_COMPILER = new JdkCompiler();

    @Override
    public Object eval(String script, Map<String, Object> params) throws Exception {
        Interpreter interpreter = new Interpreter();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            try {
                interpreter.set(entry.getKey(), entry.getValue());
            } catch (EvalError evalError) {
                continue;
            }
        }
        interpreter.setClassLoader(this.getClass().getClassLoader());
        return interpreter.eval(script);
    }

    @Override
    public Class<?> compiler(String script, ClassLoader classLoader) throws Exception {
        try {
            return JAVASSIST_COMPILER.compiler(script, classLoader);
        } catch (Exception e) {
            return JDK_COMPILER.compiler(script, classLoader);
        }
    }

    @Override
    public Class<?> compiler(File script, ClassLoader classLoader) throws Exception {
        return compiler(IoUtils.toString(script), classLoader);
    }

    @Override
    public Class<?> compiler(URL script, ClassLoader classLoader) throws Exception {
        return compiler(IoUtils.toString(script.toURI()), classLoader);
    }
}
