package com.chua.utils.http.okhttp.stream;

import com.chua.utils.http.okhttp.enums.HttpMethod;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.stream.HttpClientStream;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;

import java.util.Map;

/**
 * okhttp流式
 *
 * @author CHTK
 */
public class OkHttpStream extends HttpClientStream {

    private FormBody.Builder formBody = new FormBody.Builder();

    private HttpMethod method;

    private Interceptor interceptor;


    public OkHttpStream(String method) {
        super(method);
    }

    /**
     * 构建
     *
     * @return
     */
    @Override
    public OkHttpStreamBuilder build() {
        RequestConfig requestConfig = getRequestConfig();
        Request.Builder request = new Request.Builder().url(requestConfig.getUrl());
        this.method = HttpMethod.valueOf(requestConfig.getMethod());

        Map<String, Object> body = requestConfig.getBody();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            this.formBody.add(entry.getKey(), entry.getValue() + "");
        }

        return new OkHttpStreamBuilder(method, requestConfig, formBody, null, interceptor, request);
    }
}
