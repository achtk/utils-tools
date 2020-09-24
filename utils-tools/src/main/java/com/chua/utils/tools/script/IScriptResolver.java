package com.chua.utils.tools.script;

import java.io.InputStream;
import java.util.Map;

/**
 * 脚本解释器
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 10:53
 */
public interface IScriptResolver {
    /**
     * 添加环境变量
     * @param key 索引
     * @param value 数据
     * @return
     */
    public IScriptResolver env(final String key, final Object value);

    /**
     * 添加环境变量
     * @param envs 变量
     * @return
     */
    public IScriptResolver env(final Map<String, Object> envs);

    /**
     * 添加脚本
     * @param source 原数据
     * @return
     */
    public IScriptResolver script(final String source);

    /**
     * 添加脚本流
     * @param is 流
     * @return
     */
    public IScriptResolver script(final InputStream is);

    /**
     * 处理脚本
     * @param key 索引
     * @param tClass 类型
     * @return
     */
    public <T>T eval(final String key, Class<T> tClass);

    /**
     * 处理脚本
     * @param tClass 类型
     * @return
     */
    default public <T> T eval(Class<T> tClass) {
        return eval(null, tClass);
    }

    /**
     * 处理脚本
     * @return
     */
    default public Object eval() {
        return eval(null, null);
    }
}
