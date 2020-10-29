package com.chua.utils.tools.spi.entity;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.spi.Spi;
import lombok.Getter;
import lombok.Setter;

/**
 * Spi扩展器
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
     * 是否覆盖其它低{@link #order}的同名扩展
     */
    private boolean overrider;
    /**
     * 类加载器
     */
    private ClassLoader classLoader;
    /**
     * 扩展接口实现类名
     */
    protected Class<? extends T> clazz;

    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 实体对象
     * @see #single
     */
    private T obj;
    /**
     * 是否初始化
     */
    private boolean isInitial = false;
    /**
     *
     * @return
     */
    public T getObj() {
        if(!single) {
            return null == clazz ? null : ClassHelper.forObject(clazz, classLoader);
        } else {
            if(null != obj) {
                return obj;
            } else {
                if(!isInitial) {
                    this.obj = null == clazz ? null : ClassHelper.forObject(clazz, classLoader);
                    isInitial = true;
                }
            }
        }
        return obj;
    }



    /**
     * 转配Spi
     * @param spi spi
     */
    public void assembleSpi(Spi spi) {
        setInitial(false);
        setName(spi.value());
        setSingle(spi.single());
        setOverrider(spi.override());
        setOrder(spi.order());
    }
}
