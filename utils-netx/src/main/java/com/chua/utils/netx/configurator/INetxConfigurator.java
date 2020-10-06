package com.chua.utils.netx.configurator;

import com.chua.utils.tools.properties.NetxProperties;

/**
 * 配置配置
 * @author CH
 * @since 1.0
 */
public interface INetxConfigurator {
    /**
     * 启动项配置
     * @param netxProperties
     */
    public void initialConfiguration(NetxProperties netxProperties);
}
