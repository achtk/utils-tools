package com.chua.utils.tools.loader;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.function.Filter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * 负载均衡加载器
 *
 * @author CH
 * @date 2020-10-08
 */
public interface BalancerLoader<T> {

    /**
     * 创建平衡数据
     *
     * @param ts 数据源
     * @return
     */
    BalancerLoader data(Collection<T> ts);

    /**
     * 创建平衡器
     *
     * @param filter 平衡
     * @return
     */
    BalancerLoader filter(Filter<T> filter);

    /**
     * 获取平衡数据
     *
     * @return
     */
    T balancer();

    /**
     * 获取随机数据
     * @return
     */
    T random();
}
