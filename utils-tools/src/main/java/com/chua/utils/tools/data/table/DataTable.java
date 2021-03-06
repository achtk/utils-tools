package com.chua.utils.tools.data.table;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.data.table.type.TableType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 数据表
 *
 * @author CH
 * @version 1.0.0
 * @see com.chua.utils.tools.data.table.wrapper.TableWrapper
 * @since 2020/12/8
 */
@Getter
@Setter
@Builder
@Accessors(chain = true)
public class DataTable {

    /**
     * 标识
     */
    private String id;
    /**
     * 表名
     */
    private String name;
    /**
     * 数据源
     */
    private Object source;
    /**
     * 表类型
     */
    @Builder.Default
    private TableType tableType = TableType.FILE;
    /**
     * 解析器
     *
     * @see com.chua.utils.tools.data.parser.DataParser
     */
    private String parser;
    /**
     * 参数
     */
    @Builder.Default
    private OperateHashMap operate = OperateHashMap.create();
    /**
     * 额外参数
     */
    @Builder.Default
    private OperateHashMap operate2 = OperateHashMap.create();

    /**
     * 获取表名
     *
     * @return
     */
    public String getName() {
        return null == name && tableType == TableType.FILE ? FileHelper.getName(source.toString()) : name;
    }
}
