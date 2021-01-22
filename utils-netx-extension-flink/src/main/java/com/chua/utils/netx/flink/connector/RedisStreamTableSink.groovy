package com.chua.utils.netx.flink.connector

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.tuple.Tuple2
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.java.typeutils.TupleTypeInfo
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.table.sinks.RetractStreamTableSink
import org.apache.flink.table.sinks.TableSink
import org.apache.flink.table.types.DataType
import org.apache.flink.types.Row
import org.apache.flink.util.Collector
import org.apache.flink.util.InstantiationUtil

/**
 * redis stream table sink
 * @author CH* @version 1.0.0* @since 2021/1/22
 */
class RedisStreamTableSink implements RetractStreamTableSink<Row> {

    private RedisOutFormat redisOutFormat
    private String[] fieldNames;
    private DataType[] fieldTypes1;

    RedisStreamTableSink(RedisOutFormat redisOutFormat) {
        this.redisOutFormat = redisOutFormat
    }

    /**
     * Setter method for property <tt>fieldNames</tt>.
     *
     * @param fieldNames value to be assigned to property fieldNames
     */
    void setFieldNames(final String[] fieldNames) {
        this.fieldNames = fieldNames;
    }

    /**
     * Setter method for property <tt>fieldTypes</tt>.
     *
     * @param fieldTypes value to be assigned to property fieldTypes
     */
    void setFieldTypes(final DataType[] fieldTypes) {
        this.fieldTypes1 = fieldTypes;
    }

    @Override
    TypeInformation<Row> getRecordType() {
        return null
    }

    @Override
    TypeInformation<Tuple2<Boolean, Row>> getOutputType() {
        List<TypeInformation> fieldList = []
        fieldTypes1.each {
            DataType dataType ->
                fieldList.add(TypeInformation.of(dataType.getConversionClass()))
        }

        def rowTypeInfo = new RowTypeInfo()
        new TupleTypeInfo(Boolean.class, rowTypeInfo)
    }

    @Override
    DataStreamSink<?> consumeDataStream(DataStream<Tuple2<Boolean, Row>> dataStream) {
        return dataStream.flatMap(new FlatMapFunction<Tuple2<Boolean, Row>, Row>() {
            @Override
            void flatMap(final Tuple2<Boolean, Row> value, final Collector<Row> out) throws Exception {
                if (value.f0) {
                    out.collect(value.f1);
                }

            }
        }).addSink(new RedisTableSinkFunction(redisOutFormat)).name(TableConnectorUtil.generateRuntimeName(this.getClass(), fieldNames));
    }

    @Override
    TableSink<Tuple2<Boolean, Row>> configure(String[] fieldNames, TypeInformation<?>[] fieldTypes) {
        RedisStreamTableSink copy = null;
        try {
            copy = new RedisStreamTableSink(InstantiationUtil.clone(redisOutFormat));
            copy.setFieldTypes(fieldTypes)
            copy.setFieldNames(fieldNames)
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
        return copy
    }
}
