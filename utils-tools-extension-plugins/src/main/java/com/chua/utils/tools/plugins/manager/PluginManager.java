package com.chua.utils.tools.plugins.manager;

import com.chua.utils.tools.plugins.classes.ClassManager;

import java.util.Collection;

/**
 * 插件管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public interface PluginManager {
    /**
     * 获取类管理器
     *
     * @return 类管理器
     */
    ClassManager getClassManager();

    /**
     * 获取插件
     *
     * @param objectClass 插件类
     * @return 返回值
     */
    default Collection<Class<?>> getPlugin(Class<Object> objectClass) {
        return getPlugin(null == objectClass ? null : objectClass.getName());
    }
    /**
     * 获取插件
     *
     * @param objectclass 插件
     * @return 返回值
     */
    Collection<Class<?>> getPlugin(String objectclass);
}
