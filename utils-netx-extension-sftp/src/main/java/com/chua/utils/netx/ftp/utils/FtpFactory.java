package com.chua.utils.netx.ftp.utils;

import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.netx.ftp.handler.FtpFileHandler;
import com.chua.utils.netx.ftp.reader.FtpReader;
import com.chua.utils.netx.ftp.status.*;
import com.chua.utils.tools.common.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 支持断点续传的FTP实用类
 *
 * @author CH
 * @version 0.3 实现中文目录创建及中文文件创建，添加对于中文的支持
 */
@Slf4j
public class FtpFactory implements INetFactory<FTPClient> {

    private static final String WINDOW = "window";
    private static final String TYPE_FILE = "文件";
    private static final String TYPE_DIRECTORY = "文件夹";
    public FTPClient ftpClient = new FTPClient();
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final int CON_LIMIT = Runtime.getRuntime().availableProcessors() * 2 + 3;
    private static final ArrayBlockingQueue<FTPClient> QUEUE = new ArrayBlockingQueue<FTPClient>(CON_LIMIT);
    private String host;
    private int port;
    private String username;
    private String password;
    private NetProperties netProperties;


    public FtpFactory() {
        //设置将过程中使用到的命令输出到控制台
        // this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
    }

    public static FtpFactory getFTPClient(String ip, String username, String password, int port) {
        FtpFactory ftpFactory = new FtpFactory();
        try {
            ftpFactory.connect(ip, port, username, password);
        } catch (IOException e) {
            log.error("连接FTP失败");
        }
        return ftpFactory;
    }

