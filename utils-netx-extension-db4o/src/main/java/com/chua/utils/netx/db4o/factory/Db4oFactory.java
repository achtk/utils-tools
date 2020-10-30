package com.chua.utils.netx.db4o.factory;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.properties.NetProperties;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.google.common.base.Strings;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * db4o
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
@Slf4j
@RequiredArgsConstructor
public class Db4oFactory implements INetFactory<ObjectContainer> {
    @NonNull
    private NetProperties netProperties;
    private ObjectContainer objectContainer;

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public ObjectContainer client() {
        if (null == netProperties || Strings.isNullOrEmpty(netProperties.getPath())) {
            log.warn("Db4o需要配置 [path]{.yap}");
            throw new NullPointerException();
        }
        try {
            this.objectContainer = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), netProperties.getPath());
            return this.objectContainer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public boolean isStart() {
        return false;
    }

    @Override
    public void close() throws Exception {
        objectContainer.close();
    }
}
