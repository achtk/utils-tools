package com.chua.utils.tools.spider.config.scheduler;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Lists;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 增加去重的校验，对需要重复爬取的网址进行正则过滤
 *
 * @author CH
 */
public class SpikeFileCacheQueueScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, Closeable {

    private String fileUrlAllName = ".urls.txt";
    private static final String LEFT_SYMBOL = "/";
    private static final String RIGHT_SYMBOL = "\\";

    private Set<String> cacheUrl = new HashSet<>();

    private BlockingQueue<Request> queue = new LinkedBlockingQueue<>();
    private File file;

    public SpikeFileCacheQueueScheduler(String filePath, Set<String> urls) {
        if (!filePath.endsWith(LEFT_SYMBOL) && !filePath.endsWith(RIGHT_SYMBOL)) {
            filePath += LEFT_SYMBOL;
        }
        for (String url : urls) {
            queue.add(new Request(url));
        }
        this.cacheUrl(filePath, urls);
        this.initDuplicateRemover();
    }

    /**
     * 读取缓存
     * @param filePath
     * @param urls
     */
    private void cacheUrl(String filePath, Set<String> urls) {
        ArrayList<String> strings = Lists.newArrayList(urls);
        Collections.sort(strings);
        this.file = new File(filePath, DigestUtils.md5Hex(strings.toString()) + fileUrlAllName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }

        try {
            cacheUrl.addAll(IOUtils.readLines(file.toURI().toURL().openStream(), Charsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化
     */
    private void initDuplicateRemover() {
        setDuplicateRemover(
                new DuplicateRemover() {
                    @Override
                    public boolean isDuplicate(Request request, Task task) {
                        boolean contains = cacheUrl.contains(request.getUrl());
                        if(!contains) {
                            cacheUrl.add(request.getUrl());
                            this.record(request.getUrl());
                        }
                        return contains;
                    }

                    /**
                     * 记录文件
                     * @param url
                     */
                    private void record(String url) {
                        try {
                            FileUtils.writeStringToFile(file, url + "\r\n", Charsets.UTF_8, true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void resetDuplicateCheck(Task task) {
                        cacheUrl.clear();
                    }

                    @Override
                    public int getTotalRequestsCount(Task task) {
                        return cacheUrl.size();
                    }
                });
    }


    @Override
    public void close() throws IOException {
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        queue.add(request);
        cacheUrl.add(request.getUrl());
    }



    @Override
    public int getLeftRequestsCount(Task task) {
        return queue.size();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return getDuplicateRemover().getTotalRequestsCount(task);
    }


    @Override
    public Request poll(Task task) {
        return queue.poll();
    }
}
