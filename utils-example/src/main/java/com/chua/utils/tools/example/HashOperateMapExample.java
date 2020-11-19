package com.chua.utils.tools.example;

import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.collects.HashLazySortMultiValueMap;
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
        HashLazySortMultiValueMap<String, Integer> hashSortMap = new HashLazySortMultiValueMap<>();
        hashSortMap.add("test1", 2);
        hashSortMap.add("test1", 3);
        hashSortMap.add("test1", 1);
        hashSortMap.add("test2", 8);
        hashSortMap.add("test2", 1);
        hashSortMap.add("test3", 1);

        System.out.println("test1:" + hashSortMap.get("test1"));
        System.out.println("test2:" + hashSortMap.get("test2"));
        System.out.println("test3:" + hashSortMap.get("test3"));

        HashOperateMap hashOperateMap = new HashOperateMap();
        System.out.println("是否是Map: " + (hashOperateMap instanceof Map));
        hashOperateMap.append("date", new Date());
        hashOperateMap.append("time", System.currentTimeMillis());
        hashOperateMap.append("name", System.currentTimeMillis());
        hashOperateMap.append("text", "1,2,3,4,test");

        System.out.println("keySet:" + hashOperateMap.keySet());
        System.out.println("time:" + hashOperateMap.get("time"));
        System.out.println("time:" + hashOperateMap.getLong("time"));

        System.out.println("date:" + hashOperateMap.get("date"));
        System.out.println("date:" + hashOperateMap.getDate("date"));
        System.out.println("time:" + hashOperateMap.getDate("time"));
        System.out.println("date:" + hashOperateMap.getDate("date", "yyyy-MM-dd HH:mm:ss"));


        System.out.println("text To List: " + hashOperateMap.splitToList("text", ","));
        System.out.println("text To IntList: " + hashOperateMap.splitToList("text", ",", Integer.class));
        System.out.println("time + text = " + hashOperateMap.expression("time + text"));
        System.out.println("TDemoInfo: " + hashOperateMap.getBean(TDemoInfo.class));
    }
}
