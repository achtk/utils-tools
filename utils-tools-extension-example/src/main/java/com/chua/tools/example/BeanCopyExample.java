package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
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
        BeanCopy<TDemoInfoImpl> beanCopy3 = BeanCopy.of("c", TDemoInfoImpl.class);
        beanCopy3.with("names", 1);
        TDemoInfoImpl tDemoInfoImpl3 = beanCopy3.create();
        log.info(tDemoInfoImpl3.getName());

        log.info("==============================对象赋值==============================");
        TDemoInfoImpl tDemoInfoImpl1 = new TDemoInfoImpl();
        BeanCopy<TDemoInfoImpl> beanCopy1 = BeanCopy.of(tDemoInfoImpl1);
        beanCopy1.with("names", 1);
        TDemoInfoImpl tDemoInfoImpl2 = beanCopy1.create();
        log.info(tDemoInfoImpl2.getName());
        log.info("Map = {}", beanCopy1.asMap());

        log.info("==============================类赋值==============================");
        BeanCopy<TDemoInfoImpl> beanCopy = BeanCopy.of(TDemoInfoImpl.class);
        beanCopy.with("names", 1);
        TDemoInfoImpl tDemoInfoImpl = beanCopy.create("names -> name", "names -> id");
        log.info(tDemoInfoImpl.getName());
        log.info("Map = {}", beanCopy.asMap());
    }
}
