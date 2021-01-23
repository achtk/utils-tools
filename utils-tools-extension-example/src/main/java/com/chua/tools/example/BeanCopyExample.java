package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.bean.copy.BeanCopy;
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
        BeanCopy<TDemoInfo> beanCopy3 = BeanCopy.of("c", TDemoInfo.class);
        beanCopy3.with("names", 1);
        TDemoInfo tDemoInfo3 = beanCopy3.create();
        log.info(tDemoInfo3.getName());

        log.info("==============================对象赋值==============================");
        TDemoInfo tDemoInfo1 = new TDemoInfo();
        BeanCopy<TDemoInfo> beanCopy1 = BeanCopy.of(tDemoInfo1);
        beanCopy1.with("names", 1);
        TDemoInfo tDemoInfo2 = beanCopy1.create();
        log.info(tDemoInfo2.getName());
        log.info("Map = {}", beanCopy1.asMap());

        log.info("==============================类赋值==============================");
        BeanCopy<TDemoInfo> beanCopy = BeanCopy.of(TDemoInfo.class);
        beanCopy.with("names", 1);
        TDemoInfo tDemoInfo = beanCopy.create("names -> name", "names -> id");
        log.info(tDemoInfo.getName());
        log.info("Map = {}", beanCopy.asMap());
    }
}
