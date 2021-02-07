package com.chua.utils.tools.compiler;

import com.chua.utils.tools.aware.NamedAware;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.taobao.arthas.compiler.DynamicCompiler;
import lombok.extern.slf4j.Slf4j;

import static com.chua.utils.tools.constant.StringConstant.NAMED_JDK;

/**
 * jdk 编译器
 *
 * @author CHTK
 */
@Slf4j
public class JdkCompiler implements Compiler, NamedAware {
    @Override
    public Class<?> doCompile(String name, String source) throws Throwable {
        DynamicCompiler dynamicCompiler = new DynamicCompiler(ClassHelper.getDefaultClassLoader());
        dynamicCompiler.addSource(name, source);
        return FinderHelper.firstElement(dynamicCompiler.build().values());
    }

    @Override
    public String named() {
        return NAMED_JDK;
    }
}
