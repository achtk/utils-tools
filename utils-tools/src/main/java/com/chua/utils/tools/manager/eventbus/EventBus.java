package com.chua.utils.tools.manager.eventbus;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.bean.part.BeanPart;
import com.chua.utils.tools.logger.LogUtils;
import com.chua.utils.tools.manager.EventMessage;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.JsonUtils;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息总线
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
public interface EventBus {
    /**
     * 注册组件
     *
     * @param object 组件
     */
    void register(Object object);

    /**
     * 注销组件
     *
     * @param object 组件
     */
    void unregister(Object object);

    /**
     * 发布消息
     *
     * @param event 消息
     */
    void post(Object event);

    /**
     * 解析对象
     *
     * @param obj 对象
     * @return 解析集合
     */
    default BusEntity createMethodMap(Object obj) {
        List<Method> byAnnotation = BeanPart.of(obj).getMethodsByAnnotation(Subscribe.class);
        Map<Class<?>, Method> methodMap = new HashMap<>(byAnnotation.size());
        for (Method method : byAnnotation) {
            int parameterCount = method.getParameterCount();
            if (1 == parameterCount) {
                methodMap.put(method.getParameterTypes()[0], method);
            }
        }
        BusEntity busEntity = new BusEntity();
        busEntity.setEntity(obj);
        busEntity.setName(obj.getClass().getName());
        busEntity.setMethods(methodMap);
        return busEntity;
    }

    /**
     * 获取所有消息实体
     *
     * @return 消息实体
     */
    List<BusEntity> getBus();

    /**
     * 订阅
     *
     * @param busName     消息总线名称
     * @param jsonMessage json消息
     */
    default void subscription(String busName, String jsonMessage) {
        if(null == jsonMessage) {
            return;
        }
        jsonMessage = jsonMessage.replace("\\", "");
        if (jsonMessage.startsWith("\"")) {
            jsonMessage = jsonMessage.substring(1, jsonMessage.length() - 1);
        }
        EventMessage eventMessage = JsonUtils.fromJson(jsonMessage, EventMessage.class);
        for (BusEntity entity : getBus()) {
            sendInformation(entity.getEntity(), entity.getMethods(), eventMessage);
        }
    }

    /**
     * 下发信息
     *
     * @param subscribe    订阅者
     * @param methods      方法集
     * @param eventMessage 消息
     */
    default void sendInformation(Object subscribe, Map<Class<?>, Method> methods, EventMessage eventMessage) {
        String messageType = eventMessage.getType();
        if (null == messageType) {
            LogUtils.warn("消息类型无法解析");
            return;
        }
        Class<?> aClass = ClassUtils.forName(messageType);
        Object message = eventMessage.getMessage();
        if (null == aClass) {
            if (LogUtils.isDebugEnabled()) {
                LogUtils.warn("消息类型: {}, 无法解析", messageType);
            }
            return;
        }

        AtomicInteger atomicInteger = new AtomicInteger();

        for (Map.Entry<Class<?>, Method> entry : methods.entrySet()) {
            Class<?> type = entry.getKey();
            Method method = entry.getValue();
            if (type.isAssignableFrom(aClass)) {
                atomicInteger.incrementAndGet();
                invokeMethod(subscribe, method, BeanCopy.of(aClass).with(message).create());
            }
        }
    }

    /**
     * 调用方法
     *
     * @param subscribe 订阅者
     * @param method    方法
     * @param message   消息
     */
    default void invokeMethod(Object subscribe, Method method, Object message) {
        method.setAccessible(true);
        try {
            method.invoke(subscribe, message);
        } catch (Exception e) {
            if (LogUtils.isDebugEnabled()) {
                LogUtils.debug("消息下发失败");
            }
        }
    }

    /**
     * 实体存储对象
     */
    @Getter
    @Setter
    final class BusEntity {

        private String name;
        private Object entity;
        private Map<Class<?>, Method> methods;
    }
}
