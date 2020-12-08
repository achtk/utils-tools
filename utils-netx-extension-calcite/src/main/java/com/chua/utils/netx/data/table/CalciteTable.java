package com.chua.utils.netx.data.table;

import com.chua.utils.tools.data.parser.DataParser;
import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.wrapper.Wrapper;
import lombok.AllArgsConstructor;
import org.apache.calcite.schema.Table;

/**
 * 方解石表
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
@AllArgsConstructor
public class CalciteTable {

    private final DataParser dataParser;
    private final DataTable dataTable;

    /**
     * 创建表
     *
     * @param dataParser 解析器
     * @param dataTable  表信息
     * @return 表
     */
    public static Table create(DataParser dataParser, DataTable dataTable) {
        return new CalciteTable(dataParser, dataTable).create();
    }

    /**
     * 创建表
     *
     * @return 表
     */
    private Table create() {
        Wrapper wrapper = dataTable.getWrapper();
        return new CusScannerTable(dataParser, dataTable);
    }


}
