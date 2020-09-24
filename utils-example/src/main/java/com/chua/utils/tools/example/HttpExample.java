package com.chua.utils.tools.example;

import com.chua.utils.http.entity.HttpClientResponse;
import com.chua.utils.http.http.IHttpClientFactory;
import com.chua.utils.http.stream.AbstractHttpClientStream;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import org.testng.annotations.Test;

/**
 * @author CH
 */
public class HttpExample {

    @Test
    public void testNewGet() {
        ExtensionLoader<IHttpClientFactory> extensionLoader = ExtensionFactory.getExtensionLoader(IHttpClientFactory.class);
        IHttpClientFactory httpClientFactory = extensionLoader.getExtension("httpclient");
        AbstractHttpClientStream httpClientStream = httpClientFactory.newGet();
        HttpClientResponse httpClientResponse = httpClientStream.url("https://blog.csdn.net/wz6178/article/details/103721735").readTimeout(1).https().build().execute();
        System.out.println(httpClientResponse.getContent());
    }
}
