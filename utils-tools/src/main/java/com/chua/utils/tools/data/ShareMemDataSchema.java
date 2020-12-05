package com.chua.utils.tools.data;

import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.data.table.DataTable;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 添加数据处理分析工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
public interface ShareMemDataSchema extends DataSchema {
    /**
     * 数据操作
     *
     * @param consumer 消费者
     */
    void doWith(Consumer<Stream> consumer);
}
