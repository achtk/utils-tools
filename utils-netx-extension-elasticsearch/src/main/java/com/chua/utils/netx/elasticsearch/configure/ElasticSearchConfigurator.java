package com.chua.utils.netx.elasticsearch.configure;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.PropertiesHelper;
import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.chua.unified.properties.NetxProperties.*;


/**
 * es配置器
 * @author CH
 * @since 1.0
 */
@AllArgsConstructor
public class ElasticSearchConfigurator {

    private Properties properties;
    private static final String MULTI_THREADED = "multiThreaded";
    private static final String DEFAULT_MAX_TOTAL_CONNECTION_PER_ROUTE = "defaultMaxTotalConnectionPerRoute";

    /**
     * 配置启动项
     */
    public JestClientFactory configure() {
        JestClientFactory jestClientFactory = new JestClientFactory();

        String[] hosts = PropertiesHelper.arrays(properties, NetxProperties.CONFIG_FIELD_HOST);
        List<String> collect = Arrays.stream(hosts)
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
                    return scheme + "://" + text + ":" + port;
                }).filter(Objects::nonNull).collect(Collectors.toList());

        HttpClientConfig.Builder builder = new HttpClientConfig.Builder(collect);
        builder.gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create());
        //设置为多线程
        builder.multiThreaded(PropertiesHelper.booleans(properties, MULTI_THREADED));
        //最大连接数
        if(BooleanHelper.isValid(properties, DEFAULT_MAX_TOTAL_CONNECTION_PER_ROUTE)) {
            builder.defaultMaxTotalConnectionPerRoute(PropertiesHelper.ints(properties, DEFAULT_MAX_TOTAL_CONNECTION_PER_ROUTE));
        }

        //最大连接数
        if(BooleanHelper.isValid(properties, CONFIG_FIELD_MAX_CONNECTION)) {
            builder.maxTotalConnection(PropertiesHelper.ints(properties, CONFIG_FIELD_MAX_CONNECTION));
        }
        //最大连接数
        if(BooleanHelper.isValid(properties, CONFIG_FIELD_CONNECTION_TIMEOUT)) {
            builder.connTimeout(PropertiesHelper.ints(properties, CONFIG_FIELD_CONNECTION_TIMEOUT));
        }
        //设置读取超时
        if(BooleanHelper.isValid(properties, CONFIG_FIELD_READ_TIMEOUT)) {
            builder.readTimeout(PropertiesHelper.ints(properties, CONFIG_FIELD_READ_TIMEOUT));
        }

        jestClientFactory.setHttpClientConfig(builder.build());

        return jestClientFactory;
    }
}
