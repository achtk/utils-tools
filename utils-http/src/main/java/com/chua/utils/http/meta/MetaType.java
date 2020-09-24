package com.chua.utils.http.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:38
 */
@AllArgsConstructor
public enum MetaType {
    /**
     * json
     */
    APPLICATION_JSON("application/json"),
    /**
     * js
     */
    APPLICATION_JAVASCRIPT("application/javascript"),
    /**
     * text
     */
    TEXT_PlAIN("text/plain"),
    /**
     * xml
     */
    TEXT_XML("text/xml"),
    /**
     * bytes
     */
    BYTES("bytes"),
    /**
     * nbytes
     */
    NBYTES("nbytes"),
    /**
     * html
     */
    TEXT_HTML("text/html");

    @Getter
    @Setter
    private String value;

}
