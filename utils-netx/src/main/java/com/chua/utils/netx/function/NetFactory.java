package com.chua.utils.netx.function;

import com.chua.utils.netx.kit.Kit;
import com.chua.utils.tools.properties.NetProperties;

/**
 * net factory tools
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public interface NetFactory<Client> {
    /**
     * 基础配置
     *
     * @return 配置
     */
    NetProperties properties();

    /**
     * 获取客户端
     *
     * @return
     */
    Client client();

    /**
     * 创建工具箱
     *
     * @param kitClass 工具
     * @return Kit
     * @see com.chua.utils.netx.kit.Kit
     */
    Kit create(Class<? extends Kit> kitClass);
}
