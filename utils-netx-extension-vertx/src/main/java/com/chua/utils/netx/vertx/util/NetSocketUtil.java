package com.chua.utils.netx.vertx.util;

import com.chua.utils.tools.common.BeansHelper;
import com.chua.utils.tools.common.codec.binary.Hex;
import com.chua.utils.tools.enums.HandlerType;
import com.chua.utils.tools.handler.DataWriteHandler;
import com.chua.utils.tools.handler.Handler;
import com.chua.utils.tools.handler.HandlerResolver;
import com.chua.utils.tools.handler.ThrowableHandler;
import com.chua.utils.tools.loader.IIteratorLoader;
import com.chua.utils.tools.loader.IteratorLoader;
import com.chua.utils.tools.properties.NetProperties;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import io.vertx.core.AsyncResult;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * NetSocket工具类
 * @author CH
 * @date 2020-10-07
 */
@Slf4j
public class NetSocketUtil {
	/**
	 * 处理动作
	 * @param netSocket netSocket
	 * @param handlerResolver 动作
	 */
	public static void handler(NetSocket netSocket, HandlerResolver handlerResolver, boolean isServer) {
		SocketAddress socketAddress = netSocket.localAddress();
		if(handlerResolver.isEmpty()) {
			log.warn("当前没有处理方法");
			return;
		}
		if(log.isDebugEnabled() && isServer) {
			log.debug("获取到一个连接> 地址{}, 端口: {}", socketAddress.host(), socketAddress.port());
		}
		//监听连接
		Handler connectHandler = getHandler(handlerResolver, HandlerType.CONNECT);
		connectHandler.handler(socketAddress);

		//读取数据
		Handler readerHandler = getHandler(handlerResolver, HandlerType.READER);
		netSocket.handler(new ServerDataReaderHandler(readerHandler));

		//写入数据
		Handler writerHandler = getHandler(handlerResolver, HandlerType.WRITER);
		listenerWriterData((DataWriteHandler) readerHandler, netSocket);

		//任意数据
		Handler anyHandler = getHandler(handlerResolver, HandlerType.ANY);
		readerHandler.handler(anyHandler);

		//任意数据
		Handler endHandler = getHandler(handlerResolver, HandlerType.FINAL);
		netSocket.endHandler(new ServerEndHandler(endHandler));

		//关闭连接
		Handler closeHandler = getHandler(handlerResolver, HandlerType.CLOSE);
		netSocket.closeHandler(new ServerCloseHander(closeHandler));

		//异常
		Handler throwableHandler = getHandler(handlerResolver, HandlerType.EXCEPTION);
		if(throwableHandler instanceof ThrowableHandler) {
			netSocket.exceptionHandler(new ServerExceptionHandler((ThrowableHandler) throwableHandler));
		}
		if(log.isDebugEnabled()) {
			log.debug("连接处理完毕> 地址{}, 端口: {}", socketAddress.host(), socketAddress.port());
		}
	}

	/**
	 * 获取所有版本type的handler
	 * @param handlerResolver
	 * @param handlerType
	 * @return
	 */
	public static Handler getHandler(HandlerResolver handlerResolver, HandlerType handlerType) {
		Set<Handler> handlers = handlerResolver.handlers(handlerType);
		IIteratorLoader<Handler> iteratorLoader = new IteratorLoader<>();
		iteratorLoader.addItems(handlers);
		return iteratorLoader.newLoader();
	}

	/**
	 * 监听到代写数据
	 * @param handler
	 * @param netSocket
	 */
	private static void listenerWriterData(DataWriteHandler handler, NetSocket netSocket) {
		ServerDataWriterHandler writerHandler = new ServerDataWriterHandler((DataWriteHandler) handler);
		Buffer buffer = writerHandler.dataWriteHandler.getData();
		if(null == buffer) {
			return;
		}
		netSocket.write(buffer, new io.vertx.core.Handler<AsyncResult<Void>>() {
			@Override
			public void handle(AsyncResult<Void> event) {
				String s = Hex.encodeHexString(buffer.getBytes());
				if(event.succeeded()) {
					log.info("写入数据成功, 数据: {}", s);
					return;
				}
				log.error("数据{}, 写入失败", s);
			}
		});
	}

