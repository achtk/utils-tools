package com.chua.utils.tools.example.interfaces;

/**
 * @author CH
 */
public class TestImpl implements ITestInterface{
    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }
}
