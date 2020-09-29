package com.chua.utils.tools.fegin;

import com.chua.unified.interfaces.IRemoteCallFactory;
import com.chua.utils.tools.fegin.helper.FeginHelper;
import feign.Feign;

/**
 * Fegin远程调用工厂
 * @author CH
 */
public class FeginRemoteCallFactory implements IRemoteCallFactory {

    @Override
    public <T> T target(Class<T> tClass, String serverUrl) {
        return FeginHelper.getObjectForEntity(tClass, serverUrl);
    }
}
