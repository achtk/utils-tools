package com.chua.utils.tools.dsl.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.Accessors;

/**
 * 字段
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    @With
    private String name;
    @With
    private String type;
    @With
    private int length = 0;
    @With
    private String other;
}
