package com.chua.tools.example.utils;

import com.chua.tools.example.config.MockConfig;
import com.github.jsonzou.jmockdata.JMockData;

/**
 * 模拟数据
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/29
 */
public class MockUtils {
    /**
     * 生成对象数据
     *
     * @param tClass 类
     * @param <T>    类型
     * @return 对象
     */
    public static <T> T create(final Class<T> tClass) {
        try {
            return JMockData.mock(tClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成对象数据
     *
     * @param tClass 类
     * @param <T>    类型
     * @return 对象
     */
    public static <T> T create(final Class<T> tClass, final MockConfig mockConfig) {
        try {
            return JMockData.mock(tClass, mockConfig);
        } catch (Exception e) {
            return null;
        }
    }
}
