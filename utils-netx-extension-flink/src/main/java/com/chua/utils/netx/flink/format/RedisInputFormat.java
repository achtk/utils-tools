package com.chua.utils.netx.flink.format;

import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.chua.utils.tools.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.core.io.InputSplit;
import org.apache.flink.table.api.TableColumn;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
@Slf4j
public class RedisInputFormat extends FlinkInputFormat implements RedisFormat {

    private transient boolean hasNext;
    private String value;
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void openInputFormat() throws IOException {
        super.openInputFormat();
        this.initialJedis(sign);
    }

    @Override
    public void open(InputSplit inputSplit) throws IOException {
        initial();
    }

    /**
     * 初始化
     */
    private void initial() {
        try (ShardedJedis jedis = FormatConnector.<ShardedJedisPool>getPool(sign).getResource()) {
            this.hasNext = jedis.exists(FormatConnector.getIndex(sign));
            this.value = jedis.get(FormatConnector.getIndex(sign));
        }
    }

    @Override
    public boolean reachedEnd() throws IOException {
        return !hasNext;
    }

    @Override
    public Row nextRecord(Row row) throws IOException {
        if (value.startsWith("[")) {
            List<Object> strings = JsonUtils.fromJson2List(value);
            int count = atomicInteger.get();
            int max = strings.size();
            if ((count + 1) > max) {
                hasNext = false;
                return row;
            }
            Object s = strings.get(atomicInteger.getAndIncrement() % strings.size());
            if (s instanceof Map) {
                convert2Map((Map) s, row);
            } else {
                convert2String(s.toString(), row);
            }
        } else if (value.startsWith("{")) {
            hasNext = false;
            Map<String, Object> stringObjectMap = JsonUtils.fromJson2Map(value);
            convert2Map(stringObjectMap, row);
        } else {
            hasNext = false;
            convert2String(value, row);
        }
        return row;
    }

    /**
     * From String
     *
     * @param toString 值
     * @param row      行
     */
    private void convert2String(String toString, Row row) {
        int arity = row.getArity();
        String[] split = toString.split(FormatConnector.getDataDelimiter(sign));
        if (split.length == 0) {
            return;
        }

        if (split.length == 1) {
            row.setField(0, converter(0, split[0]));
            return;
        }

        if (split.length == 2) {
            row.setField(0, converter(0, split[0]));
            row.setField(1, converter(1, split[1]));
            return;
        }

        for (int i = 0; i < arity; i++) {
            row.setField(i, converter(i, split[i]));
        }
    }

    /**
     * 转化
     *
     * @param index 索引
     * @param value 值
     * @return 转化后的值
     */
    private Object converter(int index, Object value) {
        TableSchema schema = FormatConnector.getSchema(sign);
        Optional<TableColumn> tableColumn = schema.getTableColumn(index);
        TableColumn tableColumn1 = tableColumn.get();
        return EmptyOrBase.getTypeConverter(tableColumn1.getType().getConversionClass()).convert(value);
    }

    /**
     * From Map
     *
     * @param toString 值
     * @param row      行
     */
    private void convert2Map(Map toString, Row row) {
        TableSchema schema = FormatConnector.getSchema(sign);
        int index = 0;
        for (TableColumn tableColumn : schema.getTableColumns()) {
            Object o = ((Map) toString).get(tableColumn.getName());
            TypeConverter converter = EmptyOrBase.getTypeConverter(tableColumn.getType().getConversionClass());
            if (null != converter) {
                row.setField(index++, converter.convert(o));
            } else {
                row.setField(index++, o);
            }
        }
    }


    @Override
    public void close() throws IOException {
        initial();
    }


}
