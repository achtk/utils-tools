package com.chua.utils.tools.example;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.spi.processor.ReflectionExtensionProcessor;
import org.apache.commons.codec.Encoder;
import org.testng.annotations.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * spi工厂
 *
 * @author CH
 */
public class ExtensionFactoryExample {
    /**
     * 获取base64名称  IEncrypt子类
     *
     * @
     */
    @Test
    public void testGetExtensionLoader() throws Exception {
        ExtensionLoader<Encrypt> extensionLoader = ExtensionFactory.getExtensionLoader(Encrypt.class);
        System.out.println(extensionLoader);
        ExtensionLoader<Encoder> extensionLoader1 = ExtensionFactory.getExtensionLoader(Encoder.class, new ReflectionExtensionProcessor());
        System.out.println(extensionLoader1);

        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(extensionLoader1);
                System.out.println(extensionLoader.getAllSpiService().size());
            }
        }, 0, 3, TimeUnit.SECONDS);

        for(;;){}
    }
}