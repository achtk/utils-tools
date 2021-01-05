package com.chua.utils.http.okhttp.stream;

import com.chua.utils.http.okhttp.downloader.IHttpDownloader;
import com.chua.utils.http.okhttp.enums.HttpMethod;
import com.chua.utils.http.okhttp.http.OkHttpHelper;
import com.chua.utils.tools.common.HttpClientHelper;
import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.callback.ResponseCallback;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 流式
 *
 * @author CHTK
 */
@Slf4j
public class OkHttpStreamBuilder implements HttpClientBuilder {

    private final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
    private final FormBody.Builder formBody;
    private final RequestBody requestBody;
    private final Interceptor interceptor;

    private Request.Builder builder;
    private HttpMethod httpMethod;
    private RequestConfig requestConfig;


    public OkHttpStreamBuilder(HttpMethod method,
                               RequestConfig requestConfig,
                               FormBody.Builder formBody,
                               RequestBody requestBody,
                               Interceptor interceptor,
                               Request.Builder builder) {
        this.builder = builder;
        this.requestConfig = requestConfig;
        this.httpMethod = method;
        this.formBody = formBody;
        this.requestBody = requestBody;
        this.interceptor = interceptor;
    }

    /**
     * @return
     */
    @Override
    public <T> ResponseEntity<T> execute(Class<T> tClass) {
        OkHttpClient okHttp = getRequest();
        ResponseEntity responseEntity = null;

        if (httpMethod == HttpMethod.GET) {
            try {
                Response execute = okHttp.newCall(this.builder.build()).execute();
                responseEntity = new ResponseEntity(execute.code(), execute.body().string(), execute.message());
            } catch (IOException e) {
                if (null != requestConfig.getHandler()) {
                    return requestConfig.getHandler().throwable(e);
                }
            }
        } else if (httpMethod == HttpMethod.POST) {
            Request.Builder post = null;
            if (null == requestBody) {
                FormBody formBody = this.formBody.build();
                post = this.builder.post(formBody);
            } else {
                post = this.builder.post(requestBody);
            }
            try {
                Response execute = okHttp.newCall(post.build()).execute();
                responseEntity = new ResponseEntity(execute.code(), execute.body().string());
            } catch (IOException e) {
                if (null != requestConfig.getHandler()) {
                    return requestConfig.getHandler().throwable(e);
                }
            }
        } else if (httpMethod == HttpMethod.DELETE) {
            Request.Builder post = null;
            if (null == requestBody) {
                FormBody formBody = this.formBody.build();
                post = this.builder.delete(formBody);
            } else {
                post = this.builder.delete(requestBody);
            }
            try {
                Response execute = okHttp.newCall(post.build()).execute();
                responseEntity = new ResponseEntity(execute.code(), execute.body().string());
            } catch (IOException e) {
                if (null != requestConfig.getHandler()) {
                    return requestConfig.getHandler().throwable(e);
                }
            }
        } else if (httpMethod == HttpMethod.PUT) {
            Request.Builder post = null;
            if (null == requestBody) {
                FormBody formBody = this.formBody.build();
                post = this.builder.put(formBody);
            } else {
                post = this.builder.put(requestBody);
            }
            try {
                Response execute = okHttp.newCall(post.build()).execute();
                responseEntity = new ResponseEntity(execute.code(), execute.body().string());
            } catch (IOException e) {
                if (null != requestConfig.getHandler()) {
                    return requestConfig.getHandler().throwable(e);
                }
            }
        }
        return createResponseEntity(responseEntity, tClass);
    }

