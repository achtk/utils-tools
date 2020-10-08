package com.chua.utils.netx.centor;

import com.chua.utils.tools.handler.Handler;
import com.chua.utils.tools.handler.ThrowableHandler;
import com.chua.utils.tools.properties.NetxProperties;

import java.util.List;

/**
 * 服务配置中心接口
 * @author CH
 * @date 2020-10-07
 */
public interface ClientConfigCenter extends AutoCloseable {
	/**
	 * 初始化
	 * @param netxProperties 网络连接配置
	 */
	void initial(NetxProperties netxProperties);

	/**
	 * 连接
	 * @throws Throwable
	 * @param handlers 动作
	 */
	void connect(List<Handler> handlers) throws Throwable;

	/**
	 * 关闭
	 * @throws Throwable
	 */
	@Override
	void close() throws Exception;
}
