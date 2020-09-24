package com.chua.utils.netx.rpc.rmi;

import com.chua.utils.netx.rpc.adaptor.IAdaptor;
import com.chua.utils.netx.rpc.adaptor.RmiAdaptor;
import com.chua.utils.netx.rpc.config.RpcProviderConfig;
import com.chua.utils.netx.rpc.resolver.IRpcProvider;

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
public class RmiProvider implements IRpcProvider {

    private static final IAdaptor ADAPTOR = new RmiAdaptor();

    private static final AtomicBoolean RUNING = new AtomicBoolean(false);

    @Override
    public Object provider(RpcProviderConfig rpcProviderConfig) {

        try {
            if(!RUNING.get()) {
                RUNING.set(true);
                LocateRegistry.createRegistry(getPort(rpcProviderConfig.getDirectUrl()));
            }
            Object adaptor = ADAPTOR.adaptor(rpcProviderConfig.getRef(), rpcProviderConfig.getInterfaces());
            String rmi = "rmi://" + rpcProviderConfig.getDirectUrl() + "/" + rpcProviderConfig.getInterfaces().getName();
            Naming.rebind(rmi, (Remote) adaptor);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * 获取端口
     * <p>
     *     127.0.0.1:8080 -> 8080
     * </p>
     * @param host
     * @return
     */
    private int getPort(String host) {
        if(null == host) {
            return -1;
        }

        int index = host.indexOf("://");
        if(index > -1) {
            host = host.substring(index + 3);
        }

        if(host.indexOf(":") == -1) {
            return -1;
        }
        String[] strings = host.split(":");
        try {
            return Integer.valueOf(strings[1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
