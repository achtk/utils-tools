package com.chua.utils.http.okhttp.stream;

import com.chua.utils.http.meta.MetaType;
import com.chua.utils.http.okhttp.enums.HttpMethod;
import com.chua.utils.http.stream.AbstractHttpClientStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;

import java.util.Map;

/**
 * okhttp流式
 *
 * @author CHTK
 */
public class OkAbstractHttpStream extends AbstractHttpClientStream {

    private FormBody.Builder formBody = new FormBody.Builder();

    private RequestBody requestBody;

    private HttpMethod method;

    private Interceptor interceptor;

    public OkAbstractHttpStream(HttpMethod method) {
        this.method = method;
        requestConfig.setMethod(method.name());
    }


    /**
     * 添加消息内容
     *
     * @param bodyName
     * @param bodyValue
     * @return
     */
    @Override
    public OkAbstractHttpStream addBody(final String bodyName, final Object bodyValue) {
        if (isNotBlank(bodyName)) {
            formBody.add(bodyName, (String) bodyValue);
        }
        return this;
    }

    /**
     * 添加消息内容
     *
     * @param params
     * @return
     */
    @Override
    public OkAbstractHttpStream setBody(Map<String, Object> params) {
        if (isNotBlank(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (!isNotBlank(entry.getKey())) {
                    continue;
                }
                formBody.add(entry.getKey(), (String) entry.getValue());
            }
        }
        return this;
    }

    /**
     * 添加消息内容
     *
     * @param json
     * @return
     */
    @Override
    public OkAbstractHttpStream addJson(final String json) {
        if (isNotBlank(json)) {
            requestBody = RequestBody.create(MediaType.parse(MetaType.APPLICATION_JSON.getValue()), json);
        }
        return this;
    }

    @Override
    public AbstractHttpClientStream addText(String json, MetaType metaType) {
        if (isNotBlank(json)) {
            requestBody = RequestBody.create(MediaType.parse(metaType.getValue()), json);
        }
        return this;
    }

    /**
     * 添加xml
     *
     * @param xml
     * @return
     */
    @Override
    public OkAbstractHttpStream addXml(String xml) {
        if (isNotBlank(xml)) {
            requestBody = RequestBody.create(MediaType.parse(MetaType.TEXT_XML.getValue()), xml);
        }
        return this;
    }

    /**
     * 添加html
     *
     * @param html
     * @return
     */
    @Override
    public OkAbstractHttpStream addHtml(String html) {
        if (isNotBlank(html)) {
            requestBody = RequestBody.create(MediaType.parse(MetaType.TEXT_HTML.getValue()), html);
        }
        return this;
    }

    /**
     * 添加javascript
     *
     * @param script
     * @return
     */
    @Override
    public OkAbstractHttpStream addScript(String script) {
        if (isNotBlank(script)) {
            requestBody = RequestBody.create(MediaType.parse(MetaType.APPLICATION_JAVASCRIPT.getValue()), script);
        }
        return this;
    }

    /**
     * 读取时间
     *
     * @param interceptor
     * @return
     */
    public OkAbstractHttpStream log(final Interceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    /**
     * 构建
     *
     * @return
     */
    @Override
    public OkHttpStreamBuilder build() {
        if (isNotBlank(requestConfig.getUrl())) {
            Request.Builder request = new Request.Builder().url(requestConfig.getUrl());
            if (!headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    if (!isNotBlank(entry.getKey())) {
                        continue;
                    }
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            return new OkHttpStreamBuilder(method, requestConfig, formBody, requestBody, interceptor, request);
        }
        return null;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Header {
        private String name;
        private String value;
    }
}
