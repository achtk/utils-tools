package com.chua.utils.tools.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 接口信息
 *
 * @author CH
 * @date 2020-09-26
 */
@Getter
@Setter
public class InterfaceInfoProperties {
    /**
     * 接口列表
     */
    private Set<Class> classSet;
}
