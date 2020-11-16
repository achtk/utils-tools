package com.chua.utils.tools.plugins.classes;

import com.chua.utils.tools.plugins.classloader.PluginClassLoader;

import java.nio.file.Path;
import java.util.Collection;

/**
 * 类管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public interface ClassManager {
    /**
     * 获取类加载器
     *
     * @return 类加载器
     */
    PluginClassLoader createClassLoader();

    /**
     * 注册Path为Class
     *
     * @param path 路径
     */
    void register(Path path);

    /**
     * 注销注册Path
     *
     * @param path 路径
     */
    void unregister(Path path);

    /**
     * 获取子类
     *
     * @param className 类名
     * @return 返回值
     */
    Collection<Object> findSubType(String className);
}
