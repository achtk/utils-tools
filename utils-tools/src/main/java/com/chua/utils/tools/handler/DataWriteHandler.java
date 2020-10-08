package com.chua.utils.tools.handler;

/**
 * 处理动作
 * @author CH
 * @date 2020-10-07
 */
public interface DataWriteHandler<Action, Data> extends Handler<Action> {

	/**
	 * 获取数据
	 * @param <Data>
	 * @return
	 */
	<Data> Data getData();
}
