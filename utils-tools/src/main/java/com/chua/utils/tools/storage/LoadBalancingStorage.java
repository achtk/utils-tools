package com.chua.utils.tools.storage;

import com.chua.utils.tools.function.Balanceable;
import com.chua.utils.tools.function.impl.RoundRobin;

import java.util.List;

/**
 * 均衡机制
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class LoadBalancingStorage {
    /**
     * 运行任务
     *
     * @param tList 对象集
     * @param <T>   类型
     * @return 均衡对象
     */
    public static <T> T run(List<T> tList) {
        return run(tList, null);
    }

    /**
     * 运行任务
     *
     * @param tList       对象集
     * @param balanceable 均衡器
     * @param <T>         类型
     * @return 均衡对象
     */
    public static <T> T run(List<T> tList, Balanceable<T> balanceable) {
        if (null == balanceable) {
            balanceable = new RoundRobin<>();
        }
        return balanceable.balance(tList);
    }
}
