package com.chua.utils.netx.data.calcite.immutable;

import com.chua.utils.netx.data.calcite.common.CsvTable;
import com.google.common.base.Strings;
import lombok.NoArgsConstructor;
import org.apache.calcite.schema.impl.AbstractTable;

/**
 * csv处理
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
@NoArgsConstructor
public class CsvImmutableFileDataSchema extends AbstractImmutableFileDataSchema {

    @Override
    public boolean isMatcher() {
        return !Strings.isNullOrEmpty(file) && file.endsWith(".csv");
    }

    @Override
    public AbstractTable createTable() {
        return new CsvTable(source);
    }

}
