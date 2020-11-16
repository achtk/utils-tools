package com.chua.utils.tools.resource.parser;

import lombok.extern.slf4j.Slf4j;
import org.reflections.vfs.Vfs;

/**
 * 目录
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public interface ParserDir {
    String getPath();

    Iterable<ParserFile> getFiles();

    void close();
}
