package com.chua.utils.netx.vertx.result;

import com.chua.utils.netx.vertx.util.NetSocketUtil;
import com.chua.utils.tools.handler.HandlerResolver;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author CH
 * @date 2020-10-07
 */
@AllArgsConstructor
public class ResultHandler implements Handler<NetSocket> {

	private HandlerResolver handlerResolver;

	@Override
	public void handle(NetSocket event) {
		NetSocketUtil.handlerResult(event, handlerResolver);
	}
}
