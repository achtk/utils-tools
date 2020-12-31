package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.text.IdHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
@Slf4j
public class BeanCopyExample extends BaseExample {

    public static void main(String[] args) {
        log.info("==============================字符串创建类并赋值==============================");
        BeanCopy beanCopy3 = BeanCopy.of("com.chua.utils.tools.example.entity.TDemoInfo");
        beanCopy3.with("name", IdHelper.createUuid());
        TDemoInfo tDemoInfo3 = (TDemoInfo) beanCopy3.create();
        log.info(tDemoInfo3.getUuid());

        log.info("==============================类赋值==============================");
        BeanCopy<TDemoInfo> beanCopy = BeanCopy.of(TDemoInfo.class);
        beanCopy.with("name", IdHelper.createUuid());
        TDemoInfo tDemoInfo = beanCopy.create();
        log.info(tDemoInfo.getUuid());

        log.info("==============================对象赋值==============================");
        TDemoInfo tDemoInfo1 = new TDemoInfo();
        BeanCopy<TDemoInfo> beanCopy1 = BeanCopy.of(tDemoInfo1);
        beanCopy1.with("name", IdHelper.createUuid());
        TDemoInfo tDemoInfo2 = beanCopy.create();
        log.info(tDemoInfo2.getUuid());
        log.info("Map = {}", beanCopy.asMap());
    }
}
