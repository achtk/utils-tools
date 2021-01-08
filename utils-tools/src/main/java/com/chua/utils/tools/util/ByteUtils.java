package com.chua.utils.tools.util;

import com.chua.utils.tools.common.ByteHelper;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 字节工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/26
 */
public class ByteUtils extends ByteHelper {

    /**
     * 类型转化<br />
     * 默认或者无效返回0
     *
     * @param bytes  字节数组
     * @param offset 位置
     * @param length 字节长度
     * @return 默认或者无效返回0
     */
    public static BigDecimal toBigDecimal(byte[] bytes, int offset, int length) {
        byte[] target = new byte[length];
        System.arraycopy(bytes, offset, target, 0, length);
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(target);
        buffer.flip();
        if (length == 1) {
            return BigDecimal.valueOf(target[0]);
        } else if (length == 2) {
            return BigDecimal.valueOf(buffer.getShort());
        } else if (length == 4) {
            return BigDecimal.valueOf(buffer.getFloat());
        } else if (length == 8) {
            return BigDecimal.valueOf(buffer.getDouble());
        }
        return BigDecimal.ZERO;

    }
}
