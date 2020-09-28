package com.chua.utils.tools.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 返回对象
 * @author CH
 */
@Data
@Builder
@EqualsAndHashCode
public class ReturnBean<T> {

    private static final ReturnBean NONE_RETURN_BEAN = ReturnBean.builder().code(200).build();
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
     *
     * @param <T>
     * @return
     */
    public static <T>ReturnBean<T> noneBean() {
        return NONE_RETURN_BEAN;
    }
    /**
     *
     * @param <T>
     * @return
     */
    public static <T>ReturnBean<T> okReturnBean(final T data) {
        return (ReturnBean<T>) ReturnBean.builder().code(200).data(data).build();
    }

    /**
     *
     * @param <T>
     * @return
     */
    public static <T>ReturnBean<T> badRequestReturnBean(final String message) {
        return (ReturnBean<T>) ReturnBean.builder().code(400).message(message).build();
    }
    /**
     *
     * @param <T>
     * @return
     */
    public static <T>ReturnBean<T> tokenFailedReturnBean() {
        return (ReturnBean<T>) ReturnBean.builder().code(426).message("Failed to apply for token").build();
    }
    /**
     *
     * @param <T>
     * @return
     */
    public static <T>ReturnBean<T> unauthorizedReturnBean() {
        return (ReturnBean<T>) ReturnBean.builder().code(401).message("Unauthorized").build();
    }
    /**
     *
     * @param <T>
     * @return
     */
    public static <T>ReturnBean<T> throwableReturnBean() {
        return (ReturnBean<T>) ReturnBean.builder().code(500).build();
    }
    /**
     *
     * @param <T>
     * @return
     */
    public static <T>ReturnBean<T> throwableReturnBean(final String message) {
        return (ReturnBean<T>) ReturnBean.builder().code(500).message(message).build();
    }
    /**
     *
     * @param <T>
     * @return
     */
    public static <T>ReturnBean<T> throwableReturnBean(final Throwable throwable) {
        return throwableReturnBean(null, throwable);
    }
    /**
     *
     * @param <T>
     * @return
     */
    public static <T>ReturnBean<T> throwableReturnBean(final T data, final Throwable throwable) {
        return (ReturnBean<T>) ReturnBean.builder().code(500).data(data).message(null == throwable ? "" : throwable.getMessage()).build();
    }
}
