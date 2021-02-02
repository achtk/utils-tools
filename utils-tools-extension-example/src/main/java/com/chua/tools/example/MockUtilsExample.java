package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.tools.example.utils.MockUtils;
import com.chua.utils.tools.logger.LogUtils;

import java.util.Map;

/**
 * 测试数据工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/26
 */
public class MockUtilsExample extends BaseExample {

    public static void main(String[] args) {
        //生成对象数据
        testEntityData(TDemoInfoImpl.class);
        //生成对象数据
        testEntityData(Map.class);
        //生成对象集合数据
        testEntityListData(TDemoInfoImpl.class);
    }

    private static <T> void testEntityListData(Class<T> tClass) {
        LogUtils.info("{}", MockUtils.createForList(tClass));
    }

    private static <T> void testEntityData(Class<T> tClass) {
        LogUtils.info("{}", MockUtils.create(tClass));
    }
}
