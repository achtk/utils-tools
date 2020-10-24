package com.chua.utils.tools.classes;

import com.chua.utils.tools.entity.FieldInfoProperties;
import com.chua.utils.tools.entity.MethodInfoProperties;
import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * 可被修改类解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
public interface ModifiableClassResolver<T> {

    /**
     * 重置
     */
    void reset() throws NotFoundException;

    /**
     * 添加方法
     *
     * @param methodFunction 添加方法
     */
    ModifiableClassResolver addMethod(String methodFunction) throws Exception;
    /**
     * 添加方法
     *
     * @param methodInfoProperties 添加方法
     */
    ModifiableClassResolver addMethod(MethodInfoProperties methodInfoProperties) throws Exception;

    /**
     * 添加字段
     *
     * @param fieldFunction 添加字段
     */
    ModifiableClassResolver addField(String fieldFunction) throws Exception;
    /**
     * 添加字段
     *
     * @param fieldInfoProperties 添加字段
     */
    ModifiableClassResolver addField(FieldInfoProperties fieldInfoProperties) throws Exception;

    /**
     * 添加构造
     *
     * @param parameters 添加构造
     */
    ModifiableClassResolver addConstruct(Class<?>[] parameters) throws Exception;

    /**
     *
     * 添加接口
     *
     * @param interfaceNames 添加接口
     */
    ModifiableClassResolver addInterface(String... interfaceNames);

    /**
     * 添加父类
     *
     * @param superClass 父类
     */
    ModifiableClassResolver setSuperClass(String superClass) throws Exception;

    /**
     * 获取类
     * @return
     */
    Class<?> toClass() throws Exception;
    /**
     * 获取类
     * @return
     */
    Object toObject() throws Exception;
}
