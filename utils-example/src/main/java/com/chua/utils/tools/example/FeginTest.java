package com.chua.utils.tools.example;

import com.chua.unified.interfaces.IRemoteCallFactory;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.example.interfaces.ITokenService;
import com.chua.utils.tools.fegin.FeginRemoteCallFactory;
import org.testng.annotations.Test;

/**
 * @author CH
 * @date 2020-09-29
 */
public class FeginTest {

    @Test
    public void getObjectForEntity() {
        IRemoteCallFactory remoteCallFactory = new FeginRemoteCallFactory("http://localhost:18082");
        ITokenService entity = remoteCallFactory.target(ITokenService.class);
        System.out.println(JsonHelper.toJson(entity.ssoUser1()));
    }
}
