package com.chua.tools.spring.http.build;

import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.callback.ResponseCallback;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.chua.utils.tools.constant.HttpConstant.*;

/**
 * rest builder
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/5
 */
@AllArgsConstructor
public class FluxClientBuilder implements HttpClientBuilder {

    private RequestConfig requestConfig;

    @Override
    public <T> ResponseEntity<T> execute(Class<T> tClass) {
        WebClient webClient = WebClient.create();
        ResponseEntity<T> responseEntity = new ResponseEntity<>();

        Mono<ClientResponse> exchange = null;
        if (HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            exchange = doWithGet(webClient);
        } else if (HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            exchange = doWithPost(webClient);
        } else if (HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            exchange = doWithPut(webClient);
        } else if (HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            exchange = doWithDelete(webClient);
        }
        return createResponse(exchange, responseEntity, tClass);
    }

    @Override
    public <T> void execute(ResponseCallback responseCallback, Class<T> tClass) {
        WebClient webClient = WebClient.create();

        Mono<ClientResponse> exchange = null;
        if (HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            exchange = doWithGet(webClient);
        } else if (HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            exchange = doWithPost(webClient);
        } else if (HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            exchange = doWithPut(webClient);
        } else if (HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            exchange = doWithDelete(webClient);
        }
        doWithResponse(exchange, responseCallback, tClass);
    }

    /**
     * 处理回调
     *
     * @param exchange         响应
     * @param responseCallback 回调
     * @param tClass           类型
     */
    private <T> void doWithResponse(Mono<ClientResponse> exchange, ResponseCallback responseCallback, Class<T> tClass) {
        Mono<ClientResponse> responseMono = exchange.retry(requestConfig.getRetry()).timeout(Duration.ofMillis(requestConfig.getTimeout()));
        responseMono.doOnError(throwable -> {
            Optional<Consumer<Throwable>> optional = Optional.ofNullable(requestConfig.getThrowableConsumer());
            if (optional.isPresent()) {
                optional.get().accept(throwable);
            }
            responseCallback.onFailure(throwable);
        }).doOnSuccess(clientResponse -> {
            responseCallback.onResponse(createResponse(clientResponse, tClass));
        });
    }


    /**
     * 处理回调
     *
     * @param exchange          响应
     * @param webClientResponse 结果
     * @param tClass            类型
     */
    private <T> ResponseEntity<T> createResponse(Mono<ClientResponse> exchange, ResponseEntity<T> webClientResponse, Class<T> tClass) {
        Mono<ClientResponse> responseMono = exchange.retry(requestConfig.getRetry()).timeout(Duration.ofMillis(requestConfig.getTimeout()));
        responseMono.doOnError(throwable -> {
            Optional<Consumer<Throwable>> optional = Optional.ofNullable(requestConfig.getThrowableConsumer());
            if (optional.isPresent()) {
                optional.get().accept(throwable);
            }
        });
        ClientResponse clientResponse = responseMono.block();
        return createResponse(clientResponse, tClass);
    }

    /**
     * 组装响应
     *
     * @param clientResponse 结果
     * @param tClass         类型
     * @return WebClientResponse
     */
    private <T> ResponseEntity<T> createResponse(ClientResponse clientResponse, Class<T> tClass) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setCode(clientResponse.rawStatusCode());
        responseEntity.setContent(clientResponse.bodyToMono(tClass).block());
        return responseEntity;
    }

    /**
     * this http post
     *
     * @param webClient 客户端
     * @return Mono
     */
    private Mono<ClientResponse> doWithPost(WebClient webClient) {
        return webClient.post()
                .uri(requestConfig.getUrl())
                .headers(new Consumer<HttpHeaders>() {
                    @Override
                    public void accept(HttpHeaders httpHeaders) {
                        Multimap<String, String> headers = requestConfig.getHeaders();
                        for (Map.Entry<String, Collection<String>> entry : headers.asMap().entrySet()) {
                            httpHeaders.addAll(entry.getKey(), Lists.newArrayList(entry.getValue()));
                        }
                    }
                })
                .cookies(new Consumer<MultiValueMap<String, String>>() {
                    @Override
                    public void accept(MultiValueMap<String, String> httpCookie) {
                        httpCookie.putAll(requestConfig.getCookie());
                    }
                }).exchange();
    }

    /**
     * this http put
     *
     * @param webClient 客户端
     * @return Mono
     */
    private Mono<ClientResponse> doWithPut(WebClient webClient) {
        return webClient.put()
                .uri(requestConfig.getUrl())
                .headers(new Consumer<HttpHeaders>() {
                    @Override
                    public void accept(HttpHeaders httpHeaders) {
                        Multimap<String, String> headers = requestConfig.getHeaders();
                        for (Map.Entry<String, Collection<String>> entry : headers.asMap().entrySet()) {
                            httpHeaders.addAll(entry.getKey(), Lists.newArrayList(entry.getValue()));
                        }
                    }
                })
                .cookies(new Consumer<MultiValueMap<String, String>>() {
                    @Override
                    public void accept(MultiValueMap<String, String> httpCookie) {
                        httpCookie.putAll(requestConfig.getCookie());
                    }
                }).exchange();
    }

    /**
     * this http delete
     *
     * @param webClient 客户端
     * @return Mono
     */
    private Mono<ClientResponse> doWithDelete(WebClient webClient) {
        return webClient.delete()
                .uri(requestConfig.getUrl())
                .headers(new Consumer<HttpHeaders>() {
                    @Override
                    public void accept(HttpHeaders httpHeaders) {
                        Multimap<String, String> headers = requestConfig.getHeaders();
                        for (Map.Entry<String, Collection<String>> entry : headers.asMap().entrySet()) {
                            httpHeaders.addAll(entry.getKey(), Lists.newArrayList(entry.getValue()));
                        }
                    }
                })
                .cookies(new Consumer<MultiValueMap<String, String>>() {
                    @Override
                    public void accept(MultiValueMap<String, String> httpCookie) {
                        httpCookie.putAll(requestConfig.getCookie());
                    }
                }).exchange();
    }

    /**
     * this http get
     *
     * @param webClient 客户端
     * @return Mono
     */
    private Mono<ClientResponse> doWithGet(WebClient webClient) {
        return webClient.get()
                .uri(requestConfig.getUrl(), requestConfig.getBody())
                .headers((Consumer<MultiValueMap<String, String>>) httpHeaders -> {
                    Multimap<String, String> headers = requestConfig.getHeaders();
                    for (Map.Entry<String, Collection<String>> entry : headers.asMap().entrySet()) {
                        httpHeaders.addAll(entry.getKey(), Lists.newArrayList(entry.getValue()));
                    }
                })
                .cookies((Consumer<MultiValueMap<String, String>>) httpCookies -> httpCookies.putAll(requestConfig.getCookie())).exchange();

    }
}
