package com.chua.utils.tools.spi.entity;

import com.chua.utils.tools.aware.NamedFactoryAware;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.URL;

import static com.chua.utils.tools.constant.StringConstant.FILE;

/**
 * Spi扩展器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 15:16
 */
@Getter
@Setter
public class ExtensionClass<T> {
    /**
     * 扩展名称
     */
    private String name;
    /**
     * 扩展点排序值，大的优先级高
     */
    private int order;
    /**
     * 文件位置
     */
    private URL url;

    /**
     * 是否为单例模式
     */
    private boolean single;

    /**
     * 类加载器
     */
    private ClassLoader classLoader;
    /**
     * 扩展接口实现类名
     */
    protected Class<?> implClass;
    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 实体对象
     *
     * @see #single
     */
    private T obj;

    /**
     * 命名工厂
     */
    private NamedFactoryAware namedFactoryAware;
    /**
     * 记录时间
     */
    private long recordTime;

    public void setUrl(URL url) {
        if(null == url) {
            return;
        }
        String protocol = url.getProtocol();
        if(FILE.equals(protocol)) {
            this.recordTime = new File(url.getFile()).lastModified();
        }
        this.url = url;
    }
}
