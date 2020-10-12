package com.chua.utils.tools.strategy.handler;

/**
 * 策略动作
 * @author CH
 */
public interface IAsyncStrategyPolicy<T> {
    /**
     * 尝试
     *
     * <table border=1 cellpadding=5 summary="implements,desc">
     * <tr>
     *      <th>index</th>
     *      <th>implements</th>
     *      <th>desc</th>
     * </tr>
     *
     * <tr>
     *   <td>代理</td>
     *   <td>com.chua.utils.tools.strategy.resolver.ProxyStrategyResolver</td>
     *   <td>返回对象，或者对象类</td>
     * </tr>
     *
     * <tr>
     *   <td>缓存</td>
     *   <td>com.chua.utils.tools.strategy.resolver.CacheStrategyResolver</td>
     *   <td>返回对象，或者对象类</td>
     * </tr>
     *
     * <tr>
     *   <td>限流</td>
     *   <td>com.chua.utils.tools.strategy.resolver.LimitStrategyResolver</td>
     *   <td>返回对象，或者对象类</td>
     * </tr>
     *
     * <tr>
     *   <td>超时</td>
     *   <td>com.chua.utils.tools.strategy.resolver.TimeStrategyResolver</td>
     *   <td>返回结果</td>
     * </tr>
     *
     * <tr>
     *   <td>重试次数重试</td>
     *   <td>com.chua.utils.tools.strategy.resolver.RetryLimitStrategyResolver</td>
     *   <td>返回结果</td>
     * </tr>
     *
     * <tr>
     *   <td>按照条件重试</td>
     *   <td>com.chua.utils.tools.strategy.resolver.RetryConditionStrategyResolver</td>
     *   <td>返回结果</td>
     * </tr>

     *
     * </table>
     */
    void result(T result);

    /**
     * 失败
     * @param throwable 异常
     * @return
     */
    void failure(Throwable throwable);

    /**
     * 降级
     * @return
     */
    default T degrade() {
        return null;
    }

    /**
     * 异常降级结果
     * @param throwable 异常
     */
    default void degradeResult(Throwable throwable) {
        result(degrade());
        failure(throwable);
    }
}
