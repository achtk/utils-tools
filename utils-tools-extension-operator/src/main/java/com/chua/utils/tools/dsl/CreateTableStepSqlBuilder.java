package com.chua.utils.tools.dsl;

import com.chua.utils.tools.dsl.condition.Column;

import java.util.List;
import java.util.Set;

/**
 * 创建SQL工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public interface CreateTableStepSqlBuilder extends SqlBuilder {
    /**
     * 获取表
     *
     * @return 表名
     */
    String table();

    /**
     * 获取字段
     *
     * @return List
     */
    Set<Column> columns();

    /**
     * 额外信息
     *
     * @return Set
     */
    Set<String> extraInformation();

}

