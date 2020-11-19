package com.chua.utils.tools.classes.entity;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 字段描述
 * @author CH
 * @version 1.0.0
 * @since 2020/11/18
 */
@Data
public class FieldDescription {
    /**
     * 字段
     */
    private Field field;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private Class<?> type;
    /**
     * 修饰
     */
    private String modifier;
    /**
     * 注解
     */
    private List<String> annotations;

}
