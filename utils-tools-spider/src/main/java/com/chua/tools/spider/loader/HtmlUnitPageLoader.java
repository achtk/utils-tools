package com.chua.tools.spider.loader;

import com.chua.tools.spider.request.PageRequest;
import com.chua.tools.spider.util.UrlUtil;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

/**
 * htmlunit页面加载器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Slf4j
public class HtmlUnitPageLoader implements PageLoader {
    @Override
    public Document load(PageRequest pageRequest) {
        if (!UrlUtil.isUrl(pageRequest.getUrl())) {
            return null;
        }
        WebClient webClient = new WebClient();
        try {
            WebRequest webRequest = new WebRequest(new URL(pageRequest.getUrl()));

            // 请求设置
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setDoNotTrackEnabled(false);
            webClient.getOptions().setUseInsecureSSL(!pageRequest.isValidateTLSCertificates());

            if (pageRequest.getParamMap() != null && !pageRequest.getParamMap().isEmpty()) {
                for (Map.Entry<String, String> paramItem : pageRequest.getParamMap().entrySet()) {
                    webRequest.getRequestParameters().add(new NameValuePair(paramItem.getKey(), paramItem.getValue()));
                }
            }
            if (pageRequest.getCookieMap() != null && !pageRequest.getCookieMap().isEmpty()) {
                webClient.getCookieManager().setCookiesEnabled(true);
                for (Map.Entry<String, String> cookieItem : pageRequest.getCookieMap().entrySet()) {
                    webClient.getCookieManager().addCookie(new Cookie("", cookieItem.getKey(), cookieItem.getValue()));
                }
            }
            if (pageRequest.getHeaderMap() != null && !pageRequest.getHeaderMap().isEmpty()) {
                webRequest.setAdditionalHeaders(pageRequest.getHeaderMap());
            }
            if (pageRequest.getUserAgent() != null) {
                webRequest.setAdditionalHeader("User-Agent", pageRequest.getUserAgent());
            }
            if (pageRequest.getReferrer() != null) {
                webRequest.setAdditionalHeader("Referer", pageRequest.getReferrer());
            }

            webClient.getOptions().setTimeout(pageRequest.getTimeoutMillis());
            webClient.setJavaScriptTimeout(pageRequest.getTimeoutMillis());
            webClient.waitForBackgroundJavaScript(pageRequest.getTimeoutMillis());

            // 代理
//            if (pageRequest.getProxy() != null) {
//                InetSocketAddress address = (InetSocketAddress) pageRequest.getProxy().address();
//                boolean isSocks = pageRequest.getProxy().type() == Proxy.Type.SOCKS;
//                webClient.getOptions().setProxyConfig(new ProxyConfig(address.getHostName(), address.getPort(), isSocks));
//            }

            // 发出请求
            if (pageRequest.isIfPost()) {
                webRequest.setHttpMethod(HttpMethod.POST);
            } else {
                webRequest.setHttpMethod(HttpMethod.GET);
            }
            HtmlPage page = webClient.getPage(webRequest);

            String pageAsXml = page.asXml();
            if (pageAsXml != null) {
                Document html = Jsoup.parse(pageAsXml);
                return html;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (webClient != null) {
                webClient.close();
            }
        }
        return null;
    }
}
