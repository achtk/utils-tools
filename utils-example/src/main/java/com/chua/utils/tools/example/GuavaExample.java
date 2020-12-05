package com.chua.utils.tools.example;

import com.google.common.collect.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/30
 */
public class GuavaExample {

    public static void main(String[] args) {
        //测试Table
        testTable();
        //测试Range
        testRange();
    }

    private static void testRange() {
        System.out.println("============================测试Range======================");
        RangeMap rangeMap = TreeRangeMap.create();
        //1-100
        rangeMap.put(Range.closedOpen(0, 10), 1);
        rangeMap.put(Range.closedOpen(10, 13), 100);
        rangeMap.put(Range.closedOpen(13, 75), 2);
        rangeMap.put(Range.closed(75, 100), 3);

        IntStream.range(1, 10).forEach(index1 -> {

            Map<Object, LongAdder> record = new HashMap<>();
            IntStream.range(1, 10000).forEach(index -> {
                synchronized (record) {
                    Object value = rangeMap.get(new Random().nextInt(100));
                    record.compute(value, (o, o2) -> {
                        if(null != o2) {
                            o2.increment();
                            return o2;
                        }
                        LongAdder longAdder = new LongAdder();
                        longAdder.increment();
                        return longAdder;
                    });
                }
            });
            System.out.println("随机获取数据:" + record);
        });
    }

    private static void testTable() {
        System.out.println("============================测试Table======================");
        HashBasedTable hashBasedTable = HashBasedTable.create();
        hashBasedTable.put(1, 1, 1);
        hashBasedTable.put(1, 1, 2);
        hashBasedTable.put(1, 1, 3);
        System.out.println("获取row: 1, column: 1, 的值: " + hashBasedTable.get(1, 1));
    }
}
