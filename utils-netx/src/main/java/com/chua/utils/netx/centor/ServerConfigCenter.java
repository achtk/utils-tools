package com.chua.utils.netx.centor;

import com.chua.utils.tools.handler.Handler;
import com.chua.utils.tools.handler.HandlerResolver;
import com.chua.utils.tools.handler.ThrowableHandler;
import com.chua.utils.tools.properties.NetProperties;

/**
 * 服务配置中心接口
 *
 * @author CH
 * @date 2020-10-07
 */
public interface ServerConfigCenter<T> {
    /**
     * 初始化
     *
     * @param netProperties 网络连接配置
     */
    void initial(NetProperties netProperties);

    /**
     * 启动
     *
     * @param resolver 解释器
     * @throws Throwable Throwable
     */
    void start(HandlerResolver resolver) throws Throwable;

    /**
     * 停止
     *
     * @param handler 动作
     * @throws Throwable Throwable
     */
    void stop(Handler<T> handler) throws Throwable;

    /**
     * 停止
     *
     * @param throwableHandler 动作
     * @throws Throwable Throwable
     */
    void exception(ThrowableHandler throwableHandler) throws Throwable;
}
