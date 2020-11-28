package com.chua.tools.spider.util;

import com.chua.tools.spider.annotation.CssQuery;
import com.chua.tools.spider.request.PageRequest;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * jsoup同居
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class JsoupUtil {

    /**
     * 加载页面
     *
     * @param pageRequest
     * @return Document
     */
    public static Document load(PageRequest pageRequest) {
        if (!UrlUtil.isUrl(pageRequest.getUrl())) {
            return null;
        }
        try {
            // 请求设置
            Connection conn = Jsoup.connect(pageRequest.getUrl());
            if (pageRequest.getParamMap() != null && !pageRequest.getParamMap().isEmpty()) {
                conn.data(pageRequest.getParamMap());
            }
            if (pageRequest.getCookieMap() != null && !pageRequest.getCookieMap().isEmpty()) {
                conn.cookies(pageRequest.getCookieMap());
            }
            if (pageRequest.getHeaderMap() != null && !pageRequest.getHeaderMap().isEmpty()) {
                conn.headers(pageRequest.getHeaderMap());
            }
            if (pageRequest.getUserAgent() != null) {
                conn.userAgent(pageRequest.getUserAgent());
            }
            if (pageRequest.getReferrer() != null) {
                conn.referrer(pageRequest.getReferrer());
            }
            conn.timeout(pageRequest.getTimeoutMillis());
            //是否需要校验ssl
            conn.validateTLSCertificates(pageRequest.isValidateTlsCertificates());
            // 取消默认1M限制
            conn.maxBodySize(0);

            //代理
            if (pageRequest.getProxy() != null) {
                conn.proxy(pageRequest.getProxy());
            }

            // 发出请求
            return pageRequest.isIfPost() ? conn.post() : conn.get();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 非页面
     *
     * @param pageRequest 请求
     * @return 源码
     */
    public static String loadPageSource(PageRequest pageRequest) {
        if (!UrlUtil.isUrl(pageRequest.getUrl())) {
            return null;
        }
        try {
            // 请求设置
            Connection conn = Jsoup.connect(pageRequest.getUrl());
            if (pageRequest.getParamMap() != null && !pageRequest.getParamMap().isEmpty()) {
                conn.data(pageRequest.getParamMap());
            }
            if (pageRequest.getCookieMap() != null && !pageRequest.getCookieMap().isEmpty()) {
                conn.cookies(pageRequest.getCookieMap());
            }
            if (pageRequest.getHeaderMap() != null && !pageRequest.getHeaderMap().isEmpty()) {
                conn.headers(pageRequest.getHeaderMap());
            }
            if (pageRequest.getUserAgent() != null) {
                conn.userAgent(pageRequest.getUserAgent());
            }
            if (pageRequest.getReferrer() != null) {
                conn.referrer(pageRequest.getReferrer());
            }
            conn.timeout(pageRequest.getTimeoutMillis());
            conn.validateTLSCertificates(pageRequest.isValidateTlsCertificates());
            // 取消默认1M限制
            conn.maxBodySize(0);

            // 代理
            if (pageRequest.getProxy() != null) {
                conn.proxy(pageRequest.getProxy());
            }

            conn.ignoreContentType(true);
            conn.method(pageRequest.isIfPost() ? Connection.Method.POST : Connection.Method.GET);

            // 发出请求
            Connection.Response resp = conn.execute();
            return resp.body();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取页面上所有超链接地址 （<a>标签的href值）
     *
     * @param html 页面文档
     * @return Set<String>
     */
    public static Set<String> findLinks(Document html) {

        if (html == null) {
            return null;
        }

        // element
        /**
         *
         * Elements resultSelect = html.select(tagName);	// 选择器方式
         * Element resultId = html.getElementById(tagName);	// 元素ID方式
         * Elements resultClass = html.getElementsByClass(tagName);	// ClassName方式
         * Elements resultTag = html.getElementsByTag(tagName);	// html标签方式 "body"
         *
         */
        Elements hrefElements = html.select("a[href]");

        // 抽取数据
        Set<String> links = new HashSet<String>();
        if (hrefElements != null && hrefElements.size() > 0) {
            for (Element item : hrefElements) {
                //href、abs:href
                String href = item.attr("abs:href");
                if (UrlUtil.isUrl(href)) {
                    links.add(href);
                }
            }
        }
        return links;
    }

    /**
     * 获取页面上所有图片地址 （<a>标签的href值）
     *
     * @param html
     * @return Set<String>
     */
    public static Set<String> findImages(Document html) {

        Elements imgs = html.getElementsByTag("img");

        Set<String> images = new HashSet<String>();
        if (imgs != null && imgs.size() > 0) {
            for (Element element : imgs) {
                String imgSrc = element.attr("abs:src");
                images.add(imgSrc);
            }
        }

        return images;
    }

    /**
     * 抽取元素数据
     *
     * @param fieldElement
     * @param selectType
     * @param selectVal
     * @return String
     */
    public static String parseElement(Element fieldElement, CssQuery.SelectType selectType, String selectVal) {
        String fieldElementOrigin = null;
        if (CssQuery.SelectType.HTML == selectType) {
            fieldElementOrigin = fieldElement.html();
        } else if (CssQuery.SelectType.VAL == selectType) {
            fieldElementOrigin = fieldElement.val();
        } else if (CssQuery.SelectType.TEXT == selectType) {
            fieldElementOrigin = fieldElement.text();
        } else if (CssQuery.SelectType.ATTR == selectType) {
            fieldElementOrigin = fieldElement.attr(selectVal);
        } else if (CssQuery.SelectType.HAS_CLASS == selectType) {
            fieldElementOrigin = String.valueOf(fieldElement.hasClass(selectVal));
        } else {
            fieldElementOrigin = fieldElement.toString();
        }
        return fieldElementOrigin;
    }
}
