package com.chua.utils.netx.ftp.status;

/**
 * @author CH
 */
public enum RenameStatus {
    /**
     * 远程文件不存在
     */
    REMOTE_FILE_NOEXIST,
    /**
     * 断点下载文件成功
     */
    RENAME_FROM_BREAK_SUCCESS,
    /**
     * 断点下载文件失败
     */
    RENAME_FROM_BREAK_FAILED,
}
