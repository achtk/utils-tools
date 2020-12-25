package com.chua.utils.tools.search;

import com.chua.utils.tools.aware.NamedFactoryAware;
import com.chua.utils.tools.util.CmdUtils;
import com.chua.utils.tools.util.FileUtils;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * linux 文件查询
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
@Slf4j
public class LinuxFileSearch implements FileSearch, NamedFactoryAware {

    private static final List<File> ROOTS = FileUtils.ofVolumesFile();

    @Override
    public List<File> locale(String index) {
        long startTime = System.currentTimeMillis();
        String exec = CmdUtils.exec("locate " + index);
        List<File> result = Strings.isNullOrEmpty(exec) ? Collections.emptyList() : Splitter.on("\r\n").omitEmptyStrings().trimResults().splitToList(exec).stream().map(item -> new File(item)).collect(Collectors.toList());
        try {
            return result;
        } finally {
            log.info("本次检索条件{}, 共检索到文件: {}个, 耗时: {}ms", index, result.size(), (System.currentTimeMillis() - startTime));
        }
    }

    @Override
    public List<File> find(String index) {
        return locale(index);
    }

    @Override
    public String named() {
        if (!FileUtils.isWindows()) {
            return "engine";
        }
        return null;
    }
}
