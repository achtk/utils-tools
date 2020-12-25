package com.chua.utils.tools.search;

import com.chua.utils.tools.aware.NamedFactoryAware;
import com.chua.utils.tools.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * bfs 文件查询
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
@Slf4j
public class BfsFileSearch implements FileSearch, NamedFactoryAware {

    private static final List<File> ROOTS = FileUtils.ofVolumesFile();

    @Override
    public List<File> locale(String index) {
        long startTime = System.currentTimeMillis();

        List<File> result = new ArrayList<>();
        for (File root : ROOTS) {
            result.addAll(FileUtils.ofBfs(root.getAbsolutePath(), index));
        }
        log.info("本次检索条件{}, 共检索到文件: {}个, 耗时: {}ms", index, result.size(), (System.currentTimeMillis() - startTime));
        return result;
    }

    @Override
    public List<File> find(String index) {
        return locale(index);
    }

    @Override
    public String named() {
        return "engine";
    }
}
