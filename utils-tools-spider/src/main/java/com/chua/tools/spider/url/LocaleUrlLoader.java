package com.chua.tools.spider.url;

import com.chua.tools.spider.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.LongAdder;

/**
 * 本地加载器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Slf4j
public class LocaleUrlLoader implements UrlLoader {
    /**
     * 待采集URL池
     */
    private volatile LinkedBlockingQueue<String> unVisitedUrlQueue = new LinkedBlockingQueue<String>();
    /**
     * 已采集URL池
     */
    private volatile CopyOnWriteArraySet<String> visitedUrlSet = new CopyOnWriteArraySet<>();
    /**
     * 计数器
     */
    private volatile LongAdder count = new LongAdder();

    @Override
    public boolean addUrl(String url) {
        // check URL格式
        if (!UrlUtil.isUrl(url)) {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>>>>>>> addUrl fail, link not valid: {}", url);
            }
            return false;
        }
        // check 未访问过
        if (visitedUrlSet.contains(url) || unVisitedUrlQueue.contains(url)) {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>>>>>>> addUrl fail, link repeat or visited: {}", url);
            }
            return false;
        }

        unVisitedUrlQueue.add(url);
        if (log.isDebugEnabled()) {
            log.debug("addUrl success, link: {}", url);
        }
        return true;
    }

    @Override
    public String getUrl() {
        String link = null;
        try {
            link = unVisitedUrlQueue.take();
        } catch (InterruptedException e) {
            throw new IllegalStateException("getUrl interrupted.");
        }
        if (link != null) {
            count.increment();
            visitedUrlSet.add(link);
        }
        return link;
    }

    @Override
    public int nowNum() {
        return unVisitedUrlQueue.size();
    }

    @Override
    public int finishedNum() {
        return count.intValue();
    }
}
