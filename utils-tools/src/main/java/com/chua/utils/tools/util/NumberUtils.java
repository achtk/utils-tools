package com.chua.utils.tools.util;

import com.chua.utils.tools.common.NumberHelper;

import java.math.BigDecimal;

/**
 * 数字转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class NumberUtils extends NumberHelper {
    /**
     * 获取小数点后{scale}数据
     *
     * @param value 值
     * @param scale 小数点位数
     * @return 数据
     */
    public static BigDecimal scale(Long value, int scale) {
        if (null == value) {
            return BigDecimal.ZERO;
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal.setScale(scale < 0 ? 2 : scale, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }
}
