package com.chua.tools.spider.parser;

import com.chua.tools.spider.request.PageRequest;

/**
 * 解析器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface Parser {

    /**
     * 预处理
     *
     * @param pageRequest
     */
    default void pretreatment(PageRequest pageRequest) {
    }

}
