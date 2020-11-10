package com.chua.utils.tools.spi;

import java.lang.annotation.*;

/**
 * @author CHTK
 * @see java.lang.annotation.Annotation
 * @see com.chua.utils.tools.spi.entity.ExtensionClass
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Spi {
    /**
     * Spi名称
     *
     * @return
     */
    String[] value() default {};

    /**
     * 优先级
     *
     * @return
     */
    int order() default 0;

    /**
     * 覆盖
     *
     * @return
     */
    boolean override() default false;

    /**
     * 是否为单例
     *
     * @return
     */
    boolean single() default true;
}
