package com.chua.tools.example;

import java.security.Provider;
import java.security.Security;

/**
 * bcprov
 * @author CH
 * @version 1.0.0
 * @since 2021/1/28
 */
public class BcprovExample {

    public static void main(String[] args) {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println(provider.getName() + " : " + provider.getInfo());
        }
    }
}
