package com.chua.utils.tools.manager;

import com.chua.utils.tools.prop.loader.ProfileLoader;

import java.util.Set;

/**
 * 配置文件适配管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public interface ProfileAdaptorManager {
    /**
     * 获取配置文件加载器
     *
     * @param name 名称
     * @return 配置文件加载器
     * @see com.chua.utils.tools.prop.loader.XmlProfileLoader
     * @see com.chua.utils.tools.prop.loader.YamlProfileLoader
     * @see com.chua.utils.tools.prop.loader.JsonProfileLoader
     * @see com.chua.utils.tools.prop.loader.PropertiesProfileLoader
     */
    ProfileLoader get(String name);

    /**
     * 获取所有适配器名称
     * @return 所有适配器名称
     */
    Set<String> names();
}
