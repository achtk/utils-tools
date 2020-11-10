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

        System.out.println(hashSortMap.get("test1"));
        System.out.println(hashSortMap.get("test2"));
        System.out.println(hashSortMap.get("test3"));

        HashOperateMap hashOperateMap = new HashOperateMap();
        System.out.println(hashOperateMap instanceof Map);
        hashOperateMap.append("date", new Date())
                .append("time", System.currentTimeMillis())
                .append("name", System.currentTimeMillis())
                .append("text", "1,2,3,4,test");

        System.out.println(hashOperateMap.get("time"));
        System.out.println(hashOperateMap.getLong("time"));

        System.out.println(hashOperateMap.get("date"));
        System.out.println(hashOperateMap.getDate("date"));
        System.out.println(hashOperateMap.getDate("time"));
        System.out.println(hashOperateMap.dateFormatter("date", "yyyy-MM-dd HH:mm:ss"));


        //System.out.println(hashOperateMap.splitToList("text", ","));
        // System.out.println(hashOperateMap.splitToList("text", ",", Integer.class));
        System.out.println(hashOperateMap.expression("time + text"));
        System.out.println(hashOperateMap.getBean(TDemoInfo.class));
    }
}
