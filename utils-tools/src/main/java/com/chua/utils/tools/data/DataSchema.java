package com.chua.utils.tools.data;

import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.data.table.DataTable;

import java.util.List;

/**
 * 添加数据处理分析工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
public interface DataSchema<T> {
    /**
     * 初始化
     * @param t 初始化项
     */
    void initial(T t);
    /**
     * 获取数据表
     *
     * @return 数据表
     */
    DataTable getTable();

    /**
     * 获取数据库名称
     *
     * @return 数据表
     */
    String schema();

    /**
     * 设置数据库名称
     *
     * @param schema 数据库名称
     */
    void schema(String schema);

    /**
     * 额外参数
     * @return 参数
     */
    HashOperateMap operand();

}
