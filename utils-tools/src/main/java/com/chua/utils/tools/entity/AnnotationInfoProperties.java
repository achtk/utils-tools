package com.chua.utils.tools.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 注解信息
 * @author CH
 * @date 2020-09-26
 */
@Getter
@Setter
@AllArgsConstructor
public class AnnotationInfoProperties {
    /**
     * 注解名称
     */
    private String name;
    /**
     * 注解类型
     * <ul>
     *     <li>0: 类</li>
     *     <li>1: 字段</li>
     *     <li>2: 方法</li>
     * </ul>
     */
    private Integer type;
    /**
     * 归属
     */
    private String access;
}
