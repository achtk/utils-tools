package com.chua.utils.netx.data.table;

import com.chua.utils.netx.data.table.common.ColumnResolver;
import com.chua.utils.tools.data.parser.DataParser;
import com.chua.utils.tools.data.table.DataTable;
import lombok.AllArgsConstructor;
import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;

/**
 * 扫描表
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
@AllArgsConstructor
public class CusScannerTable extends AbstractTable implements ColumnResolver, ScannableTable {

    private final DataParser dataParser;
    private final DataTable dataTable;

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        return new CusEnumerable();
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return converterRowType(typeFactory, dataParser.getDataType());
    }

    /**
     * 枚举器
     */
    private class CusEnumerable extends AbstractEnumerable<Object[]> {

        @Override
        public Enumerator<Object[]> enumerator() {
            return new CusEnumerator();
        }
    }

    /**
     * 枚举器
     */
    private class CusEnumerator implements Enumerator<Object[]> {

        @Override
        public Object[] current() {
            return dataParser.getCurrent();
        }

        @Override
        public boolean moveNext() {
            return dataParser.hasNext();
        }

        @Override
        public void reset() {
            dataParser.reset();
        }

        @Override
        public void close() {

        }
    }
}
