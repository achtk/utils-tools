package com.chua.utils.netx.kitdb.context;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.netx.function.RKv;
import com.chua.utils.netx.function.RKvProducer;
import com.chua.utils.netx.function.RList;
import com.chua.utils.netx.function.RListProducer;
import com.chua.utils.netx.kitdb.factory.KitDbFactory;
import com.chua.utils.netx.kitdb.function.KitDbRKv;
import com.chua.utils.netx.kitdb.function.KitDbRList;
import com.chua.utils.tools.properties.NetProperties;
import top.thinkin.lightd.db.DB;

/**
 * kitdb上下文
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
public class KitDbContext implements RKvProducer<String, byte[]>, RListProducer<String, byte[]>, AutoCloseable {

    private final DB db;
    private INetFactory<DB> netxFactory;

    public KitDbContext(NetProperties netProperties) {
        this.netxFactory = new KitDbFactory(netProperties);
        this.db = this.netxFactory.client();
        this.netxFactory.start();
    }

    @Override
    public RKv<String, byte[]> getKv() {
        return new KitDbRKv(db.getrKv());
    }


    @Override
    public void close() throws Exception {
        this.netxFactory.close();
    }

    @Override
    public RList<String, byte[]> getRList() {
        return new KitDbRList(db.getList());
    }
}
