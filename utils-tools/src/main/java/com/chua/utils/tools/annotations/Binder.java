package com.chua.utils.tools.annotations;

import com.chua.utils.tools.factory.BinderFactory;

import java.lang.annotation.*;

/**
 * 绑定器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Binder {
    /**
     * 值
     *
     * @return 值
     */
    String value() default "";

    /**
     * 配置文件
     *
     * @return 配置文件
     * @see Type#PROFILE
     */
    String profile() default "";

    /**
     * 注册类型
     *
     * @return 注册类型
     */
    Type type() default Type.BEAN;

    /**
     * 工厂
     *
     * @return factory
     * @see Type#PROXY
     */
    Class<? extends BinderFactory> factory() default BinderFactory.class;

    /**
     * 类型
     */
    enum Type {
        /**
         * 注册Bean等价于@Autowire(require = false)
         */
        BEAN,
        /**
         * 脚本(e.g.test.script)
         */
        SCRIPT,
        /**
         * java文件
         */
        JAVA,
        /**
         * 值
         */
        VALUE,
        /**
         * restful
         */
        REST,
        /**
         * profile
         */
        PROFILE,
        /**
         * Proxy
         */
        PROXY
    }
}
