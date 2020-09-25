package com.chua.utils.netx.ftp.reader;

import com.chua.utils.netx.ftp.utils.Downloader;
import com.chua.utils.tools.common.SizeHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;

/**
 *
 */
@Slf4j
public class FtpReader {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.00");

    /**
     * 写入本地文件
     * @param localFile
     * @param executorService
     * @param downloader
     */
    public static void readToFile(File localFile, ExecutorService executorService, Downloader downloader) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                FTPClient ftpClient = downloader.getFtpClient();
                ftpClient.setRestartOffset(localFile.length());
                try {
                    InputStream in = ftpClient.retrieveFileStream(downloader.getRemoteFile());
                    mkFile(in, new File(downloader.getLocal()), downloader);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    boolean isDo = false;
                    try {
                        isDo = ftpClient.completePendingCommand();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     * 写入本地文件
     * @param in
     * @param local
     * @param downloader
     */
    public static void readToFile(final InputStream in, final File local, final Downloader downloader) {
        mkFile(in, local, downloader);
    }

    /**
     *
     */
    public static void mkFile(final InputStream in, final File local, final Downloader downloader) {
        //远程文件长度
        final float lRemoteSize = downloader.getRemoteSize();
        //本地文件长度
        float localSize = local.length();
        //进行断点续传，并记录状态
        try(FileOutputStream out = new FileOutputStream(local, true)) {
            byte[] bytes = null;
            if(lRemoteSize > SizeHelper.G) {
                bytes = new byte[1024 * 32];
            }if(lRemoteSize > SizeHelper.M) {
                bytes = new byte[1024 * 16];
            } else {
                bytes = new byte[1024];
            }
            float step = lRemoteSize / 100f;
            float process = 0L;
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
                if(log.isTraceEnabled()) {
                    localSize += c;
                    float nowProcess = 100 / (lRemoteSize / localSize);
                    final String df = DECIMAL_FORMAT.format(nowProcess);
                    nowProcess = Float.valueOf(df);
                    if (process != nowProcess) {
                        log.trace("文件:{}, 下载进度{}%：", downloader.getName(), nowProcess);
                        process = nowProcess;
                        //TODO 更新文件下载进度,值存放在process变量中
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
