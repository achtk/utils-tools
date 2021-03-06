package com.chua.tools.example;

import com.chua.utils.tools.search.FileSearch;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.factory.ExtensionFactory;

import java.io.File;
import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
public class FileSearchExample extends BaseExample {

    public static void main(String[] args) {
        ExtensionLoader<FileSearch> extensionLoader = ExtensionFactory.getExtensionLoader(FileSearch.class);
        FileSearch fileSearch = extensionLoader.getExtension();
        FileSearch bfs = extensionLoader.getExtension("bfs");

        List<File> locale = fileSearch.locale("*.json");
        log.info("file search *.json: {}", locale.size());
        List<File> locale1 = bfs.locale("*.json");
        log.info("bfs search *.json: {}", locale1.size());

    }
}
