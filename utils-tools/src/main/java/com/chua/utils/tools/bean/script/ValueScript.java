package com.chua.utils.tools.bean.script;

import com.chua.utils.tools.annotations.BinderScript;
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
     * 主体数据
     */
    public static final String VALUE = "value";
    /**
     * profile
     */
    public static final String PROFILE = "profile";
    /**
     * 工厂数据
     */
    public static final String FACTORY = "factory";
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
    private BinderScript.Type type;
    /**
     * 参数
     *
     * @see BinderScript#type()
     * @see BinderScript#value()
     * @see BinderScript#profile()
     * @see BinderScript#factory()
     */
    private OperateHashMap operate;
}
