package com.chua.utils.tools.manager;

import com.chua.utils.tools.classes.reflections.ReflectionsFactory;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.manager.parser.ClassDescriptionParser;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * 对象管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/27
 */
public interface ContextManager {
    /**
     * 获取消息总线
     *
     * @return EventBusContextManager
     */
    EventBusContextManager createEventBusContextManager();

    /**
     * 获取对象管理器
     *
     * @param scanner 自动扫描
     * @return EventBusContextManager
     */
    ObjectContextManager createObjectContextManager(boolean scanner);

    /**
     * 获取策略管理器
     *
     * @return StrategyContextManager
     */
    StrategyContextManager createStrategyContextManager();

    /**
     * 配置文件适配管理器
     *
     * @param extensionProcessor 处理器
     * @return 配置文件适配管理器
     */
    ProfileAdaptorManager createProfileAdaptorManager(ExtensionProcessor extensionProcessor);

    /**
     * 配置文件适配管理器
     *
     * @return 配置文件适配管理器
     */
    default ProfileAdaptorManager createProfileAdaptorManager() {
        return createProfileAdaptorManager(null);
    }

    /**
     * 获取对象管理器
     *
     * @return EventBusContextManager
     */
    default ObjectContextManager createObjectContextManager() {
        return createObjectContextManager(true);
    }

    /**
     * 获取管理器
     *
     * @param managerClass 管理器类型
     * @param <Manager>    管理器
     * @return List
     */
    <Manager> List<Manager> createContextManager(Class<Manager> managerClass);

    /**
     * 添加管理器
     *
     * @param managerClass 管理器类型
     * @param <Manager>    管理器
     * @return Manager 成功返回添加的管理器, 反之返回null
     */
    <Manager> Manager add(Manager managerClass);

    /**
     * 创建类描述解析器
     * @return 类描述解析器
     */
    <T>ClassDescriptionParser<T> createClassDescriptionParser(Class<T> tClass);
}
