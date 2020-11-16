package com.chua.utils.tools.plugins.paths;

import java.nio.file.Path;

/**
 * 路径管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public interface PathManager {
    /**
     * 接收创建文件
     *
     * @param path 路径
     */
    void postCreate(Path path);

    /**
     * 接收删除文件
     *
     * @param path 路径
     */
    void postDelete(Path path);

    /**
     * 接收更新文件
     *
     * @param path 路径
     */
    void postModify(Path path);
}
