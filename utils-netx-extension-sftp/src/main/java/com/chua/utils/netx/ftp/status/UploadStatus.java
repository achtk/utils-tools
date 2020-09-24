package com.chua.utils.netx.ftp.status;

/**
 * @author CH
 */
public enum UploadStatus {
    /**
     * 远程服务器相应目录创建失败
     */
    CREATE_DIRECTORY_FAIL,
    /**
     * 远程服务器闯将目录成功
     */
    CREATE_DIRECTORY_SUCCESS,
    /**
     * 上传新文件成功
     */
    UPLOAD_NEW_FILE_SUCCESS,
    /**
     * 上传新文件失败
     */
    UPLOAD_NEW_FILE_FAILED,
    /**
     * 文件已经存在
     */
    FILE_EXITS,
    /**
     * 远程文件大于本地文件
     */
    REMOTE_BIGGER_LOCAL,
    /**
     * 断点续传成功
     */
    UPLOAD_FROM_BREAK_SUCCESS,
    /**
     * 断点续传失败
     */
    UPLOAD_FROM_BREAK_FAILED,
    /**
     * 断点续传成功
     */
    DELETE_REMOTE_FAILD;
}
