package com.chua.utils.tools.data.table;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.data.table.type.TableType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 数据表
 *
 * @author CH
 * @version 1.0.0
 * @see com.chua.utils.tools.data.table.wrapper.TableWrapper
 * @since 2020/12/8
 */
@Getter
@Setter
public class FilterDataTable<F> extends DataTable {

    private List<F> filters;

    FilterDataTable(String id, String name, Object source, TableType tableType, String parser, OperateHashMap operate, OperateHashMap operate2) {
        super(id, name, source, tableType, parser, operate, operate2);
    }
}
