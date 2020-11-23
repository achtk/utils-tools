package com.chua.utils.netx.resolver.fs;

import java.io.IOException;
import java.util.Set;

/**
 * 文件服务器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public interface FileServer {
    /**
     * 查询信息
     *
     * @param name 关键词
     * @return Set
     * @throws IOException IOException
     */
    Set<String> search(String name) throws IOException;

    /**
     * 创建文件夹
     *
     * @param name 关键词
     * @return boolean
     * @throws IOException IOException
     */
    boolean mkdir(String name) throws IOException;

    /**
     * 删除文件
     *
     * @param name 关键词
     * @return boolean
     * @throws IOException IOException
     */
    boolean deleteFile(String name) throws IOException;

    /**
     * 文件或文件夹存在
     *
     * @param name 关键词
     * @return boolean
     */
    boolean exist(String name);
}
