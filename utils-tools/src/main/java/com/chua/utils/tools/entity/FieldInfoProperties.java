package com.chua.utils.tools.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 属性信息
 * @author CH
 * @date 2020-09-26
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class FieldInfoProperties {
    /**
     * 方法名称
     */
    private String name;
    /**
     * 类型
     */
    private String returnType;
    /**
     * 修饰符
     */
    private String modifier;
    /**
     * 内容
     */
    private String content;
    /**
     * 注解
     */
    private Set<String> annotations;
}
