package com.chua.utils.netx.vertx.result;

import com.chua.utils.netx.vertx.util.NetSocketUtil;
import com.chua.utils.tools.properties.NetProperties;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.net.NetServer;
import lombok.AllArgsConstructor;

/**
 * @author CH
 * @date 2020-10-07
 */
@AllArgsConstructor
public class AsyncServerResultHandler implements Handler<AsyncResult<NetServer>> {

	private NetProperties netProperties;

	@Override
	public void handle(AsyncResult<NetServer> event) {
		NetSocketUtil.handlerAsyncResult(event, netProperties);
	}
}
