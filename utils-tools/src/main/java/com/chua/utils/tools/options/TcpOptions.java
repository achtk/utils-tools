package com.chua.utils.tools.options;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * tcp配置
 * @author CH
 * @date 2020-10-07
 */
@Getter
@Setter
public class TcpOptions {

	/**
	 * The default value of TCP send buffer size
	 */
	public static final int DEFAULT_SEND_BUFFER_SIZE = -1;

	/**
	 * The default value of TCP receive buffer size
	 */
	public static final int DEFAULT_RECEIVE_BUFFER_SIZE = -1;

	/**
	 * The default value of traffic class
	 */
	public static final int DEFAULT_TRAFFIC_CLASS = -1;

	/**
	 * The default value of reuse address
	 */
	public static final boolean DEFAULT_REUSE_ADDRESS = true;

	/**
	 * The default value of reuse port
	 */
	public static final boolean DEFAULT_REUSE_PORT = false;

	/**
	 * The default log enabled = false
	 */
	public static final boolean DEFAULT_LOG_ENABLED = false;

	/**
	 * The default port for proxy connect = 3128
	 *
	 * 3128 is the default port for e.g. Squid
	 */
	public static final int DEFAULT_PORT = 3128;

	/**
	 * The default hostname for proxy connect = "localhost"
	 */
	public static final String DEFAULT_HOST = "localhost";

	/**
	 * The default value for reconnect attempts = 0
	 */
	public static final int DEFAULT_RECONNECT_ATTEMPTS = 0;

	/**
	 * The default value for reconnect interval = 1000 ms
	 */
	public static final long DEFAULT_RECONNECT_INTERVAL = 1000;

	/**
	 * Default value to determine hostname verification algorithm hostname verification (for SSL/TLS) = ""
	 */
	public static final String DEFAULT_HOSTNAME_VERIFICATION_ALGORITHM = "";

	/**
	 * The default value of TCP-no-delay = true (Nagle disabled)
	 */
	public static final boolean DEFAULT_TCP_NO_DELAY = true;

	/**
	 * The default value of TCP keep alive = false
	 */
	public static final boolean DEFAULT_TCP_KEEP_ALIVE = false;

	/**
	 * Default idle timeout = 0
	 */
	public static final int DEFAULT_IDLE_TIMEOUT = 0;

	/**
	 * Default idle time unit = SECONDS
	 */
	public static final TimeUnit DEFAULT_IDLE_TIMEOUT_TIME_UNIT = TimeUnit.SECONDS;

	private int sendBufferSize;
	private int receiveBufferSize;
	private int trafficClass;
	private boolean reuseAddress;
	private boolean logActivity;
	private boolean reusePort;

	private boolean tcpKeepAlive;

	private int connectTimeout;
	private boolean trustAll;
	private String metricsName;
	private String localAddress;

	private String host;
	private int port;
	private String username;
	private String password;

	private SSLOptions sslOptions;
	private boolean tcpNoDelay;
	private int idleTimeout;
	private TimeUnit idleTimeoutUnit;

	private int reconnectAttempts = DEFAULT_RECONNECT_ATTEMPTS;
	private long reconnectInterval = DEFAULT_RECONNECT_INTERVAL;
	private String hostnameVerificationAlgorithm = DEFAULT_HOSTNAME_VERIFICATION_ALGORITHM;

	/**
	 * Default constructor
	 */
	public TcpOptions() {
		sendBufferSize = DEFAULT_SEND_BUFFER_SIZE;
		receiveBufferSize = DEFAULT_RECEIVE_BUFFER_SIZE;
		reuseAddress = DEFAULT_REUSE_ADDRESS;
		trafficClass = DEFAULT_TRAFFIC_CLASS;
		logActivity = DEFAULT_LOG_ENABLED;
		reusePort = DEFAULT_REUSE_PORT;

		tcpKeepAlive = DEFAULT_TCP_KEEP_ALIVE;
		tcpNoDelay = DEFAULT_TCP_NO_DELAY;
		idleTimeout = DEFAULT_IDLE_TIMEOUT;
		idleTimeoutUnit = DEFAULT_IDLE_TIMEOUT_TIME_UNIT;

		host = DEFAULT_HOST;
		port = DEFAULT_PORT;
	}
}
