package com.chua.utils.netx.rpc.rmi;

import com.chua.utils.netx.rpc.adaptor.Adaptor;
import com.chua.utils.netx.rpc.adaptor.RmiAdaptor;
import com.chua.utils.netx.rpc.config.RpcProviderConfig;
import com.chua.utils.netx.rpc.resolver.RpcProvider;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author CH
 * @version 1.0.0
 * @className RmiProvider
 * @since 2020/8/1 2:05
 */
public class RmiProvider<T> implements RpcProvider<T> {

    private static final Adaptor ADAPTOR = new RmiAdaptor();

    private static final AtomicBoolean RUNING = new AtomicBoolean(false);

    @Override
    public T provider(RpcProviderConfig<T> rpcProviderConfig) {
        try {
            if (!RUNING.get()) {
                RUNING.set(true);
                LocateRegistry.createRegistry(rpcProviderConfig.getPort(this));
            }
            Object adaptor = ADAPTOR.adaptor(rpcProviderConfig.getRef(), rpcProviderConfig.getInterfaces());
            String rmi = "rmi://" + rpcProviderConfig.getDirectUrl() + "/" + rpcProviderConfig.getInterfaces().getName();
            Naming.rebind(rmi, (Remote) adaptor);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


    @Override
    public int getDefaultPort() {
        return 1099;
    }
}
