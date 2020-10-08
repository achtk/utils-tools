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
	 * 属性
	 */
	private Properties properties;

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
}
