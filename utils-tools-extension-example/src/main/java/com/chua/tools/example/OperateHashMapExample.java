package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.collects.OperateHashMap;

import java.util.Date;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class OperateHashMapExample extends BaseExample {

    public static void main(String[] args) {
        //测试可操作集合
        testHashOperateMap();
    }

    private static void testHashOperateMap() {
        log.info("******************************测试可操作集合********************************");
        OperateHashMap operateHashMap = new OperateHashMap();
        log.info("是否是Map: " + (operateHashMap instanceof Map));
        operateHashMap.append("date", new Date());
        operateHashMap.append("time", System.currentTimeMillis());
        operateHashMap.append("name", System.currentTimeMillis());
        operateHashMap.append("text", "1,2,3,4,test");
        log.info("当前存储的数据: " + operateHashMap);

        log.info("keySet:" + operateHashMap.keySet());
        log.info("time:" + operateHashMap.get("time"));
        log.info("time:" + operateHashMap.getLong("time"));

        log.info("date:" + operateHashMap.get("date"));
        log.info("date:" + operateHashMap.getDate("date"));
        log.info("time:" + operateHashMap.getDate("time"));
        log.info("date:" + operateHashMap.getDate("date", "yyyy-MM-dd HH:mm:ss"));


        log.info("text To List: " + operateHashMap.splitToList("text", ","));
        log.info("text To IntList: " + operateHashMap.splitToList("text", ",", Integer.class));
        log.info("time + text = " + operateHashMap.expression("time + text"));
        log.info("TDemoInfo: " + operateHashMap.getBean(TDemoInfo.class));
    }

}
