package com.chua.utils.tools.function;

/**
 * 不转化转化器
 * @author CH
 */
public class NoneAbstractConverter extends AbstractConverter {
    @Override
    public Object doForward(Object o) {
        return o;
    }

    @Override
    public Object doBackward(Object o) {
        return o;
    }
}
