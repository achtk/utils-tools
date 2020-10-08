package com.chua.utils.tools.handler;

import lombok.EqualsAndHashCode;

/**
 * 周期性任务
 * @author CH
 * @date 2020-10-08
 */
public abstract class PeriodicHandler<Action> extends MessageHandler<Action>{
	@Override
	public void handler(Action action) {

	}

	/**
	 * 延迟多少时间推送
	 * @return
	 */
	abstract public long delay();
}
