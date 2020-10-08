package com.chua.utils.tools.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 处理类型
 * @author CH
 * @date 2020-10-08
 */
@Getter
@AllArgsConstructor
public enum HandlerType {
	/**
	 * 连接
	 */
	CONNECT,
	/**
	 * 读取
	 */
	READER,
	/**
	 * 写入
	 */
	WRITER,
	/**
	 * 释放
	 */
	CLOSE,
	/**
	 * 结束
	 */
	FINAL,
	/**
	 * 任意
	 */
	ANY,
	/**
	 * ws消息
	 */
	MESSAGE,
	/**
	 * 周期性
	 */
	PERIODIC,
	/**
	 * 异常
	 */
	EXCEPTION;
}
