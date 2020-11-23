package com.chua.utils.tools.example;

import com.chua.utils.tools.common.NetHelper;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class NetHelperExample {

    public static void main(String[] args) {
        System.out.println("获取本地地址: " + NetHelper.getLocalAddress());
        System.out.println("获取所有有效网卡: " + NetHelper.getNetworkInterfaces());
    }
}
