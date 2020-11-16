package com.chua.utils.tools.util;

import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.empty.EmptyOrBase;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_AT;

/**
 * 系统信息
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class SystemUtil {

    /**
     * 获取 pid
     *
     * @return pid
     */
    public static String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split(SYMBOL_AT)[0];
    }

    /**
     * 是window系统
     *
     * @return 是window系统返回true
     */
    public static boolean isWindow() {
        return EmptyOrBase.OS_NAME.toLowerCase().contains(StringConstant.WINDOW);
    }

    /**
     * 获取本机地址
     *
     * @return 本机地址
     */
    public static String getLocalAddress() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /**
     * 获取Tomcat端口号
     *
     * @return Tomcat端口号
     */
    public static String getTomcatPort() {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = null;
        try {
            objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        } catch (MalformedObjectNameException e) {
            return null;
        }
        return objectNames.iterator().next().getKeyProperty("port");
    }
}
