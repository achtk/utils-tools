package com.chua.utils.netx.vertx.tcp;

import com.chua.utils.netx.centor.ServerConfigCenter;
import com.chua.utils.netx.vertx.result.AsyncServerResultHandler;
import com.chua.utils.netx.vertx.result.ResultHandler;
import com.chua.utils.netx.vertx.util.NetSocketUtil;
import com.chua.utils.tools.common.BeansHelper;
import com.chua.utils.tools.handler.Handler;
import com.chua.utils.tools.handler.HandlerResolver;
import com.chua.utils.tools.handler.ThrowableHandler;
import com.chua.utils.tools.properties.NetxProperties;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * vertx-server-tcp实现
 * @author CH
 * @date 2020-10-07
 */
@Slf4j
@RequiredArgsConstructor
public class VertxNetServerConfigurationCenter extends AbstractVerticle implements ServerConfigCenter<NetSocket> {

	@NonNull
	private NetxProperties netxProperties;

	private NetServer netServer;

	private Vertx vertx;
	@Setter
	private volatile NetServerOptions netServerOptions;

	public VertxNetServerConfigurationCenter(@NonNull NetxProperties netxProperties, Vertx vertx) {
		this.netxProperties = netxProperties;
		this.vertx = vertx;
	}

	@Override
	public void initial(NetxProperties netxProperties) {
		this.netxProperties = netxProperties;
	}

	@Override
	public void start(HandlerResolver resolver) throws Throwable {
		this.vertx = Vertx.vertx(NetSocketUtil.newVertxOption(netxProperties));
		this.vertx.deployVerticle(this);

		this.netServerOptions = NetSocketUtil.newHttpServerOption(netxProperties);
		this.netServer = vertx.createNetServer(netServerOptions);
		this.netServer.connectHandler(new ResultHandler(resolver));
		this.netServer.listen(netxProperties.getPort(), netxProperties.getHostifOnly(), new AsyncServerResultHandler(netxProperties));
	}


	@Override
	public void stop(Handler handler) throws Throwable {
		if(null == this.netServer) {
			return;
		}
		if(null == handler) {
			this.netServer.close();
			return;
		}
		this.netServer.close(new io.vertx.core.Handler<AsyncResult<Void>>() {
			@Override
			public void handle(AsyncResult<Void> event) {
				handler.handler(event);
			}
		});
		this.vertx.close();
	}

	@Override
	public void exception(ThrowableHandler throwableHandler) throws Throwable {
		if(null == throwableHandler) {
			return;
		}
		this.netServer.exceptionHandler(new io.vertx.core.Handler<Throwable>() {
			@Override
			public void handle(Throwable event) {
				throwableHandler.handler(event);
			}
		});
	}


}
