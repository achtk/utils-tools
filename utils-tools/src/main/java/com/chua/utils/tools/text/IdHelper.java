package com.chua.utils.tools.text;

import com.chua.utils.tools.text.snowflake.Snowflake;

import java.util.ArrayList;
import java.util.List;
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
     * @param workerId   工作ID (0~31)
     * @param dataCenter 数据中心ID (0~31)
     * @return 雪花算法
     */
    public static Snowflake createSnowflake(long workerId, long dataCenter) {
        return new Snowflake(workerId, dataCenter);
    }

    /**
     * 创建自增版本
     * <p>e.g.createVersion(3, 10, 11) = 0.1.1</p>
     * <p>e.g.createVersion(3, 11, 11) = 0.0.11</p>
     * <p>e.g.createVersion(3, 2, 11) = 1.2.1</p>
     *
     * @param versionNumber 版本序号
     * @param maxNumber     最大序号
     * @param currentNumber 当前序号
     * @return 版本
     */
    public static String createVersion(int versionNumber, long maxNumber, long currentNumber) {
        List<Long> temp = new ArrayList<>();
        for (int i = 1; i < versionNumber; i++) {
            temp.add(currentNumber % maxNumber);
            currentNumber = currentNumber / maxNumber;
        }
        temp.add(currentNumber);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = versionNumber - 1; i > -1; i--) {
            stringBuilder.append(".").append(temp.get(i));

        }
        return stringBuilder.substring(1);
    }
}
