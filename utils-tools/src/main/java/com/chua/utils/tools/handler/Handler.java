package com.chua.utils.tools.handler;

/**
 * 处理动作
 * @author CH
 * @date 2020-10-07
 */
public interface Handler<Action> {

	/**
	 * 处理动作
	 * @param action 动作
	 */
	void handler(Action action);
}
