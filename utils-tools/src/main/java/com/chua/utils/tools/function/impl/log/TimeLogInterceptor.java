package com.chua.utils.tools.function.impl.log;

import com.chua.utils.tools.function.LogInterceptor;
import com.chua.utils.tools.util.DateUtils;
import lombok.EqualsAndHashCode;

/**
 * 时间拦截
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/28
 */
@EqualsAndHashCode
public class TimeLogInterceptor implements LogInterceptor {
    @Override
    public String identifier() {
        return "%time";
    }

    @Override
    public Object intercept(Object... args) {
        return DateUtils.currentString();
    }
}
