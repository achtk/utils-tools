package com.chua.utils.tools.spring.assembly;

import java.lang.annotation.*;

/**
 * 自动装配
 * @author CH
 * @date 2020-09-28
 * @see com.chua.utils.tools.spring.environment.EnvironmentFactory
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Assembly {
    /**
     *
     * @return
     */
    String prefix() default "";
}
