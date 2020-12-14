package com.chua.utils.tools.data.table.wrapper;

import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.type.TableType;
import lombok.AllArgsConstructor;

/**
 * 内存表修饰器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/12
 */
@AllArgsConstructor
public class MemDataTableWrapper {

    private String name;

    private final TableType tableType = TableType.MEM;

    /**
     * 构建数据
     *
     * @param source 数据源
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(Object source) {
        return () -> {
            DataTable.DataTableBuilder builder = DataTable.builder();
            builder.source(source);
            builder.tableType(tableType);
            builder.name(name);
            return builder.build();
        };
    }

}
