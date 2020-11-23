package com.chua.utils.tools.manager.handler;

import java.net.URI;
import java.util.Map;

/**
 * uri处理模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public interface UriTemplateHandler {

    /**
     * 用URI变量图扩展给定的URI模板。
     *
     * @param uriTemplate  the URI template
     * @param uriVariables variable values
     * @return the created URI instance
     */
    URI expand(String uriTemplate, Map<String, ?> uriVariables);

    /**
     * 用URI变量图扩展给定的URI模板。
     *
     * @param uriTemplate  the URI template
     * @param uriVariables variable values
     * @return the created URI instance
     */
    URI expand(String uriTemplate, Object... uriVariables);
}
