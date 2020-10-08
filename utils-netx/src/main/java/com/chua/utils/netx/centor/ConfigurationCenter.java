package com.chua.utils.netx.centor;

import com.chua.utils.netx.entity.CenterConfig;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.function.producer.IEnvironmentProducer;
import com.chua.utils.tools.function.producer.NetxPropertiesProducer;
import com.chua.utils.tools.function.producer.SimpleServerProducer;

import java.util.ArrayList;
import java.util.List;
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
	 * @throws Exception
	 */
	void register(CenterConfig centerConfig) throws Exception;

	/**
	 * 更新配置
	 *
	 * @param centerConfig 配置
	 * @throws Exception
	 */
	void update(CenterConfig centerConfig) throws Exception;

	/**
	 * 删除索引
	 *
	 * @param name CenterConfig的名称
	 */
	void remove(String name);

	/**
	 * 获取数据
	 *
	 * @param key Properties数据索引
	 * @return
	 */
	List<CenterConfig> getCenterConfigs(final String key);
	/**
	 * 获取数据
	 *
	 * @param name CenterConfig的名称
	 * @return
	 */
	CenterConfig getConfig(String name);

	/**
	 * 获取数据
	 *
	 * @param name CenterConfig的名称
	 * @return
	 */
	default Properties getProperty(final String name) {
		CenterConfig centerConfig = getConfig(name);
		return centerConfig.getProperties();
	}

	/**
	 * 获取数据
	 *
	 * @param key  索引
	 * @param name 名称
	 * @return
	 */
	default Object getCenterConfigs(String name, String key) {
		CenterConfig centerConfig = getConfig(name);
		if(null == centerConfig) {
			return null;
		}
		return centerConfig.getValue(key);
	}

	/**
	 * 获取数据(有且只有一个数据)
	 *
	 * @param key 索引
	 * @return
	 */
	default Object getIfOnly(final String key) {
		List<CenterConfig> centerConfigs = getCenterConfigs(key);
		return BooleanHelper.hasLength(centerConfigs) ? FinderHelper.firstElement(centerConfigs).getValue(key): null;
	}
	/**
	 * 获取数据(有且只有一个数据)
	 *
	 * @param key 索引
	 * @return
	 */
	default List<Object> getValues(final String key) {
		List<CenterConfig> centerConfigs = getCenterConfigs(key);
		List<Object> result = new ArrayList<>(centerConfigs.size());
		for (CenterConfig centerConfig : centerConfigs) {
			if(centerConfig.isContainer(key)) {
				continue;
			}
			result.add(centerConfig.getValue(key));
		}

		return result;
	}
}
