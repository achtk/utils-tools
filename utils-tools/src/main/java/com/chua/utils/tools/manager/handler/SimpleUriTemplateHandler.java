package com.chua.utils.tools.manager.handler;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.UrlHelper;

import java.net.URI;
import java.util.Map;

/**
 * 简单的uri处理模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class SimpleUriTemplateHandler implements UriTemplateHandler {


    @Override
    public URI expand(String uriTemplate, Map<String, ?> uriVariables) {
        StringBuilder stringBuilder = new StringBuilder(uriTemplate);
        if (BooleanHelper.hasLength(uriVariables)) {
            stringBuilder.append("?");
            StringBuilder uri = new StringBuilder();
            uriVariables.forEach((key, value) -> {
                uri.append("&").append(key).append("=").append(value);
            });

            stringBuilder.append(uri.substring(1));
        }
        return UrlHelper.toUri(stringBuilder.toString());
    }

    @Override
    public URI expand(String uriTemplate, Object... uriVariables) {
        StringBuilder stringBuilder = new StringBuilder(uriTemplate);
        if (BooleanHelper.hasLength(uriVariables)) {
            stringBuilder.append("?");
            StringBuilder uri = new StringBuilder();
            for (Object value : uriVariables) {
                uri.append("&").append(value);
            }

            stringBuilder.append(uri.substring(1));
        }
        return UrlHelper.toUri(stringBuilder.toString());
    }
}
