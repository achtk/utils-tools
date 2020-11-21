package com.chua.tools.spider.url;

import java.net.URL;

/**
 * url加载器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface UrlLoader {
    /**
     * 添加url
     *
     * @param url url
     * @return 添加成功返回true
     */
    boolean addUrl(String url);

    /**
     * 获取url
     *
     * @return url
     */
    String getUrl();

    /**
     * 当前url数量
     *
     * @return 数量
     */
    int nowNum();
    /**
     * 处理url数量
     *
     * @return 数量
     */
    int finishedNum();
}
