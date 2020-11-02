package com.chua.utils.tools.dsl.impl;

import com.chua.utils.tools.dsl.CreateTableConditionStep;
import com.chua.utils.tools.dsl.CreateTableStep;
import com.chua.utils.tools.operator.enums.Case;
import com.chua.utils.tools.operator.util.OperatorUtil;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;

import javax.persistence.Table;

/**
 * 简单创建
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
@AllArgsConstructor
public class SimpleCreateTableStep implements CreateTableStep {

    private Case aCase;

    @Override
    public CreateTableConditionStep table(String tableName) {
        Preconditions.checkArgument(null != tableName);
        return new SimpleCreateTableConditionStep(getNameByCase(tableName));
    }

    /**
     * 通过Case获取名称
     *
     * @param tableName 数据
     * @return
     */
    private String getNameByCase(String tableName) {
        if (aCase == Case.UPPER) {
            return tableName.toUpperCase();
        }
        return aCase == Case.LOWER ? tableName.toLowerCase() : tableName;
    }

    @Override
    public CreateTableConditionStep table(Class<?> aClass) {
        Preconditions.checkArgument(null != aClass);

        return new SimpleCreateTableConditionStep(getNameByCase(OperatorUtil.humpToUnderline(aClass.getSimpleName())));
    }

    @Override
    public CreateTableConditionStep tableJpa(Class<?> aClass) {
        Preconditions.checkArgument(null != aClass);

        Table table = aClass.getDeclaredAnnotation(Table.class);
        if (null == table) {
            return table(aClass);
        }

        return new SimpleCreateTableConditionStep(getNameByCase(table.name()));
    }
}
