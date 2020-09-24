package com.chua.utils.http.callback;


import com.chua.utils.http.entity.HttpClientResponse;

/**
 *  异步回调
 * @author admin 
 * @updateTime 2020/5/30 23:31
 * @throws 
 */
public interface Callback {
    /**
     *异常
     * @param e 异常
     */
    public void onFailure(Throwable e);
    /**
     * 成功
     * @param response 结果
     */
    public void onResponse(HttpClientResponse response);
}
