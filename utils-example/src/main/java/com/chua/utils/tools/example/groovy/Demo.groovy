package com.chua.utils.tools.example.groovy


import com.chua.utils.tools.example.entity.TDemoInfo
import com.chua.utils.tools.example.interfaces.ITestInterface
import com.chua.utils.tools.text.IdHelper

/**
 * @author CH* @since 2021/1/11
 * @version 1.0.0
 */
class Demo implements ITestInterface, GroovyInterceptable {

    def a = 1I

    def arr = [1, 2, 3] as int[]

    enum AP {
        A, B, C
    }

    def demo(a = 1, b) {
        a + b;
    }


    static void main(args) {
        def tDemoInfo = new TDemoInfo()
        tDemoInfo.id '1'
        println tDemoInfo
    }

    @Override
    String getName() {
        IdHelper.createUuid();
    }

    @Override
    Object invokeMethod(String name, Object args) {
        return 1
    }
}
