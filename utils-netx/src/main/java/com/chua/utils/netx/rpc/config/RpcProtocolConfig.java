package com.chua.utils.netx.rpc.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 服务端协议配置
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 13:53
 */
@Getter
@Setter
@Accessors(chain = true)
public class RpcProtocolConfig {
    /**
     * 服务端注册协议
     */
    private String protocol;
    /**
     * 注册端口
     */
    private int port;
    /**
     * 主机
     */
    private String host;
    /**
     * 进程
     */
    private boolean daemon;


    public static RpcProtocolConfig newProtocol() {
        return new RpcProtocolConfig();
    }
}
