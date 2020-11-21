package com.chua.tools.spider.url;

import redis.clients.jedis.Jedis;

/**
 * 申请两个 Redis Key：
 * <pre>
 *      unVisitedUrl：待采集URL池
 *      visitedUrl：已采集URL池
 * </pre>
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class RedisUrlLoader implements UrlLoader {

    private Jedis jedis;

    private String unVisitedUrl = "unVisitedUrl";
    private String visitedUrl = "visitedUrl";

    public RedisUrlLoader(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public boolean addUrl(String url) {
        jedis.lpushx(unVisitedUrl, url);
        return true;
    }

    @Override
    public String getUrl() {
        String link = jedis.rpop(unVisitedUrl);
        jedis.lpushx(visitedUrl, link);
        return link;
    }

    @Override
    public int nowNum() {
        return jedis.llen(unVisitedUrl).intValue();
    }

    @Override
    public int finishedNum() {
        return jedis.llen(visitedUrl).intValue();
    }
}
