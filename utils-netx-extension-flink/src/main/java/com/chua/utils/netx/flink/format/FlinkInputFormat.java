package com.chua.utils.netx.flink.format;

import org.apache.flink.api.common.io.RichInputFormat;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.core.io.InputSplit;
import org.apache.flink.types.Row;

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

    public void setSign(String sign) {
        this.sign = sign;
    }

    public FlinkInputFormat() {
    }

    public FlinkInputFormat(String sign) {
        this.sign = sign;
    }

}
