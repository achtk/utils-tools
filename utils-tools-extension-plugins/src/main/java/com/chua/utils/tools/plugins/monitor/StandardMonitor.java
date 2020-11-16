package com.chua.utils.tools.plugins.monitor;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.plugins.paths.PathManager;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * 监视器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class StandardMonitor implements Monitor, Runnable {

    private String path;
    private ExecutorService executorService;
    private WatchService watchService;
    private PathManager pathManager;

    public StandardMonitor(String path, PathManager pathManager) {
        this.path = path;
        this.pathManager = pathManager;
        this.executorService = ThreadHelper.newSingleThreadExecutor("plugin-monitor");
        this.initialScanner();
    }

    /**
     * 初始化扫描
     */
    private void initialScanner() {
        FileHelper.doWith(path, path -> {
            if (path.toFile().isFile()) {
                pathManager.postCreate(path);
            }
        });
    }

    @Override
    public void monitor() throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(this.path);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        this.executorService.execute(this);
    }

    @Override
    public void close() throws Exception {
        if (null != this.executorService) {
            this.executorService.shutdown();
        }
    }

    @Override
    public void run() {
        while (true) {
            WatchKey watchKey = null;
            try {
                watchKey = watchService.take();
            } catch (InterruptedException e) {
                continue;
            }
            String string = watchKey.watchable().toString();
            for (WatchEvent<?> pollEvent : watchKey.pollEvents()) {
                WatchEvent.Kind<?> kind = pollEvent.kind();
                Path path = Paths.get(string, pollEvent.context().toString());
                //防止window占用
                try {
                    Thread.sleep(TimeUnit.MILLISECONDS.toMillis(30L));
                } catch (InterruptedException e) {
                    continue;
                }
                if (kind == ENTRY_CREATE) {
                    pathManager.postCreate(path);
                } else if (kind == ENTRY_DELETE) {
                    pathManager.postDelete(path);
                } else if (kind == ENTRY_MODIFY) {
                    pathManager.postModify(path);
                }
            }
            watchKey.reset();
        }
    }
}
