package com.chua.utils.tools.data.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件数据表
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
@Data
@EqualsAndHashCode
public class FileDataTable extends DataTable {
    /**
     * 文件
     */
    private String file;
}
