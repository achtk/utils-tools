package com.chua.utils.tools.resource.parser;

import com.chua.utils.tools.classes.ClassHelper;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * 编译java
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public interface ParserJava {
    /**
     * 编译Java
     *
     * @param classLoader 类加载器
     * @return class
     */
    Class<?> toClass(ClassLoader classLoader);

    /**
     * 编译Java
     *
     * @param classLoader 类加载器
     * @return Map
     */
    Map<String, ByteBuffer> toBuffer(ClassLoader classLoader);

    /**
     * 编译Java
     *
     * @return class
     */
    default Class<?> toClass() {
        return toClass(ClassHelper.getDefaultClassLoader());
    }

    /**
     * 编译Java
     *
     * @return Map
     */
    default Map<String, ByteBuffer> toBuffer() {
        return toBuffer(ClassHelper.getDefaultClassLoader());
    }
}
