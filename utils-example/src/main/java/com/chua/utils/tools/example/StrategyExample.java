package com.chua.utils.tools.example;

import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.strategy.CycleStrategy;
import com.chua.utils.tools.strategy.Strategy;
import com.google.common.base.Predicate;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 * @see com.chua.utils.tools.example.ObjectContextManagerExample
 */
@Deprecated
public class StrategyExample {

    public static void main(String[] args) {
        Predicate predicate = new Predicate() {
            @Override
            public boolean apply(@Nullable Object input) {
                return input == null;
            }
        };
        TDemoInfo tDemoInfo1 = new TDemoInfo();
        tDemoInfo1.name(UUID.randomUUID().toString());

        //Strategy<TDemoInfo> strategy = new RetryStrategy<>(predicate);
        // Strategy<TDemoInfo> strategy = new CacheStrategy<>();
        // Strategy<TDemoInfo> strategy = new TokenLimitStrategy<>(2.0);
        Strategy<TDemoInfo> strategy = new CycleStrategy<>();
        TDemoInfo tDemoInfo = strategy.create(tDemoInfo1);
        System.out.println(tDemoInfo.name());
        /*try (AsyncStrategy<TDemoInfo> strategy = new AsyncStrategy<>(new Consumer() {
            @Override
            public void accept(Object o) {
                System.out.println(o);
            }
        })) {
            TDemoInfo tDemoInfo = strategy.create(tDemoInfo1);
            System.out.println(tDemoInfo.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
    }
}
