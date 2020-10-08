package com.chua.utils.netx.zookeeper.center;

import com.chua.utils.netx.centor.ConfigurationCenter;
import com.chua.utils.netx.entity.CenterConfig;
import com.chua.utils.netx.zookeeper.context.ZookeeperContext;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.properties.NetxProperties;
import com.google.common.base.Strings;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * zookeeper数据中台
 * @author CH
 * @date 2020-10-07
 */
@Slf4j
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
		zookeeperContext.createEphemeral(newKey, JsonHelper.toJson(centerConfig.getProperties()));
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
		getValue(name);
		zookeeperContext.createEphemeral(newKey, JsonHelper.toJson(centerConfig.getProperties()));
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
	public Map<String, Object> getValue(String key) {
		List<String> children = zookeeperContext.getChildren(configName);
		if(!BooleanHelper.hasLength(children)) {
			return null;
		}
		Map<String, Object> result = new HashMap<>();
		for (String child : children) {
			result.put(child, getValue(child, key));
		}
		return result;
	}

	@Override
	public Properties get(String name) {
		if(Strings.isNullOrEmpty(name)) {
			return null;
		}
		byte[] bytes = zookeeperContext.queryForString(configName + "/" + name);
		try {
			return JsonHelper.fromJson(bytes, Properties.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getValue(String name, String key) {
		if(Strings.isNullOrEmpty(name)) {
			return null;
		}
		byte[] bytes = zookeeperContext.queryForString(configName + "/" + name);
		Map<String, Object> objectMap = JsonHelper.fromJson2Map(bytes);
		if(!BooleanHelper.hasLength(objectMap)) {
			return null;
		}
		return null;
	}

	@Override
	public Object getIfOnly(String key) {
		Map<String, Object> map = getValue(key);
		return map.size() == 1 ? FinderHelper.firstElement(map.values()) : null;
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
