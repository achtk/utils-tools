package com.chua.utils.netx.flink.connector.factory;

import com.chua.utils.netx.flink.format.FlinkInputFormat;
import com.chua.utils.netx.flink.format.FlinkOutputFormat;
import com.chua.utils.netx.flink.format.MemInputFormat;
import com.chua.utils.netx.flink.format.MemOutputFormat;

/**
 * 内存
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public class MemFlinkTableFactory implements FlinkTableFactory {
    @Override
    public String connectorType() {
        return "mem";
    }

    @Override
    public FlinkInputFormat createInputFormat() {
        return new MemInputFormat();
    }

    @Override
    public FlinkOutputFormat createOutputFormat() {
        return new MemOutputFormat();
    }

}
