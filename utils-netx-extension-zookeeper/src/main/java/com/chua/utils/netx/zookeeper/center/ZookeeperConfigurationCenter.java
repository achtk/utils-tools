package com.chua.utils.netx.zookeeper.center;

import com.chua.utils.netx.centor.listener.Listener;
import com.chua.utils.netx.centor.service.EventPublishingConfigService;
import com.chua.utils.netx.zookeeper.context.ZookeeperContext;
import com.chua.utils.tools.common.*;
import com.chua.utils.tools.function.IConsumer;
import com.chua.utils.tools.properties.NetxProperties;
import com.chua.utils.tools.strategy.handler.IStrategyPolicy;
import com.chua.utils.tools.strategy.helper.StrategyHelper;
import com.google.common.base.Strings;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * zookeeper数据中台
 * @author CH
 * @date 2020-10-07
 */
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class ZookeeperConfigurationCenter implements EventPublishingConfigService, AutoCloseable {

	private static final String DEFAULT_CONFIG_NODE_NAME = "/center/config";
	private static final String CONFIG_NODE_NAME = "config";
	private static final String DEFAULT_GROUP = "group";
	@NonNull
	private NetxProperties netxProperties;
	private ZookeeperContext zookeeperContext;
	private String configName;


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

	@Override
	public String getConfig(String dataId, String group, long timeoutMs) throws Exception {
		if(Strings.isNullOrEmpty(dataId)) {
			return null;
		}
		String cacheKey = createCacheKey(dataId, group);
		if(!zookeeperContext.exist(cacheKey)) {
			return null;
		}
		if(timeoutMs < 0) {
			byte[] bytes = zookeeperContext.queryForString(cacheKey);
			return ByteHelper.toString(bytes);
		}
		return StrategyHelper.doWithTimeout(new IStrategyPolicy<String>() {
			@Override
			public String policy() {
				byte[] bytes = zookeeperContext.queryForString(cacheKey);
				return ByteHelper.toString(bytes);
			}

			@Override
			public String failure(Throwable throwable) {
				return null;
			}
		}, timeoutMs);
	}

	@Override
	public String getConfigAndSignListener(String dataId, String group, long timeoutMs, Listener listener) throws Exception {
		String config = getConfig(dataId, group, timeoutMs);
		addListener(dataId, group, listener);
		return config;
	}

	@Override
	public void addListener(String dataId, String group, Listener listener) throws Exception {
		String cacheKey = createCacheKey(dataId, group);
		zookeeperContext.addListener(cacheKey, new IConsumer<TreeCacheEvent>() {
			@Override
			public void next(TreeCacheEvent item) {
				Executor executor = listener.getExecutor();
				ChildData childData = item.getData();
				executor.execute(new Runnable() {
					@Override
					public void run() {
						listener.receiveConfigInfo(ByteHelper.toString(childData.getData()));
					}
				});
			}
		});
	}

	@Override
	public boolean publishConfig(String dataId, String group, String content) throws Exception {
		if(Strings.isNullOrEmpty(dataId)) {
			log.warn("数据不存在注册失败！");
			return false;
		}
		String newKey = createCacheKey(dataId, group);
		if(zookeeperContext.exist(newKey)) {
			zookeeperContext.delete(newKey);
		}
		zookeeperContext.createEphemeral(newKey, content);
		return true;
	}

	/**
	 * 创建索引
	 * @param dataId
	 * @param group
	 * @return
	 */
	private String createCacheKey(String dataId, String group) {
		group = StringHelper.defaultIfBlank(group, DEFAULT_GROUP);
		return configName + "/" + dataId + "/" + group;
	}

	@Override
	public boolean removeConfig(String dataId, String group) throws Exception {
		if(Strings.isNullOrEmpty(dataId)) {
			log.warn("数据不存在注册失败！");
			return false;
		}
		String newKey = createCacheKey(dataId, group);
		return !zookeeperContext.exist(newKey) ? true : zookeeperContext.delete(newKey);
	}

	@Override
	public void removeListener(String dataId, String group, Listener listener) {
		String newKey = createCacheKey(dataId, group);
		if(!zookeeperContext.exist(newKey)) {
			return;
		}
		zookeeperContext.removeListener(newKey, new IConsumer<TreeCacheEvent>() {
			@Override
			public void next(TreeCacheEvent item) {
				Executor executor = listener.getExecutor();
				ChildData childData = item.getData();
				executor.execute(new Runnable() {
					@Override
					public void run() {
						listener.receiveConfigInfo(ByteHelper.toString(childData.getData()));
					}
				});
			}
		});
	}

	@Override
	public String getServerStatus() {
		return zookeeperContext.getStatus();
	}

	@Override
	public void shutDown() throws Exception {
		close();
	}
}
