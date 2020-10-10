package com.chua.utils.tools.loader;

import com.chua.utils.tools.common.BooleanHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * 负载均衡加载器
 * @author CH
 * @date 2020-10-08
 */
public interface BalancerLoader<T> {

    /**
     * 创建获取平衡数据
     * @param ts 数据源
     * @return
     */
    T balancer(Collection<T> ts);
    /**
     * 创建获取平衡数据
     * @param ts 数据源
     * @return
     */
    default T balancer(T... ts) {
        if(!BooleanHelper.hasLength(ts)) {
            return null;
        }
        return balancer(Arrays.asList(ts));
    }
}
