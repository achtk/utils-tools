package com.chua.utils.tools.example;

import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.callback.ResponseCallback;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.chua.utils.tools.http.http.HttpClientFactory;
import com.chua.utils.tools.http.stream.HttpClientStream;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.factory.ExtensionFactory;

/**
 * @author CH
 */
public class HttpExample {

    public static void main(String[] args) {
        ExtensionLoader<HttpClientFactory> extensionLoader = ExtensionFactory.getExtensionLoader(HttpClientFactory.class);
        HttpClientFactory httpClientFactory = extensionLoader.getExtension("flux");
        HttpClientStream httpClientStream = httpClientFactory.newGet();
        HttpClientBuilder httpClientBuilder = httpClientStream.url("http://127.0.0.1:7200/v2/api-docs").build();
        //ResponseEntity responseEntity = httpClientBuilder.execute();
        httpClientBuilder.execute(new ResponseCallback() {
            @Override
            public void onFailure(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(ResponseEntity response) {
                System.out.println(response);
            }
        });
       // System.out.println(responseEntity.getContent());
    }
}
