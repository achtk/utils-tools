package com.chua.utils.http.httpclient.client;

import com.chua.utils.http.config.ClientConfig;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * 自定义HttpClient
 */
public class CustomizerClientFactory {

    /**
     *
     * @return
     */
    public static CloseableHttpClient customizer(final ClientConfig clientConfig) {
        if(null == clientConfig) {
            return HttpClients.createDefault();
        }
        HttpClientBuilder custom = HttpClients.custom();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(clientConfig.getMaxTotal());
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(clientConfig.getMaxPerRoute());

        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if(executionCount > clientConfig.getRetry()) {
                    return false;
                }
                return true;
            }
        };

        custom.setConnectionManager(poolingHttpClientConnectionManager);
        custom.setRetryHandler(httpRequestRetryHandler);

        return custom.build();
    }
}
