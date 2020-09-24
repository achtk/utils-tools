package com.chua.utils.http.okhttp.downloader;

import okhttp3.Response;

/**
 * http下载器
 * @author CH
 */
public interface IHttpDownloader {
    /**
     * 异常情况
     * @param throwable 异常
     */
    public void throwable(Throwable throwable);

    /**
     * 下载器
     * @param response
     */
    public void download(Response response);
}
