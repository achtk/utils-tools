package com.chua.utils.tools.spider.config.scheduler;

import com.chua.utils.netx.datasource.properties.DataSourceProperties;
import com.chua.utils.netx.datasource.template.StandardJdbcOperatorTemplate;
import com.chua.utils.netx.datasource.transform.JdbcOperatorTransform;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.properties.OperatorProperties;
import net.sf.cglib.beans.BeanMap;
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
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 增加去重的校验，对需要重复爬取的网址进行正则过滤
 *
 * @author CH
 */
public class DbCacheQueueScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, Closeable {

    private OperatorProperties operatorProperties;
    private StandardJdbcOperatorTemplate jdbcOperatorTemplate;
    private static final String SPIDER_NAME = "T_SPIDER_INFO";
    private static final String SPIDER_URL = "COL_URL_NAME";
    private static final String CREATE_TABLE = "CREATE TABLE " + SPIDER_NAME + "(" + SPIDER_NAME + " VARCHAR(255))";
    private static final String SELECT_TABLE = "SELECT * FROM " + SPIDER_NAME;
    private static final String INSERT_TABLE = "INSERT INTO " + SPIDER_NAME + " VALUES (?)";

    private BlockingQueue<Request> queue = new LinkedBlockingQueue<>();
    private JdbcOperatorTransform jdbcOperatorTransform = new JdbcOperatorTransform();

    private Set<String> cacheUrl = new HashSet<>();
    private ExecutorService executorService = ThreadHelper.newSingleThreadExecutor("monitor spider");
    private final Queue<String> urlQueue = new LinkedBlockingQueue<>();

    public DbCacheQueueScheduler(OperatorProperties operatorProperties) {
        this.operatorProperties = operatorProperties;
        this.jdbcOperatorTemplate = new StandardJdbcOperatorTemplate(jdbcOperatorTransform.transform(operatorProperties));

        this.initial();
    }

    public DbCacheQueueScheduler(String username, String password, String driver, String url) {
        this.operatorProperties = new OperatorProperties();
        operatorProperties.username(username);
        operatorProperties.password(password);
        operatorProperties.url(url);
        operatorProperties.driver(driver);

        this.jdbcOperatorTemplate = new StandardJdbcOperatorTemplate(jdbcOperatorTransform.transform(operatorProperties));

        this.initial();
    }

    private void initial() {
        try {
            jdbcOperatorTemplate.execute(CREATE_TABLE);
        } catch (Exception e) {
        }
        try {
            List<Map<String, Object>> maps = jdbcOperatorTemplate.queryForList(SELECT_TABLE);
            for (Map<String, Object> map : maps) {
                Object value = map.get("tSpiderInfo");
                if (null != value) {
                    cacheUrl.add(value.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.execute(() -> {
            while (true) {
                String poll = urlQueue.poll();
                if (null == poll) {
                    continue;
                }
                List<List<Object>> urls = new ArrayList<>();
                while (true) {
                    String poll1 = urlQueue.poll();
                    if (null == poll1) {
                        break;
                    }
                    urls.add(Collections.singletonList(poll1));
                }

                synchronized (jdbcOperatorTemplate) {
                    try {
                        jdbcOperatorTemplate.batch(INSERT_TABLE, urls);
                    } catch (Exception e) {
                        for (List<Object> url : urls) {
                            for (Object o : url) {
                                urlQueue.add(o.toString());
                            }
                        }
                        continue;
                    }
                }
            }
        });
        this.initDuplicateRemover();
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
                        if (!contains) {
                            cacheUrl.add(request.getUrl());
                            urlQueue.add(request.getUrl());
                        }
                        return contains;
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
