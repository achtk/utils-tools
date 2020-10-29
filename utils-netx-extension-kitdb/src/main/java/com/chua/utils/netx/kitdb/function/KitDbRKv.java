package com.chua.utils.netx.kitdb.function;

import com.chua.utils.netx.function.RKv;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * kitdb-kv
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
@AllArgsConstructor
public class KitDbRKv implements RKv<String, byte[]> {

    private top.thinkin.lightd.db.RKv rKv;

    @Override
    public void set(String key, byte[] value) throws Exception {
        rKv.set(key, value);
    }

    @Override
    public byte[] get(String key) throws Exception {
        return rKv.get(key);
    }

    @Override
    public void incr(String key, int step, int ttl) throws Exception {
        rKv.incr(key, step, ttl);
    }

    @Override
    public void incr(String key, int step) throws Exception {
        rKv.incr(key, step);
    }

    @Override
    public void set(Map<String, byte[]> map) throws Exception {
        rKv.set(map);
    }

    @Override
    public void set(Map<String, byte[]> map, int ttl) throws Exception {
        rKv.set(map, ttl);
    }

    @Override
    public void set(String key, byte[] value, int ttl) throws Exception {
        rKv.set(key, value, ttl);
    }

    @Override
    public void ttl(String key, int ttl) throws Exception {
        rKv.ttl(key, ttl);
    }

    @Override
    public Map<String, byte[]> get(List<String> keys) throws Exception {
        return rKv.get(keys);
    }

    @Override
    public void del(String key) throws Exception {
        rKv.del(key);
    }

    @Override
    public void delPrefix(String key_) throws Exception {
        rKv.delPrefix(key_);
    }

    @Override
    public List<byte[]> keys(String key_) throws Exception {
        List<String> keys = getKeys(key_);
        List<byte[]> value = new ArrayList<>();
        for (String key : keys) {
            value.add(get(key));
        }
        return value;
    }

    @Override
    public List<String> getKeys(String key_) throws Exception {
        return rKv.keys(key_, 0, 100);
    }

}
