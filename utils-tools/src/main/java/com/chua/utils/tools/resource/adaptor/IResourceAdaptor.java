package com.chua.utils.tools.resource.adaptor;


import com.chua.utils.tools.resource.context.ResourceContext;

import java.io.IOException;

/**
 * 资源处理器
 * @author CH
 * @since 1.0
 */
public interface IResourceAdaptor {
    /**
     * 解析文件成 ResourceContext
     * @param path 文件路径/文件
     * @return ResourceContext
     * @throws  IOException IOException
     */
    public ResourceContext analyze(String path) throws IOException;
}
