package com.chua.utils.http.httpclient.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息头
 * @author Administrator
 */
@Getter
@Setter
@AllArgsConstructor
public class Header {
    private String name;
    private String value;
}
