package com.chua.utils.netx.zookeeper.center;

import com.chua.utils.netx.centor.ConfigurationCenter;
import com.chua.utils.netx.entity.CenterConfig;
import com.chua.utils.netx.zookeeper.context.ZookeeperContext;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.properties.NetxProperties;
import com.google.common.base.Strings;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * zookeeper数据中台
 * @author CH
 * @date 2020-10-07
 */
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class ZookeeperConfigurationCenter implements ConfigurationCenter {

	private static final String DEFAULT_CONFIG_NODE_NAME = "/center/config";
	private static final String CONFIG_NODE_NAME = "config";
	@NonNull
	private NetxProperties netxProperties;
	private ZookeeperContext zookeeperContext;
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
		String newKey = configName + "/" + name;
		if(zookeeperContext.exist(newKey)) {
			zookeeperContext.delete(newKey);
		}
		zookeeperContext.createEphemeral(newKey, JsonHelper.toJson(centerConfig));
	}

	@Override
	public void update(CenterConfig centerConfig) throws Exception {
		if(null == centerConfig) {
			log.warn("数据不存在注册失败！");
			return;
		}
		String name = centerConfig.getName();
		if(Strings.isNullOrEmpty(name)) {
			log.warn("数据索引[name]不存在注册失败！");
			return;
		}
		String newKey = configName + "/" + name;
		if(!zookeeperContext.exist(newKey)) {
			register(centerConfig);
			return;
		}
		getCenterConfigs(name);
		zookeeperContext.createEphemeral(newKey, JsonHelper.toJson(centerConfig));
	}

	@Override
	public void remove(String name) {
		if(Strings.isNullOrEmpty(name)) {
			return;
		}
		String newKey = configName + "/" + name;
		if(!zookeeperContext.exist(newKey)) {
			return;
		}
		zookeeperContext.delete(newKey);
	}

	@Override
	public List<CenterConfig> getCenterConfigs(String key) {
		List<String> children = zookeeperContext.getChildren(configName);
		if(!BooleanHelper.hasLength(children)) {
			return null;
		}
		List<CenterConfig> centerConfigs = new ArrayList<>(children.size());
		for (String child : children) {
			CenterConfig centerConfig = getConfig(child);
			if(null == centerConfig) {
				continue;
			}
			if(!centerConfig.isContainer(key)) {
				continue;
			}
			centerConfigs.add(centerConfig);
		}

		Collections.sort(centerConfigs, new Comparator<CenterConfig>() {
			@Override
			public int compare(CenterConfig o1, CenterConfig o2) {
				return o1.getOrder() - o2.getOrder() > 0 ? -1 : 1;
			}
		});
		return centerConfigs;
	}

	@Override
	public CenterConfig getConfig(String name) {
		if(Strings.isNullOrEmpty(name)) {
			return null;
		}
		byte[] bytes = zookeeperContext.queryForString(configName + "/" + name);
		CenterConfig centerConfig = JsonHelper.fromJson(bytes, CenterConfig.class);
		return centerConfig;
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
		this.zookeeperContext = new ZookeeperContext(netxProperties);
		this.initialNode();
	}

	/**
	 * 初始化节点
	 */
	private void initialNode() {
		this.configName = netxProperties.getProperty(CONFIG_NODE_NAME, DEFAULT_CONFIG_NODE_NAME);
		try {
			this.zookeeperContext.createPersistent(configName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void close() throws Exception {
		this.zookeeperContext.close();
	}
}
