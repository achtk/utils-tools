package com.chua.utils.netx.vertx.ws;

import com.chua.utils.netx.centor.ServerConfigCenter;
import com.chua.utils.netx.vertx.util.NetSocketUtil;
import com.chua.utils.tools.common.NetHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.enums.HandlerType;
import com.chua.utils.tools.handler.*;
import com.chua.utils.tools.properties.NetxProperties;
import com.google.common.base.Strings;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.ext.web.impl.RouterImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * vetx-ws-client
 * <p>
 * 以vertx创建一个websocket服务端，需要NetxProperties
 * </p>
 * <p>
 *
 * @author CH
 * @date 2020-10-08
 * @see com.chua.utils.tools.properties.NetxProperties
 * </p>
 * <p>传递<b>必要</b>参数，以及非必要参数</p>
 * <p>
 * 	<ul>
 * 	   <li>inbound: 设置从客户端发送消息到服务端的地址，根据自己的需要可以创建任意个多个以,隔开: 必填</li>
 * 	   <li>path: 设置路由路径，默认："/eventbus/*"</li>
 * 	   <li>maxWebSocketFrameSize: 设置数据量的大小</li>
 * 	   <li>port: 绑定的端口</li>
 * 	   <li>host: 绑定的地址, 支持127.0.0.1:8080, 设置该数据则当port为空取当前端口</li>
 * 	   <li>timeout: 设置数据推送出去的时间限制, 默认: 30000</li>
 * 	</ul>
 * </p>
 */
@Slf4j
public class VertxWsServerConfigurationCenter extends AbstractVerticle implements ServerConfigCenter<Object> {

	private static final String DEFAULT_ROUTE_PATH = "/eventbus/*";
	private NetxProperties netxProperties;
	private Vertx vertx;
	private BridgeOptions bridgeOptions;
	private SockJSHandlerOptions sockJSHandlerOptions;
	private HttpServerOptions httpServerOptions;
	private DeliveryOptions deliveryOptions;
	private int port;
	private String host;
	private HandlerResolver handlerResolver;
	private EventBus eventBus;

	@Override
	public void initial(NetxProperties netxProperties) {
		this.netxProperties = netxProperties;
		this.vertx = Vertx.vertx(NetSocketUtil.newVertxOption(netxProperties));
		this.bridgeOptions = NetSocketUtil.newBridgeOption(netxProperties);
		this.sockJSHandlerOptions = NetSocketUtil.newSockJSHandlerOption(netxProperties);
		this.httpServerOptions = NetSocketUtil.newHttpServerOption(netxProperties);
		this.deliveryOptions = NetSocketUtil.newDeliveryOption(netxProperties);
		String hostifOnly = netxProperties.getHostifOnly();
		int port1 = netxProperties.getPort();
		this.host = Strings.isNullOrEmpty(hostifOnly) ? null : NetHelper.getHost(hostifOnly);
		this.port = port1 < 0 ? NetHelper.getPort(hostifOnly) : port1;

		this.vertx.deployVerticle(this);
	}

	@Override
	public void stop(Handler handler) throws Throwable {

	}

	@Override
	public void exception(ThrowableHandler throwableHandler) throws Throwable {

	}

	@Override
	public void start(HandlerResolver handlerResolver) throws Throwable {
		this.handlerResolver = handlerResolver;
		//默认25秒，具体查看SockJSHandlerOptions类
		SockJSHandler sockJSHandler = SockJSHandler.create(vertx, this.sockJSHandlerOptions);
		//创建路由规则
		Router router = new RouterImpl(vertx);
		//解决跨域问题
		router.route().handler(CorsHandler.create("*")
				.allowedMethod(HttpMethod.GET)
				.allowedMethod(HttpMethod.OPTIONS)
				.allowedMethod(HttpMethod.POST)
				.allowedMethod(HttpMethod.PUT)
				.allowedHeader("X-PINGARUNER")
				.allowedHeader("Content-Type"));
		router.route().handler(BodyHandler.create().setBodyLimit(-1));

		router.route(StringHelper.defaultIfBlank(netxProperties.getPath(), DEFAULT_ROUTE_PATH))
				.handler(sockJSHandler);
		//创建一个eventbus，用来数据通讯
		this.eventBus = vertx.eventBus();
		HttpServer server = vertx.createHttpServer(this.httpServerOptions);

		HttpServer httpServer = server.requestHandler(router::accept);
		if (Strings.isNullOrEmpty(host)) {
			httpServer.listen(port, new ListenerHander());
		} else {
			httpServer.listen(port, host, new ListenerHander());
		}
		//接口绑定
		doBindForHandler();
		//周期性推送数据
		doBindPeriodicHandler();
	}

