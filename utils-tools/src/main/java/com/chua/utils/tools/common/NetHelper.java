package com.chua.utils.tools.common;

import com.google.common.base.Strings;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 网络工具类
 *
 * @author CH
 */
public class NetHelper {

    /**
     * 最小端口
     */
    private static final int MIN_PORT = 0;
    /**
     * 最大端口
     */
    private static final int MAX_PORT = 65535;

    /**
     * 任意地址
     */
    public static final String ANYHOST = "0.0.0.0";
    /**
     * 本机地址正则
     */
    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

    /**
     * IPv4地址
     */
    public static final Pattern IPV4_PATTERN = Pattern
            .compile(
                    "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static String HTTP = "http://";
    private static String HTTPS = "https://";

    /**
     * 是否本地地址 127.x.x.x 或者 localhost
     *
     * @param host 地址
     * @return 是否本地地址
     */
    public static boolean isLocalHost(String host) {
        return StringHelper.isNotBlank(host) && (LOCAL_IP_PATTERN.matcher(host).matches() || "localhost".equalsIgnoreCase(host));
    }

    /**
     * 是否默认地址 0.0.0.0
     *
     * @param host 地址
     * @return 是否默认地址
     */
    public static boolean isAnyHost(String host) {
        return ANYHOST.equals(host);
    }

    /**
     * 是否IPv4地址 0.0.0.0
     *
     * @param host 地址
     * @return 是否默认地址
     */
    public static boolean isIPv4Host(String host) {
        return StringHelper.isNotBlank(host) && IPV4_PATTERN.matcher(host).matches();
    }

    /**
     * 是否非法地址（本地或默认）
     *
     * @param host 地址
     * @return 是否非法地址
     */
    static boolean isInvalidLocalHost(String host) {
        return StringHelper.isBlank(host) || isAnyHost(host) || isLocalHost(host);
    }

    /**
     * 是否合法地址（非本地，非默认的IPv4地址）
     *
     * @param address InetAddress
     * @return 是否合法
     */
    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String name = address.getHostAddress();
        return (name != null
                && !isAnyHost(name)
                && !isLocalHost(name)
                && isIPv4Host(name));
    }

    /**
     * 是否网卡上的地址
     *
     * @param host 地址
     * @return 是否默认地址
     */
    public static boolean isHostInNetworkCard(String host) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            return NetworkInterface.getByInetAddress(addr) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 得到本机IPv4地址
     *
     * @return ip地址
     */
    public static String getLocalIpv4() {
        InetAddress address = getLocalAddress();
        return address == null ? null : address.getHostAddress();
    }

    /**
     * 遍历本地网卡，返回第一个合理的IP，保存到缓存中
     *
     * @return 本地网卡IP
     */
    public static InetAddress getLocalAddress() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        while (addresses.hasMoreElements()) {
                            try {
                                InetAddress address = addresses.nextElement();
                                if (isValidAddress(address)) {
                                    return address;
                                }
                            } catch (Throwable e) {
                                continue;
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return localAddress;
    }

    /**
     * InetSocketAddress转 host:port 字符串
     *
     * @param address InetSocketAddress转
     * @return host:port 字符串
     */
    public static String toAddressString(final InetSocketAddress address) {
        if (address == null) {
            return "";
        } else {
            return toIpString(address) + ":" + address.getPort();
        }
    }

    /**
     * 得到ip地址
     *
     * @param address InetSocketAddress
     * @return ip地址
     */
    public static String toIpString(final InetSocketAddress address) {
        if (address == null) {
            return null;
        } else {
            InetAddress inetAddress = address.getAddress();
            return inetAddress == null ? address.getHostName() : inetAddress.getHostAddress();
        }
    }


    /**
     * 通过连接远程地址得到本机内网地址
     *
     * @param remoteAddress 远程地址
     * @return 本机内网地址
     */
    private static InetAddress getLocalHostBySocket(InetSocketAddress remoteAddress) {
        InetAddress host = null;
        try {
            // 去连一下远程地址
            Socket socket = new Socket();
            try {
                socket.connect(remoteAddress, 1000);
                // 得到本地地址
                host = socket.getLocalAddress();
            } finally {
                IOHelper.closeQuietly(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return host;
    }

    /**
     * 判断端口是否有效 0-65535
     *
     * @param port 端口
     * @return 是否有效
     */
    public static boolean isInvalidPort(int port) {
        return port > MAX_PORT || port < MIN_PORT;
    }

    /**
     * 检查当前指定端口是否可用，不可用则自动+1再试（随机端口从默认端口开始检查）
     *
     * @param host    当前ip地址
     * @param port    当前指定端口
     * @param maxPort 最大端口
     * @return 从指定端口开始后第一个可用的端口
     */
    public static int getAvailablePort(String host, int port, int maxPort) {
        if (isAnyHost(host) || isLocalHost(host) || isHostInNetworkCard(host)) {
            if (port < MIN_PORT) {
                port = MIN_PORT;
            }
            for (int i = port; i <= maxPort; i++) {
                ServerSocket ss = null;
                try {
                    ss = new ServerSocket();
                    ss.bind(new InetSocketAddress(host, i));
                    return i;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOHelper.closeQuietly(ss);
                }
            }
        }
        return -1;
    }

    /**
     * 连接转字符串
     *
     * @param local  本地地址
     * @param remote 远程地址
     * @return 地址信息字符串
     */
    public static String connectToString(InetSocketAddress local, InetSocketAddress remote) {
        return toAddressString(local) + " <-> " + toAddressString(remote);
    }

    /**
     * 获取IP
     * <p>
     *     127.0.0.1:8080 -> 127.0.0.1
     * </p>
     * @param host
     * @return
     */
    public static String getHost(String host) {
        if(Strings.isNullOrEmpty(host)) {
            return null;
        }

        int index = host.indexOf("://");
        if(index > -1) {
            host = host.substring(index + 3);
        }

        if(host.indexOf(":") == -1) {
            return host;
        }
        String[] strings = host.split(":");
        return strings[0];
    }
    /**
     * 获取端口
     * <p>
     *     127.0.0.1:8080 -> 8080
     * </p>
     * @param host
     * @return
     */
    public static int getPort(String host) {
        if(Strings.isNullOrEmpty(host)) {
            return -1;
        }

        int index = host.indexOf("://");
        if(index > -1) {
            host = host.substring(index + 3);
        }

        if(host.indexOf(":") == -1) {
            return -1;
        }
        String[] strings = host.split(":");
        return NumberHelper.toInt(strings[1]);
    }

    /**
     * 去除重复 /
     * @param uri uri
     * @return
     */
    public static String removeDuplicateSymbols(String uri) {
        if(null == uri) {
            return "";
        }
        String newUri = uri.replace("\\", "/").replaceAll("//", "/");
        if(newUri.startsWith(HTTPS)) {
            return newUri;
        }
        return newUri.startsWith(HTTP) ? newUri : HTTP + newUri;
    }

    /**
     * 获取protocol
     * @param address 地址
     * @return
     */
	public static String getProtocol(String address) {
	    if(Strings.isNullOrEmpty(address)) {
	        return "";
        }
        int index = address.indexOf("://");
	    return index == -1 ? "" : address.substring(0, index);
    }
}
