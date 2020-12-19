package com.chua.utils.tools.example.interfaces;

import com.chua.utils.tools.text.IdHelper;

/**
 * @author CH
 */
public class TestImpl implements ITestInterface{
    @Override
    public String getName() {
        return IdHelper.createUuid();
    }
}
