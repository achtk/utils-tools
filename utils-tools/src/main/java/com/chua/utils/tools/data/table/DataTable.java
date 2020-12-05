package com.chua.utils.tools.data.table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 数据表
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
@Data
@EqualsAndHashCode
public class DataTable {
    /**
     * 数据表
     */
    private String table;

    @Override
    public String toString() {
        return table;
    }
}
