package com.chua.utils.tools.annotations;

import java.lang.annotation.*;

/**
 * 映射绑定器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BinderMapper {
    /**
     * 值
     *
     * @return 值
     */
    String value() default "";
}
