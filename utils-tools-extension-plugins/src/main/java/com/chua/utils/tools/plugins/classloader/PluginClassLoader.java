package com.chua.utils.tools.plugins.classloader;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.plugins.entity.ClassInfo;
import lombok.Setter;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.chua.utils.tools.constant.StringConstant.CLASS_FILE_EXTENSION;

/**
 * 插件类加载器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class PluginClassLoader extends URLClassLoader {

    @Setter
    private ClassInfo classInfo;
    private final ConcurrentMap<String, Class<?>> loader = new ConcurrentHashMap<>();

    public PluginClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (loader.containsKey(name)) {
            return loader.get(name);
        }
        try {
            Class<?> aClass = super.loadClass(name);
            loader.put(name, aClass);
            return aClass;
        } catch (ClassNotFoundException e) {
            ByteBuffer buffer = classInfo.getByteBuffer();
            try {
                Class<?> aClass = defineClass(FileHelper.deleteSuffix(name, CLASS_FILE_EXTENSION), buffer.array(), 0, buffer.array().length);
                loader.put(name, aClass);
                return aClass;
            } catch (ClassFormatError ignore) {
            }
        }
        loader.put(name, null);
        return null;
    }
}
