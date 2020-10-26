package com.chua.utils.tools.loader;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.CollectionHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.entity.HttpBody;
import com.chua.utils.tools.enums.HttpStatus;
import com.chua.utils.tools.function.Filter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * url均衡
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/26
 */
public class UrlBalancerLoader implements BalancerLoader<String> {

    private Filter<String> filter;
    private Collection<String> urls;
    private final ExecutorService executorService = ThreadHelper.newSingleThreadExecutor("balancer-url-executor");
    private final CopyOnWriteArraySet<String> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
    private int size;

    {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                if (copyOnWriteArraySet.size() != 0) {
                    checkUrlCode(copyOnWriteArraySet.iterator().next());
                }
            }
        });
    }

    @Override
    public BalancerLoader data(Collection<String> source) {
        this.urls = source;
        this.size = CollectionHelper.getSize(source);
        return this;
    }

    @Override
    public BalancerLoader filter(Filter<String> filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public String balancer() {
        return getValidUrl();
    }

    /**
     * 获取有效的URL
     *
     * @return 有效的URL
     */
    private String getValidUrl() {
        if (BooleanHelper.hasLength(copyOnWriteArraySet)) {
            return copyOnWriteArraySet.iterator().next();
        }
        for (String url : urls) {
            if (404 == checkUrlCode(url)) {
                checkCacheUrl(url);
                continue;
            }
            copyOnWriteArraySet.clear();
            copyOnWriteArraySet.add(url);
            return url;
        }
        return null;
    }

    /**
     * 检测地址有效性
     *
     * @param url
     * @return
     */
    private int checkUrlCode(String url) {
        if (null == url) {
            return HttpStatus.NOT_FOUND.code();
        }
        try {
            URL url1 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
            return httpURLConnection.getResponseCode();
        } catch (IOException e) {
            return checkUrlCode(null);
        }
    }

    /**
     * 检测缓存
     *
     * @param url 地址
     */
    private void checkCacheUrl(String url) {
        if (copyOnWriteArraySet.size() == 0) {
            return;
        }
        String next = copyOnWriteArraySet.iterator().next();
        if (next.equals(url)) {
            copyOnWriteArraySet.clear();
        }
    }

    @Override
    public String random() {
        if (0 == size) {
            return null;
        }
        Double doubles = Math.random() * Math.pow(size, 10);
        return FinderHelper.findElement(doubles.intValue(), urls);
    }
}
