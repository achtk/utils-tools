package com.chua.utils.tools.function.impl.log;

import com.chua.utils.tools.function.LogInterceptor;
import com.chua.utils.tools.util.SystemUtil;
import lombok.EqualsAndHashCode;

/**
 * 时间拦截
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/28
 */
@EqualsAndHashCode
public class IpLogInterceptor implements LogInterceptor {
    @Override
    public String identifier() {
        return "%ip";
    }

    @Override
    public Object intercept(Object... args) {
        return SystemUtil.getLocalAddress();
    }
}
