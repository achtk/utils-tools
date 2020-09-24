package com.chua.tools.token.entity;

import java.util.Map;

/**
 * token config
 * @author CH
 * @since 1.0
 */
public class TokenConfig {
    /**
     * 超时时间
     */
    private long expire = -1;
    /**
     * 基础信息
     */
    private Map<String, Object> message;


    public TokenConfig(long expire) {
        this.expire = expire;
    }

    public TokenConfig() {
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public Map<String, Object> getMessage() {
        return message;
    }

    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }
}
