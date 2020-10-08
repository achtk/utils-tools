package com.chua.utils.netx.nacos.center;

import com.chua.utils.netx.centor.ConfigurationCenter;
import com.chua.utils.netx.entity.CenterConfig;
import com.chua.utils.netx.nacos.context.NacosContext;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.properties.NetxProperties;
import com.google.common.base.Strings;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * nacos数据中台
 * @author CH
 * @date 2020-10-07
 */
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class NacosConfigurationCenter implements ConfigurationCenter {

	private static final String DEFAULT_CONFIG_NODE_NAME = "DEFAULT_GROUP";
	private static final String CONFIG_NODE_NAME = "config";
	@NonNull
	private NetxProperties netxProperties;
	private NacosContext nacosContext;
	private String configName;

	@Override
	public void register(CenterConfig centerConfig) throws Exception {
		if(null == centerConfig) {
			log.warn("数据不存在注册失败！");
			return;
		}
		String name = centerConfig.getName();
		if(Strings.isNullOrEmpty(name)) {
			log.warn("数据索引[name]不存在注册失败！");
			return;
		}
		nacosContext.createConfig(name, name, JsonHelper.toJson(centerConfig));
	}

	@Override
	public void update(CenterConfig centerConfig) throws Exception {
		update(centerConfig);
	}

	@Override
	public void remove(String name) {
		try {
			nacosContext.removeConfig(name, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<CenterConfig> getCenterConfigs(String key) {
		List<String> configChildren = nacosContext.getConfigChildren(key, key);
		List<CenterConfig> result = new ArrayList<>(configChildren.size());
		for (String configChild : configChildren) {
			CenterConfig centerConfig = JsonHelper.fromJson(configChild, CenterConfig.class);
			if(null == centerConfig) {
				continue;
			}
			result.add(centerConfig);
		}
		return result;
	}

	@Override
	public CenterConfig getConfig(String name) {
		List<CenterConfig> centerConfigs = getCenterConfigs(name);
		return FinderHelper.firstElement(centerConfigs);
	}

	@Override
	public void initial(NetxProperties netxProperties) {
		this.netxProperties = netxProperties;
	}

	@Override
	public Properties getEnvironment() {
		return netxProperties;
	}

	@Override
	public void setEnvironment(Properties properties) {
		if(null != netxProperties && null != properties) {
			netxProperties.putAll(properties);
		}
	}
	@Override
	public void start() throws Exception {
		this.nacosContext = new NacosContext(netxProperties);
		this.configName = netxProperties.getProperty(CONFIG_NODE_NAME, DEFAULT_CONFIG_NODE_NAME);
	}

	@Override
	public void close() throws Exception {

	}
}
