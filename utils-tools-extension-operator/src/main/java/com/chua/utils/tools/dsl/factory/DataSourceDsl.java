package com.chua.utils.tools.dsl.factory;

import com.chua.utils.tools.dsl.CreateDataBaseStep;
import com.chua.utils.tools.dsl.CreateTableStep;
import com.chua.utils.tools.dsl.DropTableStep;
import com.chua.utils.tools.dsl.impl.SimpleCreateDataBaseStep;
import com.chua.utils.tools.dsl.impl.SimpleCreateTableStep;
import com.chua.utils.tools.dsl.impl.SimpleDropTableStep;
import com.chua.utils.tools.operator.enums.Case;

/**
 * 数据源DSL
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class DataSourceDsl {
    /**
     * 创建表
     *
     * @return CreateStep
     */
    public static CreateDataBaseStep createDb() {
        return new SimpleCreateDataBaseStep();
    }
    /**
     * 创建表
     *
     * @return CreateStep
     */
    public static DropTableStep dropTable() {
        return new SimpleDropTableStep();
    }

    /**
     * 创建表
     *
     * @return CreateStep
     */
    public static CreateTableStep createTable() {
        return new SimpleCreateTableStep(Case.NONE);
    }

    /**
     * 创建表
     *
     * @param caseType 表大小类型
     * @return CreateStep
     */
    public static CreateTableStep createTable(Case caseType) {
        return new SimpleCreateTableStep(caseType);
    }
}
