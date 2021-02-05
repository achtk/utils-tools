package com.chua.utils.tools.config;

import com.chua.utils.tools.action.ActionListener;
import com.google.common.cache.CacheLoader;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

/**
 * 缓存配置项
 *
 * @author CH
 */
@Getter
@Setter
public class CacheProperties {
    /**
     * 缓存名称
     */
    private String name;

    /**
     * 是否永久，true超时无效
     */
    private boolean eternal;
    /**
     * 类加载器
     */
    private CacheLoader<Object, Object> cacheLoader;
    /**
     * 内存存储最大数量
     */
    private int memMaximumSize = 500000;
    /**
     * 硬盘存储最大数量
     */
    private long diskMaximumSize = 500000;
    /**
     * 超时时间
     */
    private long expire = 3L;
    /**
     * 超时时间
     */
    private long writeExpire = -1L;
    /**
     * 本地缓存目录
     */
    private String dir = ".";

    /**
     * 删除监听
     */
    private ActionListener removeListener;

    /**
     * 更新监听
     */
    private ActionListener updateListener;
    /**
     * 额外属性
     */
    private Properties properties;
}
