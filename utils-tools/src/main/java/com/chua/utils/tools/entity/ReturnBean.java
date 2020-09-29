package com.chua.utils.tools.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 返回对象
 * @author CH
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReturnBean<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;
    /**
     * 数据
     */
    private T data;
    /**
     * 提示信息
     */
    private String message;
    /**
     * timestamp
     */
    private long timestamp = System.currentTimeMillis();

}
