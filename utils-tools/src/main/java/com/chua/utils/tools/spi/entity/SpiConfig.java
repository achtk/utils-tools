package com.chua.utils.tools.spi.entity;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * spi配置
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 15:32
 */
@Getter
@Setter
public class SpiConfig {
    /**
     * 扩展注解
     * 注解默认@Spi
     */
    private Class<? extends Annotation> extension;
    /**
     * 目录
     */
    private List<String> extensionLoaderPath = Lists.newArrayList("META-INF/extension", "META-INF/extensions");
}
