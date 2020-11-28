package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.adaptor.AsmAdaptor;
import com.chua.utils.tools.classes.adaptor.MetadataAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 * 元数据适配器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/28
 */
public class MetadataAdapterExample {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Paths.get("D:\\work\\utils-tools-parent\\utils-example\\target\\classes\\com\\chua\\utils\\tools\\example\\entity\\TDemoInfo.class").toUri().toURL().openStream();
        MetadataAdapter metadataAdapter = new AsmAdaptor(inputStream);
        System.out.println("获取父类:" + metadataAdapter.getSuperClass());
        System.out.println("获取接口:" + metadataAdapter.getInterfaceName());
        System.out.println("获取接口:" + metadataAdapter.getInterfaceName());
        System.out.println("获取方法:" + metadataAdapter.getMethods());
        System.out.println("获取字段:" + metadataAdapter.getFields());
        System.out.println();

    }

}
