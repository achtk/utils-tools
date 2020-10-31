package com.chua.utils.tools.dsl.impl;

import com.chua.utils.tools.dsl.CreateConditionStep;
import com.chua.utils.tools.dsl.CreateStep;
import com.chua.utils.tools.dsl.condition.Column;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
@RequiredArgsConstructor
public class SimpleCreateConditionStep implements CreateConditionStep {

    @NonNull
    private String tableName;
    @NonNull
    private CreateStep createStep;

    private List<Column> columns = new ArrayList<>();
    private List<String> others = new ArrayList<>();

    @Override
    public CreateConditionStep withColumn(String columnName, String columnType, int length, String other) {
        Column column = new Column();
        column.withName(columnName).withType(columnType).withLength(length).withOther(other);
        columns.add(column);
        return this;
    }

    @Override
    public CreateConditionStep withColumn(Column column) {
        columns.add(column);
        return this;
    }

    @Override
    public CreateConditionStep withOther(String other) {
        others.add(other);
        return this;
    }

    @Override
    public CreateStep create() {
        return createStep;
    }
}
