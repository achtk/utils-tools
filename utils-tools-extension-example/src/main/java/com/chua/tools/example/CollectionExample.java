package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.collects.*;
import com.chua.utils.tools.common.JsonHelper;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class CollectionExample extends BaseExample {

    public static void main(String[] args) throws InterruptedException {
        //测试Collect
        testCollect();
        //测试map
        testMap();
    }

    private static void testCollect() {
        log.info("***********************测试LoopLinkList***********************");
        LoopList<String> loopLinkList = new LoopLinkList<>();
        loopLinkList.add("a");
        loopLinkList.add("b");
        loopLinkList.add("c");

        log.info("LoopLinkList: " + loopLinkList.toString());
        log.info("get(0): " + loopLinkList.get(0));
        log.info("get(1): " + loopLinkList.get(1));
        log.info("get(3): " + loopLinkList.get(3));

        loopLinkList.remove(0);
        log.info("loopLinkList.remove(0) : " + loopLinkList.toString());
        log.info("get(0): " + loopLinkList.get(0));
        log.info("get(1): " + loopLinkList.get(1));
        log.info("get(3): " + loopLinkList.get(3));
        loopLinkList.set(0, "a1");
        log.info(" loopLinkList.set(0, \"a1\"): " + loopLinkList.toString());
        log.info("get(0): " + loopLinkList.get(0));
        log.info("get(1): " + loopLinkList.get(1));
        log.info("get(3): " + loopLinkList.get(3));

    }

    private static void testMap() throws InterruptedException {
        log.info("***********************测试DuplexMap***********************");
        DuplexMap<String, Integer> duplexMap = new DuplexHashMap<>();
        duplexMap.put("a", 1);
        duplexMap.put("b", 2);
        duplexMap.put("c", 3);
        log.info("DuplexMap: " + JsonHelper.toJson(duplexMap));
        log.info("get(a): " + duplexMap.get("a"));
        log.info("getInverse(1): " + duplexMap.getInverse(1));


        log.info("***********************测试MultiValueMap***********************");
        MultiListValueMap<String, Integer> multiValueMap = new MultiValueMap<>();
        multiValueMap.add("a", 1);
        multiValueMap.add("a", 2);
        multiValueMap.add("b", 2);
        multiValueMap.add("c", 3);
        log.info("ListMultiValueMap: " + JsonHelper.toJson(multiValueMap));
        log.info("get(a): " + multiValueMap.get("a"));
        log.info("***********************测试 MultiSortValueMap***********************");
        MultiSortValueMap<String, TDemoInfo> sortValueMap = MultiValueSortMap.create("id");
        sortValueMap.put("a", new TDemoInfo().setId(3));
        sortValueMap.put("a", new TDemoInfo().setId(1));
        sortValueMap.put("a", new TDemoInfo().setId(33));
        sortValueMap.put("b", new TDemoInfo().setId(3));
        sortValueMap.put("c", new TDemoInfo().setId(3));
        log.info("MultiSortValueMap: " + JsonHelper.toJson(sortValueMap));
        log.info("get(a): " + sortValueMap.get("a"));

        log.info("***********************测试RangeMap***********************");
        RangeMap<Integer, String> rangeMap = new RangeHashMap<>();
        rangeMap.putOpen(0, 10, "a");
        rangeMap.putOpen(10, 100, "b");
        rangeMap.putOpen(100, 1000, "c");
        log.info("RangeMap: " + JsonHelper.toJson(rangeMap));
        log.info("get(1): " + rangeMap.get(1));
        log.info("get(11): " + rangeMap.get(11));
        log.info("get(111): " + rangeMap.get(111));
        log.info("get(1111): " + rangeMap.get(1111));

        log.info("***********************测试TimeMap***********************");
        TimeMap<String, Integer> timeMap = new TimeHashMap<>(1);
        timeMap.detector((s, integer) -> log.info("索引:" + s + ", 值：" + integer + "超时被删除"));
        timeMap.put("a", 1);
        timeMap.put("b", 2);
        timeMap.put("c", 3);
        log.info("TimeMap: " + JsonHelper.toJson(timeMap));
        log.info("get(a): " + timeMap.get("a"));
    }
}
