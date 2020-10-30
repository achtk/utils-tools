package com.chua.utils.netx.zookeeper.factory;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.properties.NetProperties;
import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
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
 * @since 2020/8/5 11:39
 */
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class ZookeeperFactory implements INetFactory<CuratorFramework> {

    @NonNull
    private NetProperties netProperties;
    private CuratorFramework curatorFramework;
    /**
     * 状态
     */
    @Getter
    private final AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public CuratorFramework client() {
        return this.curatorFramework;
    }

    @Override
    public void start() {
        log.info(">>>>>>>>>>> ZookeeperFactory Starting to connect");

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        builder.connectString(Joiner.on(",").join(netProperties.getHost()));

        if (MapHelper.isValid(NetProperties.CONFIG_FIELD_SESSION_TIMEOUT, netProperties)) {
            builder.sessionTimeoutMs(MapHelper.ints(NetProperties.CONFIG_FIELD_SESSION_TIMEOUT, netProperties));
        }

        if (MapHelper.isValid(NetProperties.CONFIG_FIELD_CONNECTION_TIMEOUT, netProperties)) {
            builder.connectionTimeoutMs(MapHelper.ints(NetProperties.CONFIG_FIELD_CONNECTION_TIMEOUT, netProperties));
        }

        builder.retryPolicy(new RetryNTimes(MapHelper.ints(NetProperties.CONFIG_FIELD_RETRY, netProperties), 1000));
        CountDownLatch countDownLatch = new CountDownLatch(1);
        this.curatorFramework = builder.build();
        this.curatorFramework.start();
        this.curatorFramework.getConnectionStateListenable().addListener((client, newState) -> {
            log.info("Zookeeper waiting for connection");
            state.set(newState.isConnected());
            if (newState.isConnected()) {
                log.info("Zookeeper connection succeeded...");
                countDownLatch.countDown();
            }
        });

        try {
            boolean await = countDownLatch.await(10, TimeUnit.SECONDS);
            if (!await) {
                close();
            }
            log.info(">>>>>>>>>>> ZookeeperFactory connection complete.");
        } catch (Exception e) {
            e.printStackTrace();
            log.info(">>>>>>>>>>> ZookeeperFactory connection activation failed.");
        }
    }

    @Override
    public boolean isStart() {
        CuratorFrameworkState state = this.curatorFramework.getState();
        return null != this.curatorFramework && state == STARTED;
    }

    @Override
    public void close() {
        if (null != curatorFramework) {
            curatorFramework.close();
            curatorFramework = null;
        }
    }
}
