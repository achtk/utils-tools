package com.chua.tools.file.everything;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;

import java.nio.Buffer;

/**
 * Everything
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
public interface Everything extends Library {

    Everything instanceDll = Native.loadLibrary("Everything64", Everything.class);

    static final int EVERYTHING_ERROR_MEMORY = 1;
    static final int EVERYTHING_ERROR_IPC = 2;
    static final int EVERYTHING_ERROR_REGISTERCLASSEX = 3;
    static final int EVERYTHING_ERROR_CREATEWINDOW = 4;
    static final int EVERYTHING_ERROR_CREATETHREAD = 5;
    static final int EVERYTHING_ERROR_INVALIDINDEX = 6;
    static final int EVERYTHING_ERROR_INVALIDCALL = 7;

    /**
     * 设置查询条件
     *
     * @param lpSearchString 查询条件
     * @return int
     */
    int Everything_SetSearchW(WString lpSearchString);

    /**
     * 设置查询路径
     *
     * @param bEnable 设置查询路径
     */
    void Everything_SetMatchPath(boolean bEnable);

    /**
     * 设置查询Case
     *
     * @param bEnable 设置查询Case
     */
    void Everything_SetMatchCase(boolean bEnable);

    /**
     * 设置查询全词
     *
     * @param bEnable 设置查询全词
     */
    void Everything_SetMatchWholeWord(boolean bEnable);

    /**
     * 设置查询正则
     *
     * @param bEnable 设置查询正则
     */
    void Everything_SetRegex(boolean bEnable);

    /**
     * 设置查询最大值
     *
     * @param dwMax 设置查询最大值
     */
    void Everything_SetMax(int dwMax);

    /**
     * 设置查询位置
     *
     * @param dwOffset 设置查询位置
     */
    void Everything_SetOffset(int dwOffset);

    /**
     * 获取查询路径
     *
     * @return boolean
     */
    boolean Everything_GetMatchPath();

    /**
     * 获取查询Case
     *
     * @return boolean
     */
    boolean Everything_GetMatchCase();

    /**
     * 获取查询全词
     *
     * @return boolean
     */
    boolean Everything_GetMatchWholeWord();

    /**
     * 获取查询正则
     *
     * @return boolean
     */
    boolean Everything_GetRegex();

    /**
     * 获取查询最大值
     *
     * @return int
     */
    int Everything_GetMax();

    /**
     * 获取查询位置
     *
     * @return int
     */
    int Everything_GetOffset();

    /**
     * 获取查询索引
     *
     * @return 索引
     */
    WString Everything_GetSearchW();

    /**
     * 获取查询异常信息
     *
     * @return int
     */
    int Everything_GetLastError();

    /**
     * 设置查询是否排序
     *
     * @param bWait 设置查询是否排序
     * @return boolean
     */
    boolean Everything_QueryW(boolean bWait);

    /**
     * 路径排序
     */
    void Everything_SortResultsByPath();

    /**
     * 文件数
     *
     * @return 文件数
     */
    int Everything_GetNumFileResults();

    /**
     * 文件夹数
     *
     * @return 文件夹数
     */
    int Everything_GetNumFolderResults();

    /**
     * 结果数
     *
     * @return 结果数
     */
    int Everything_GetNumResults();

    /**
     * 子文件数
     *
     * @return 子文件数
     */
    int Everything_GetTotFileResults();

    /**
     * 子文件夹数
     *
     * @return 子文件夹数
     */
    int Everything_GetTotFolderResults();

    /**
     * 子结果数
     *
     * @return 子结果数
     */
    int Everything_GetTotResults();

    /**
     * 是否是Volume
     *
     * @param nIndex 索引
     * @return boolean
     */
    boolean Everything_IsVolumeResult(int nIndex);

    /**
     * 是否是Folder
     *
     * @param nIndex 索引
     * @return boolean
     */
    boolean Everything_IsFolderResult(int nIndex);

    /**
     * 是否是File
     *
     * @param nIndex 索引
     * @return boolean
     */
    boolean Everything_IsFileResult(int nIndex);

    /**
     * 获取结果全路径名
     *
     * @param nIndex    索引
     * @param lpString  名称
     * @param nMaxCount 最大数量
     */
    void Everything_GetResultFullPathNameW(int nIndex, Buffer lpString, int nMaxCount);

    /**
     * 重置
     */
    void Everything_Reset();


}
