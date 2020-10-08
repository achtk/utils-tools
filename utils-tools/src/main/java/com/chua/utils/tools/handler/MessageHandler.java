package com.chua.utils.tools.handler;

import lombok.EqualsAndHashCode;

/**
 * @author CH
 * @date 2020-10-08
 */
public abstract class MessageHandler<Action> implements Handler<Action>{
	@Override
	public void handler(Action action) {

	}

	/**
	 * 消费
	 * @param data 数据
	 * @return
	 */
	abstract public void consumer(Object data);
	/**
	 * 发布
	 * @return
	 */
	abstract public Message publish();

	@lombok.Data
	@EqualsAndHashCode
	public static class Message {
		/**
		 * 接收方名称
		 */
		private String name;
		/**
		 * 数据
		 */
		private Object data;

	}

}
