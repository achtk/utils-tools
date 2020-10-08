package com.chua.utils.tools.options;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ssl配置
 * @author CH
 * @date 2020-10-07
 */
@Getter
@Setter
public class SSLOptions {

	/**
	 * The default value of SO_linger = -1
	 */
	public static final int DEFAULT_SO_LINGER = -1;

	/**
	 * The default value of Netty use pooled buffers = false
	 */
	public static final boolean DEFAULT_USE_POOLED_BUFFERS = false;

	/**
	 * SSL enable by default = false
	 */
	public static final boolean DEFAULT_SSL = false;

	/**
	 * Default use alpn = false
	 */
	public static final boolean DEFAULT_USE_ALPN = false;

	/**
	 * The default ENABLED_SECURE_TRANSPORT_PROTOCOLS value = { "SSLv2Hello", "TLSv1", "TLSv1.1", "TLSv1.2" }
	 * <p/>
	 * SSLv3 is NOT enabled due to POODLE vulnerability http://en.wikipedia.org/wiki/POODLE
	 * <p/>
	 * "SSLv2Hello" is NOT enabled since it's disabled by default since JDK7
	 */
	public static final List<String> DEFAULT_ENABLED_SECURE_TRANSPORT_PROTOCOLS = Collections.unmodifiableList(Arrays.asList("TLSv1", "TLSv1.1", "TLSv1.2"));

	/**
	 * The default TCP_FASTOPEN value = false
	 */
	public static final boolean DEFAULT_TCP_FAST_OPEN = false;

	/**
	 * The default TCP_CORK value = false
	 */
	public static final boolean DEFAULT_TCP_CORK = false;

	/**
	 * The default TCP_QUICKACK value = false
	 */
	public static final boolean DEFAULT_TCP_QUICKACK = false;

	/**
	 * The default value of SSL handshake timeout = 10
	 */
	public static final long DEFAULT_SSL_HANDSHAKE_TIMEOUT = 10L;

	/**
	 * Default SSL handshake time unit = SECONDS
	 */
	public static final TimeUnit DEFAULT_SSL_HANDSHAKE_TIMEOUT_TIME_UNIT = TimeUnit.SECONDS;

	private int soLinger;
	private boolean usePooledBuffers;
	private boolean ssl;
	private long sslHandshakeTimeout;
	private TimeUnit sslHandshakeTimeoutUnit;
	private Set<String> enabledCipherSuites;
	private ArrayList<String> crlPaths;
	private boolean useAlpn;
	private Set<String> enabledSecureTransportProtocols;
	private boolean tcpFastOpen;
	private boolean tcpCork;
	private boolean tcpQuickAck;


	public SSLOptions() {
		soLinger = DEFAULT_SO_LINGER;
		usePooledBuffers = DEFAULT_USE_POOLED_BUFFERS;
		ssl = DEFAULT_SSL;
		sslHandshakeTimeout = DEFAULT_SSL_HANDSHAKE_TIMEOUT;
		sslHandshakeTimeoutUnit = DEFAULT_SSL_HANDSHAKE_TIMEOUT_TIME_UNIT;
		enabledCipherSuites = new LinkedHashSet<>();
		crlPaths = new ArrayList<>();
		useAlpn = DEFAULT_USE_ALPN;
		enabledSecureTransportProtocols = new LinkedHashSet<>(DEFAULT_ENABLED_SECURE_TRANSPORT_PROTOCOLS);
		tcpFastOpen = DEFAULT_TCP_FAST_OPEN;
		tcpCork = DEFAULT_TCP_CORK;
		tcpQuickAck = DEFAULT_TCP_QUICKACK;
	}
}
