package com.chua.utils.netx.centor;

import com.chua.utils.tools.handler.HandlerResolver;
import com.chua.utils.tools.properties.NetProperties;

/**
 * 服务配置中心接口
 * @author CH
 * @date 2020-10-07
 */
public interface ClientConfigCenter extends AutoCloseable {
	/**
	 * 初始化
	 * @param netProperties 网络连接配置
	 */
	void initial(NetProperties netProperties);

	/**
	 * 连接
	 * @throws Throwable
	 * @param handlerResolver 动作
	 */
	void connect(HandlerResolver handlerResolver) throws Throwable;

	/**
	 * 关闭
	 * @throws Exception Exception
	 */
	@Override
	void close() throws Exception;
}
