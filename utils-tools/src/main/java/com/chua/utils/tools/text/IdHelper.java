package com.chua.utils.tools.text;

import com.chua.utils.tools.text.snowflake.Snowflake;

import java.util.UUID;

/**
 * 唯一ID
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class IdHelper {
    /**
     * 生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
     *
     * @return UUID
     */
    public static String createUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
     *
     * @return UUID
     */
    public static String createSimpleUuid() {
        return createUuid().replace("-", "");
    }

    /**
     * 雪花ID
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     * @return 雪花算法
     */
    public static Snowflake createSnowflake(long workerId, long datacenterId) {
        return new Snowflake(3, 1);
    }
}
