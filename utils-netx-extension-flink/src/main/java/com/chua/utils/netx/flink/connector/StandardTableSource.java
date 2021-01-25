package com.chua.utils.netx.flink.connector;

import com.chua.utils.netx.flink.format.FlinkInputFormat;
import org.apache.flink.api.common.io.InputFormat;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.sources.InputFormatTableSource;
import org.apache.flink.types.Row;

/**
 * redis table source
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public class StandardTableSource extends InputFormatTableSource {

    private String sign;
    private TableSchema schema;
    private FlinkInputFormat flinkInputFormat;

    public StandardTableSource(String sign, TableSchema schema, FlinkInputFormat flinkInputFormat) {
        this.sign = sign;
        this.schema = schema;
        this.flinkInputFormat = flinkInputFormat;
        this.flinkInputFormat.setSign(sign);
    }

    @Override
    public TypeInformation<Row> getReturnType() {
        return new RowTypeInfo(this.schema.getFieldTypes(), schema.getFieldNames());
    }

    @Override
    public TableSchema getTableSchema() {
        return schema;
    }

    @Override
    public InputFormat getInputFormat() {
        return flinkInputFormat;
    }
}
