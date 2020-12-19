package com.chua.utils.tools.bean.creator;

import com.chua.utils.tools.annotations.Binder;
import com.chua.utils.tools.bean.script.ValueScript;
import com.chua.utils.tools.function.ValueMatcher;

/**
 * 值构造器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public interface ValueCreator extends ValueMatcher<Binder.Type> {
    /**
     * 生成值
     * <p>
     * {@link ValueScript#getOperate()} 主要包含：
     * </p>
     *
     * @param valueScript 脚本
     * @return 值
     */
    Object create(ValueScript valueScript);

}
