package com.chua.utils.netx.db4o.factory;

import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.properties.NetxProperties;
import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.internal.config.EmbeddedConfigurationImpl;
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
public class Db4oFactory implements INetxFactory<ObjectContainer> {
    @NonNull
    private NetxProperties netxProperties;
    private ObjectContainer objectContainer;

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public ObjectContainer client() {
        if (null == netxProperties || Strings.isNullOrEmpty(netxProperties.getPath())) {
            log.warn("Db4o需要配置 [path]{.yap}");
            throw new NullPointerException();
        }
        try {
            this.objectContainer = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), netxProperties.getPath());
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
