package com.chua.utils.netx.flink.connector


import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.DataSet
import org.apache.flink.api.java.ExecutionEnvironment
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.table.api.TableSchema
import org.apache.flink.table.functions.AsyncTableFunction
import org.apache.flink.table.functions.TableFunction
import org.apache.flink.table.sources.BatchTableSource
import org.apache.flink.table.sources.LookupableTableSource
import org.apache.flink.table.sources.StreamTableSource
import org.apache.flink.table.types.DataType
import org.apache.flink.types.Row

import static org.apache.flink.table.types.utils.TypeConversions.fromDataTypeToLegacyInfo

/**
 * redis table source
 * @author CH* @version 1.0.0* @since 2021/1/22
 */
class RedisStreamTableSource implements LookupableTableSource<Row>, StreamTableSource<Row>, BatchTableSource<Row> {

    TableSchema schema
    RedisInputFormat redisInputFormat

    RedisStreamTableSource(TableSchema schema, RedisInputFormat redisInputFormat) {
        this.schema = schema
        this.redisInputFormat = redisInputFormat
    }

    @Override
    TableFunction<Row> getLookupFunction(String[] lookupKeys) {
        new RedisTableSourceFunction(redisInputFormat)
    }

    @Override
    AsyncTableFunction<Row> getAsyncLookupFunction(String[] lookupKeys) {
        return null
    }

    @Override
    boolean isAsyncEnabled() {
        return false
    }

    @Override
    DataStream<Row> getDataStream(StreamExecutionEnvironment execEnv) {
        return null
    }

    @Override
    TableSchema getTableSchema() {
        schema
    }

    @Override
    DataType getProducedDataType() {
        return TableSchema.builder()
                .fields(schema.getFieldNames(), schema.getFieldDataTypes())
                .build()
                .toRowDataType()
    }

    @Override
    String explainSource() {
        'Redis'
    }

    @Override
    TypeInformation<Row> getReturnType() {
        return new RowTypeInfo(schema.getFieldTypes(), schema.getFieldNames())
    }

    @Override
    DataSet<Row> getDataSet(ExecutionEnvironment execEnv) {
        return execEnv
                .createInput(redisInputFormat, (TypeInformation<Row>) fromDataTypeToLegacyInfo(getProducedDataType()))
                .name(explainSource());
    }
}
