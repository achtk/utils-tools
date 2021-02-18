package com.chua.utils.tools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 返回对象
 *
 * @author CH
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ReturnBeanBuilder<T> implements Serializable {
    private static final ReturnBean NONE_RETURN_BEAN = builder().code(200).build();

    /**
     * @return
     */
    public static <T> Builder<T> builder() {
        return new ReturnBeanBuilder.Builder<T>();
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ReturnBean<T> noneBean() {
        return NONE_RETURN_BEAN;
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ReturnBean<T> okReturnBean(final T data) {
        return (ReturnBean<T>) builder().code(200).data(data).build();
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ReturnBean<T> badRequestReturnBean(final String message) {
        return (ReturnBean<T>) builder().code(400).message(message).build();
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ReturnBean<T> tokenFailedReturnBean() {
        return (ReturnBean<T>) builder().code(426).message("Failed to apply for token").build();
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ReturnBean<T> unauthorizedReturnBean() {
        return (ReturnBean<T>) builder().code(401).message("Unauthorized").build();
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ReturnBean<T> throwableReturnBean() {
        return (ReturnBean<T>) builder().code(500).build();
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ReturnBean<T> throwableReturnBean(final String message) {
        return (ReturnBean<T>) builder().code(500).message(message).build();
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ReturnBean<T> throwableReturnBean(final Throwable throwable) {
        return throwableReturnBean(null, throwable);
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ReturnBean<T> throwableReturnBean(final T data, final Throwable throwable) {
        return (ReturnBean<T>) builder().code(500).data(data).message(null == throwable ? "" : throwable.getMessage()).build();
    }

    public static ReturnBean<Boolean> falseReturnBean(int code) {
        Builder<Boolean> objectBuilder = builder();
        return objectBuilder.code(code).data(false).build();
    }

    public static ReturnBean<Boolean> trueReturnBean(int code) {
        Builder<Boolean> objectBuilder = builder();
        return objectBuilder.code(code).data(true).build();
    }

    @Setter
    @Accessors(fluent = true)
    private static class Builder<T> {

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


        public ReturnBean<T> build() {
            ReturnBean<T> returnBean = new ReturnBean();
            returnBean.setCode(code);
            returnBean.setData(data);
            returnBean.setMessage(message);
            return returnBean;
        }

    }
}
