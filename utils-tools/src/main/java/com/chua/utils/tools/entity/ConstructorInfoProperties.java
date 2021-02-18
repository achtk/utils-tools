package com.chua.utils.tools.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 构造信息
 *
 * @author CH
 * @date 2020-09-26
 */
@Getter
@Setter
@EqualsAndHashCode
public class ConstructorInfoProperties {
    /**
     * 参数类型
     */
    private Class[] parameterType;
}
