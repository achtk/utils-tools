package com.chua.utils.tools.example.groovy


import com.chua.utils.tools.example.interfaces.ITestInterface
import com.chua.utils.tools.text.IdHelper

/**
 * @author CH* @since 2021/1/11
 * @version 1.0.0
 */
class Demo implements ITestInterface, GroovyInterceptable {

    def a = 1

    def arr = [1, 2, 3]

    enum AP {
        A, B, C
    }

    def demo(a = 1, b) {
        a + b;
    }

    def demo() {
        def demo = new Demo();
        (1..10).collect() {
            it + 1
        }
    }

    static void main(args) {
        Demo demo = new Demo();
        print demo.add(10, 2);
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
