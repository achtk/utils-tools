package com.chua.utils.http.okhttp.downloader;

import okhttp3.Response;

/**
 * http下载器
 * @author CH
 */
public class HttpDownloader implements IHttpDownloader {

    @Override
    public void throwable(Throwable throwable) {

    }

    @Override
    public void download(Response response) {
        System.out.println();
    }
}
