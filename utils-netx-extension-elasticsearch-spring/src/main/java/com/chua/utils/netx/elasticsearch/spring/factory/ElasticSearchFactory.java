package com.chua.utils.netx.elasticsearch.spring.factory;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.CollectionHelper;
import com.chua.utils.tools.common.PropertiesHelper;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.util.CloseableIterator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.chua.unified.properties.NetxProperties.*;
import static com.chua.unified.properties.NetxProperties.CONFIG_FIELD_READ_TIMEOUT;

/**
 * Elasticsearch Factory
 *
 * @author CH
 */
@NoArgsConstructor
@RequiredArgsConstructor
public class ElasticSearchFactory implements INetxFactory<AbstractElasticsearchTemplate> {

    @NonNull
    private NetxProperties netxProperties;
    private AbstractElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public AbstractElasticsearchTemplate client() {
        return elasticsearchTemplate;
    }

    @Override
    public void start() {
        this.elasticsearchTemplate = getElasticsearchTemplate();
    }

    @Override
    public boolean isStart() {
        return null != elasticsearchTemplate;
    }

    @Override
    public void close() throws Exception {

    }

    /**
     * 获取 ElasticsearchTemplate
     *
     * @return
     */
    private AbstractElasticsearchTemplate getElasticsearchTemplate() {
        RestClientBuilder.RequestConfigCallback requestConfigCallback = getRequestConfigCallback();
        RestClientBuilder.HttpClientConfigCallback httpClientConfigCallback = getHttpClientConfigCallback();

        RestClientBuilder restClientBuilder = RestClient.builder(CollectionHelper.toArray(getHttpHost()));
        restClientBuilder.setRequestConfigCallback(requestConfigCallback);
        restClientBuilder.setHttpClientConfigCallback(httpClientConfigCallback);

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        return new ElasticsearchRestTemplate(restHighLevelClient);
    }

    /**
     * http配置
     *
     * @return
     */
    private RestClientBuilder.HttpClientConfigCallback getHttpClientConfigCallback() {
        return new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                //最大连接数
                if (BooleanHelper.isValid(netxProperties, CONFIG_FIELD_MAX_CONNECTION)) {
                    httpClientBuilder.setMaxConnTotal(PropertiesHelper.ints(netxProperties, CONFIG_FIELD_MAX_CONNECTION));
                }

                return httpClientBuilder;
            }
        };
    }

    /**
     * 请求配置
     *
     * @return
     */
    private RestClientBuilder.RequestConfigCallback getRequestConfigCallback() {
        return new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {

                //最大连接数
                if (BooleanHelper.isValid(netxProperties, CONFIG_FIELD_CONNECTION_TIMEOUT)) {
                    requestConfigBuilder.setConnectTimeout(PropertiesHelper.ints(netxProperties, CONFIG_FIELD_CONNECTION_TIMEOUT));
                }
                //设置读取超时
                if (BooleanHelper.isValid(netxProperties, CONFIG_FIELD_READ_TIMEOUT)) {
                    requestConfigBuilder.setSocketTimeout(PropertiesHelper.ints(netxProperties, CONFIG_FIELD_READ_TIMEOUT));
                }
                return requestConfigBuilder;
            }
        };
    }

    /**
     * 获取服务器
     *
     * @return
     */
    private List<HttpHost> getHttpHost() {
        return Arrays.stream(netxProperties.getHost())
                .map(host -> {
                    String text = host;
                    String scheme = null;
                    final int schemeIdx = text.indexOf("://");
                    if (schemeIdx > 0) {
                        scheme = text.substring(0, schemeIdx);
                        text = text.substring(schemeIdx + 3);
                    } else {
                        scheme = "http";
                    }
                    int port = 9200;
                    final int portIdx = text.lastIndexOf(":");
                    if (portIdx > 0) {
                        try {
                            port = Integer.parseInt(text.substring(portIdx + 1));
                        } catch (NumberFormatException ex) {
                            throw new IllegalArgumentException("Invalid HTTP host: " + text);
                        }
                        text = text.substring(0, portIdx);
                    }
                    return new HttpHost(text, port, scheme);
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }



}