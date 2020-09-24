package com.chua.utils.tools.spring.config;

import com.google.common.collect.Lists;
import lombok.Data;

import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 模块配置
 * @author CH
 * @version 1.0.0
 * @className ModuleConfig
 * @since 2020/8/4 15:44
 */
@Data
public class ModuleProperties {

    /**
     * 模块名,建议用英文命名,忽略大小写
     */
    private String name;

    /**
     * 模块描述
     */
    private String desc;

    /**
     * 是否启用模块,默认启用
     */
    private Boolean enabled = true;

    /**
     * spring扫描注解的包，当该set不为空时启动包扫描，将自动扫描注解形式的bean
     * <p>
     * <strong>当xml中和注解同时定义了一个相同名字的bean将会以xml中的为主，也就是注解定义的bean会被xml定义的bean 覆盖</strong>
     * <p>
     * <strong>xml中的bean不能依赖注解bean，注解bean可以依赖xml定义的bean</strong>
     */
    private Set<String> scanPackages = new CopyOnWriteArraySet<>();

    /**
     * 模块的版本，如1.0.0.20120609 版本变化会触发模块重新部署
     */
    private String version;

    /**
     * 模块里的BEAN需要的配置信息,集成了SPING properties
     */
    private Properties properties = new Properties();

    /**
     * 模块指定需要覆盖的Class的包名,不遵循双亲委派, 模块的类加载器加载这些包
     * <p>
     * 如果子模块中加载不到那么仍然会到父容器中加载
     */
    private List<String> overridePackages = Lists.newArrayList();

    /**
     * JAR 包资源地址,模块存放的地方
     */
    private List<URL> moduleUrl = Lists.newArrayList();
    /**
     *
     */
    private boolean isNeedUnloadOldVersion = true;
}
