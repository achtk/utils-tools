package com.chua.utils.netx.centor.configuration;

import com.chua.utils.netx.centor.ConfigurationCenter;
import com.chua.utils.netx.entity.CenterConfig;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.cache.ICacheProvider;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.properties.NetxProperties;
import com.google.common.base.Strings;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * 默认数据中台
 * @author CH
 * @date 2020-10-07
 */
public class DefaultConfigurationCenter implements ConfigurationCenter {

	protected final ICacheProvider<String, CenterConfig> centerConfigCache = new ConcurrentCacheProvider<>();

	@Override
	public synchronized void register(CenterConfig centerConfig) {
		if(null == centerConfig) {
			String name = centerConfig.getName();
			String newName = StringHelper.defaultIfBlank(name, StringHelper.uuid());
			centerConfigCache.put(newName, centerConfig);
		}
	}

	@Override
	public void update(CenterConfig centerConfig) throws Exception {
		register(centerConfig);
	}

	@Override
	public void remove(String name) {
		if(Strings.isNullOrEmpty(name)) {
			return;
		}
		centerConfigCache.remove(name);
	}


	@Override
	public List<CenterConfig> getCenterConfigs(String key) {
		if(null == key) {
			return Collections.emptyList();
		}
		List<CenterConfig> result = new ArrayList<>();
		ConcurrentMap<String, CenterConfig> asMap = centerConfigCache.asMap();
		for (Map.Entry<String, CenterConfig> entry : asMap.entrySet()) {
			result.add(entry.getValue());
		}
		return result;
	}

	@Override
	public CenterConfig getConfig(String name) {
		return centerConfigCache.get(name);
	}

	@Override
	public Properties getEnvironment() {
		ConcurrentMap<String, CenterConfig> concurrentMap = centerConfigCache.asMap();
		return MapHelper.toProperties(concurrentMap);
	}

	@Override
	public void setEnvironment(Properties properties) {

	}

	@Override
	public void initial(NetxProperties netxProperties) {

	}

	@Override
	public void start() throws Exception {

	}

	@Override
	public void close() throws Exception {

	}
}
