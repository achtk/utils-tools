package com.chua.tools.example;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.resource.template.ResourceTemplate;

/**
 * 资源查找
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/29
 */
public class ResourceExample extends BaseExample {

    private static final ResourceTemplate TEMPLATE = new ResourceTemplate();

    public static void main(String[] args) {
        log.info("查找根目录下的xml: classpath:*.xml -> {}", TEMPLATE.getResources("classpath:*.xml"));
        log.info("查找资源加载器下[Encrypt.class]子类(查询时间与文件数成正比) -> {}", TEMPLATE.getSubOfType(Encrypt.class));
    }
}
