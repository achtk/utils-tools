package com.chua.utils.tools.util;

import com.chua.utils.tools.common.NetHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 网络工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/7
 */
public class NetUtils extends NetHelper {
    /**
     * 连通性检测
     *
     * @param host 地址
     * @return 连通返回true
     */
    public static boolean isConnectable(String host) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(getHost(host), getPort(host)));
        } catch (IOException e) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        return true;
    }

    /**
     * 连通性检测
     *
     * @param ip   地址
     * @param port 端口
     * @return 连通返回true
     */
    public static boolean isConnectable(String ip, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port));
        } catch (IOException e) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        return true;
    }
}
