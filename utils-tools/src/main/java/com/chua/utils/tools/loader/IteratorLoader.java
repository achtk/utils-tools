package com.chua.utils.tools.loader;

import com.chua.utils.tools.common.CollectionHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.google.common.collect.HashMultimap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

/**
 * 遍历加载器
 * @author CH
 * @date 2020-10-08
 */
public class IteratorLoader<V> implements IIteratorLoader<V> {

	private final CopyOnWriteArraySet<V> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
	private final ThreadLocal<HashMultimap<Class, V>> threadLocal = new ThreadLocal<HashMultimap<Class, V>>() {
		@Override
		protected HashMultimap<Class, V> initialValue() {
			return HashMultimap.create();
		}
	};

	@Override
	public IIteratorLoader<V> addItem(V item) {
		if(null != item) {
			copyOnWriteArraySet.add(item);
			recordLength(item);
		}
		return this;
	}

	/**
	 * 记录接口长度
	 * @param item
	 */
	private void recordLength(V item) {
		Class<?> aClass = item.getClass();
		Class<?>[] interfaces = aClass.getInterfaces();
		for (Class<?> anInterface : interfaces) {
			threadLocal.get().put(anInterface, item);
		}
	}

	@Override
	public IIteratorLoader<V> addItems(Set<V> items) {
		CollectionHelper.forEach(items, new Consumer<V>() {
			@Override
			public void accept(V item) {
				addItem(item);
			}
		});
		return this;
	}

	@Override
	public V newLoader() {
		if(copyOnWriteArraySet.size() == 0) {
			return null;
		}
		Class<?> interfaces = getInterfaceByLength();
		if(null == interfaces) {
			return null;
		}
		V v = FinderHelper.firstElement(copyOnWriteArraySet);
		return (V) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{interfaces}, new MethodInvocationHandler());
	}

	/**
	 * 尝试通过长度获取接口
	 * @return
	 */
	private Class<?> getInterfaceByLength() {
		int size = copyOnWriteArraySet.size();
		HashMultimap<Class, V> classVHashMultimap = threadLocal.get();
		for (Class aClass : classVHashMultimap.keySet()) {
			Set<V> vs = classVHashMultimap.get(aClass);
			if(vs.size() != size) {
				continue;
			}
			return aClass;
		}
		return null;
	}

	/**
	 * 方法拦截器
	 */
	private class MethodInvocationHandler implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			for (V v : copyOnWriteArraySet) {
				return method.invoke(v, args);
			}
			return null;
		}
	}
}
