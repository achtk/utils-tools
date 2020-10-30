package com.chua.utils.netx.kitdb.factory;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.properties.NetProperties;
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
public class KitDbFactory implements INetFactory<DB> {
    @NonNull
    private NetProperties netProperties;
    private DB db;

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public DB client() {
        if (null == netProperties || Strings.isNullOrEmpty(netProperties.getPath())) {
            log.warn("KitDb需要配置 [path]");
            throw new NullPointerException();
        }
        try {
            this.db = DB.buildTransactionDB(netProperties.getPath(), true);
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
