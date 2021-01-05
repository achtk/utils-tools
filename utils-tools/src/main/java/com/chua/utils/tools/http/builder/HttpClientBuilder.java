package com.chua.utils.tools.http.builder;


import com.chua.utils.tools.http.callback.ResponseCallback;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.chua.utils.tools.util.JsonUtils;

/**
 * Client构造
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:20
 */
public interface HttpClientBuilder {

    /**
     * 执行
     *
     * @param tClass 类型
     * @param <T>    类型
     * @return
     */
    <T> ResponseEntity<T> execute(Class<T> tClass);

    /**
     * 执行
     *
     * @return
     */
    default ResponseEntity execute() {
        return execute(String.class);
    }

    /**
     * 执行
     *
     * @param tClass           类型
     * @param <T>              类型
     * @param responseCallback 回调
     */
    <T> void execute(ResponseCallback responseCallback, Class<T> tClass);

    /**
     * 执行
     *
     * @param responseCallback 回调
     */
    default void execute(ResponseCallback responseCallback) {

    }

    /**
     * 创建响应
     *
     * @param responseEntity 响应
     * @param tClass         类型
     * @param <T>            类型
     * @return 创建
     */
    default <T> ResponseEntity<T> createResponseEntity(ResponseEntity<T> responseEntity, Class<T> tClass) {
        if (null == responseEntity) {
            return null;
        }

        Object content = responseEntity.getContent();

        if (String.class.isAssignableFrom(tClass) && content instanceof String) {
            return responseEntity;
        } else {
            String json = JsonUtils.toJson(content);
            T fromJson = JsonUtils.fromJson(json, tClass);
            responseEntity.setContent(fromJson);
            return responseEntity;
        }
    }

    /**
     * 创建响应
     *
     * @param callback 响应回调
     * @param <T>      类型
     * @param tClass   类型
     * @return 创建
     */
    default <T> ResponseCallback createCallback(ResponseCallback callback, Class<T> tClass) {
        if (null == callback) {
            return null;
        }

        return new ResponseCallback() {

            @Override
            public void onFailure(Throwable e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(ResponseEntity response) {
                ResponseEntity responseEntity = createResponseEntity(response, tClass);
                callback.onResponse(response);
            }
        };
    }
}
