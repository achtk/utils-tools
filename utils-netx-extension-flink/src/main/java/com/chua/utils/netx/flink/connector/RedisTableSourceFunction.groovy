package com.chua.utils.netx.flink.connector

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.table.functions.FunctionContext
import org.apache.flink.table.functions.TableFunction
import org.apache.flink.types.Row

/**
 * redis table source
 * @author CH* @version 1.0.0* @since 2021/1/22
 */
class RedisTableSourceFunction extends TableFunction<Row> {

    private RedisInputFormat redisInputFormat;

    RedisTableSourceFunction(final RedisInputFormat redisInputFormat) {
        this.redisInputFormat = redisInputFormat;
    }

    @Override
    TypeInformation<Row> getResultType() {
        return super.getResultType()
    }

    @Override
    void open(FunctionContext context) throws Exception {
        redisInputFormat.openInputFormat();
    }

    @Override
    void close() throws Exception {
        super.close();
        redisInputFormat.close();
    }
}
