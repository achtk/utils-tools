package com.chua.utils.netx.flink.format;

import org.apache.flink.api.common.io.RichOutputFormat;
import org.apache.flink.types.Row;

import java.io.Flushable;

/**
 * link input format
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public abstract class FlinkOutputFormat extends RichOutputFormat<Row> implements Flushable {
    /**
     * 标识符
     */
    protected String sign;

    public void setSign(String sign) {
        this.sign = sign;
    }

    public FlinkOutputFormat() {
    }

    public FlinkOutputFormat(String sign) {
        this.sign = sign;
    }
}
