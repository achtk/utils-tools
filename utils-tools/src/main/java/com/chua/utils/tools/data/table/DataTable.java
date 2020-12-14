package com.chua.utils.tools.data.table;

import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.data.table.type.TableType;
import com.chua.utils.tools.data.wrapper.Wrapper;
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
     * 获取表名
     *
     * @return
     */
    public String getName() {
        return null == name && tableType == TableType.FILE ? FileHelper.getName(source.toString()) : name;
    }

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
    private HashOperateMap operate = HashOperateMap.create();
    /**
     * 数据包裹方式
     */
    private Wrapper<?> wrapper;
    /**
     * 额外参数
     */
    @Builder.Default
    private HashOperateMap operate2 = HashOperateMap.create();
}
