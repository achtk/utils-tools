package com.chua.utils.netx.flink.connector;

import com.chua.utils.netx.flink.format.FlinkOutputFormat;
import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.sinks.OutputFormatTableSink;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.types.utils.TypeConversions;
import org.apache.flink.table.utils.TableSchemaUtils;
import org.apache.flink.types.Row;

/**
 * redis table source
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public class StandardTableSink extends OutputFormatTableSink<Row> {

    private String sign;
    private TableSchema schema;
    private FlinkOutputFormat flinkOutputFormat;

    public StandardTableSink(String sign, TableSchema schema, FlinkOutputFormat flinkOutputFormat) {
        this.sign = sign;
        this.schema = TableSchemaUtils.checkNoGeneratedColumns(schema);
        this.flinkOutputFormat = flinkOutputFormat;
        this.flinkOutputFormat.setSign(sign);
    }

    @Override
    public TableSchema getTableSchema() {
        return schema;
    }

    @Override
    public TableSink<Row> configure(String[] fieldNames, TypeInformation<?>[] fieldTypes) {
        return new StandardTableSink(sign, schema, flinkOutputFormat);
    }

    @Override
    public OutputFormat<Row> getOutputFormat() {
        return flinkOutputFormat;
    }

    @Override
    public TypeInformation<Row> getOutputType() {
        return new RowTypeInfo(TypeConversions.fromDataTypeToLegacyInfo(this.schema.getFieldDataTypes()), schema.getFieldNames());
    }
}
