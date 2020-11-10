package com.chua.utils.tools.manager.producer;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.function.able.InitializingCacheable;
import com.chua.utils.tools.manager.ProfileAdaptorManager;
import com.chua.utils.tools.prop.loader.ProfileLoader;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 标准的配置文件适配管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class StandardProfileAdaptorManager extends InitializingCacheable implements ProfileAdaptorManager {

    private static final CacheProvider<String, ProfileLoader> PROFILE_LOADER_MAP = new ConcurrentCacheProvider<String, ProfileLoader>();

    private final ExtensionProcessor extensionProcessor;

    public StandardProfileAdaptorManager(ExtensionProcessor extensionProcessor) {
        this.extensionProcessor = extensionProcessor;
        Map<String, ProfileLoader> extension = ExtensionFactory.getTempExtensionLoader(ProfileLoader.class, extensionProcessor).getPriorityExtension();
        PROFILE_LOADER_MAP.putAll(extension);
    }

    @Override
    public ProfileLoader get(String name) {
        return PROFILE_LOADER_MAP.get(name);
    }

    @Override
    public Set<String> names() {
        return PROFILE_LOADER_MAP.asMap().keySet();
    }
}
