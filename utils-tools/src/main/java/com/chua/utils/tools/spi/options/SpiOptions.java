package com.chua.utils.tools.spi.options;

/**
 * 配置项关键字
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 15:52
 */
public class SpiOptions {
    /**
     * 决定本配置文件的加载顺序，越大越往后加载
     */
    public static final String SPI_CFG_ORDER = "spi.config.order";
    /**
     * 扩展点加载的路径
     */
    public static final String EXTENSION_LOAD_PATH = "extension.load.path";
    /**
     * meta 扩展点名称
     */
    public static final String EXTENSION_META_NAME = "extension.meta.name";
}
