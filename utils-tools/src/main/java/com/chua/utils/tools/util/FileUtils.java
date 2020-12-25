package com.chua.utils.tools.util;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.common.filefilter.FileFileFilter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * 文件工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
@Slf4j
public class FileUtils extends FileHelper {

    /**
     * 检索文件夹下的文件
     *
     * @param folder     文件夹
     * @param fileFilter 过滤器
     * @return 匹配的文件
     */
    public static List<File> of(final String folder, final FileFilter fileFilter) {
        if (Strings.isNullOrEmpty(folder)) {
            return Collections.emptyList();
        }
        return of(new File(folder), fileFilter, null);
    }

    /**
     * 检索文件夹下的文件
     *
     * @param folder 文件夹
     * @param match  匹配
     * @return 匹配的文件
     */
    public static List<File> of(final String folder, final String match) {
        if (Strings.isNullOrEmpty(folder)) {
            return Collections.emptyList();
        }
        return of(new File(folder), new FileFileFilter(), match);
    }

    /**
     * 检索文件夹下的文件
     *
     * @param folder     文件夹
     * @param fileFilter 过滤器
     * @param match      匹配
     * @return 匹配的文件
     */
    public static List<File> of(final File folder, final FileFilter fileFilter, final String match) {
        if (null == folder || !folder.isDirectory()) {
            return Collections.emptyList();
        }
        long startTime = System.currentTimeMillis();
        List<File> result = new ArrayList<>();
        try {
            Files.walkFileTree(folder.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    File file1 = file.toFile();
                    boolean accept = fileFilter.accept(file1);
                    if (accept) {
                        if (null == match) {
                            result.add(file1);
                        }
                        if (wildcardMatch(file1.getAbsolutePath(), match)) {
                            result.add(file1);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (log.isDebugEnabled()) {
            log.debug("nio 查询文件耗时: {}ms", (System.currentTimeMillis() - startTime));
        }
        return result;
    }

    /**
     * 广度优先搜索文件或文件夹
     *
     * @param path  要搜索的目录
     * @param regex 搜索的通配符
     */
    public static List<File> ofBfs(String path, String regex) {
        long startTime = System.currentTimeMillis();

        List<File> result = new ArrayList<>();
        Queue<File> queue = new LinkedBlockingQueue<>();
        File[] fs = new File(path).listFiles();
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(ThreadHelper.newProcessorThreadExecutor());

        //遍历第一层
        for (File f : fs) {
            //把第一层文件夹加入队列
            if (f.isDirectory()) {
                queue.offer(f);
            } else {
                if (wildcardMatches(f.getAbsolutePath(), regex)) {
                    result.add(f);
                }
            }
        }

        List<ListenableFuture<List<File>>> listenableFutures = new ArrayList<>();
        int processor = ThreadHelper.processor();
        for (int i = 0; i < processor; i++) {
            listenableFutures.add(listeningExecutorService.submit(() -> {
                List<File> result1 = new ArrayList<>();
                //逐层搜索下去
                while (!queue.isEmpty()) {
                    //从队列头取一个元素
                    File fileTemp = queue.poll();
                    if (null == fileTemp) {
                        continue;
                    }
                    File[] fileListTemp = fileTemp.listFiles();
                    if (fileListTemp == null) {
                        //遇到无法访问的文件夹跳过
                        continue;
                    }
                    for (File f : fileListTemp) {
                        if (f.isDirectory()) {
                            //从队列尾插入一个元素
                            queue.offer(f);
                        } else {
                            if (wildcardMatches(f.getAbsolutePath(), regex)) {
                                result1.add(f);
                            }
                        }
                    }
                }
                return result1;
            }));
        }

        try {
            List<List<File>> lists = Futures.successfulAsList(listenableFutures).get();
            for (List<File> list : lists) {
                result.addAll(list);
            }
            listeningExecutorService.shutdownNow();
        } catch (Exception e) {
        }

        if (log.isDebugEnabled()) {
            log.debug("广度查询文件耗时: {}ms", (System.currentTimeMillis() - startTime));
        }
        return result;
    }

    /**
     * 获取系统卷标
     *
     * @return 系统卷标
     */
    public static List<File> ofVolumesFile() {
        return Arrays.asList(File.listRoots());
    }

    /**
     * 获取系统卷标
     *
     * @return 系统卷标
     */
    public static List<File> ofVolumes() {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        return ofVolumesFile().stream().map(file -> new File(fileSystemView.getSystemDisplayName(file))).collect(Collectors.toList());
    }

    /**
     * 文件拷贝
     *
     * @param urls       urls
     * @param targetPath 目标目录
     * @throws IOException IOException
     */
    public static void copyFiles(URL[] urls, String targetPath) throws IOException {
        copyFiles(urls, targetPath, false);
    }

    /**
     * 文件拷贝
     *
     * @param urls       urls
     * @param targetPath 目标目录
     * @param overlay    覆盖
     * @throws IOException IOException
     */
    public static void copyFiles(URL[] urls, String targetPath, boolean overlay) throws IOException {
        Preconditions.checkNotNull(urls);
        createIfNotExist(targetPath);
        copyFiles(Arrays.stream(urls).map(url -> new File(url.getFile())).toArray(File[]::new), targetPath, overlay);
    }

    /**
     * 文件拷贝
     *
     * @param files      文件
     * @param targetPath 目标目录
     * @throws IOException IOException
     */
    public static void copyFiles(File[] files, String targetPath) throws IOException {
        copyFiles(files, targetPath, false);
    }

    /**
     * 文件拷贝
     *
     * @param files      文件
     * @param targetPath 目标目录
     * @param overlay    覆盖
     * @throws IOException IOException
     */
    public static void copyFiles(File[] files, String targetPath, boolean overlay) throws IOException {
        Preconditions.checkNotNull(files);
        createIfNotExist(targetPath);

        List<Exception> exceptions = new ArrayList<>();
        Arrays.stream(files).forEach(url -> {
            try {
                copyFile(url, targetPath, overlay);
            } catch (IOException e) {
                exceptions.add(e);
            }
        });

        exceptions.forEach(e -> e.printStackTrace());
    }

    /**
     * 文件拷贝
     *
     * @param url        url
     * @param targetPath 目标目录
     * @throws IOException IOException
     */
    public static void copyFile(URL url, String targetPath) throws IOException {
        copyFile(url, targetPath, false);
    }

    /**
     * 文件拷贝
     *
     * @param url        url
     * @param targetPath 目标目录
     * @param overlay    覆盖
     * @throws IOException IOException
     */
    public static void copyFile(URL url, String targetPath, boolean overlay) throws IOException {
        createIfNotExist(targetPath);
        copyFile(toFile(url), targetPath, overlay);
    }

    /**
     * 文件拷贝
     *
     * @param sourceFile 源文件
     * @param targetPath 目标目录
     * @throws IOException IOException
     */
    public static void copyFile(File sourceFile, String targetPath) throws IOException {
        copyFile(sourceFile, targetPath, false);
    }

    /**
     * 文件拷贝
     *
     * @param sourceFile 源文件
     * @param targetPath 目标目录
     * @param overlay    覆盖
     * @throws IOException IOException
     */
    public static void copyFile(File sourceFile, String targetPath, boolean overlay) throws IOException {
        if (null == sourceFile) {
            return;
        }
        createIfNotExist(targetPath);
        Path path = Paths.get(targetPath, sourceFile.getName());
        boolean exists = Files.exists(path);
        if (!exists || (exists && overlay)) {
            CopyOption[] copyOptions = new CopyOption[0];
            if (exists) {
                copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
            }
            Files.copy(sourceFile.toPath(), path, copyOptions);
        }
    }

    /**
     * 如果不存在则创建文件夹
     *
     * @param folder 文件夹
     */
    public static void createIfNotExist(final String folder) throws IOException {
        if (Strings.isNullOrEmpty(folder)) {
            return;
        }

        Path path = Paths.get(folder);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    /**
     * 如果不存在则创建文件夹
     *
     * @param folder 文件夹
     */
    public static void createIfNotExist(final File folder) throws IOException {
        if (null == folder) {
            return;
        }
        createIfNotExist(folder.getAbsolutePath());
    }

    /**
     * 获取文件
     *
     * @param url url
     * @return 文件
     */
    private static File toFile(URL url) {
        return null != url ? new File(url.getFile()) : null;
    }
}
