package com.chua.utils.tools.resource.parser;

import com.chua.utils.tools.common.IoHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * 解析文件
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public interface ParserFile {
    /**
     * 获取文件名
     *
     * @return 文件名
     */
    String getName();

    /**
     * 获取文件路径
     *
     * @return 文件路径
     */
    String getRelativePath();

    /**
     * 获取文件路径
     *
     * @return 文件路径
     */
    Path path();

    /**
     * 数据流
     *
     * @return 数据流
     * @throws IOException IOException
     */
    InputStream openInputStream() throws IOException;

    /**
     * 字节缓冲
     *
     * @return 字节缓冲
     */
    default ByteBuffer toByteBuffer() {
        try (InputStream inputStream = openInputStream()) {
            return IoHelper.copyInputStream(inputStream);
        } catch (IOException e) {
        }
        return null;
    }
}
