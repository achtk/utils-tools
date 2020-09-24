package com.chua.utils.tools.resource.adaptor;

/**
 * 资源查找器
 * @author CH
 * @since 1.0
 */
public interface IResourceFinderAdaptor extends IResourceAdaptor {
    /**
     * 保存索引
     * @param path 路径
     */
    public void saveDB(String path);

    /**
     * 重建索引
     */
    public void rebuilderDB();
}
