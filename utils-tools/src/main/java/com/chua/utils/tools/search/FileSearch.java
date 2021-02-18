package com.chua.utils.tools.search;

import java.io.File;
import java.util.List;

/**
 * 文件检索器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/24
 */
public interface FileSearch {

    /**
     * 索引检索
     *
     * @param index 索引
     * @return 检索的索引
     */
    List<File> locale(String index);

    /**
     * 实时检索
     *
     * @param index 索引
     * @return 检索的索引
     */
    List<File> find(String index);
}
