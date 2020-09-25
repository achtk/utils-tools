package com.chua.utils.netx.zookeeper.factory;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.common.MapHelper;
import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.curator.framework.imps.CuratorFrameworkState.STARTED;

/**
 * zookeeper工具
 *
 * @author CH
 * @version 1.0.0
 * @className ZookeeperFactory
 * @since 2020/8/5 11:39
 */
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class ZookeeperFactory implements INetxFactory<CuratorFramework> {

    @NonNull
    private NetxProperties netxProperties;
    private CuratorFramework curatorFramework;
    /**
     * 状态
     */
    @Getter
    private AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public CuratorFramework client() {
        return this.curatorFramework;
    }

    @Override
    public void start() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        builder.connectString(Joiner.on(",").join(netxProperties.getHost()));

        if (MapHelper.isValid(NetxProperties.CONFIG_FIELD_SESSION_TIMEOUT, netxProperties)) {
            builder.sessionTimeoutMs(MapHelper.ints(NetxProperties.CONFIG_FIELD_SESSION_TIMEOUT, netxProperties));
        }

        if (MapHelper.isValid(NetxProperties.CONFIG_FIELD_CONNECTION_TIMEOUT, netxProperties)) {
            builder.connectionTimeoutMs(MapHelper.ints(NetxProperties.CONFIG_FIELD_CONNECTION_TIMEOUT, netxProperties));
        }

        if (MapHelper.isValid(NetxProperties.CONFIG_FIELD_RETRY, netxProperties)) {
            builder.retryPolicy(new RetryNTimes(MapHelper.ints(NetxProperties.CONFIG_FIELD_RETRY, netxProperties), 1000));
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        this.curatorFramework = builder.build();
        this.curatorFramework.start();
        this.curatorFramework.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                log.info("等待连接");
                state.set(newState.isConnected());
                if (newState.isConnected()) {
                    log.info("连接成功...");
                    countDownLatch.countDown();
                }
            }
        });

        try {
            boolean await = countDownLatch.await(10, TimeUnit.SECONDS);
            if (!await) {
                close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isStart() {
        CuratorFrameworkState state = this.curatorFramework.getState();
        return null != this.curatorFramework && state == STARTED;
    }

    @Override
    public void close() throws Exception {
        if (null != curatorFramework) {
            curatorFramework.close();
            curatorFramework = null;
        }
    }
}
