package com.chua.utils.netx.elasticsearch.action;

/**
 * @author CH
 */
public interface CallBack<T> {
    /**
     * 成功回调
     * @param t
     */
    public void onResponse(T t);

    /**
     * 异常回调
     * @param throwable
     */
    public void failture(Throwable throwable);
}
