package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.utils.tools.classes.ClassAccess;
import com.chua.utils.tools.logger.LogUtils;
import com.chua.utils.tools.time.Cost;
import com.chua.utils.tools.util.ClassUtils;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassAccess
 * @author CH
 * @version 1.0.0
 * @since 2021/1/7
 */
public class ClassAccessExample {

    public static void main(String[] args) {

        List<TDemoInfoImpl> result = new ArrayList<>();
        int size = 10000;
        for (int i = 0; i < size; i++) {
            List<TDemoInfoImpl> tDemoInfoImpls = JMockData.mock(new TypeReference<List<TDemoInfoImpl>>() {});
            result.addAll(tDemoInfoImpls);
            if(result.size() > size) {
                break;
            }
        }
        result = result.subList(0, size);

        Cost cost = Cost.mill();
        AtomicInteger atomicInteger = new AtomicInteger();
        ClassAccess<TDemoInfoImpl> classAccess = ClassAccess.build(TDemoInfoImpl.class);
        for (TDemoInfoImpl tDemoInfoImpl : result) {
            Object getUuid = ClassUtils.getMethodValue(tDemoInfoImpl, "getUuid", new Object[0]);
            //Object getUuid = classAccess.invoke(tDemoInfo, "getUuid", new Class[0]);
            LogUtils.info("{}: {}", atomicInteger.incrementAndGet(), getUuid);
        }
        long stop = cost.stop();
        int length = result.size();
        LogUtils.info("测试数量: {}, 耗时: {} ms, 平均: {}ms", length, stop, stop/ length);
    }
}
