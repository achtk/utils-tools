package com.chua.utils.tools.http.callback;


import com.chua.utils.tools.http.entity.ResponseEntity;

/**
 * 异步回调
 *
 * @author admin
 * @updateTime 2020/5/30 23:31
 */
public interface ResponseCallback {
    /**
     * 异常
     *
     * @param e 异常
     */
    void onFailure(Throwable e);

    /**
     * 成功
     *
     * @param response 结果
     */
    void onResponse(ResponseEntity response);
}
