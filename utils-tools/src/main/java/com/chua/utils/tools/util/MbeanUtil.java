package com.chua.utils.tools.util;

import com.chua.utils.tools.manager.ContextManager;
import com.chua.utils.tools.manager.producer.StandardContextManager;
import com.chua.utils.tools.manager.template.MbeanTemplate;

/**
 * MBean工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class MbeanUtil {

    private static final ContextManager MANAGER = new StandardContextManager();
    private static final MbeanTemplate M_BEAN_TEMPLATE = MANAGER.createMbeanTemplate();

    private static final String M_BEAN = "MBean";

    /**
     * 注册MBean
     *
     * @param t   对象
     * @param <T> 类
     */
    public static <T> void registerMbean(T t) throws Exception {
        M_BEAN_TEMPLATE.register(t);
    }

    /**
     * 注销MBean
     *
     * @param t   对象
     * @param <T> 类
     */
    public static <T> void unregister(T t) throws Exception {
        M_BEAN_TEMPLATE.unregister(t);
    }

}
