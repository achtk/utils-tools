package com.chua.utils.netx.vertx.tcp;

import com.chua.utils.netx.centor.ClientConfigCenter;
import com.chua.utils.netx.vertx.result.AsyncResultHandler;
import com.chua.utils.netx.vertx.util.NetSocketUtil;
import com.chua.utils.tools.handler.HandlerResolver;
import com.chua.utils.tools.properties.NetProperties;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * vertx-client-tcp实现
 *
 * @author CH
 * @date 2020-10-07
 */
@Slf4j
@RequiredArgsConstructor
public class VertxNetClientConfigurationCenter extends AbstractVerticle implements ClientConfigCenter {

	@NonNull
	private NetProperties netProperties;

	private Vertx vertx;
	private NetClient netClient;
	@Setter
	private volatile NetClientOptions netClientOptions;


	public VertxNetClientConfigurationCenter(@NonNull NetProperties netProperties, Vertx vertx) {
		this.netProperties = netProperties;
		this.vertx = vertx;
	}

	@Override
	public void initial(NetProperties netProperties) {
		this.netProperties = netProperties;
	}

	@Override
	public void connect(HandlerResolver handlerResolver) throws Throwable {
		this.vertx = Vertx.vertx(NetSocketUtil.newVertxOption(netProperties));
		this.vertx.deployVerticle(this);
		this.netClientOptions = NetSocketUtil.newNetClientOption(netProperties);

		this.netClient = vertx.createNetClient(netClientOptions);
		this.netClient.connect(netProperties.getPort(), netProperties.getHostIfOnly(), new AsyncResultHandler(handlerResolver));
	}

	@Override
	public void close() throws Exception {
		if (null == this.netClient) {
			return;
		}
		this.netClient.close();
		this.vertx.close();
	}

}
