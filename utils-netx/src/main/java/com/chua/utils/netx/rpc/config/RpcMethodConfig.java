package com.chua.utils.netx.rpc.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * rpc-method
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 15:48
 */
@Setter
@Getter
public class RpcMethodConfig {

    /**
     * 方法名称，无法做到重载方法的配置
     */
    private String name;

    /**
     * The Parameters. 自定义参数
     */
    protected Map<String, String> parameters;

    /**
     * The Timeout. 远程调用超时时间(毫秒)
     */
    protected Integer timeout;

    /**
     * The Retries. 失败后重试次数
     */
    protected Integer retries;

    /**
     * 调用方式
     */
    protected String invokeType;

    /**
     * The Validation. 是否jsr303验证
     */
    protected Boolean validation;

    /**
     * 最大并发执行（不管服务端还是客户端）
     */
    protected Integer concurrents;

    /**
     * 是否启用客户端缓存
     */
    protected Boolean cache;

    /**
     * 是否启动压缩
     */
    protected String compress;

    /**
     * 目标参数（机房/分组）索引，第一个参数从0开始
     */
    protected Integer dstParam;
}
