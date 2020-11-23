package com.chua.utils.http.okhttp.http;


import com.chua.utils.http.okhttp.enums.HttpMethod;
import com.chua.utils.http.okhttp.stream.OkHttpStream;

/**
 * okhttp 工具类
 * @author CHTK
 */
public class OkHttpHelper {


    /**
     * get
     * @return
     */
    public static OkHttpStream newGet() {
        return new OkHttpStream(HttpMethod.GET);
    }
    /**
     * get
     * @return
     */
    public static OkHttpStream newPost() {
        return new OkHttpStream(HttpMethod.POST);
    }

    /**
     * put
     * @return
     */
    public static OkHttpStream newPut() {
        return new OkHttpStream(HttpMethod.PUT);
    }
    
    /**
     * delete
     * @return
     */
    public static OkHttpStream newDelete() {
        return new OkHttpStream(HttpMethod.DELETE);
    }

    /**
     * 下载器
     * @return
     */
    public static OkHttpStream newDownloader() {
        return new OkHttpStream(HttpMethod.DOWNLOADER);
    }

}
