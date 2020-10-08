package com.chua.utils.netx.entity;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 中台配置
 * @author CH
 * @date 2020-10-07
 */
@Data
@EqualsAndHashCode
public class CenterConfig {
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 优先级越大越高
	 */
	private int order;
	/**
	 * 属性
	 */
	private Properties properties = new Properties();

	/**
	 * 获取数据
	 * @param key 索引
	 * @return
	 */
	public Object getValue(String key) {
		if(Strings.isNullOrEmpty(key)) {
			return null;
		}
		return null == properties ? null : properties.get(key);
	}
	/**
	 * 是否存在数据
	 * @param key 索引
	 * @return
	 */
	public boolean isContainer(String key) {
		if(Strings.isNullOrEmpty(key)) {
			return false;
		}
		return null == properties ? false : properties.containsKey(key);
	}

	/**
	 * 设置值
	 */
	public void put(Object key, Object value) {
		if(null == key || null == value) {
			throw new NullPointerException();
		}
		properties.put(key, value);
	}
}
