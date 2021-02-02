package com.chua.tools.example.utils;

import com.chua.tools.example.config.MockConfig;
import com.chua.utils.tools.random.RandomUtil;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.CollectionUtils;
import com.github.jsonzou.jmockdata.JMockData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if (Map.class.isAssignableFrom(tClass)) {
                int anInt = RandomUtil.randomInt(20);
                Map<String, Object> map = new HashMap<>(anInt);
                for (int i = 0; i < anInt; i++) {
                    map.put(JMockData.mock(String.class), createRandom());
                }
                return (T) map;
            }

            if (List.class.isAssignableFrom(tClass)) {
                int anInt = RandomUtil.randomInt(20);
                List<Object> result = new ArrayList<>(anInt);
                for (int i = 0; i < anInt; i++) {
                    result.add(createRandom());
                }
                return (T) result;
            }
            return JMockData.mock(tClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 随机数据
     *
     * @return 数据
     */
    private static Object createRandom() {
        Class<?> random = CollectionUtils.getRandom(ClassUtils.PRIMITIVE_WRAPPER_MAP.values());
        if (Void.TYPE.isAssignableFrom(random)) {
            return null;
        }
        return JMockData.mock(random);
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

    /**
     * 生成对象数据
     *
     * @param tClass 类
     * @param <T>    类型
     * @return 对象
     */
    public static <T> List<T> createForList(final Class<T> tClass) {
        int randomInt = RandomUtil.randomInt(100);
        List<T> result = new ArrayList<>(randomInt);
        for (int i = 0; i < randomInt; i++) {
            try {
                result.add(create(tClass));
            } catch (Exception e) {
                return null;
            }
        }
        return result;

    }
}
