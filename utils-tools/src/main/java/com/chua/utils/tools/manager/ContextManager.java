package com.chua.utils.tools.manager;

import com.chua.utils.tools.data.factory.DataFactory;
import com.chua.utils.tools.data.factory.StandardDataFactory;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.chua.utils.tools.manager.parser.ClassDescriptionParser;
import com.chua.utils.tools.manager.template.HttpTemplate;
import com.chua.utils.tools.manager.template.MbeanTemplate;
import com.chua.utils.tools.resource.factory.FastResourceFactory;
import com.chua.utils.tools.resource.factory.ResourceFactory;
import com.chua.utils.tools.resource.template.ResourceTemplate;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;
import com.chua.utils.tools.table.TableFactory;
import com.chua.utils.tools.util.ClassUtils;

import java.util.List;

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
     *
     * @param tClass 类
     * @return 类描述解析器
     */
    <T> ClassDescriptionParser<T> createClassDescriptionParser(Class<T> tClass);

    /**
     * 获取资源查找器
     *
     * @param resourceFactory 资源查找工厂
     * @return 资源查找器
     */
    ResourceTemplate createResourceTemplate(ResourceFactory resourceFactory);

    /**
     * 获取资源查找器
     *
     * @return 资源查找器
     */
    default ResourceTemplate createResourceTemplate() {
        return createResourceTemplate(new FastResourceFactory());
    }

    /**
     * 创建Mbean模板
     *
     * @return MBeanTemplate
     * @see MbeanTemplate
     */
    default MbeanTemplate createMbeanTemplate() {
        return new MbeanTemplate();
    }

    /**
     * 创建Http模板
     *
     * @return HttpTemplate
     * @see HttpTemplate
     */
    default HttpTemplate createHttpTemplate() {
        return new HttpTemplate();
    }

    /**
     * 创建类型转化器
     *
     * @param aClass 待转化的类
     * @return
     */
    default <T> TypeConverter<T> createTypeConverter(Class<T> aClass) {
        return EmptyOrBase.getTypeConverter(aClass);
    }

    /**
     * 数据工厂
     *
     * @return 数据工厂
     */
    default DataFactory createDataFactory() {
        return new StandardDataFactory();
    }

    /**
     * 数据表
     *
     * @return 数据表
     */
    default TableFactory createTableFactory() {
        String tableFactory = "com.chua.utils.netx.flink.table.Tables";
        if (ClassUtils.isPresent(tableFactory)) {
            return ClassUtils.forObject(tableFactory, TableFactory.class);
        }
        try {
            throw new ClassNotFoundException(tableFactory);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
