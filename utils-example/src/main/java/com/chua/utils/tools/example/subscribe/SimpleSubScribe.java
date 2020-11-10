package com.chua.utils.tools.example.subscribe;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.spi.Spi;
import com.google.common.eventbus.Subscribe;

import javax.annotation.Resource;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class SimpleSubScribe {

    @Resource
    @Spi
    private Encrypt encrypt;

    @Subscribe
    public void subscribe(Object value) {
        System.out.println(value);
        System.out.println(encrypt);
    }
}
