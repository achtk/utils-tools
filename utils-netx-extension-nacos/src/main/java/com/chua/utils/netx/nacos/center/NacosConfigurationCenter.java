package com.chua.utils.netx.nacos.center;

import com.alibaba.nacos.api.naming.listener.Event;
import com.chua.utils.netx.centor.listener.Listener;
import com.chua.utils.netx.centor.service.EventPublishingConfigService;
import com.chua.utils.netx.centor.service.EventPublishingNamingService;
import com.chua.utils.netx.nacos.context.NacosContext;
import com.chua.utils.tools.function.producer.NetxPropertiesProducer;
import com.chua.utils.tools.properties.NetxProperties;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * nacos数据中台
 * @author CH
 * @date 2020-10-07
 */
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class NacosConfigurationCenter implements EventPublishingConfigService, EventPublishingNamingService, NetxPropertiesProducer, AutoCloseable {

	@NonNull
	private NetxProperties netxProperties;
	private NacosContext nacosContext;

	@Override
	public void close() throws Exception {
		nacosContext.shutDown();
	}

	@Override
	public String getConfig(String dataId, String group, long timeoutMs) throws Exception {
		return nacosContext.getConfig(dataId, group, timeoutMs);
	}

	@Override
	public String getConfigAndSignListener(String dataId, String group, long timeoutMs, Listener listener) throws Exception {
		return nacosContext.getConfigAndSignListener(dataId, group, timeoutMs, listener);
	}

	@Override
	public void addListener(String dataId, String group, Listener listener) throws Exception {
		nacosContext.addConfigListener(dataId, group, listener);
	}

	@Override
	public boolean publishConfig(String dataId, String group, String content) throws Exception {
		nacosContext.createConfig(dataId, group, content);
		return true;
	}

	@Override
	public boolean removeConfig(String dataId, String group) throws Exception {
		return nacosContext.removeConfig(dataId, group);
	}

	@Override
	public void removeListener(String dataId, String group, Listener listener) throws Exception {
		nacosContext.removeConfigListener(dataId, group, listener);
	}

	@Override
	public String getServerStatus() {
		return nacosContext.getServerStatus();
	}

	@Override
	public void shutDown() throws Exception {
		close();
	}

	@Override
	public void initial(NetxProperties netxProperties) {
		this.netxProperties = netxProperties;
		this.nacosContext = new NacosContext(netxProperties);
	}

	@Override
	public void registerInstance(String serviceName, String group, String ip, int port, String clusterName) throws Exception {
		nacosContext.registerInstance(serviceName, group, ip, port, clusterName);
	}

	@Override
	public void deregisterInstance(String serviceName, String group, String ip, int port, String clusterName) throws Exception {
		nacosContext.deregisterInstance(serviceName, group, ip, port, clusterName);
	}

	@Override
	public List<Map<String, Object>> selectInstances(String serviceName, boolean healthy) throws Exception {
		return nacosContext.selectInstances(serviceName, healthy);
	}

	@Override
	public List<Map<String, Object>> getAllInstances(String serviceName) throws Exception {
		return nacosContext.getAllInstances(serviceName);
	}

	@Override
	public <T>void subscribe(String serviceName, Listener<T> listener) throws Exception {
		nacosContext.subscribe(serviceName, (Listener<Event>) listener);
	}

	@Override
	public <T>void unsubscribe(String serviceName, Listener<T> listener) throws Exception {
		nacosContext.unsubscribe(serviceName, (Listener<Event>) listener);
	}
}
