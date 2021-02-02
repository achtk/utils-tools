package com.chua.utils.tools.annotations;

import java.lang.annotation.*;

/**
 * rpc
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/16
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rpc {
}
