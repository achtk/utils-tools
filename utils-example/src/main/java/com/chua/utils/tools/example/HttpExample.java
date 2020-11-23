package com.chua.utils.tools.example;

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
        HttpClientFactory httpClientFactory = extensionLoader.getExtension("httpclient");
        HttpClientStream httpClientStream = httpClientFactory.newGet();
        ResponseEntity responseEntity = httpClientStream.url("https://blog.csdn.net/wz6178/article/details/103721735").readTimeout(1).https().build().execute();
        System.out.println(responseEntity.getContent());
    }
}
