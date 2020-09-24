package com.chua.utils.netx.rpc.config;

import lombok.Getter;
import lombok.Setter;

/**
 * rpc应用配置
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 15:42
 */
@Getter
@Setter
public class RpcApplicationConfig {

    /**
     * The App name.
     */
    protected String appName = "default-app";

    /**
     * The App id.
     */
    protected String appId;

    /**
     * The Ins id.
     */
    protected String insId;
    /**
     * qos
     */
    protected boolean qosEnable;
    /**
     * qos-port
     */
    protected int qosPort;
    /**
     * qos-accept-foreign-ip
     */
    protected boolean qosAcceptForeignIp;
}
