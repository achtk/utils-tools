package com.chua.utils.netx.centor;

import com.chua.utils.tools.handler.Handler;
import com.chua.utils.tools.handler.ThrowableHandler;
import com.chua.utils.tools.properties.NetxProperties;

import java.io.IOException;
import java.util.List;

/**
 * 服务配置中心接口
 * @author CH
 * @date 2020-10-07
 */
public interface ServerConfigCenter<T> {
	/**
	 * 初始化
	 * @param netxProperties 网络连接配置
	 */
	void initial(NetxProperties netxProperties);

	/**
	 * 启动
	 * @throws Throwable
	 * @param handlers 处理
	 */
	void start(List<Handler> handlers) throws Throwable;

	/**
	 * 停止
	 * @throws Throwable
	 * @param handler 动作
	 */
	void stop(Handler<T> handler) throws Throwable;

	/**
	 * 停止
	 * @throws Throwable
	 * @param throwableHandler 动作
	 */
	void exception(ThrowableHandler throwableHandler) throws Throwable;
}
