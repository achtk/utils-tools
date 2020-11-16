package com.chua.utils.tools.plugins.paths;

import com.chua.utils.tools.plugins.classes.ClassManager;

import java.nio.file.Path;

/**
 * 路径管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class StandardPathManager implements PathManager {

    private final ClassManager classManager;

    public StandardPathManager(ClassManager classManager) {
        this.classManager = classManager;
    }

    @Override
    public void postCreate(Path path) {
        classManager.register(path);
    }

    @Override
    public void postDelete(Path path) {
        classManager.unregister(path);
    }

    @Override
    public void postModify(Path path) {
        classManager.unregister(path);
        classManager.register(path);
    }
}
