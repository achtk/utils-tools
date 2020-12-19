package com.chua.utils.tools.example;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.example.entity.TDemoInfo;

import java.util.Date;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class HashOperateMapExample {

    public static void main(String[] args) {
        //测试可操作集合
        testHashOperateMap();
    }

    private static void testHashOperateMap() {
        System.out.println("******************************测试可操作集合********************************");
        OperateHashMap operateHashMap = new OperateHashMap();
        System.out.println("是否是Map: " + (operateHashMap instanceof Map));
        operateHashMap.append("date", new Date());
        operateHashMap.append("time", System.currentTimeMillis());
        operateHashMap.append("name", System.currentTimeMillis());
        operateHashMap.append("text", "1,2,3,4,test");
        System.out.println("当前存储的数据: " + operateHashMap);

        System.out.println("keySet:" + operateHashMap.keySet());
        System.out.println("time:" + operateHashMap.get("time"));
        System.out.println("time:" + operateHashMap.getLong("time"));

        System.out.println("date:" + operateHashMap.get("date"));
        System.out.println("date:" + operateHashMap.getDate("date"));
        System.out.println("time:" + operateHashMap.getDate("time"));
        System.out.println("date:" + operateHashMap.getDate("date", "yyyy-MM-dd HH:mm:ss"));


        System.out.println("text To List: " + operateHashMap.splitToList("text", ","));
        System.out.println("text To IntList: " + operateHashMap.splitToList("text", ",", Integer.class));
        System.out.println("time + text = " + operateHashMap.expression("time + text"));
        System.out.println("TDemoInfo: " + operateHashMap.getBean(TDemoInfo.class));
    }

}
