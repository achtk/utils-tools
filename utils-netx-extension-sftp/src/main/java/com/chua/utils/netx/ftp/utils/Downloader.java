package com.chua.utils.netx.ftp.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.ftp.FTPClient;

/**
 * 下载文件/文件夹
 * @author CH
 */
@Getter
@Setter
public class Downloader {
    /**
     * 文件名
     */
    private String name;
    /**
     * 远程文件路径
     */
    private String remote;
    /**
     * 远程文件
     */
    private String remoteFile;
    /**
     * 远程文件大小
     */
    private float remoteSize = 0f;
    /**
     * 本地文件路径
     */
    private String local;
    /**
     * 计数器
     */
    private int cnt = 0;
    /**
     * 总数
     */
    private int total;
    /**
     * 客户端
     */
    private FTPClient ftpClient;
}