	/**
	 * 处理结果
	 * @param event
	 * @param handlerResolver
	 */
	public static void handlerAsyncResult(AsyncResult<NetSocket> event, HandlerResolver handlerResolver) {
		if(event.succeeded()) {
			NetSocket netSocket = event.result();
			SocketAddress socketAddress = netSocket.remoteAddress();
			log.info("连接成功, 当前绑定IP<{}>, 端口:<{}>", socketAddress.host(), socketAddress.port());
			NetSocketUtil.handler(netSocket, handlerResolver, false);
			return;
		}
		log.error("启动失败, 导致原因: {}", event.cause().getMessage());
		Handler handler = getHandler(handlerResolver, HandlerType.EXCEPTION);
		if(handler instanceof ThrowableHandler) {
			handler.handler(event.cause());
		}
	}
	/**
	 * 处理结果
	 * @param netSocket
	 * @param handlerResolver
	 */
	public static void handlerResult(NetSocket netSocket, HandlerResolver handlerResolver) {
		NetSocketUtil.handler(netSocket, handlerResolver, true);
	}
	/**
	 * 处理结果
	 * @param event
	 * @param netProperties
	 */
	public static void handlerAsyncResult(AsyncResult<NetServer> event, NetProperties netProperties) {
		if(event.succeeded()) {
			log.info("启动成功, 当前绑定IP<{}>, 端口:<{}>", netProperties.getHostIfOnly(), netProperties.getPort());
			return;
		}
		log.error("启动失败, 导致原因: {}", event.cause().getMessage());
	}

	/**
	 * 获取 VertxOptions
	 * @param netProperties
	 * @return
	 */
	public static VertxOptions newVertxOption(NetProperties netProperties) {
		VertxOptions vertxOptions = new VertxOptions();
		return vertxOptions;
	}

	/**
	 * 创建 BridgeOptions
	 * @param netProperties
	 * @return
	 */
	public static BridgeOptions newBridgeOption(NetProperties netProperties) {
		BridgeOptions bridgeOptions = new BridgeOptions();
		BeansHelper.copier(netProperties, bridgeOptions);
		//设置从客户端发送消息到服务端的地址，根据自己的需要可以创建任意个
		String inbound = netProperties.getProperty("inbound");
		if(!Strings.isNullOrEmpty(inbound)) {
			List<String> strings = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(inbound);
			for (String string : strings) {
				bridgeOptions.addInboundPermitted(new PermittedOptions().setAddress(string));
			}
		}
		//设置从服务端发送消息到客户端端的地址，根据自己的需要可以创建任意个
		String outbound = netProperties.getProperty("outbound");
		if(!Strings.isNullOrEmpty(outbound)) {
			List<String> strings = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(outbound);
			for (String string : strings) {
				bridgeOptions.addOutboundPermitted(new PermittedOptions().setAddress(string));
			}
		}
		return bridgeOptions;
	}

	/**
	 * 创建 SockJSHandlerOptions
	 * @param netProperties
	 * @return
	 */
	public static SockJSHandlerOptions newSockJSHandlerOption(NetProperties netProperties) {
		SockJSHandlerOptions sockJSHandlerOptions = new SockJSHandlerOptions();
		BeansHelper.copier(netProperties, sockJSHandlerOptions);
		return sockJSHandlerOptions;
	}

	/**
	 * 创建 HttpServerOptions
	 * @param netProperties
	 * @return
	 */
	public static HttpServerOptions newHttpServerOption(NetProperties netProperties) {
		HttpServerOptions httpServerOptions = new HttpServerOptions();
		BeansHelper.copier(netProperties, httpServerOptions);
		return httpServerOptions;
	}

	/**
	 * 创建 DeliveryOptions
	 * @param netProperties
	 * @return
	 */
	public static DeliveryOptions newDeliveryOption(NetProperties netProperties) {
		DeliveryOptions deliveryOptions = new DeliveryOptions();
		BeansHelper.copier(netProperties, deliveryOptions);
		return deliveryOptions;
	}

	/**
	 * 创建 NetClientOptions
	 * @return
	 * @param netProperties
	 */
	public static NetClientOptions newNetClientOption(@NonNull NetProperties netProperties) {
		NetClientOptions netClientOptions = new NetClientOptions();
		BeansHelper.copier(netProperties, netClientOptions);
		return netClientOptions;
	}

	/**
	 * 关闭处理
	 */
	@AllArgsConstructor
	private static class ServerCloseHander implements io.vertx.core.Handler<Void> {

		private Handler closeHandler;

		@Override
		public void handle(Void event) {
			closeHandler.handler(null);
		}
	}
	/**
	 * 连接处理
	 */
	@AllArgsConstructor
	private static class ServerExceptionHandler implements io.vertx.core.Handler<Throwable> {

		private ThrowableHandler listenerHandler;

		@Override
		public void handle(Throwable throwable) {
			listenerHandler.handler(throwable);
		}
	}

	@AllArgsConstructor
	private static class ServerEndHandler implements io.vertx.core.Handler<Void> {

		private Handler endHandler;

		@Override
		public void handle(Void voids) {
			endHandler.handler(voids);
		}
	}

	@AllArgsConstructor
	protected static class ServerDataReaderHandler implements io.vertx.core.Handler<Buffer> {

		private Handler handler;

		@Override
		public void handle(Buffer buffer) {
			handler.handler(buffer);
		}
	}
	@AllArgsConstructor
	protected static class ServerDataWriterHandler implements io.vertx.core.Handler<Buffer> {

		private DataWriteHandler<Buffer, Buffer> dataWriteHandler;

		@Override
		public void handle(Buffer buffer) {
			dataWriteHandler.handler(buffer);
		}
	}
}
