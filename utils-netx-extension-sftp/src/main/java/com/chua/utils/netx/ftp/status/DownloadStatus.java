package com.chua.utils.netx.ftp.status;

/**
 * @author CH
 */
public enum DownloadStatus {
    /**
     * 远程文件不存在
     */
    REMOTE_FILE_NOEXIST,
    /**
     * 本地文件大于远程文件
     */
    LOCAL_BIGGER_REMOTE,
    /**
     * 断点下载文件成功
     */
    DOWNLOAD_FROM_BREAK_SUCCESS,
    /**
     * 断点下载文件失败
     */
    DOWNLOAD_FROM_BREAK_FAILED,
    /**
     * 全新下载文件成功
     */
    DOWNLOAD_NEW_SUCCESS,
    /**
     * 全新下载文件失败
     */
    DOWNLOAD_NEW_FAILED;
}
