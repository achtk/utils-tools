package com.chua.utils.tools.dsl.condition;

import com.chua.utils.tools.operator.enums.Case;
import com.google.common.base.Strings;
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
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    private String name;
    private String type = "varchar";
    private int length = 0;
    private String other;

    /**
     * 获取sql
     *
     * @param caseType 字段大小写
     * @return
     */
    public String toString(Case caseType) {
        StringBuffer columnSql = new StringBuffer();
        String name = this.name;
        if (caseType == Case.UPPER) {
            name = name.toUpperCase();
        } else if (caseType == Case.LOWER) {
            name = name.toLowerCase();
        }

        columnSql.append(name).append(" ").append(type);
        if (length > 0) {
            columnSql.append("(").append(length).append(")");
        }
        if (!Strings.isNullOrEmpty(other)) {
            columnSql.append(" ").append(other);
        }
        return columnSql.toString();
    }
}
