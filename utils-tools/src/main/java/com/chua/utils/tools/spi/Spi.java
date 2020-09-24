package com.chua.utils.tools.spi;

import java.lang.annotation.*;

/**
 * @spi
 * @author CHTK
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Spi {
    /**
     * Spi名称
     * @return
     */
    String value();

    /**
     * 优先级
     * @return
     */
    int order() default 0;

    /**
     * 覆盖
     * @return
     */
    boolean override() default false;

    /**
     * 是否为单例
     * @return
     */
    boolean single() default true;
}
