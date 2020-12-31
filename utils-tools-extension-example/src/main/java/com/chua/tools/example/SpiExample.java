package com.chua.tools.example;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Spi测试
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/29
 */
@Slf4j
public class SpiExample extends BaseExample{

    public static void main(String[] args) {
        //1.获取加载器(待查询: Encrypt.class)
        ExtensionLoader<Encrypt> extensionLoader = ExtensionFactory.getExtensionLoader(Encrypt.class);
        //2.获取[Base64]实现, 不存在返回 null
        log.info("获取[Base64]实现: {}", extensionLoader.getExtension("base64"));
        log.info("获取唯一一个实现类: {}", extensionLoader.getExtension());
        log.info("获取所有实现类(Set): {}", extensionLoader.getAllSpiService());
        log.info("获取所有实现类(Map): {}", extensionLoader.getPriorityExtension());

    }

}
