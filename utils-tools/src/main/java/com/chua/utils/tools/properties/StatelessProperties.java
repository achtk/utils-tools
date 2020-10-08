package com.chua.utils.tools.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 无状态配置
 * @author CH
 * @date 2020-10-07
 */
public interface StatelessProperties {
	/**
	 * 获取数据实体
	 */
	<T>T getEntity();

	/**
	 * 设置实体
	 * @param t
	 */
	<T>void setEntity(T t);
}
