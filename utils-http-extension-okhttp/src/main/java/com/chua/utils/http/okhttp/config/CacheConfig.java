package com.chua.utils.http.okhttp.config;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * @author CH
 */
@Getter@Setter
public class CacheConfig {
    /**
     * 缓存文件
     */
    private File cacheFile;
    /**
     * 缓存大小
     */
    private long cacheSize;
}
