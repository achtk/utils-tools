package com.chua.utils.tools.plugins.manager;

import com.chua.utils.tools.plugins.classes.ClassManager;
import com.chua.utils.tools.plugins.classes.StandardClassManager;
import com.chua.utils.tools.plugins.config.PluginConfiguration;
import com.chua.utils.tools.plugins.monitor.Monitor;
import com.chua.utils.tools.plugins.monitor.StandardMonitor;
import com.chua.utils.tools.plugins.paths.PathManager;
import com.chua.utils.tools.plugins.paths.StandardPathManager;
import lombok.Getter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * 插件管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class StandardPluginManager implements PluginManager {

    private final Monitor monitor;
    private final PathManager pathManager;
    @Getter
    private final ClassManager classManager;
    private PluginConfiguration pluginConfiguration;

    public StandardPluginManager(String path) throws IOException {
        this(new PluginConfiguration(path, false));
    }

    public StandardPluginManager(PluginConfiguration pluginConfiguration) throws IOException {
        this.pluginConfiguration = pluginConfiguration;
        this.classManager = new StandardClassManager();
        this.pathManager = new StandardPathManager(this.classManager);
        this.monitor = new StandardMonitor(pluginConfiguration.path(), this.pathManager);
        this.monitor.monitor();
    }

    @Override
    public Collection<Class<?>> getPlugin(String className) {
        try {
            return classManager.findSubType(className);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
