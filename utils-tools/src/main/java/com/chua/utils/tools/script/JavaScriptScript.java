package com.chua.utils.tools.script;

import com.chua.utils.tools.exceptions.NotSupportedException;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
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
public class JavaScriptScript implements Script {

    private static final ScriptEngineManager MANAGER = new ScriptEngineManager();
    private static final ScriptEngine ENGINE = MANAGER.getEngineByName("JavaScript");

    @Override
    public Object eval(String script, Map<String, Object> params) throws Exception {
        Bindings bindings = ENGINE.createBindings();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            bindings.put(entry.getKey(), entry.getValue());
        }
        return ENGINE.eval(script, bindings);
    }

    @Override
    public Class<?> compiler(String script, ClassLoader classLoader) throws Exception {
        throw new NotSupportedException();
    }

    @Override
    public Class<?> compiler(File script, ClassLoader classLoader) throws Exception {
        throw new NotSupportedException();
    }

    @Override
    public Class<?> compiler(URL script, ClassLoader classLoader) throws Exception {
        throw new NotSupportedException();
    }
}

