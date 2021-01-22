package com.chua.utils.netx.flink.connector

import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.Counter
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.types.Row

/**
 * redis table
 * @author CH* @since 2021/1/22
 * @version 1.0.0
 */
class RedisTableSinkFunction extends RichSinkFunction<Row> {

    private RedisOutFormat redisOutFormat
    private Counter counter

    RedisTableSinkFunction(final RedisOutFormat redisOutFormat) {
        this.redisOutFormat = redisOutFormat
    }

    @Override
    void invoke(final Row value, final Context context) throws Exception {
        redisOutFormat.writeRecord(value)
        counter.inc()
    }

    @Override
    void open(final Configuration parameters) throws Exception {
        super.open(parameters)
        RuntimeContext ctx = getRuntimeContext()
        redisOutFormat.setRuntimeContext(ctx)
        this.counter = getRuntimeContext().getMetricGroup().counter("RedisSink")
    }

    @Override
    void close() throws Exception {
        super.close()
    }
}
