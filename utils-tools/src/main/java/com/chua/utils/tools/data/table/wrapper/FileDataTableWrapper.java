package com.chua.utils.tools.data.table.wrapper;

import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.type.TableType;
import lombok.AllArgsConstructor;

import java.io.File;

/**
 * 内存表修饰器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/12
 */
@AllArgsConstructor
public class FileDataTableWrapper {

    private final TableType tableType = TableType.FILE;
    private final String name;

    /**
     * 构建数据
     *
     * @param source 数据源
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(File source) {
        return source(null == source ? null : source.getAbsolutePath());
    }

    /**
     * 构建数据
     *
     * @param file 数据源
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String file) {
        return () -> {
            DataTable.DataTableBuilder builder = DataTable.builder();
            builder.source(file);
            builder.tableType(tableType);
            builder.name(name);
            return builder.build();
        };
    }

}
