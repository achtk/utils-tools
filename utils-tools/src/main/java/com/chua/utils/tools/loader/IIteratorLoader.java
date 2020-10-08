package com.chua.utils.tools.loader;

import java.util.Set;

/**
 * 遍历加载器
 * @author CH
 * @date 2020-10-08
 */
public interface IIteratorLoader<V> {
	/**
	 * 添加对象
	 * @param item 对象
	 * @return
	 */
	IIteratorLoader<V> addItem(V item);
	/**
	 * 添加对象
	 * @param items 对象
	 * @return
	 */
	IIteratorLoader<V> addItems(Set<V> items);

	/**
	 * 创建加载器
	 * @return
	 */
	V newLoader();
}
