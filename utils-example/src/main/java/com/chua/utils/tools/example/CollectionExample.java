package com.chua.utils.tools.example;

import com.chua.utils.tools.collects.*;
import com.chua.utils.tools.common.JsonHelper;

import java.util.concurrent.TimeUnit;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class CollectionExample {

    public static void main(String[] args) throws InterruptedException {
        //测试Collect
        testCollect();
        //测试map
        testMap();
    }

    private static void testCollect() {
        System.out.println("***********************测试LoopLinkList***********************");
        LoopList<String> loopLinkList = new LoopLinkList<>();
        loopLinkList.add("a");
        loopLinkList.add("b");
        loopLinkList.add("c");

        System.out.println("LoopLinkList: " + loopLinkList.toString());
        System.out.println("get(0): " + loopLinkList.get(0));
        System.out.println("get(1): " + loopLinkList.get(1));
        System.out.println("get(3): " + loopLinkList.get(3));

        loopLinkList.remove(0);
        System.out.println("loopLinkList.remove(0) : " + loopLinkList.toString());
        System.out.println("get(0): " + loopLinkList.get(0));
        System.out.println("get(1): " + loopLinkList.get(1));
        System.out.println("get(3): " + loopLinkList.get(3));
        loopLinkList.set(0, "a1");
        System.out.println(" loopLinkList.set(0, \"a1\"): " + loopLinkList.toString());
        System.out.println("get(0): " + loopLinkList.get(0));
        System.out.println("get(1): " + loopLinkList.get(1));
        System.out.println("get(3): " + loopLinkList.get(3));

    }

    private static void testMap() throws InterruptedException {
        System.out.println("***********************测试DuplexMap***********************");
        DuplexMap<String, Integer> duplexMap = new DuplexHashMap<>();
        duplexMap.put("a", 1);
        duplexMap.put("b", 2);
        duplexMap.put("c", 3);
        System.out.println("DuplexMap: " + JsonHelper.toJson(duplexMap));
        System.out.println("get(a): " + duplexMap.get("a"));
        System.out.println("getInverse(1): " + duplexMap.getInverse(1));


        System.out.println("***********************测试MultiValueMap***********************");
        ListMultiValueMap<String, Integer> multiValueMap = new MultiValueMap<>();
        multiValueMap.add("a", 1);
        multiValueMap.add("a", 2);
        multiValueMap.add("b", 2);
        multiValueMap.add("c", 3);
        System.out.println("ListMultiValueMap: " + JsonHelper.toJson(multiValueMap));
        System.out.println("get(a): " + multiValueMap.get("a"));

        System.out.println("***********************测试RangeMap***********************");
        RangeMap<Integer, String> rangeMap = new RangeHashMap<>();
        rangeMap.putOpen(0, 10, "a");
        rangeMap.putOpen(10, 100, "b");
        rangeMap.putOpen(100, 1000, "c");
        System.out.println("RangeMap: " + JsonHelper.toJson(rangeMap));
        System.out.println("get(1): " + rangeMap.get(1));
        System.out.println("get(11): " + rangeMap.get(11));
        System.out.println("get(111): " + rangeMap.get(111));
        System.out.println("get(1111): " + rangeMap.get(1111));

        System.out.println("***********************测试TimeMap***********************");
        TimeMap<String, Integer> timeMap = new TimeHashMap<>(1);
        timeMap.detector((s, integer) -> System.out.println("索引:" + s + ", 值：" + integer + "超时被删除"));
        timeMap.put("a", 1);
        timeMap.put("b", 2);
        timeMap.put("c", 3);
        System.out.println("TimeMap: " + JsonHelper.toJson(timeMap));
        Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        System.out.println("get(a): " + timeMap.get("a"));
    }
}
