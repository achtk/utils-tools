package com.chua.utils.tools.http.build;

import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.callback.ResponseCallback;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.chua.utils.tools.http.sync.Sync;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;

import static com.chua.utils.tools.constant.HttpConstant.*;

/**
 * url builder
 *
 * @author CHTK
 */
@AllArgsConstructor
public class UrlClientBuilder implements HttpClientBuilder {

    private final RequestConfig<?> requestConfig;
    private final Sync sync;

    public UrlClientBuilder(RequestConfig<?> requestConfig) {
        this.requestConfig = requestConfig;
        this.sync = new Sync(requestConfig);
    }

    @Override
    public <T> ResponseEntity<T> execute(Class<T> tClass) {
        ResponseEntity<T> responseEntity = null;
        if (HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            responseEntity = sync.executeGet();
        } else if (HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            responseEntity = sync.executePost();
        } else if (HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            responseEntity = sync.executePut();
        } else if (HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            responseEntity = sync.executeDelete();
        }

        return createResponseEntity(responseEntity, tClass);
    }

    @Override
    public <T> void execute(ResponseCallback responseCallback, Class<T> tClass) {
        ExecutorService executorService = ThreadHelper.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                ResponseEntity<T> responseEntity = execute(tClass);
                responseCallback.onResponse(responseEntity);
            } catch (Exception e) {
                responseCallback.onFailure(e);
            } finally {
                executorService.shutdownNow();
            }
        });
    }
}
