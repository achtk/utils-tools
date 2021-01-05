package com.chua.utils.http.okhttp.http;


import com.chua.utils.http.okhttp.stream.OkHttpStream;

import static com.chua.utils.tools.constant.HttpConstant.*;

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
        return new OkHttpStream(HTTP_METHOD_GET);
    }
    /**
     * get
     * @return
     */
    public static OkHttpStream newPost() {
        return new OkHttpStream(HTTP_METHOD_POST);
    }

    /**
     * put
     * @return
     */
    public static OkHttpStream newPut() {
        return new OkHttpStream(HTTP_METHOD_PUT);
    }
    
    /**
     * delete
     * @return
     */
    public static OkHttpStream newDelete() {
        return new OkHttpStream(HTTP_METHOD_DELETE);
    }

}
