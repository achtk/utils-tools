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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
	public Object getValue(String name, String key) {
		if(centerConfigCache.container(name)) {
			return null;
		}
		CenterConfig centerConfig = centerConfigCache.get(name);
		return centerConfig.getValue(key);
	}

	@Override
	public Map<String, Object> getValue(String key) {
		if(null == key) {
			return Collections.emptyMap();
		}
		Map<String, Object> result = new HashMap<>();
		ConcurrentMap<String, CenterConfig> asMap = centerConfigCache.asMap();
		for (Map.Entry<String, CenterConfig> entry : asMap.entrySet()) {
			result.put(entry.getKey(), entry.getValue().getValue(key));
		}
		return result;
	}

	@Override
	public Properties get(String key) {
		Map<String, Object> value = getValue(key);
		return MapHelper.toProperties(value);
	}

	@Override
	public Object getIfOnly(String key) {
		Map<String, Object> map = getValue(key);
		return map.size() == 1 ? FinderHelper.firstElement(map.values()) : null;
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
