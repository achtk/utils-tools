package com.chua.utils.netx.flink.format;

import com.chua.utils.netx.flink.utils.RowUtils;
import com.chua.utils.tools.util.CollectionUtils;
import com.chua.utils.tools.util.JsonUtils;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.types.Row;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
@Slf4j
public class RedisOutputFormat extends FlinkOutputFormat implements RedisFormat {

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void configure(Configuration parameters) {
    }


    @Override
    public void open(int taskNumber, int numTasks) throws IOException {
        this.initialJedis(sign);
    }

    @Override
    public void writeRecord(Row record) throws IOException {
        String index = FormatConnector.getIndex(sign);
        ShardedJedisPool pool = FormatConnector.<ShardedJedisPool>getPool(sign);
        try (ShardedJedis jedis = pool.getResource()) {
            String s = jedis.get(index);
            if (s.startsWith("[")) {
                List<Object> objects = JsonUtils.fromJson2List(s);
                Object first = CollectionUtils.findFirst(objects);
                if (null == first) {
                    objects.add(RowUtils.toMap(record, FormatConnector.getSchema(sign)));
                } else {
                    Class<?> aClass = first.getClass();
                    if (Map.class.isAssignableFrom(aClass)) {
                        objects.add(RowUtils.toMap(record, FormatConnector.getSchema(sign)));
                    } else {
                        objects.add(JsonUtils.toJson(RowUtils.toMap(record, FormatConnector.getSchema(sign))));
                    }
                }
                jedis.set(index, JsonUtils.toJson(objects));
            } else if (s.startsWith("{")) {
                Map<String, Object> stringObjectMap = JsonUtils.fromJson2Map(s);
                if (null == stringObjectMap) {
                    stringObjectMap = new HashMap<>();
                }
                stringObjectMap.putAll(RowUtils.toMap(record, FormatConnector.getSchema(sign)));
                jedis.set(index, JsonUtils.toJson(stringObjectMap));
            } else {
                Map<String, Object> objectMap = RowUtils.toMap(record, FormatConnector.getSchema(sign));
                s += "," + Joiner.on(",").withKeyValueSeparator(":").join(objectMap);
                jedis.set(index, s);
            }
        }
    }

    @Override
    public void close() throws IOException {
        ShardedJedisPool pool = FormatConnector.<ShardedJedisPool>getPool(sign);
        if (null != pool) {
            pool.close();
        }
    }


}
