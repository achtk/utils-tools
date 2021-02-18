package com.chua.utils.tools.function.producer;

import com.chua.utils.tools.properties.NetProperties;

/**
 * NetxProperties提供者
 *
 * @author CH
 * @date 2020-10-07
 */
public interface NetxPropertiesProducer {
    /**
     * 初始化NetxProperties
     *
     * @param netProperties NetxProperties
     */
    void initial(NetProperties netProperties);
}