    /**
     * 连接到FTP服务器
     *
     * @param host     主机名
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return 是否连接成功
     * @throws IOException
     */
    public FTPClient connect(String host, int port, String username, String password) throws IOException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(host, port);
        if (OS_NAME.contains(WINDOW)) {
            ftpClient.setControlEncoding("GBK");
        } else {
            ftpClient.setControlEncoding("UTF-8");
        }
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(username, password)) {
                this.ftpClient = ftpClient;
                return ftpClient;
            }
        }
        return null;
    }

    /**
     * 显示目录下的文件
     *
     * @param directory
     * @return
     */
    public List<Map<String, String>> list(final String directory) throws IOException {
        return list(false, directory);
    }

    /**
     * 显示目录下的文件
     *
     * @param directory
     * @return
     */
    public List<Map<String, String>> listAll(final String directory) throws IOException {
        return list(true, directory);
    }

    /**
     * 显示目录下的文件
     *
     * @return
     */
    public List<Map<String, String>> list() throws IOException {
        return list(false, "/");
    }

    /**
     * 显示目录下的文件
     *
     * @return
     */
    public List<Map<String, String>> listAll() throws IOException {
        return list(true, "/");
    }

    /**
     * 删除文件夹
     *
     * @param remote 远程文件
     * @return
     */
    public List<ListDeleteStatus> deleteFolder(String remote) throws IOException {
        List<ListDeleteStatus> listDeleteStatuses = new ArrayList<>();
        //设置被动模式
        ftpClient.enterLocalPassiveMode();
        //设置以二进制方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        remote = getFileName(remote);

        FTPFile[] ftpFiles = ftpClient.listFiles(remote);
        if (ftpFiles.length < 0) {
            log.error("远程文件夹不存在");
            listDeleteStatuses.add(new ListDeleteStatus().deleteStatus(DeleteStatus.REMOTE_FILE_NOEXIST));
            return listDeleteStatuses;
        }

        for (FTPFile ftpFile : ftpFiles) {
            String pathFile = getPathFile(ftpFile);
            ListDeleteStatus listDeleteStatus = new ListDeleteStatus();
            int type = ftpFile.getType();
            if (0 == type) {
                boolean b = ftpClient.deleteFile(remote + pathFile);
                listDeleteStatus.deleteStatus(b ? DeleteStatus.DELETE_FROM_BREAK_SUCCESS : DeleteStatus.DELETE_FROM_BREAK_FAILED);
                listDeleteStatus.file(remote + pathFile);
                listDeleteStatuses.add(listDeleteStatus);
            } else {
                deleteFolder(remote + pathFile);
            }
        }

        ftpClient.removeDirectory(remote);
        return listDeleteStatuses;
    }

    /**
     * 删除文件
     *
     * @param remote 远程文件
     * @return
     */
    public DeleteStatus deleteFile(String remote) throws IOException {
        //设置被动模式
        ftpClient.enterLocalPassiveMode();
        //设置以二进制方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        remote = getFileName(remote);

        FTPFile[] ftpFiles = ftpClient.listFiles(remote);
        if (ftpFiles.length < 0) {
            log.error("远程文件不存在");
            return DeleteStatus.REMOTE_FILE_NOEXIST;
        }
        boolean b = ftpClient.deleteFile(remote);
        return b ? DeleteStatus.DELETE_FROM_BREAK_SUCCESS : DeleteStatus.DELETE_FROM_BREAK_FAILED;
    }

    /**
     * 文件是否存在
     *
     * @param ftpClient
     * @param remote    远程
     * @return
     */
    public FTPFile exist(FTPClient ftpClient, final String remote) {
        //检查远程文件是否存在
        FTPFile[] files = new FTPFile[0];
        try {
            files = ftpClient.listFiles(remote);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (files.length < 1) {
            try {
                files = ftpClient.listDirectories(remote);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (files.length < 1) {
                //log.error("远程文件不存在");
                return null;
            }
        }
        if (files.length > 1) {
            FTPFile ftpFile = new FTPFile();
            ftpFile.setName(remote);
            ftpFile.setType(1);
            return ftpFile;
        }

        return files[0];
    }

    /**
     * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
     *
     * @param remote 远程文件夹路径
     * @param local  本地文件夹路径
     * @param muilti 多线程
     * @return 上传的状态
     * @throws IOException
     */
    public DownloadStatus downloadFolderFlow(final String remote, final String local, final boolean muilti) throws IOException {
        //设置被动模式
        ftpClient.enterLocalPassiveMode();
        //设置以二进制方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        List<Map<String, String>> folders = new ArrayList<>();
        List<Map<String, String>> files = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        // ObjectPoolFactory<FTPClient> objectPoolFactory = ObjectPoolFactory.newPool(ftpClient, 30);
        ExecutorService executorService = Executors.newCachedThreadPool();

        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final AtomicInteger finish = new AtomicInteger(0);
        getAllFileAndFolder(remote, folders, files, new FtpFileHandler() {
            @Override
            public void executor(Map<String, String> temp, int size) {
                String path = temp.get("path");
                String name = temp.get("name");
                File file = new File(local, path);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }

                Downloader downloader = new Downloader();
                downloader.setCnt(atomicInteger.incrementAndGet());
                downloader.setRemote(path);
                downloader.setLocal(local + "/" + path);
                // downloader.setTotal(size);

                executorService.execute(new Runnable() {
                    @Override
                    public synchronized void run() {
                        FTPClient object = ftpClient;
                        try {
                            downloadFile(object, downloader);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            //  objectPoolFactory.returnObject(object);
                            if (log.isDebugEnabled()) {
                                log.debug("文件{}下载成功。当前进度({}/{})", name, finish.incrementAndGet(), atomicInteger.get());
                            }
                        }
                    }
                });
            }
        });
        return DownloadStatus.DOWNLOAD_FROM_BREAK_SUCCESS;
    }

    /**
     * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
     *
     * @param remote 远程文件夹路径
     * @param local  本地文件夹路径
     * @param muilti 多线程
     * @return 上传的状态
     * @throws IOException
     */
    public DownloadStatus downloadFolder(final String remote, final String local, final boolean muilti) throws IOException {
        //设置被动模式
        ftpClient.enterLocalPassiveMode();
        //设置以二进制方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        List<Map<String, String>> folders = new ArrayList<>();
        List<Map<String, String>> files = new ArrayList<>();
        getAllFileAndFolder(remote, folders, files, null);

        File temp = null, parentFile = null;
        int size = files.size();
        log.debug("当前查询到文件个数:{}", size);
        final AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executorService = null;
        CountDownLatch countDownLatch = null;
        int size1 = files.size();

        if (muilti && size1 > 64) {
            countDownLatch = new CountDownLatch(files.size());
            executorService = Executors.newFixedThreadPool(CON_LIMIT);
            int ext = size1 / 50;
            if (ext > 40) {
                ext = 40;
            }
            initalFtpClient(CON_LIMIT + ext);
        }
        long startTime = System.currentTimeMillis();
        for (Map<String, String> ftpFile : files) {
            String path = ftpFile.get("path");
            String name = ftpFile.get("name");
            temp = new File(local, path);
            parentFile = temp.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            Downloader downloader = new Downloader();
            downloader.setCnt(atomicInteger.incrementAndGet());
            downloader.setRemote(path);
            downloader.setLocal(local + "/" + path);
            downloader.setTotal(size);
            try {
                if (null != executorService) {
                    FTPClient multiConnection = getMultiConnection();
                    if (null != multiConnection) {
                        downloader.setFtpClient(multiConnection);
                        CountDownLatch finalCountDownLatch = countDownLatch;
                        executorService.execute(new Runnable() {
                            @Override
                            public synchronized void run() {
                                try {
                                    downloadFile(multiConnection, downloader);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    finalCountDownLatch.countDown();
                                    close(multiConnection);
                                    if (log.isDebugEnabled()) {
                                        long count = size - finalCountDownLatch.getCount();
                                        log.debug("文件{}下载成功。当前进度({}/{})", name, count, size);
                                    }
                                }
                            }
                        });
                    } else {
                        try {
                            downloadFile(this.ftpClient, downloader);
                        } finally {
                            countDownLatch.countDown();
                            if (log.isDebugEnabled()) {
                                long count = size - countDownLatch.getCount();
                                log.debug("文件{}下载成功。当前进度({}/{})", name, count, size);
                            }
                        }
                    }
                } else {
                    try {
                        downloadFile(this.ftpClient, downloader);
                    } finally {
                        if (log.isDebugEnabled()) {
                            int i = atomicInteger.get();
                            log.debug("文件{}下载成功。当前进度({}/{})", name, i, size);
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != countDownLatch) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (null != executorService) {
                    executorService.shutdownNow();
                    executorService.shutdown();

                    if (!QUEUE.isEmpty()) {
                        for (FTPClient client : QUEUE) {
                            client.disconnect();
                            QUEUE.poll();
                        }
                    }
                }
            }
        }
        if (log.isDebugEnabled()) {
            long time = (System.currentTimeMillis() - startTime) / 1000;
            String fs = "0";
            if (size1 != 0) {
                fs = StringHelper.calcString(size1, time);
            }
            log.debug("下载文件夹整体耗时: {}s, 每秒{}文件数", time, fs);
        }
        return DownloadStatus.DOWNLOAD_FROM_BREAK_SUCCESS;
    }

    /**
     * 文件路径
     *
     * @param ftpFile
     * @return
     */
    private String getPathFile(FTPFile ftpFile) {
        return "/" + ftpFile.getName();
    }


    /**
     * 从FTP服务器下载文件,支持断点续传，上传百分比汇报
     *
     * @param remote 远程文件路径
     * @param local  本地文件路径
     */
    public DownloadStatus downloadFile(final String remote, final String local) throws IOException {
        long start = System.currentTimeMillis();
        Downloader downloader = new Downloader();
        downloader.setRemote(remote);
        downloader.setLocal(local);
        DownloadStatus downloadStatus = downloadFile(this.ftpClient, downloader);
        log.debug("[{}]文件下载耗时:{}s", remote, (System.currentTimeMillis() - start) / 1000);
        return downloadStatus;
    }

    /**
     * 从FTP服务器下载文件,支持断点续传，上传百分比汇报
     *
     * @param remote 远程文件路径
     */
    public InputStream downloadInputStream(final String remote) throws IOException {
        return ftpClient.retrieveFileStream(remote);
    }

    /**
     * 从FTP服务器下载大文件
     *
     * @param remote 远程文件路径
     * @param local  本地文件路径
     */
    public DownloadStatus downloadBigFile(final String remote, final String local) throws IOException {
        //设置被动模式
        ftpClient.enterLocalPassiveMode();
        //设置以二进制方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        String fileName = getFileName(remote);
        //检查远程文件是否存在
        FTPFile ftpFile = exist(ftpClient, fileName);
        if (null == ftpFile) {
            log.error("远程文件不存在");
            return DownloadStatus.REMOTE_FILE_NOEXIST;
        }
        final long remoteSize = ftpFile.getSize();
        final long oneFences = SizeHelper.L_M * 300;
        if (remoteSize < oneFences) {
            return downloadFile(remote, local);
        }


        long localSize = 0;
        String name = ftpFile.getName();

        File localFile = new File(local, name);
        if (localFile.exists()) {
            localSize = localFile.length();
        }
        File parentFile = localFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        if (localSize >= remoteSize) {
            log.debug("本地文件大于远程文件不进行下载");
            return DownloadStatus.DOWNLOAD_FROM_BREAK_SUCCESS;
        }
        long start = System.currentTimeMillis();
        int fences = NumberHelper.fences(remoteSize - localSize, oneFences);
        ExecutorService executorService = Executors.newFixedThreadPool(fences);
        CountDownLatch countDownLatch = new CountDownLatch(fences);

        long finalLocalSize = localSize;
        for (int i = 0; i < fences; i++) {
            int finalI = i;
            executorService.execute(new Runnable() {
                @Override
                public synchronized void run() {
                    long start = (finalI) * oneFences + finalLocalSize;
                    Long end = (finalI + 1) * oneFences + finalLocalSize;
                    int endi = end.intValue();
                    try {
                        RandomAccessFile randomAccess = new RandomAccessFile(localFile, "rw");
                        randomAccess.seek(start);
                        FTPClient connect = connect(host, port, username, password);
                        connect.setRestartOffset(start);

                        int sum = 0;
                        try (InputStream is = connect.retrieveFileStream(fileName)) {
                            byte[] bytes = new byte[1024 * 1024];
                            int line = 0;
                            while ((line = is.read(bytes)) != -1) {
                                sum += line;
                                if (log.isDebugEnabled()) {
                                    log.debug("文件[{}]#{}下载进度。当前进度{}%", name, finalI, StringHelper.calcFloat(sum, oneFences) * 100);
                                }
                                if (sum > endi) {
                                    line = sum - endi;
                                    randomAccess.write(bytes, 0, line);
                                    break;
                                } else {
                                    randomAccess.write(bytes, 0, line);
                                }
                            }
                        }

                        connect.completePendingCommand();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }

                }
            });
        }
        try {
            countDownLatch.await();
            executorService.shutdownNow();
            executorService.shutdown();
            log.debug("[{}]文件下载耗时:{}s", name, (System.currentTimeMillis() - start) / 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 上传文件到FTP服务器，支持断点续传
     *
     * @param local  本地文件名称，绝对路径
     * @param remote 远程文件路径，使用/home/directory1/subdirectory/file.ext 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @return 上传结果
     * @throws IOException
     */
    public UploadStatus uploadFile(String local, String remote) throws IOException {
        return uploadFile(local, remote, true);
    }

    /**
     * 上传文件到FTP服务器，支持断点续传
     *
     * @param local    本地文件名称，绝对路径
     * @param remote   远程文件路径，使用/home/directory1/subdirectory/file.ext 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @param override 是否覆盖
     * @return 上传结果
     * @throws IOException
     */
    public UploadStatus uploadFile(String local, String remote, boolean override) throws IOException {
        //设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();
        //设置以二进制流的方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        UploadStatus result = null;
        //创建远程文件夹
        createdirectory(local, remote);
        //根目录
        String parentPath = new File(local).getParent();
        //上传文件
        long remoteSize = 0;
        if (FileHelper.isFile(local)) {
            String replace = local.replace(parentPath, "").replace("\\", "/");
            replace = StringHelper.noRepeat(remote + replace);
            if (override) {
                deleteFile(replace);
            } else {
                FTPFile exist = exist(ftpClient, replace);
                remoteSize = null != exist ? exist.getSize() : 0;
            }
            uploadFile(replace, local, remoteSize);
        } else {
            Collection<String> files = FileHelper.listFiles(local);
            for (String file : files) {
                String replace = file.replace(parentPath, "").replace("\\", "/");
                replace = StringHelper.noRepeatSlash(remote + replace);
                if (override) {
                    deleteFile(replace);
                } else {
                    FTPFile exist = exist(ftpClient, replace);
                    remoteSize = null != exist ? exist.getSize() : 0;
                }
                uploadFile(replace, file, remoteSize);
            }
        }
        return result;
    }

    /**
     * 断开与远程服务器的连接
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }

    /**
     * 递归创建远程服务器目录
     *
     * @param local
     * @param remote 远程服务器文件绝对路径
     * @return 目录创建是否成功
     * @throws IOException
     */
    public UploadStatus createdirectory(String local, String remote) throws IOException {
        UploadStatus status = UploadStatus.CREATE_DIRECTORY_SUCCESS;

        String parentPath = new File(local).getParent();
        Collection<File> files = FileHelper.listSelfFolders(local);
        //  ListHelper.sortByLength((List<File>) files);
        for (File file : files) {
            String replace = file.getPath().replace(parentPath, "").replace("\\", "/");
            replace = StringHelper.noRepeatSlash(remote + replace);
            FTPFile exist = exist(ftpClient, getFileName(replace));
            if (null == exist) {
                boolean b = ftpClient.makeDirectory(getFileName(replace));
                if (log.isDebugEnabled() && !b) {
                    log.trace("{} 创建失败", replace);
                }
            }
        }
        return status;
    }

    /**
     * 上传文件到服务器,新上传和断点续传
     *
     * @param remoteFile 远程文件名，在上传之前已经将服务器工作目录做了改变
     * @param localFile  本地文件File句柄，绝对路径
     * @param remoteSize 需要显示的处理进度步进值
     * @return
     * @throws IOException
     */
    public UploadStatus uploadFile(String remoteFile, String localFile, long remoteSize) throws IOException {
        UploadStatus status;
        //显示进度的上传
        long step = localFile.length();
        long process = 0;
        long localreadbytes = 0L;
        RandomAccessFile raf = new RandomAccessFile(localFile, "r");
        OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"), "iso-8859-1"));
        //断点续传
        if (remoteSize > 0) {
            ftpClient.setRestartOffset(remoteSize);
            raf.seek(remoteSize);
            localreadbytes = remoteSize;
        }
        byte[] bytes = new byte[1024];
        int c;
        long point = -1;
        while ((c = raf.read(bytes)) != -1) {
            out.write(bytes, 0, c);
            localreadbytes += c;
            if (point != process) {
                point = process;
                process = localreadbytes / step * 100;
                log.debug("文件{},上传进度: {}%", remoteFile, process);
                point = process;
            }
            //TODO 汇报上传状态
        }
        out.flush();
        raf.close();
        out.close();
        boolean result = ftpClient.completePendingCommand();
        if (remoteSize > 0) {
            status = result ? UploadStatus.UPLOAD_FROM_BREAK_SUCCESS : UploadStatus.UPLOAD_FROM_BREAK_FAILED;
        } else {
            status = result ? UploadStatus.UPLOAD_NEW_FILE_SUCCESS : UploadStatus.UPLOAD_NEW_FILE_FAILED;
        }
        return status;
    }

    /**
     * 重命名
     *
     * @param remote 远程文件
     * @param rename 重命名
     * @return
     */
    public RenameStatus rename(String remote, String rename) {
        remote = getFileName(remote);
        FTPFile files = exist(ftpClient, remote);
        if (null == files) {
            log.error("远程文件不存在");
            return RenameStatus.REMOTE_FILE_NOEXIST;
        }
        try {
            rename = getFileName(rename);
            String name = FileHelper.getPath(remote);

            boolean rename1 = ftpClient.rename(remote, name + rename);
            return rename1 ? RenameStatus.RENAME_FROM_BREAK_SUCCESS : RenameStatus.RENAME_FROM_BREAK_FAILED;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return RenameStatus.RENAME_FROM_BREAK_FAILED;
    }

    /**
     * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
     *
     * @param downloader 下载信息
     * @return 上传的状态
     * @throws IOException
     */
    private DownloadStatus downloadFile(final FTPClient ftpClient, final Downloader downloader) throws IOException {
        //设置被动模式
        ftpClient.enterLocalPassiveMode();
        //设置以二进制方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        String fileName = getFileName(downloader.getRemote());
        //检查远程文件是否存在
        FTPFile ftpFile = exist(ftpClient, fileName);
        if (null == ftpFile) {
            log.error("远程文件不存在");
            return DownloadStatus.REMOTE_FILE_NOEXIST;
        }

        String local = downloader.getLocal();
        File temp = new File(local);
        if (temp.isDirectory()) {
            downloader.setLocal(downloader.getLocal() + "/" + ftpFile.getName());
        }

        return downFtpFile(ftpClient, ftpFile, fileName, downloader);
    }

    /**
     * 下载文件
     *
     * @param ftpFile    ftp文件
     * @param file       远程文件
     * @param downloader 下载配置
     * @return
     * @throws IOException
     */
    private DownloadStatus downFtpFile(final FTPClient ftpClient, final FTPFile ftpFile, final String file, final Downloader downloader) throws IOException {
        long lRemoteSize = ftpFile.getSize();
        File localFile = new File(downloader.getLocal());
        downloader.setName(ftpFile.getName());
        downloader.setRemoteSize(lRemoteSize);
        //本地存在文件，进行断点下载
        if (localFile.exists()) {
            float localSize = localFile.length();
            String name = ftpFile.getName();
            //判断本地文件大小是否大于远程文件大小
            if (localSize >= lRemoteSize) {
                log.warn("本地文件{}已存在, 且本地文件大于远程文件，下载中止！！当前进度({}/{})", name, downloader.getCnt(), downloader.getTotal());
                return DownloadStatus.LOCAL_BIGGER_REMOTE;
            }

            ftpClient.setRestartOffset(localFile.length());
            InputStream in = ftpClient.retrieveFileStream(file);
            FtpReader.readToFile(in, localFile, downloader);
            boolean isDo = ftpClient.completePendingCommand();
            if (isDo) {
                return DownloadStatus.DOWNLOAD_FROM_BREAK_SUCCESS;
            } else {
                return DownloadStatus.DOWNLOAD_FROM_BREAK_FAILED;
            }
        } else {
            if (lRemoteSize >= 0) {
                File parentFile = localFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }

                InputStream in = ftpClient.retrieveFileStream(file);
                FtpReader.readToFile(in, localFile, downloader);
            }

            boolean upNewStatus = false;
            try {
                upNewStatus = ftpClient.completePendingCommand();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (upNewStatus) {
                return DownloadStatus.DOWNLOAD_NEW_SUCCESS;
            } else {
                return DownloadStatus.DOWNLOAD_NEW_FAILED;
            }
        }
    }


    /**
     * 上传文件夹
     *
     * @param oldLocal  上传本地根目录
     * @param file      本地文件
     * @param remote    远程文件
     * @param overrider 是否覆盖
     */
    private void updateFolder(String oldLocal, File file, String remote, boolean overrider) {
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                updateFolder(oldLocal, file1, remote, overrider);
            } else {
                String path = file1.getPath();
                String newPath = path.replace(oldLocal, "").replace("\\", "/");
                try {
                    uploadFile(path, remote + newPath, overrider);
                } catch (IOException e) {
                    continue;
                }
            }
        }
    }

    /**
     * 显示目录下的文件
     *
     * @param directory
     * @return
     */
    protected List<Map<String, String>> list(final boolean deep, final String... directory) throws IOException {
        String one = "/";
        if (BooleanHelper.hasLength(directory)) {
            one = FinderHelper.firstElement(directory);
        }

        if (OS_NAME.contains(WINDOW)) {
            ftpClient.setControlEncoding("GBK");
        } else {
            ftpClient.setControlEncoding("UTF-8");
        }
        List<Map<String, String>> fileList = new ArrayList<>();

        List<Map<String, String>> folders = new ArrayList<>();
        List<Map<String, String>> files = new ArrayList<>();

        if (deep) {
            getAllFileAndFolder(one, folders, files, null);
            folders.addAll(files);
            return folders;
        } else {
            FTPFile[] ftpFiles = ftpClient.listFiles(one);
            if (ftpFiles.length < 1) {
                return fileList;
            }
            return Arrays.asList(getFTPFile(ftpFiles[0], one));
        }
    }

    /**
     * 获取所有文件夹和文件
     *
     * @param ftpFile ftp文件
     * @return
     */
    protected Map<String, String> getFTPFile(FTPFile ftpFile, final String remote) {
        Map<String, String> temp;
        String pathFile = getPathFile(ftpFile);
        int type = ftpFile.getType();
        String remoteFile = remote + pathFile;
        temp = new HashMap<>();
        temp.put("name", ftpFile.getName());
        temp.put("path", remoteFile);
        temp.put("type", 0 == type ? TYPE_FILE : TYPE_DIRECTORY);
        temp.put("date", DateHelper.format(ftpFile.getTimestamp().getTime(), DateFormatter.YYYY0MM0DD$HHMMSS));
        temp.put("size", SizeHelper.kb(ftpFile.getSize()));
        return temp;
    }

    /**
     * @param file
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getFileName(final String file) {
        try {
            if (OS_NAME.contains(WINDOW)) {
                return new String(file.getBytes("GBK"), "iso-8859-1");
            } else {
                return new String(file.getBytes("UTF-8"), "iso-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            return file;
        }
    }

    /**
     * 获取所有文件夹和文件
     *
     * @param remote
     * @return
     */
    protected void getAllFileAndFolder(String remote, List<Map<String, String>> folders, List<Map<String, String>> files, final FtpFileHandler ftpFileHandler) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(remote);
        if (ftpFiles.length > 0) {
            Map<String, String> temp;

            String pathFile = null, remoteFile;
            int type = -1;
            for (FTPFile ftpFile : ftpFiles) {
                pathFile = getPathFile(ftpFile);
                type = ftpFile.getType();
                remoteFile = remote + pathFile;
                temp = new HashMap<>();
                temp.put("name", ftpFile.getName());
                temp.put("path", StringHelper.noRepeatSlash(remoteFile));
                temp.put("type", 0 == type ? TYPE_FILE : TYPE_DIRECTORY);
                //temp.put("date", DateHelper.time(ftpFile.getTimestamp().getTime(), DateFormatter.YYYY0MM0DD$HHMMSS));
                //temp.put("size", SizeHelper.kb(ftpFile.getSize()));
                if (type == 0) {
                    files.add(temp);
                    if (null != ftpFileHandler) {
                        ftpFileHandler.executor(temp, files.size());
                    }
                } else {
                    folders.add(temp);
                    getAllFileAndFolder(remoteFile, folders, files, ftpFileHandler);
                }
            }
        }

    }

    /**
     * 获取连接
     *
     * @param max
     * @return
     */
    private synchronized void initalFtpClient(int max) {
        if (QUEUE.isEmpty()) {
            for (int i = 0; i < max; i++) {
                FTPClient connect = null;
                try {
                    connect = connect(host, port, username, password);
                    QUEUE.offer(connect);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    /**
     * 获取连接
     *
     * @return
     */
    private synchronized FTPClient getMultiConnection() {
        if (!QUEUE.isEmpty()) {
            return QUEUE.poll();
        }
        return null;
    }

    /**
     * @param ftpClient
     */
    private void close(final FTPClient ftpClient) {
        if (null != ftpClient) {
            QUEUE.offer(ftpClient);
        }
    }

    /**
     * 获取客户端
     *
     * @return
     */
    public FTPClient get() {
        return ftpClient;
    }

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public FTPClient client() {
        return get();
    }

    @Override
    public void start() {
        String host = FinderHelper.firstElement(netProperties.getHost());
        try {
            this.ftpClient = this.connect(
                    NetHelper.getHost(host),
                    NetHelper.getPort(host),
                    netProperties.getUsername(),
                    netProperties.getPassword()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isStart() {
        return ftpClient.isConnected();
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}