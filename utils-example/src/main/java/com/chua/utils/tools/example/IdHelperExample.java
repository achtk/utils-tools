package com.chua.utils.tools.example;

import com.chua.utils.tools.text.IdHelper;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
public class IdHelperExample {

    public static void main(String[] args) {
        System.out.println("uuid: " + IdHelper.createUuid());
        System.out.println("simple-uuid: " + IdHelper.createSimpleUuid());
        for (int i = 0; i < 100; i++) {
            System.out.println("version: " + IdHelper.createVersion(3, 20, i));
        }
        System.out.println("snowflake: " + IdHelper.createSnowflake(3, 3));
    }
}
