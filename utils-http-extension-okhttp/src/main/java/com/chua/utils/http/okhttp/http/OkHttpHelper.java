package com.chua.utils.http.okhttp.http;


import com.chua.utils.http.okhttp.enums.HttpMethod;
import com.chua.utils.http.okhttp.stream.OkAbstractHttpStream;

/**
 * okhttp 工具类
 * @author CHTK
 */
public class OkHttpHelper {


    /**
     * get
     * @return
     */
    public static OkAbstractHttpStream newGet() {
        return new OkAbstractHttpStream(HttpMethod.GET);
    }
    /**
     * get
     * @return
     */
    public static OkAbstractHttpStream newPost() {
        return new OkAbstractHttpStream(HttpMethod.POST);
    }

    /**
     * put
     * @return
     */
    public static OkAbstractHttpStream newPut() {
        return new OkAbstractHttpStream(HttpMethod.PUT);
    }
    
    /**
     * delete
     * @return
     */
    public static OkAbstractHttpStream newDelete() {
        return new OkAbstractHttpStream(HttpMethod.DELETE);
    }

    /**
     * 下载器
     * @return
     */
    public static OkAbstractHttpStream newDownloader() {
        return new OkAbstractHttpStream(HttpMethod.DOWNLOADER);
    }

}
