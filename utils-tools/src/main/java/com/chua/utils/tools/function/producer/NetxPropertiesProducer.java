package com.chua.utils.tools.function.producer;

import com.chua.utils.tools.properties.NetxProperties;

/**
 * NetxProperties提供者
 * @author CH
 * @date 2020-10-07
 */
public interface NetxPropertiesProducer {
	/**
	 * 初始化NetxProperties
	 * @param netxProperties NetxProperties
	 */
	void initial(NetxProperties netxProperties);
}
