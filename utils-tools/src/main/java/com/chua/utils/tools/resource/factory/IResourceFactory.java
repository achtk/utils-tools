package com.chua.utils.tools.resource.factory;

import com.chua.utils.tools.resource.Resource;

import java.util.Set;

/**
 * 资源工厂
 * @author CH
 * @since 1.0
 */
public interface IResourceFactory {

    /**
     * 设置类加载器
     * @param classLoader 类加载器
     * @return
     */
    public IResourceFactory classLoader(ClassLoader classLoader);

    /**
     * 是否缓存数据
     * @return
     */
    public IResourceFactory cache(boolean cache);
    /**
     * 是否计数
     * @return
     */
    public IResourceFactory count();

    /**
     * 获取资源
     * @param name 待查询资源，支持通配符
     * @param excludes 文件夹|文件, 支持通配符
     * @return
     */
    public Set<Resource> getResources(String name, String... excludes);
}