    /**
     * @return
     */
    @Override
    public <T> void execute(final ResponseCallback callback, Class<T> tClass) {
        OkHttpClient okHttp = getRequest();
        Request.Builder post = null;

        Callback callback1 = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(createResponseEntity(new ResponseEntity(response.code(), response.body().string()), tClass));
            }
        };
        if (httpMethod == HttpMethod.GET) {
            okHttp.newCall(this.builder.build()).enqueue(callback1);
        } else if (httpMethod == HttpMethod.POST) {
            if (null == requestBody) {
                FormBody formBody = this.formBody.build();
                post = this.builder.post(formBody);
            } else {
                post = this.builder.post(requestBody);
            }
            okHttp.newCall(post.build()).enqueue(callback1);
        } else if (httpMethod == HttpMethod.DELETE) {
            if (null == requestBody) {
                FormBody formBody = this.formBody.build();
                post = this.builder.delete(formBody);
            } else {
                post = this.builder.delete(requestBody);
            }
            okHttp.newCall(post.build()).enqueue(callback1);
        } else if (httpMethod == HttpMethod.PUT) {
            if (null == requestBody) {
                FormBody formBody = this.formBody.build();
                post = this.builder.put(formBody);
            } else {
                post = this.builder.put(requestBody);
            }
            okHttp.newCall(post.build()).enqueue(callback1);
        }
    }

    /**
     * @return
     */
    private OkHttpClient getRequest() {
        if (requestConfig.getReadTimeout() > 0) {
            okHttpClient.readTimeout(requestConfig.getReadTimeout(), TimeUnit.SECONDS);
        }
        if (requestConfig.getConnectTimeout() > 0) {
            okHttpClient.connectTimeout(requestConfig.getConnectTimeout(), TimeUnit.SECONDS);
        }
        if (null != interceptor) {
            okHttpClient.addInterceptor(interceptor);
        }
        if (requestConfig.isHttps()) {
            Object sslSocketFactory = requestConfig.getSslSocketFactory();
            if (null == sslSocketFactory) {
                sslSocketFactory = HttpClientHelper.createSslSocketFactory();
            }
            if (sslSocketFactory instanceof SSLSocketFactory) {
                try {
                    okHttpClient.sslSocketFactory((SSLSocketFactory) sslSocketFactory, new HttpClientHelper.TrustAllCerts());
                } catch (Throwable e) {
                }
            }
        }

        List<String> protocols = requestConfig.getProtocols();
        if (null != protocols && !protocols.isEmpty()) {
            List<Protocol> collect = protocols.stream().map(protocol -> {
                try {
                    return Protocol.get(protocol);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(Objects::isNull).collect(Collectors.toList());
            okHttpClient.protocols(collect);
        }

//        CacheConfig cacheConfig = requestConfig.getCacheConfig();
//        if (null != cacheConfig) {
//            okHttpClient.cache(new Cache(cacheConfig.getCacheFile(), cacheConfig.getCacheSize()));
//        }

        String dns = requestConfig.getDns();
        if (null != dns && !"".equals(dns)) {
            okHttpClient.dns(new Dns() {
                @Override
                public List<InetAddress> lookup(String hostname) throws UnknownHostException {
                    return Arrays.asList(InetAddress.getAllByName(dns));
                }
            });
        }

        if (requestConfig.getRetry() > 0) {
            okHttpClient.retryOnConnectionFailure(true);
            okHttpClient.addInterceptor(new Interceptor() {
                //最大重试次数
                public int maxRetry = requestConfig.getRetry();
                //假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
                private AtomicInteger retryNum = new AtomicInteger(0);

                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    while (!response.isSuccessful() && retryNum.get() < maxRetry) {
                        retryNum.incrementAndGet();
                        log.debug("retryNum: " + retryNum.get());
                        try {
                            response = chain.proceed(request);
                        } catch (Throwable e) {
                            if (retryNum.get() < maxRetry) {
                                response.close();
                            }
                        }
                    }
                    return response;
                }
            });
        }

        OkHttpClient okHttp = okHttpClient.build();
        return okHttp;
    }


    /**
     * 下载器下载文件
     */
    public void download(IHttpDownloader httpDownloader, String... retryUrls) {
        OkHttpClient okHttp = getRequest();

        Response execute = null;
        try {
            execute = okHttp.newCall(this.builder.build()).execute();
            try {
                if (execute.code() != 200) {
                    retry(httpDownloader, retryUrls);
                } else {
                    httpDownloader.download(execute);
                }
            } finally {
                execute.close();
            }
        } catch (IOException e) {
            httpDownloader.throwable(e);
            if (null != execute) {
                execute.close();
            }
            retry(httpDownloader, retryUrls);

        }
    }

    /**
     * 尝试
     *
     * @param httpDownloader http下载器
     * @param retryUrls      尝试地址
     */
    private void retry(IHttpDownloader httpDownloader, String... retryUrls) {
        if (null != retryUrls && retryUrls.length > 0) {
            String[] newRetry = new String[retryUrls.length - 1];
            System.arraycopy(retryUrls, 1, newRetry, 0, retryUrls.length - 1);
            OkHttpStream okHttpStream = OkHttpHelper.newGet();
            OkHttpStreamBuilder build = (OkHttpStreamBuilder) okHttpStream.url(retryUrls[0]).build();
            build.download(httpDownloader, newRetry);
        }
    }


}
