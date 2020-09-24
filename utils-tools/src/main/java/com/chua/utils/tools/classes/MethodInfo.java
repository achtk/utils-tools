package com.chua.utils.tools.classes;

import lombok.Data;

/**
 * 方法信息
 * @author CH
 */
@Data
public class MethodInfo {
    /**
     * 方法名
     */
    private String name;
    /**
     * 返回类型
     */
    private Class returnType;
    /**
     * 参数类型
     */
    private Class[] parameterTypes;
}
