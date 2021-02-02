package com.chua.utils.netx.rpc.config;

import com.chua.utils.netx.rpc.resolver.RpcConsumer;
import com.chua.utils.netx.rpc.resolver.RpcProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_COLON;

/**
 * 消费者服务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 13:56
 */
@Getter
@Setter
@Accessors(chain = true)
public class RpcCommonConfig<T> {

    /**
     * 直连调用地址
     */
    protected String directUrl = "localhost";

    /**
     * 获取端口
     * <p>
     * 127.0.0.1:8080 -> 8080
     * </p>
     *
     * @param rpcConsumer 消费者
     * @return 端口
     */
    public <T> int getPort(RpcConsumer<T> rpcConsumer) {
        String host = getDirectUrl();
        if (null == host) {
            return rpcConsumer.getDefaultPort();
        }

        int index = host.indexOf("://");
        if (index > -1) {
            host = host.substring(index + 3);
        }

        if (host.indexOf(SYMBOL_COLON) == -1) {
            return rpcConsumer.getDefaultPort();
        }
        String[] strings = host.split(SYMBOL_COLON);
        try {
            return Integer.valueOf(strings[1]);
        } catch (NumberFormatException e) {
            return rpcConsumer.getDefaultPort();
        }
    }

    /**
     * 获取端口
     * <p>
     * 127.0.0.1:8080 -> 8080
     * </p>
     *
     * @param tRpcProvider 生产者
     * @return 端口
     */
    public <T> int getPort(RpcProvider<T> tRpcProvider) {
        String host = getDirectUrl();
        if (null == host) {
            return tRpcProvider.getDefaultPort();
        }

        int index = host.indexOf("://");
        if (index > -1) {
            host = host.substring(index + 3);
        }

        if (host.indexOf(SYMBOL_COLON) == -1) {
            return tRpcProvider.getDefaultPort();
        }
        String[] strings = host.split(SYMBOL_COLON);
        try {
            return Integer.valueOf(strings[1]);
        } catch (NumberFormatException e) {
            return tRpcProvider.getDefaultPort();
        }
    }
}
