package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfo;
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

        List<TDemoInfo> result = new ArrayList<>();
        int size = 10000;
        for (int i = 0; i < size; i++) {
            List<TDemoInfo> tDemoInfos = JMockData.mock(new TypeReference<List<TDemoInfo>>() {});
            result.addAll(tDemoInfos);
            if(result.size() > size) {
                break;
            }
        }
        result = result.subList(0, size);

        Cost cost = Cost.mill();
        AtomicInteger atomicInteger = new AtomicInteger();
        ClassAccess<TDemoInfo> classAccess = ClassAccess.build(TDemoInfo.class);
        for (TDemoInfo tDemoInfo : result) {
            Object getUuid = ClassUtils.getMethodValue(tDemoInfo, "getUuid", new Object[0]);
            //Object getUuid = classAccess.invoke(tDemoInfo, "getUuid", new Class[0]);
            LogUtils.println("{}: {}", atomicInteger.incrementAndGet(), getUuid);
        }
        long stop = cost.stop();
        int length = result.size();
        LogUtils.println("测试数量: {}, 耗时: {} ms, 平均: {}ms", length, stop, stop/ length);
    }
}
