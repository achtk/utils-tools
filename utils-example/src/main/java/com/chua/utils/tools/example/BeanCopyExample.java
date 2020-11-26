package com.chua.utils.tools.example;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.bean.copy.StandardBeanCopy;
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
        System.out.println("==============================类赋值==============================");
        BeanCopy<TDemoInfo> beanCopy = StandardBeanCopy.of(TDemoInfo.class);
        beanCopy.with("name", IdHelper.createUuid());
        TDemoInfo tDemoInfo = beanCopy.create();
        System.out.println(tDemoInfo.getUuid());

        System.out.println("==============================对象赋值==============================");
        TDemoInfo tDemoInfo1 = new TDemoInfo();
        BeanCopy<TDemoInfo> beanCopy1 = StandardBeanCopy.of(tDemoInfo1);
        beanCopy1.with("name", IdHelper.createUuid());
        TDemoInfo tDemoInfo2 = beanCopy.create();
        System.out.println(tDemoInfo2.getUuid());
        System.out.println(beanCopy.asMap());
    }
}
