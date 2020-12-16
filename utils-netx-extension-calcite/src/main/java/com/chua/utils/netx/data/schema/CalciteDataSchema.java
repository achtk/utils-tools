package com.chua.utils.netx.data.schema;

import com.chua.utils.netx.data.table.CalciteTable;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.data.factory.StandardDataFactory;
import com.chua.utils.tools.data.parser.DataParser;
import com.chua.utils.tools.data.table.DataTable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * 方解石数据架图工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
@Slf4j
@AllArgsConstructor
public class CalciteDataSchema extends AbstractSchema {

    private List<String> ids;

    @Override
    protected Map<String, Table> getTableMap() {
        Map<String, Table> tableMap = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        ids.forEach(id -> {
            DataTable dataTable = StandardDataFactory.getDataTable(id);
            if (null == dataTable.getParser()) {
                log.warn("The parser does not exist and cannot parse the data." +
                        "either use StandardDataFactory.addDefaultParser(Parser urlType)\n" +
                        "with your specialized Parser.");
                return;
            }
            DataParser dataParser = ClassHelper.forObject(dataTable.getParser(), DataParser.class);
            if(null == dataParser) {
                log.warn("The parser does not exist and cannot parse the data." +
                        "either use StandardDataFactory.addDefaultParser(Parser urlType)\n" +
                        "with your specialized Parser.");
                return;
            }
            dataParser.setOperate(dataTable.getOperate());
            dataParser.setSource(dataTable.getSource());
            tableMap.put(dataTable.getName(), CalciteTable.create(dataParser, dataTable));
        });
        return tableMap;
    }
}
