package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.collects.OperateMap;
import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.common.codec.encrypt.Md5Encrypt;
import com.chua.utils.tools.logger.LogUtils;

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
        log.info("******************************测试集合[基础操作]********************************");
        OperateHashMap operateHashMap = new OperateHashMap();
        log.info("是否是Map: " + (operateHashMap instanceof Map));
        operateHashMap.append("date", new Date());
        operateHashMap.append("time", System.currentTimeMillis());
        operateHashMap.append("name", System.currentTimeMillis());
        operateHashMap.append("text", "1,2,3,4,test");
        log.info("当前存储的数据:\n " + operateHashMap.toString());

        LogUtils.info("获取对象索引: keySet() => {} ", operateHashMap.keySet());
        LogUtils.info("获取索引数据: get(time) => {} ", operateHashMap.get("time"));
        LogUtils.info("获取索引[Long]数据: getLong(time) => {} ", operateHashMap.getLong("time"));
        LogUtils.info("获取索引[Date]数据: getDate(time) => {} ", operateHashMap.getDate("time"));
        LogUtils.info("获取索引[Date]数据: getDate(date) => {} ", operateHashMap.getDate("date"));
        LogUtils.info("获取索引[Date][yyyy-MM-dd HH:mm:ss]数据: getDate(date, yyyy-MM-dd HH:mm:ss)=> {} ", operateHashMap.getDate("date", "yyyy-MM-dd HH:mm:ss"));
        LogUtils.info("获取索引[List]数据: splitToList(text) => {} ", operateHashMap.splitToList("text"));
        LogUtils.info("获取索引[表达式]数据: expression(time + text) => {} ", operateHashMap.expression("time + text"));
        LogUtils.info("获取索引[List<Integer>]数据: splitToList(text, Integer.class) => {} ", operateHashMap.splitToList("text", Integer.class));
        LogUtils.info("获取索引[List<Double>]数据: splitToList(text, Double.class) => {} ", operateHashMap.splitToList("text", Double.class));
        LogUtils.info("装配对象[TDemoInfo]数据: assemblyBean(TDemoInfoImpl.class) => {} ", operateHashMap.assemblyBean(TDemoInfoImpl.class));
        LogUtils.info("索引重命名[time -> time1]数据: rename(TDemoInfoImpl.class) => {} ", operateHashMap.rename("time", "time1"));

        log.info("******************************测试集合[对象存储]********************************");
        OperateMap operateMap = new OperateHashMap(Encrypt.class);
        operateMap.putObject("base64", "com.chua.utils.tools.common.codec.encrypt.Base64Encrypt");
        operateMap.putObject("md5", Md5Encrypt.class.getName());
        operateMap.putObject("md5", "com");
        operateMap.putObject("md5Error", "com");
        log.info("当前存储的数据:\n " + operateMap.toString());
    }

}
