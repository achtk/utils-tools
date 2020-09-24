package com.chua.utils.tools.strategy;

import com.chua.utils.tools.interfaces.ITestInterface;
import com.chua.utils.tools.strategy.enums.StrategyType;
import org.testng.annotations.Test;

/**
 * @author CH
 */
public class StrategyTypeTest {

    @Test
    public void testCallee() {
        Strategy<ITestInterface> strategy =
                Strategy.newBuilder(StrategyType.RETRY).setInterfaces(ITestInterface.class)
                        .build();

        ITestInterface callee = strategy.newProxy();
        System.out.println(callee.getTime());
    }
}