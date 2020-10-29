package com.chua.utils.netx.kitdb.factory;

import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.properties.NetxProperties;
import com.google.common.base.Strings;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.thinkin.lightd.db.DB;
import top.thinkin.lightd.exception.KitDBException;

/**
 * kitdb
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
@Slf4j
@RequiredArgsConstructor
public class KitDbFactory implements INetxFactory<DB> {
    @NonNull
    private NetxProperties netxProperties;
    private DB db;

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public DB client() {
        if (null == netxProperties || Strings.isNullOrEmpty(netxProperties.getPath())) {
            log.warn("KitDb需要配置 [path]");
            throw new NullPointerException();
        }
        try {
            this.db = DB.buildTransactionDB(netxProperties.getPath(), true);
            return this.db;
        } catch (KitDBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public boolean isStart() {
        return true;
    }

    @Override
    public void close() throws Exception {
        if (null != this.db) {
            this.db.close();
        }
    }
}
