package com.chua.utils.tools.classes;

import com.chua.utils.tools.entity.FieldInfoProperties;
import com.chua.utils.tools.entity.MethodInfoProperties;
import javassist.NotFoundException;

/**
 * 可被修改类解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
public interface ModifiableClassResolver {

    /**
     * 重置
     *
     * @throws NotFoundException NotFoundException
     */
    void reset() throws NotFoundException;

    /**
     * 添加方法
     *
     * @param methodFunction 添加方法
     * @return ModifiableClassResolver
     * @throws Exception Exception
     */
    ModifiableClassResolver addMethod(String methodFunction) throws Exception;

    /**
     * 添加方法
     *
     * @param methodInfoProperties 添加方法
     * @return ModifiableClassResolver
     * @throws Exception Exception
     */
    ModifiableClassResolver addMethod(MethodInfoProperties methodInfoProperties) throws Exception;

    /**
     * 添加字段
     *
     * @param fieldFunction 添加字段
     * @return ModifiableClassResolver
     * @throws Exception Exception
     */
    ModifiableClassResolver addField(String fieldFunction) throws Exception;

    /**
     * 添加字段
     *
     * @param fieldInfoProperties 添加字段
     * @return ModifiableClassResolver
     * @throws Exception Exception
     */
    ModifiableClassResolver addField(FieldInfoProperties fieldInfoProperties) throws Exception;

    /**
     * 添加构造
     *
     * @param parameters 添加构造
     * @return ModifiableClassResolver
     * @throws Exception Exception
     */
    ModifiableClassResolver addConstruct(Class<?>[] parameters) throws Exception;

    /**
     * 添加接口
     *
     * @param interfaceNames 添加接口
     * @return ModifiableClassResolver
     */
    ModifiableClassResolver addInterface(String... interfaceNames);

    /**
     * 添加父类
     *
     * @param superClass 父类
     * @return ModifiableClassResolver
     * @throws Exception Exception
     */
    ModifiableClassResolver setSuperClass(String superClass) throws Exception;

    /**
     * 获取类
     *
     * @return 类
     * @throws Exception Exception
     */
    Class<?> toClass() throws Exception;

    /**
     * 获取对象
     *
     * @return 对象
     * @throws Exception Exception
     */
    Object toObject() throws Exception;
}
