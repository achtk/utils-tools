package com.chua.utils.tools.plugins.entity;

import com.chua.utils.tools.plugins.classloader.PluginClassLoader;
import lombok.Data;

import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * 类信息
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
@Data
public class ClassInfo {
    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private Path path;
    /**
     * 文件
     */
    private ByteBuffer byteBuffer;
    /**
     * 类加载器
     */
    private ClassLoader classLoader;
}
