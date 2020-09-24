package com.chua.utils.http.httpclient.stream;

/**
 * httpclient
 * @author CHTK
 */
public class HttpClient {
    /**
     * get
     * @return
     */
    public static HttpClientStream newGet() {
        return new HttpClientStream("GET");
    }

    /**
     * post
     * @return
     */
    public static HttpClientStream newPost() {
        return new HttpClientStream("POST");
    }

    /**
     * delete
     * @return
     */
    public static HttpClientStream newDelete() {
        return new HttpClientStream("DELETE");
    }

    /**
     * put
     * @return
     */
    public static HttpClientStream newPut() {
        return new HttpClientStream("PUT");
    }
}
