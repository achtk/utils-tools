package com.chua.utils.tools.spring.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.context.ConfigurableApplicationContext;

import java.beans.Introspector;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 模块
 * @author CH
 * @version 1.0.0
 * @className Module
 * @since 2020/8/4 15:47
 */
@Data
@Slf4j
public class Module {

    private ModuleProperties moduleProperties;
    private final ConfigurableApplicationContext applicationContext;

    public Module(ModuleProperties moduleProperties, ConfigurableApplicationContext applicationContext) {
        this.moduleProperties = moduleProperties;
        this.applicationContext = applicationContext;
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (log.isInfoEnabled()) {
            log.info("Close application context: {}", applicationContext);
        }
        //close spring context
        closeQuietly(applicationContext);
        //clean classloader
        clear(applicationContext.getClassLoader());
    }

    /**
     * 清除类加载器
     *
     * @param classLoader
     */
    public static void clear(ClassLoader classLoader) {
        checkNotNull(classLoader, "classLoader is null");
        Introspector.flushCaches();
        //从已经使用给定类加载器加载的缓存中移除所有资源包
        ResourceBundle.clearCache(classLoader);
        //Clear the introspection cache for the given ClassLoader
        CachedIntrospectionResults.clearClassLoader(classLoader);
        //LogFactory.release(classLoader);
    }

    /**
     * 关闭Spring上下文
     * @param applicationContext
     */
    private static void closeQuietly(ConfigurableApplicationContext applicationContext) {
        checkNotNull(applicationContext, "applicationContext is null");
        try {
            applicationContext.close();
        } catch (Exception e) {
            log.error("Failed to close application context", e);
        }
    }
}
