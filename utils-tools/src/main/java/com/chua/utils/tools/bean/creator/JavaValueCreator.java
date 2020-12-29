package com.chua.utils.tools.bean.creator;

import com.chua.utils.tools.annotations.BinderScript;
import com.chua.utils.tools.bean.script.ValueScript;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.compiler.Compiler;
import com.chua.utils.tools.compiler.JavassistCompiler;
import com.chua.utils.tools.compiler.JdkCompiler;
import com.chua.utils.tools.util.UrlUtils;
import com.google.common.base.Strings;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * java文件 -> Value
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class JavaValueCreator implements ValueCreator {
    @Override
    public Object create(ValueScript valueScript) {
        String scriptPath = valueScript.getOperate().getString(ValueScript.VALUE);
        //脚本不存在
        if (Strings.isNullOrEmpty(scriptPath)) {
            return null;
        }

        URL url = UrlUtils.parseFileUrl(scriptPath);
        if (null == url) {
            return null;
        }
        Class<?> compilerClass = null;
        Compiler compiler = new JavassistCompiler();
        try {
            compilerClass = compiler.compiler(Files.asCharSource(new File(url.getFile()), StandardCharsets.UTF_8).read());
        } catch (IOException e) {
            compiler = new JdkCompiler();
            try {
                compilerClass = compiler.compiler(Files.asCharSource(new File(url.getFile()), StandardCharsets.UTF_8).read());
            } catch (IOException e1) {
                return null;
            }
        }
        if (null == compilerClass) {
            return null;
        }
        return ClassHelper.forObject(compilerClass);
    }

    @Override
    public boolean isMatcher(BinderScript.Type type) {
        return type == BinderScript.Type.JAVA;
    }
}
