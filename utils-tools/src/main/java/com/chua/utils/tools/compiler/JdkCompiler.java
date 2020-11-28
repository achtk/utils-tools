package com.chua.utils.tools.compiler;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.taobao.arthas.compiler.DynamicCompiler;
import javassist.CtClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdk 编译器
 *
 * @author CHTK
 */
@Slf4j
public class JdkCompiler implements Compiler {
    @Override
    public Class<?> doCompile(String name, String source) throws Throwable {
        DynamicCompiler dynamicCompiler = new DynamicCompiler(ClassHelper.getDefaultClassLoader());
        dynamicCompiler.addSource(name, source);
        return FinderHelper.firstElement(dynamicCompiler.build().values());
    }
}