	/**
	 * 周期性推送数据
	 */
	private void doBindPeriodicHandler() {
		Set<Handler> handlers = handlerResolver.handlers(HandlerType.PERIODIC);
		if(null == handlers ) {
			return;
		}
		for (Handler handler : handlers) {
			PeriodicHandler periodicHandler = (PeriodicHandler) handler;
			vertx.setPeriodic(periodicHandler.delay(), new MessagePeriodicHandler(periodicHandler));
		}
	}

	/**
	 * 接口绑定
	 */
	private void doBindForHandler() {
		List<PermittedOptions> inboundPermitteds = this.bridgeOptions.getInboundPermitteds();
		for (PermittedOptions inboundPermitted : inboundPermitteds) {
			eventBus.consumer(inboundPermitted.getAddress(), new InboundMessageHandler());
		}
	}

	/**
	 * BridgeEventHander
	 *
	 * @see VertxWsServerConfigurationCenter#start(HandlerResolver) 
	 */
	private class BridgeEventHander implements io.vertx.core.Handler<BridgeEvent> {
		@Override
		public void handle(BridgeEvent bridgeEvent) {
			switch (bridgeEvent.type()) {
				case SOCKET_CREATED:
					System.out.println(new Date() + ":This event will occur when a new SockJS socket is created.");
					break;
				case SOCKET_IDLE:
					System.out.println(new Date() + ":This event will occur when SockJS socket is on idle for longer period of time than initially configured.");
					break;
				case SOCKET_PING:
					System.out.println(new Date() + ":This event will occur when the last ping timestamp is updated for the SockJS socket.");
					break;
				case SOCKET_CLOSED:
					System.out.println(new Date() + ":This event will occur when a SockJS socket is closed.");
					break;
				case SEND:
					System.out.println(new Date() + ":This event will occur when a message is attempted to be sent from the client to the server.");
					break;
				case PUBLISH:
					System.out.println(new Date() + ":This event will occur when a message is attempted to be published from the client to the server.");
					break;
				case RECEIVE:
					System.out.println(new Date() + ":This event will occur when a message is attempted to be delivered from the server to the client.");
					break;
				case REGISTER:
					System.out.println(new Date() + ":This event will occur when a client attempts to register a handler.");
					break;
				case UNREGISTER:
					System.out.println(new Date() + ":This event will occur when a client attempts to unregister a handler.");
					break;
				default:
					break;
			}
			//设置为true，可以处理任何在eventbus上的事件
			bridgeEvent.complete(true);
		}
	}

	private class ListenerHander implements io.vertx.core.Handler<AsyncResult<HttpServer>> {
		@Override
		public void handle(AsyncResult<HttpServer> event) {
			if (event.succeeded()) {
				log.info("服务开启成功！");
				return;
			}
			log.error("服务开启失败");
		}
	}

	/**
	 * 消息处理
	 */
	private class InboundMessageHandler implements io.vertx.core.Handler<Message<Object>> {
		@Override
		public void handle(Message<Object> event) {
			Handler handler = NetSocketUtil.getHandler(handlerResolver, HandlerType.MESSAGE);
			if(null == handler || handler instanceof MessageHandler) {
				return;
			}
			Object body = event.body();
			String address = event.address();
			MessageHandler messageHandler = (MessageHandler) handler;
			messageHandler.consumer(body);

			MessageHandler.Message message = messageHandler.publish();
			if(null == message) {
				return;
			}
			eventBus.publish(StringHelper.defaultIfBlank(message.getName(), address), message.getData(), deliveryOptions);
		}
	}

	/**
	 * 周期新任务
	 */
	@AllArgsConstructor
	private class MessagePeriodicHandler implements io.vertx.core.Handler<Long> {

		private PeriodicHandler periodicHandler;

		@Override
		public void handle(Long event) {
			MessageHandler.Message message = periodicHandler.publish();
			if(null == message) {
				return;
			}
			String name = message.getName();
			if(Strings.isNullOrEmpty(name)) {
				return;
			}
			eventBus.publish(name, message.getData(), deliveryOptions);
		}
	}
}
