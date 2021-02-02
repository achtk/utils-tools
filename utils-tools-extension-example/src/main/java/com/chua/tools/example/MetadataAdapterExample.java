package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.utils.tools.classes.adaptor.AsmAdaptor;
import com.chua.utils.tools.classes.adaptor.MetadataAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * 元数据适配器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/28
 */
public class MetadataAdapterExample extends BaseExample {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Class<?> aClass = TDemoInfoImpl.class;
        String externalForm = aClass.getProtectionDomain().getCodeSource().getLocation().toExternalForm();
        File file = new File(new StringBuilder(externalForm.replace("file:/", "").replace("target/classes", "src/main/java")).append(aClass.getName().replace(".", "/")).append(".java").toString());
        InputStream inputStream = new FileInputStream(file);
        //创建Asm元数据适配器
        MetadataAdapter metadataAdapter = new AsmAdaptor(inputStream);
        log.info("获取父类: {}" + metadataAdapter.getSuperClass());
        log.info("获取接口: {}" + metadataAdapter.getAnnotations());
        log.info("获取接口: {}" + metadataAdapter.getInterfaceName());
        log.info("获取方法: {}" + metadataAdapter.getMethods());
        log.info("获取字段: {}" + metadataAdapter.getFields());

    }

}
