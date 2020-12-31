package com.chua.tools.example.interfaces;

import com.chua.utils.tools.text.IdHelper;

/**
 * @author CH
 */
public class TestImpl implements TestInterface {
    @Override
    public String getName() {
        return IdHelper.createUuid();
    }
}
