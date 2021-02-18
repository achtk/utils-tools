package com.chua.utils.tools.bean.interpreter;

import com.chua.utils.tools.named.NamedHelper;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_MINS;

/**
 * 命名解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class NamedInterpreter implements NameInterpreter {
    @Override
    public boolean isMatcher(String name) {
        return name.indexOf(SYMBOL_MINS) > -1;
    }

    @Override
    public String resolve(String name) {
        return NamedHelper.toHump(name);
    }
}
