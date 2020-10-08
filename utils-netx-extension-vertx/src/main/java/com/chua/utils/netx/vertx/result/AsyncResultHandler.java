package com.chua.utils.netx.vertx.result;

import com.chua.utils.netx.vertx.util.NetSocketUtil;
import com.chua.utils.tools.handler.HandlerResolver;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;
import lombok.AllArgsConstructor;

/**
 * @author CH
 * @date 2020-10-07
 */
@AllArgsConstructor
public class AsyncResultHandler implements Handler<AsyncResult<NetSocket>> {

	private HandlerResolver handlerResolver;

	@Override
	public void handle(AsyncResult<NetSocket> event) {
		NetSocketUtil.handlerAsyncResult(event, handlerResolver);
	}
}
