package com.chua.utils.netx.flink.format;

import org.apache.flink.api.common.io.DefaultInputSplitAssigner;
import org.apache.flink.api.common.io.RichInputFormat;
import org.apache.flink.api.common.io.statistics.BaseStatistics;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.io.GenericInputSplit;
import org.apache.flink.core.io.InputSplit;
import org.apache.flink.core.io.InputSplitAssigner;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.types.utils.TypeConversions;
import org.apache.flink.types.Row;

import java.io.IOException;

/**
 * link input format
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public abstract class FlinkInputFormat extends RichInputFormat<Row, InputSplit> implements ResultTypeQueryable<Row> {
    /**
     * 标识符
     */
    protected String sign;
    private Object[][] parameterValues;

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setParameterValues(Object[][] parameterValues) {
        this.parameterValues = parameterValues;
    }

    public FlinkInputFormat() {
    }

    public FlinkInputFormat(String sign) {
        this.sign = sign;
    }

    @Override
    public void configure(Configuration configuration) {
    }

    @Override
    public BaseStatistics getStatistics(BaseStatistics baseStatistics) throws IOException {
        return null;
    }

    @Override
    public InputSplit[] createInputSplits(int minNumSplits) throws IOException {
        if (parameterValues == null) {
            return new GenericInputSplit[]{new GenericInputSplit(0, 1)};
        }
        GenericInputSplit[] ret = new GenericInputSplit[parameterValues.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new GenericInputSplit(i, ret.length);
        }
        return ret;
    }

    @Override
    public InputSplitAssigner getInputSplitAssigner(InputSplit[] inputSplits) {
        return new DefaultInputSplitAssigner(inputSplits);
    }

    @Override
    public void open(InputSplit split) throws IOException {

    }

    @Override
    public TypeInformation<Row> getProducedType() {
        TableSchema schema = FormatConnector.getSchema(sign);
        return new RowTypeInfo(TypeConversions.fromDataTypeToLegacyInfo(schema.getFieldDataTypes()), schema.getFieldNames());
    }

}
