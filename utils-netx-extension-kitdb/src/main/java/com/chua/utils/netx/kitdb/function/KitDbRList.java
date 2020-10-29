package com.chua.utils.netx.kitdb.function;

import com.chua.utils.netx.function.RList;
import com.chua.utils.tools.function.Matcher;
import lombok.AllArgsConstructor;
import top.thinkin.lightd.db.REntry;
import top.thinkin.lightd.db.RIterator;

import java.util.List;

/**
 * kitdb-zset
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
@AllArgsConstructor
public class KitDbRList implements RList<String, byte[]> {

    private top.thinkin.lightd.db.RList rList;

    @Override
    public void delete(String s) throws Exception {
        rList.delete(s);
    }

    @Override
    public void add(String s, byte[] bytes) throws Exception {
        rList.add(s, bytes);
    }

    @Override
    public void addAll(String s, List<byte[]> bytes) throws Exception {
        rList.addAll(s, bytes);
    }

    @Override
    public void add(String s, byte[] bytes, int ttl) throws Exception {
        rList.addMayTTL(s, bytes, ttl);
    }

    @Override
    public void addAll(String s, List<byte[]> bytes, int ttl) throws Exception {
        rList.addAllMayTTL(s, bytes, ttl);
    }

    @Override
    public boolean exist(String s) throws Exception {
        return rList.isExist(s);
    }

    @Override
    public void iterator(String v, Matcher<byte[]> matcher) throws Throwable {
        RIterator<top.thinkin.lightd.db.RList> rIterator = rList.iterator(v);
        while (rIterator.hasNext()) {
            top.thinkin.lightd.db.RList.Entry entry = rIterator.next();
            matcher.doWith(entry.getValue());
        }
    }
}
