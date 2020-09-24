package com.chua.utils.tools.config;

import com.chua.utils.tools.action.ActionListener;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.handler.AgencyHandler;
import com.google.common.base.Predicate;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 策略对象
 * @author CH
 */
@Getter
@Setter
public class StrategyProperties<T> {
    /**
     * 尝试次数
     */
    private int retry;
    /**
     * 超时
     */
    private int timeout;
    /**
     * 动作
     */
    //private IStrategyPolicy<T> strategyPolicy;
    /**
     * 降级
     */
    private Object degrade;
    /**
     * 代理的类
     */
    private Class<?> classes;
    /**
     * 异常监听
     */
    private ActionListener throwableListener;
    /**
     * 异步监听
     */
    private ActionListener asyncListener;
    /**
     * 尝试策略使用
     * <p>Predicate#true重试</p>
     */
    private Predicate predicate;
    /**
     * 代理策略使用
     * <p>Predicate#true重试</p>
     */
    private AgencyHandler agencyHandler;
    /**
     * 额外属性
     */
    private Properties properties;
    /**
     * 方法以及拦截器
     */
    private Map<String, MethodIntercept> methodInterceptMap = new HashMap<>();
}
