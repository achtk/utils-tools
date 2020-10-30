package com.chua.utils.tools.spi.entity;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.spi.Spi;
import lombok.Getter;
import lombok.Setter;

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

}
