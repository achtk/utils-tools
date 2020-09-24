package com.chua.utils.tools.script;

import com.chua.utils.tools.classes.ClassHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * js脚本解释器
 * @author CH
 * @version 1.0.0
 * @since 2020/5/29 14:04
 */
public class JavaScriptResolver implements IScriptResolver {

    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    private static final ScriptEngine SCRIPT_ENGINE = SCRIPT_ENGINE_MANAGER.getEngineByName("JavaScript");
    private static final Bindings BINDINGS = SCRIPT_ENGINE.createBindings();
    private Object value;

    @Override
    public IScriptResolver env(String key, Object value) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));

        BINDINGS.put(key, value);
        return this;
    }

    @Override
    public IScriptResolver env(Map<String, Object> envs) {
        Preconditions.checkArgument(null != envs);

        BINDINGS.putAll(envs);
        return this;
    }

    @Override
    public IScriptResolver script(String source) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(source));

        try {
            this.value = SCRIPT_ENGINE.eval(source, BINDINGS);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public IScriptResolver script(InputStream is) {
        Preconditions.checkArgument(null != is);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))){
            this.value = SCRIPT_ENGINE.eval(reader, BINDINGS);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public <T> T eval(String key, Class<T> tClass) {
        return Strings.isNullOrEmpty(key) ? format(tClass) : indexFormat(key, tClass);
    }

    /**
     * 格式化数据
     * @param tClass 类型
     * @param <T>
     * @return
     */
    private <T> T format(Class<T> tClass) {
        return null == tClass ? (T) (this.value + "") : ClassHelper.forObject(this.value, tClass);
    }
    /**
     * 格式化数据
     * @param key 索引
     * @param tClass 类型
     * @param <T>
     * @return
     */
    private <T> T indexFormat(String key, Class<T> tClass) {
        Object o = SCRIPT_ENGINE.get(key);
        if(null == o) {
            return null;
        }
        return null == tClass ? (T) (o + "") : ClassHelper.forObject(o, tClass);
    }


}
