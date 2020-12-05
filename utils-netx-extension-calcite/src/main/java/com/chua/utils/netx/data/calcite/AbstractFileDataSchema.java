package com.chua.utils.netx.data.calcite;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import lombok.Setter;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.util.Source;
import org.apache.calcite.util.Sources;

import java.net.URL;
import java.util.Map;

/**
 * 文件数据
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
public abstract class AbstractFileDataSchema extends AbstractSchema {

    @Setter
    protected String file;
    protected Source source;
    @Setter
    protected SchemaPlus schemaPlus;
    @Setter
    protected String s;
    @Setter
    protected Map<String, Object> map;

    /**
     * 后缀匹配
     *
     * @return 后缀匹配返回true
     */
    public abstract boolean isMatcher();

    /**
     * 创建表信息
     *
     * @return 表信息
     */
    public abstract AbstractTable createTable();

    @Override
    protected Map<String, Table> getTableMap() {
        URL url = Resources.getResource(file);
        this.source = Sources.of(url);
        final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
        builder.put(this.file.split("\\.")[0], createTable());
        // 一个数据库有多个表名，这里初始化，大小写要注意了,TEST01是表名。
        return builder.build();
    }

}
