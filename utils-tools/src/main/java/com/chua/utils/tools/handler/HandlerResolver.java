package com.chua.utils.tools.handler;

import com.chua.utils.tools.cache.ConcurrentSetValueCacheProvider;
import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.enums.HandlerType;

import java.util.Set;

/**
 * handler处理器
 * @author CH
 * @date 2020-10-08
 */
public class HandlerResolver {

	private final CacheProvider<HandlerType, Handler> cacheProvider = new ConcurrentSetValueCacheProvider();

	/**
	 * 获取handler
	 * @param handlerType
	 * @return
	 */
	public Set<Handler> handlers(HandlerType handlerType) {
		return (Set<Handler>) cacheProvider.get(handlerType);
	}

	/**
	 * 注册handler
	 * @param handlerType
	 * @param handler
	 * @return
	 */
	public HandlerResolver addHandler(HandlerType handlerType, Handler handler) {
		if(null == handler || null == handlerType) {
			return this;
		}
		cacheProvider.put(handlerType, handler);
		return this;
	}
	/**
	 * 是否存在Handler
	 * @param handlerType
	 * @return
	 */
	public boolean isHandler(HandlerType handlerType) {
		if(null == handlerType) {
			return false;
		}
		return cacheProvider.containsKey(handlerType);
	}

	/**
	 * 是否为空
	 * @return
	 */
	public boolean isEmpty() {
		return cacheProvider.size() == 0;
	}
}
