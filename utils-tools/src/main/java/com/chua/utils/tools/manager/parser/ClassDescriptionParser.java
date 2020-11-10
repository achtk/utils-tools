package com.chua.utils.tools.manager.parser;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.manager.parser.description.FieldDescription;
import com.chua.utils.tools.manager.parser.description.MethodDescription;
import com.google.common.base.Strings;

import java.util.Collection;
import java.util.List;

/**
 * 类描述解析器
 *
 * @param <T> 解析的类
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public interface ClassDescriptionParser<T> {
    /**
     * 字段描述
     *
     * @return 字段描述
     */
    Collection<FieldDescription<T>> fieldDescription();

    /**
     * 方法描述
     *
     * @return 方法描述
     */
    Collection<MethodDescription<T>> methodDescription();

    /**
     * 接口描述
     *
     * @return 接口描述
     */
    Collection<Class<?>> interfaceDescription();

    /**
     * 父类描述
     *
     * @return 父类描述
     */
    Collection<Class<?>> superDescription();

    /**
     * 创建类描述修改修饰器
     *
     * @return 修饰器
     */
    ClassModifyDescriptionParser modify();

    /**
     * 查找方法描述
     *
     * @param filter 方法名称
     * @return 方法描述
     */
    default List<MethodDescription<T>> doWithMethodDescription(Filter<MethodDescription<T>> filter) {
        if (null == filter) {
            return null;
        }

        return CollectionHelper.doWithFilter(methodDescription(), filter);
    }

    /**
     * 查找方法描述
     *
     * @param methodName 方法名称
     * @return 方法描述
     */
    default MethodDescription<T> findMethodDescription(String methodName) {
        if (Strings.isNullOrEmpty(methodName)) {
            return null;
        }

        return CollectionHelper.getIfOnly(doWithMethodDescription(item -> {
            return methodName.equals(item.getName());
        }));
    }

    /**
     * 查找字段描述
     *
     * @param fieldName 字段名称
     * @return 字段描述
     */
    default FieldDescription<T> findFieldDescription(String fieldName) {
        if (Strings.isNullOrEmpty(fieldName)) {
            return null;
        }

        return CollectionHelper.getIfOnly(doWithFieldDescription(findFieldDescription -> {
            return fieldName.equals(findFieldDescription.getName());
        }));
    }

    /**
     * 查找字段描述
     *
     * @param filter 过滤器
     * @return 字段描述
     */
    default List<FieldDescription<T>> doWithFieldDescription(Filter<FieldDescription<T>> filter) {
        if (null == filter) {
            return null;
        }

        return CollectionHelper.doWithFilter(fieldDescription(), filter);
    }

    /**
     * 是否是{interfaceName} 接口
     *
     * @param interfaceName 接口名称
     * @return 是查询接口的子类返回true, 如果无法解析参数返回false
     */
    boolean isSubType(String interfaceName);
}
