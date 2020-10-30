package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;

import java.util.Collection;

/**
 * 扩展解释器实现Spi机制
 *
 * @author CH
 * @version 1.0.0
 * @see com.chua.utils.tools.spi.entity.SpiConfig
 * @see com.chua.utils.tools.spi.entity.ExtensionClass
 * @see com.chua.utils.tools.spi.factory.ExtensionFactory
 * @see com.chua.utils.tools.spi.processor.SimpleExtensionProcessor
 * @since 2020/6/3 15:22
 */
public interface ExtensionProcessor<T> {
    /**
     * spi初始化配置
     *
     * @param spiConfig
     */
    void init(SpiConfig spiConfig);

    /**
     * Spi机制解析对象
     *
     * @param service     接口
     * @param classLoader 类加载器
     * @return Multimap
     */
    Collection<ExtensionClass<?>> analyze(Class<T> service, ClassLoader classLoader);

    /**
     * 删除缓存
     */
    void removeAll();
    /**
     * 删除缓存
     * @param tClass 类
     */
    void remove(Class<T> tClass);
}
