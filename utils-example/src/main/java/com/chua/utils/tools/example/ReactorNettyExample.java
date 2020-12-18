package com.chua.utils.tools.example;

import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/15
 */
public class ReactorNettyExample {

    public static void main(String[] args) throws Exception {
        HttpServer.create().port(8888).route(routes -> {
            routes.get("/test", (request, response) -> {
                return response.header(CONTENT_TYPE, TEXT_PLAIN).sendString(Mono.just(request.path()));
            });
        }).bindNow().onDispose().block();
    }

}
