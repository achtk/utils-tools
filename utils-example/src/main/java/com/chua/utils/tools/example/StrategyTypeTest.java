package com.chua.utils.tools.example;

import com.chua.utils.tools.example.interfaces.ITestInterface;
import com.chua.utils.tools.example.interfaces.TestImpl;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.chua.utils.tools.proxy.ProxyLoader;
import com.chua.utils.tools.strategy.helper.StrategyHelper;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @author CH
 */
public class StrategyTypeTest {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            ITestInterface testInterface = StrategyHelper.doWithCache(TestImpl.class, null);
            System.out.println(testInterface.getTime());
        }
    }
}