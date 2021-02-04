package com.chua.tools.example;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.resource.factory.ReflectionFactory;
import com.chua.utils.tools.resource.template.ResourceTemplate;

/**
 * 资源查找
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/29
 */
public class ResourceExample extends BaseExample {

    private static final String NAME = "classpath:*.xml";
    private static final Class<?> SUB = Encrypt.class;

    public static void main(String[] args) {
        //通过jdk查询资源
        testResourceForJdk();
        //通过reflections查询资源
        testResourceForReflections();
    }

    private static void testResourceForReflections() {
        log.info("=====================================[通过reflections查询资源]====================================");
        ResourceTemplate template = new ResourceTemplate(new ReflectionFactory());
        log.info("查找根目录下的xml: classpath:*.xml -> {}", template.getResources(NAME));
        log.info("查找资源加载器下[Encrypt.class]子类(查询时间与文件数成正比) -> {}", template.getSubOfType(SUB));
    }

    /**
     * 通过jdk查询资源
     */
    private static void testResourceForJdk() {
        log.info("=====================================[通过jdk查询资源]====================================");

        ResourceTemplate template = new ResourceTemplate();
        log.info("查找根目录下的xml: classpath:*.xml -> {}", template.getResources(NAME));
        log.info("查找资源加载器下[Encrypt.class]子类(查询时间与文件数成正比) -> {}", template.getSubOfType(SUB));
    }
}
