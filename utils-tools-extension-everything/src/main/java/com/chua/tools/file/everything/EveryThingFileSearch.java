package com.chua.tools.file.everything;

import com.chua.utils.tools.aware.NamedFactoryAware;
import com.chua.utils.tools.aware.OrderAware;
import com.chua.utils.tools.search.FileSearch;
import com.chua.utils.tools.util.CmdUtils;
import com.chua.utils.tools.util.FileUtils;
import com.chua.utils.tools.util.ProfileUtils;
import com.google.common.io.Resources;
import com.sun.jna.WString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * every thing file search
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
@Slf4j
public class EveryThingFileSearch implements FileSearch, OrderAware, NamedFactoryAware {

    private static final String EVERYTHING = "Everything.exe";

    {
        checkIfEverythingStarted();
    }

    @Override
    public int order() {
        return 1;
    }


    /**
     * 检测everything状态
     */
    private void checkIfEverythingStarted() {
        URL url = Resources.getResource(EVERYTHING);
        if (null == url) {
            log.info("everything不存在");
        }
        String userHome = ProfileUtils.getUserHome();
        try {
            FileUtils.copyFile(url, userHome);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CmdUtils.execProcess(userHome + "/" + EVERYTHING + " -startup");
    }

    @Override
    public List<File> locale(String index) {
        long startTime = System.currentTimeMillis();

        Everything.instanceDll.Everything_SetSearchW(new WString(index));
        Everything.instanceDll.Everything_QueryW(true);
        Buffer p = CharBuffer.allocate(260);
        List<File> result = new ArrayList<>();
        for (int i = 0; i < Everything.instanceDll.Everything_GetNumResults(); i++) {
            Everything.instanceDll.Everything_GetResultFullPathNameW(i, p, 260);
            char[] buf = (char[]) p.array();
            result.add(new File(new String(buf).trim()));
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
        if (FileUtils.isWindows()) {
            return "engine";
        }
        return null;
    }
}
