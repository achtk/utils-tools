package com.chua.utils.netx.resolver.fs;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Set;

/**
 * 远程文件服务器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public interface RemoteFileServer extends AutoCloseable{
    /**
     * 下载文件
     *
     * @param name      文件
     * @param localPath 本地目录
     * @throws IOException IOException
     */
    void download(String name, File localPath) throws IOException;

    /**
     * 上传文件
     *
     * @param buffer 文件
     * @param path   路径
     * @return buffer
     * @throws IOException IOException
     */
    void upload(String path, ByteBuffer buffer) throws IOException;
}
