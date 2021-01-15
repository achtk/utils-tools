package com.chua.utils.tools.resource.factory;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.entity.Resource;

import java.util.Set;

/**
 * 资源工厂
 *
 * @author CH
 * @since 1.0
 */
public interface ResourceFactory {

    /**
     * 设置类加载器
     *
     * @param classLoader 类加载器
     * @return
     */
    ResourceFactory classLoader(ClassLoader classLoader);

    /**
     * 设置类加载器
     *
     * @param matcher 匹配器
     * @return IResourceFactory
     */
    ResourceFactory matcher(Matcher<Resource> matcher);

    /**
     * 是否缓存数据
     *
     * @param cache 是否缓存
     * @return IResourceFactory
     */
    ResourceFactory cache(boolean cache);

    /**
     * 是否计数
     *
     * @return
     */
    ResourceFactory count();

    /**
     * 获取资源
     *
     * @param name     待查询资源，支持通配符
     * @param excludes 文件夹|文件, 支持通配符
     * @return
     */
    Set<Resource> getResources(String name, String... excludes);
}
