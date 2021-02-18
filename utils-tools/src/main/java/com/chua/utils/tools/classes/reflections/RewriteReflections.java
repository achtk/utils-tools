package com.chua.utils.tools.classes.reflections;

import com.chua.utils.tools.classes.reflections.configuration.RewriteConfiguration;
import com.chua.utils.tools.classes.reflections.scan.RewriteScan;
import com.chua.utils.tools.common.ThreadHelper;

import java.net.URL;
import java.util.concurrent.ExecutorService;

/**
 * 反射处理工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class RewriteReflections extends RewriteScan {
    /**
     * 构造
     *
     * @param rewriteConfiguration 配置
     */
    public RewriteReflections(RewriteConfiguration rewriteConfiguration) {
        super.store = STORE;
        this.rewriteConfiguration = rewriteConfiguration;
        this.scan();
    }

    /**
     * 扫描URL
     *
     * @param url URL
     */
    public void scanUrl(URL url) {
        this.scan(url);
    }

    /**
     * 扫描URL
     *
     * @param urls URL
     */
    public void asyncScanUrl(URL... urls) {
        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();
        for (URL url : urls) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    scan(url);
                }
            });
        }
        executorService.shutdown();
    }

}
