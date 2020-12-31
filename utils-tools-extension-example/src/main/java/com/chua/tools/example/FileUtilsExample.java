package com.chua.tools.example;

import com.chua.utils.tools.common.filefilter.FileFileFilter;
import com.chua.utils.tools.util.FileUtils;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/24
 */
@Slf4j
public class FileUtilsExample {

    public static void main(String[] args) throws InterruptedException, IOException {
        //测试获取文件夹下的文件
        testToGetTheFilesInTheFolder();
        //测试获取文件夹下的文件并过滤
        testToGetTheFilesInTheFolderAndFilter();
        //测试Bfs获取文件夹下的文件并过滤
        testToBfsGetTheFilesInTheFolderAndFilter();
        //测试获取系统卷标
        testToGetSystemVolume();
        //测试获取系统卷标文件
        testToGetSystemVolumeFile();
        //测试文件拷贝
        testToFileCopy();
    }

    private static void testToFileCopy() throws IOException {
        log.info("文件拷贝");
        FileUtils.copyFile(Resources.getResource("Everything.exe"), "E:/et");
    }

    private static void testToGetSystemVolume() {
        log.info("系统卷标: {}", FileUtils.ofVolumes());
    }

    private static void testToGetSystemVolumeFile() {
        log.info("系统卷标文件: {}", FileUtils.ofVolumesFile());
    }

    private static void testToBfsGetTheFilesInTheFolderAndFilter() {
        log.info("获取文件夹下的文件并过滤[*.txt]：{}", FileUtils.ofBfs("E://", "*.txt").size());
    }

    private static void testToGetTheFilesInTheFolderAndFilter() {
        log.info("获取文件夹下的文件并过滤[*.txt]：{}", FileUtils.of("E://", "*.txt").size());
    }

    private static void testToGetTheFilesInTheFolder() {
        log.info("获取文件夹下的文件：{}", FileUtils.of("E://", new FileFileFilter()).size());
    }
}
