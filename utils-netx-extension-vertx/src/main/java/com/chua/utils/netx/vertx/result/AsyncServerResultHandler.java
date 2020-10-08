package com.chua.utils.netx.vertx.result;

import com.chua.utils.netx.vertx.util.NetSocketUtil;
import com.chua.utils.tools.handler.HandlerResolver;
import com.chua.utils.tools.properties.NetxProperties;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author CH
 * @date 2020-10-07
 */
@AllArgsConstructor
public class AsyncServerResultHandler implements Handler<AsyncResult<NetServer>> {

	private NetxProperties netxProperties;

	@Override
	public void handle(AsyncResult<NetServer> event) {
		NetSocketUtil.handlerAsyncResult(event, netxProperties);
	}
}
