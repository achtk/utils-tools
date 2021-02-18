package com.chua.utils.tools.data.table.wrapper;

import com.chua.utils.tools.data.table.DataTable;

/**
 * 数据表修饰器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/12
 */
public interface DataTableWrapperBuilder {
    /**
     * 创建表
     *
     * @return 表
     */
    DataTable create();
}
