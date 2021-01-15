package com.chua.utils.tools.example.groovy

import com.chua.utils.tools.example.interfaces.ITestInterface
import com.chua.utils.tools.text.IdHelper

/**
 * @author CH* @since 2021/1/11
 * @version 1.0.0
 */
class Demo implements ITestInterface{

    def a = 1;

    def add(a=1, b) {
        a + b;
    }

    static void main(args) {
        Demo demo = new Demo();
        print demo.add(10, 2);
    }

    @Override
    String getName() {
        IdHelper.createUuid();
    }
}
