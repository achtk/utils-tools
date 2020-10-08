package com.chua.utils.netx.centor;

import com.chua.utils.netx.entity.CenterConfig;
import com.chua.utils.tools.function.producer.IEnvironmentProducer;
import com.chua.utils.tools.function.producer.NetxPropertiesProducer;
import com.chua.utils.tools.function.producer.SimpleServerProducer;
import com.chua.utils.tools.properties.NetxProperties;

import java.util.Map;
import java.util.Properties;

/**
 * 配置中台
 *
 * @author CH
 * @date 2020-10-07
 */
public interface ConfigurationCenter extends IEnvironmentProducer, NetxPropertiesProducer, SimpleServerProducer {
	/**
	 * 注册配置
	 *
	 * @param centerConfig 配置
	 * @exception Exception
	 */
	void register(CenterConfig centerConfig) throws Exception;
	/**
	 * 更新配置
	 *
	 * @param centerConfig 配置
	 * @exception Exception
	 */
	void update(CenterConfig centerConfig) throws Exception;

	/**
	 * 删除索引
	 * @param name
	 */
	void remove(String name);
	/**
	 * 获取数据
	 *
	 * @param key 索引
	 * @return
	 */
	Map<String, Object> getValue(final String key);
	/**
	 * 获取数据
	 *
	 * @param key 索引
	 * @return
	 */
	Properties get(final String key);

	/**
	 * 获取数据
	 *
	 * @param key  索引
	 * @param name 名称
	 * @return
	 */
	Object getValue(String name, String key);

	/**
	 * 获取数据(有且只有一个数据)
	 *
	 * @param key 索引
	 * @return
	 */
	Object getIfOnly(final String key);
}
