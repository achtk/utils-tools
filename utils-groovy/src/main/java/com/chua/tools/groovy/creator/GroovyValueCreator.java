package com.chua.tools.groovy.creator;

import com.chua.tools.groovy.script.GroovyScript;
import com.chua.utils.tools.annotations.BinderScript;
import com.chua.utils.tools.bean.creator.ValueCreator;
import com.chua.utils.tools.bean.script.ValueScript;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.script.Script;
import com.chua.utils.tools.util.UrlUtils;
import com.google.common.base.Strings;
import com.google.common.io.Files;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * groovy 脚本
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/11
 */
public class GroovyValueCreator implements ValueCreator {
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
        Script script = new GroovyScript();
        try {
            Class<?> compiler = script.compiler(Files.asCharSource(new File(url.getFile()), StandardCharsets.UTF_8).read());
            return ClassHelper.forObject(compiler);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isMatcher(BinderScript.Type type) {
        return type == BinderScript.Type.GROOVY;
    }
}
