package com.chua.utils.tools.resource.parser;

/**
 * 目录
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public interface ParserDir {
    /**
     * 获取路径
     *
     * @return 路径
     */
    String getPath();

    /**
     * 遍历文件
     *
     * @return 文件
     */
    Iterable<ParserFile> getFiles();

    /**
     * 关闭流
     */
    void close();
}
