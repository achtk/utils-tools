package com.chua.utils.tools.bean.script;

import com.chua.utils.tools.annotations.Binder;
import com.chua.utils.tools.collects.OperateHashMap;
import lombok.Getter;
import lombok.Setter;

/**
 * 值脚本
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
@Getter
@Setter
public class ValueScript {
    /**
     * 对象
     */
    private Object bean;
    /**
     * 目标对象
     */
    private Class<?> targetClass;
    /**
     * 类型
     */
    private Binder.Type type;
    /**
     * 参数
     *
     * @see com.chua.utils.tools.annotations.Binder#type()
     * @see com.chua.utils.tools.annotations.Binder#value()
     * @see com.chua.utils.tools.annotations.Binder#factory()
     */
    private OperateHashMap operate;
    /**
     * 主体数据
     */
    public static final String VALUE = "value";
    /**
     * 工厂数据
     */
    public static final String FACTORY = "factory";
}
