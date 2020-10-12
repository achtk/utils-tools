package com.chua.utils.tools.mapper;

import com.chua.utils.tools.function.intercept.MethodIntercept;

/**
 * 代理映射
 * @author CH
 */
public class AllProxyMapper extends ProxyMapper {

    private MethodIntercept methodIntercept;

    public AllProxyMapper(MethodIntercept methodIntercept) {
        this.methodIntercept = methodIntercept;
        this.addIntercept("*", methodIntercept);
    }
}
