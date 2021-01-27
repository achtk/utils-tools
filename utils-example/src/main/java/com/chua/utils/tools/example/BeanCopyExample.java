package com.chua.utils.tools.example;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.text.IdHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
@Slf4j
public class BeanCopyExample {

    public static void main(String[] args) {
        System.out.println("==============================字符串创建类并赋值==============================");
        BeanCopy beanCopy3 = BeanCopy.of("com.chua.utils.tools.example.entity.TDemoInfo");
        beanCopy3.with("name", IdHelper.createUuid());
        TDemoInfo tDemoInfo3 = (TDemoInfo) beanCopy3.create();
        System.out.println(tDemoInfo3.uuid());

        System.out.println("==============================类赋值==============================");
        BeanCopy<TDemoInfo> beanCopy = BeanCopy.of(TDemoInfo.class);
        beanCopy.with("name", IdHelper.createUuid());
        TDemoInfo tDemoInfo = beanCopy.create();
        System.out.println(tDemoInfo.uuid());

        System.out.println("==============================对象赋值==============================");
        TDemoInfo tDemoInfo1 = new TDemoInfo();
        BeanCopy<TDemoInfo> beanCopy1 = BeanCopy.of(tDemoInfo1);
        beanCopy1.with("name", IdHelper.createUuid());
        TDemoInfo tDemoInfo2 = beanCopy.create();
        System.out.println(tDemoInfo2.uuid());
        System.out.println(beanCopy.asMap());
    }
}
