package com.chua.utils.tools.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 信息
 * @author CH
 * @date 2020-09-26
 */
@Getter
@Setter
public class ClassInfoProperties {
    /**
     * 属性信息
     */
    private Set<FieldInfoProperties> fieldInfoProperties;
    /**
     * 方法信息
     */
    private Set<MethodInfoProperties> methodInfoProperties;
    /**
     * 构造信息
     */
    private Set<ConstructorInfoProperties> constructorInfoProperties;
    /**
     * 接口信息
     */
    private InterfaceInfoProperties interfaceInfoProperties;
}
