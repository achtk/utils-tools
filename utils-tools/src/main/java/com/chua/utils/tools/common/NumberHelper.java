package com.chua.utils.tools.common;


import com.chua.utils.tools.constant.StringConstant;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.regex.Pattern;

import static com.chua.utils.tools.constant.StringConstant.HEX_16_UPPER;
import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * 数字处理
 *
 * @author Administrator
 */
public class NumberHelper {
    /**
     * 默认除法运算精度
     */
    private static final int DEFAUT_DIV_SCALE = 10;
    /**
     * Reusable Long constant for zero.
     */
    public static final Long LONG_ZERO = Long.valueOf(0L);
    /**
     * Reusable Long constant for one.
     */
    public static final Long LONG_ONE = Long.valueOf(1L);
    /**
     * Reusable Long constant for minus one.
     */
    public static final Long LONG_MINUS_ONE = Long.valueOf(-1L);
    /**
     * Reusable Integer constant for zero.
     */
    public static final Integer INTEGER_ZERO = Integer.valueOf(0);
    /**
     * Reusable Integer constant for one.
     */
    public static final Integer INTEGER_ONE = Integer.valueOf(1);
    /**
     * Reusable Integer constant for two
     */
    public static final Integer INTEGER_TWO = Integer.valueOf(2);
    /**
     * Reusable Integer constant for minus one.
     */
    public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
    /**
     * Reusable Short constant for zero.
     */
    public static final Short SHORT_ZERO = Short.valueOf((short) 0);
    /**
     * Reusable Short constant for one.
     */
    public static final Short SHORT_ONE = Short.valueOf((short) 1);
    /**
     * Reusable Short constant for minus one.
     */
    public static final Short SHORT_MINUS_ONE = Short.valueOf((short) -1);
    /**
     * Reusable Byte constant for zero.
     */
    public static final Byte BYTE_ZERO = Byte.valueOf((byte) 0);
    /**
     * Reusable Byte constant for one.
     */
    public static final Byte BYTE_ONE = Byte.valueOf((byte) 1);
    /**
     * Reusable Byte constant for minus one.
     */
    public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte) -1);
    /**
     * Reusable Double constant for zero.
     */
    public static final Double DOUBLE_ZERO = Double.valueOf(0.0d);
    /**
     * Reusable Double constant for one.
     */
    public static final Double DOUBLE_ONE = Double.valueOf(1.0d);
    /**
     * Reusable Double constant for minus one.
     */
    public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0d);
    /**
     * Reusable Float constant for zero.
     */
    public static final Float FLOAT_ZERO = Float.valueOf(0.0f);
    /**
     * Reusable Float constant for one.
     */
    public static final Float FLOAT_ONE = Float.valueOf(1.0f);
    /**
     * Reusable Float constant for minus one.
     */
    public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0f);

    private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param v     值
     * @param scale 保留小数位数
     * @return 新值
     */
    public static BigDecimal round(double v, int scale) {
        return round(v, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param v     值
     * @param scale 保留小数位数
     * @return 新值
     */
    public static String roundStr(double v, int scale) {
        return round(v, scale).toString();
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param numberStr 数字值的字符串表现形式
     * @param scale     保留小数位数
     * @return 新值
     */
    public static BigDecimal round(String numberStr, int scale) {
        return round(numberStr, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param number 数字值
     * @param scale  保留小数位数
     * @return 新值
     * @since 4.1.0
     */
    public static BigDecimal round(BigDecimal number, int scale) {
        return round(number, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param numberStr 数字值的字符串表现形式
     * @param scale     保留小数位数
     * @return 新值
     * @since 3.2.2
     */
    public static String roundStr(String numberStr, int scale) {
        return round(numberStr, scale).toString();
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param v            值
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     */
    public static BigDecimal round(double v, int scale, RoundingMode roundingMode) {
        return round(Double.toString(v), scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param v            值
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     * @since 3.2.2
     */
    public static String roundStr(double v, int scale, RoundingMode roundingMode) {
        return round(v, scale, roundingMode).toString();
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param numberStr    数字值的字符串表现形式
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(String numberStr, int scale, RoundingMode roundingMode) {
        if (scale < 0) {
            scale = 0;
        }
        return round(toBigDecimal(numberStr), scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param number       数字值
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(BigDecimal number, int scale, RoundingMode roundingMode) {
        if (null == number) {
            number = BigDecimal.ZERO;
        }
        if (scale < 0) {
            scale = 0;
        }
        if (null == roundingMode) {
            roundingMode = RoundingMode.HALF_UP;
        }

        return number.setScale(scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param numberStr    数字值的字符串表现形式
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     * @since 3.2.2
     */
    public static String roundStr(String numberStr, int scale, RoundingMode roundingMode) {
        return round(numberStr, scale, roundingMode).toString();
    }

    /**
     * 四舍六入五成双计算法
     * <p>
     * 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
     * </p>
     *
     * <pre>
     * 算法规则:
     * 四舍六入五考虑，
     * 五后非零就进一，
     * 五后皆零看奇偶，
     * 五前为偶应舍去，
     * 五前为奇要进一。
     * </pre>
     *
     * @param number 需要科学计算的数据
     * @param scale  保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundHalfEven(Number number, int scale) {
        return roundHalfEven(toBigDecimal(number), scale);
    }

    /**
     * 四舍六入五成双计算法
     * <p>
     * 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
     * </p>
     *
     * <pre>
     * 算法规则:
     * 四舍六入五考虑，
     * 五后非零就进一，
     * 五后皆零看奇偶，
     * 五前为偶应舍去，
     * 五前为奇要进一。
     * </pre>
     *
     * @param value 需要科学计算的数据
     * @param scale 保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundHalfEven(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.HALF_EVEN);
    }

    /**
     * 保留固定小数位数，舍去多余位数
     *
     * @param number 需要科学计算的数据
     * @param scale  保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundDown(Number number, int scale) {
        return roundDown(toBigDecimal(number), scale);
    }

    /**
     * 保留固定小数位数，舍去多余位数
     *
     * @param value 需要科学计算的数据
     * @param scale 保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundDown(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.DOWN);
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 和
     */
    public static double add(float v1, float v2) {
        return add(Float.toString(v1), Float.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 和
     */
    public static double add(float v1, double v2) {
        return add(Float.toString(v1), Double.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 和
     */
    public static double add(double v1, float v2) {
        return add(Double.toString(v1), Float.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 和
     */
    public static double add(double v1, double v2) {
        return add(Double.toString(v1), Double.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 和
     * @since 3.1.1
     */
    public static double add(Double v1, Double v2) {
        //noinspection RedundantCast
        return add((Number) v1, (Number) v2).doubleValue();
    }

    /**
     * 提供精确的加法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 和
     */
    public static BigDecimal add(Number v1, Number v2) {
        return add(new Number[]{v1, v2});
    }

    /**
     * 提供精确的加法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被加值
     * @return 和
     * @since 4.0.0
     */
    public static BigDecimal add(Number... values) {
        if (ArraysHelper.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        Number value = values[0];
        BigDecimal result = null == value ? BigDecimal.ZERO : new BigDecimal(value.toString());
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (null != value) {
                result = result.add(new BigDecimal(value.toString()));
            }
        }
        return result;
    }

    /**
     * 提供精确的加法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被加值
     * @return 和
     * @since 4.0.0
     */
    public static BigDecimal add(String... values) {
        if (ArraysHelper.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        String value = values[0];
        BigDecimal result = null == value ? BigDecimal.ZERO : new BigDecimal(value);
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (null != value) {
                result = result.add(new BigDecimal(value));
            }
        }
        return result;
    }

    /**
     * 提供精确的加法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被加值
     * @return 和
     * @since 4.0.0
     */
    public static BigDecimal add(BigDecimal... values) {
        if (ArraysHelper.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        BigDecimal value = values[0];
        BigDecimal result = null == value ? BigDecimal.ZERO : value;
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (null != value) {
                result = result.add(value);
            }
        }
        return result;
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static double sub(float v1, float v2) {
        return sub(Float.toString(v1), Float.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static double sub(float v1, double v2) {
        return sub(Float.toString(v1), Double.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static double sub(double v1, float v2) {
        return sub(Double.toString(v1), Float.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static double sub(double v1, double v2) {
        return sub(Double.toString(v1), Double.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static double sub(Double v1, Double v2) {
        //noinspection RedundantCast
        return sub((Number) v1, (Number) v2).doubleValue();
    }

    /**
     * 提供精确的减法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static BigDecimal sub(Number v1, Number v2) {
        return sub(new Number[]{v1, v2});
    }

    /**
     * 提供精确的减法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被减值
     * @return 差
     * @since 4.0.0
     */
    public static BigDecimal sub(Number... values) {
        if (ArraysHelper.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        Number value = values[0];
        BigDecimal result = null == value ? BigDecimal.ZERO : new BigDecimal(value.toString());
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (null != value) {
                result = result.subtract(new BigDecimal(value.toString()));
            }
        }
        return result;
    }

    /**
     * 提供精确的减法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被减值
     * @return 差
     * @since 4.0.0
     */
    public static BigDecimal sub(String... values) {
        if (ArraysHelper.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        String value = values[0];
        BigDecimal result = null == value ? BigDecimal.ZERO : new BigDecimal(value);
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (null != value) {
                result = result.subtract(new BigDecimal(value));
            }
        }
        return result;
    }

    /**
     * 提供精确的减法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被减值
     * @return 差
     * @since 4.0.0
     */
    public static BigDecimal sub(BigDecimal... values) {
        if (ArraysHelper.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        BigDecimal value = values[0];
        BigDecimal result = null == value ? BigDecimal.ZERO : value;
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (null != value) {
                result = result.subtract(value);
            }
        }
        return result;
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     */
    public static double mul(float v1, float v2) {
        return mul(Float.toString(v1), Float.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     */
    public static double mul(float v1, double v2) {
        return mul(Float.toString(v1), Double.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     */
    public static double mul(double v1, float v2) {
        return mul(Double.toString(v1), Float.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     */
    public static double mul(double v1, double v2) {
        return mul(Double.toString(v1), Double.toString(v2)).doubleValue();
    }

    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     */
    public static double mul(Double v1, Double v2) {
        //noinspection RedundantCast
        return mul((Number) v1, (Number) v2).doubleValue();
    }

    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     */
    public static BigDecimal mul(Number v1, Number v2) {
        return mul(new Number[]{v1, v2});
    }

    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被乘值
     * @return 积
     * @since 4.0.0
     */
    public static BigDecimal mul(Number... values) {
        if (ArraysHelper.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        Number value = values[0];
        BigDecimal result = new BigDecimal(value.toString());
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            result = result.multiply(new BigDecimal(value.toString()));
        }
        return result;
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     * @since 3.0.8
     */
    public static BigDecimal mul(String v1, String v2) {
        return mul(new BigDecimal(v1), new BigDecimal(v2));
    }

    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被乘值
     * @return 积
     * @since 4.0.0
     */
    public static BigDecimal mul(String... values) {
        if (ArraysHelper.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        BigDecimal result = new BigDecimal(values[0]);
        for (int i = 1; i < values.length; i++) {
            result = result.multiply(new BigDecimal(values[i]));
        }

        return result;
    }

    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被乘值
     * @return 积
     * @since 4.0.0
     */
    public static BigDecimal mul(BigDecimal... values) {
        if (ArraysHelper.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        BigDecimal result = values[0];
        for (int i = 1; i < values.length; i++) {
            result = result.multiply(values[i]);
        }
        return result;
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(float v1, float v2) {
        return div(v1, v2, DEFAUT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(float v1, double v2) {
        return div(v1, v2, DEFAUT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, float v2) {
        return div(v1, v2, DEFAUT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEFAUT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(Double v1, Double v2) {
        return div(v1, v2, DEFAUT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     * @since 3.1.0
     */
    public static BigDecimal div(Number v1, Number v2) {
        return div(v1, v2, DEFAUT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal div(String v1, String v2) {
        return div(v1, v2, DEFAUT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(float v1, float v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(float v1, double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(double v1, float v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(Double v1, Double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     * @since 3.1.0
     */
    public static BigDecimal div(Number v1, Number v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static BigDecimal div(String v1, String v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(float v1, float v2, int scale, RoundingMode roundingMode) {
        return div(Float.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(float v1, double v2, int scale, RoundingMode roundingMode) {
        return div(Float.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(double v1, float v2, int scale, RoundingMode roundingMode) {
        return div(Double.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale, RoundingMode roundingMode) {
        return div(Double.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(Double v1, Double v2, int scale, RoundingMode roundingMode) {
        //noinspection RedundantCast
        return div((Number) v1, (Number) v2, scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     * @since 3.1.0
     */
    public static BigDecimal div(Number v1, Number v2, int scale, RoundingMode roundingMode) {
        return div(v1.toString(), v2.toString(), scale, roundingMode);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static BigDecimal div(String v1, String v2, int scale, RoundingMode roundingMode) {
        return div(new BigDecimal(v1), new BigDecimal(v2), scale, roundingMode);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     * @since 3.0.9
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        Assert.notNull(v2, "Divisor must be not null !");
        if (null == v1) {
            return BigDecimal.ZERO;
        }
        if (scale < 0) {
            scale = -scale;
        }
        return v1.divide(v2, scale, roundingMode);
    }

    /**
     * 补充Math.ceilDiv() JDK8中添加了和Math.floorDiv()但却没有ceilDiv()
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     * @since 5.3.3
     */
    public static int ceilDiv(int v1, int v2) {
        return (int) Math.ceil((double) v1 / v2);
    }

    /**
     * <pre>
     *   NumberHelper.toInt(null) = 0
     *   NumberHelper.toInt("")   = 0
     *   NumberHelper.toInt("1")  = 1
     * </pre>
     *
     * @param str
     * @return
     */
    public static int toInt(final String str) {
        return toInt(str, 0);
    }


    /**
     * <pre>
     *   NumberHelper.toInt(null, 1) = 1
     *   NumberHelper.toInt("", 1)   = 1
     *   NumberHelper.toInt("1", 0)  = 1
     * </pre>
     *
     * @param source       原始数据
     * @param defaultValue 默认值
     */
    public static int toInt(final String source, final int defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(source);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *   NumberHelper.toInt(null) = 0
     *   NumberHelper.toInt("")   = 0
     *   NumberHelper.toInt("1")  = 1
     * </pre>
     *
     * @param str
     * @return
     */
    public static int toInteger(final String str) {
        return toInteger(str, null);
    }

    /**
     * <pre>
     *   NumberHelper.Integer(null, 1) = 1
     *   NumberHelper.Integer("", 1)   = 1
     *   NumberHelper.Integer("1", 0)  = 1
     * </pre>
     *
     * @param source       原始数据
     * @param defaultValue 默认值
     */
    public static Integer toInteger(final String source, final Integer defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(source);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *   NumberHelper.toLong(null) = 0L
     *   NumberHelper.toLong("")   = 0L
     *   NumberHelper.toLong("1")  = 1L
     * </pre>
     *
     * @param str 原始数据
     * @since 2.1
     */
    public static long toLong(final String str) {
        return toLong(str, 0L);
    }

    /**
     * <pre>
     *   NumberHelper.toLong(null, 1L) = 1L
     *   NumberHelper.toLong("", 1L)   = 1L
     *   NumberHelper.toLong("1", 0L)  = 1L
     * </pre>
     *
     * @param str          原始数据
     * @param defaultValue 默认值
     * @return the long represented by the string, or the default if conversion fails
     * @since 2.1
     */
    public static long toLong(final String str, final long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *   NumberHelper.toLong(null, 1L) = 1L
     *   NumberHelper.toLong("", 1L)   = 1L
     *   NumberHelper.toLong("1", 0L)  = 1L
     * </pre>
     *
     * @param str          原始数据
     * @param defaultValue 默认值
     * @return the long represented by the string, or the default if conversion fails
     * @since 2.1
     */
    public static Long toLong(final String str, final Long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 字符串转化为Number
     *
     * @param text
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
        Assert.notNull(text, "Text must not be null");
        Assert.notNull(targetClass, "Target class must not be null");
        String trimmed = StringHelper.trimAllWhitespace(text);

        if (Byte.class == targetClass) {
            return (T) (BooleanHelper.isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed));
        } else if (Short.class == targetClass) {
            return (T) (BooleanHelper.isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
        } else if (Integer.class == targetClass) {
            return (T) (BooleanHelper.isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
        } else if (Long.class == targetClass) {
            return (T) (BooleanHelper.isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
        } else if (BigInteger.class == targetClass) {
            return (T) (BooleanHelper.isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed));
        } else if (Float.class == targetClass) {
            return (T) Float.valueOf(trimmed);
        } else if (Double.class == targetClass) {
            return (T) Double.valueOf(trimmed);
        } else if (BigDecimal.class == targetClass || Number.class == targetClass) {
            return (T) new BigDecimal(trimmed);
        } else {
            throw new IllegalArgumentException(
                    "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
        }
    }

    /**
     * 解析bigint
     *
     * @param value
     * @return
     */
    private static BigInteger decodeBigInteger(String value) {
        int radix = 10;
        int index = 0;
        boolean negative = false;

        if (value.startsWith(SYMBOL_MINS)) {
            negative = true;
            index++;
        }

        if (value.startsWith(StringConstant.HEX_16, index) || value.startsWith(HEX_16_UPPER, index)) {
            index += 2;
            radix = 16;
        } else if (value.startsWith(SYMBOL_WELL, index)) {
            index++;
            radix = 16;
        } else if (value.startsWith("0", index) && value.length() > 1 + index) {
            index++;
            radix = 8;
        }

        BigInteger result = new BigInteger(value.substring(index), radix);
        return (negative ? result.negate() : result);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>float</code>, returning
     * <code>0.0f</code> if the conversion fails.</p>
     *
     * <p>If the string <code>str</code> is <code>null</code>,
     * <code>0.0f</code> is returned.</p>
     *
     * <pre>
     *   NumberHelper.toFloat(null)   = 0.0f
     *   NumberHelper.toFloat("")     = 0.0f
     *   NumberHelper.toFloat("1.5")  = 1.5f
     * </pre>
     *
     * @param str the string to convert, may be <code>null</code>
     * @return the float represented by the string, or <code>0.0f</code>
     * if conversion fails
     * @since 2.1
     */
    public static float toFloat(final String str) {
        return toFloat(str, 0.0f);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>float</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string <code>str</code> is <code>null</code>, the default
     * value is returned.</p>
     *
     * <pre>
     *   NumberHelper.toFloat(null, 1.1f)   = 1.0f
     *   NumberHelper.toFloat("", 1.1f)     = 1.1f
     *   NumberHelper.toFloat("1.5", 0.0f)  = 1.5f
     * </pre>
     *
     * @param str          the string to convert, may be <code>null</code>
     * @param defaultValue the default value
     * @return the float represented by the string, or defaultValue
     * if conversion fails
     * @since 2.1
     */
    public static float toFloat(final String str, final float defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>double</code>, returning
     * <code>0.0d</code> if the conversion fails.</p>
     *
     * <p>If the string <code>str</code> is <code>null</code>,
     * <code>0.0d</code> is returned.</p>
     *
     * <pre>
     *   NumberHelper.toDouble(null)   = 0.0d
     *   NumberHelper.toDouble("")     = 0.0d
     *   NumberHelper.toDouble("1.5")  = 1.5d
     * </pre>
     *
     * @param str the string to convert, may be <code>null</code>
     * @return the double represented by the string, or <code>0.0d</code>
     * if conversion fails
     * @since 2.1
     */
    public static double toDouble(final String str) {
        return toDouble(str, 0.0d);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>double</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string <code>str</code> is <code>null</code>, the default
     * value is returned.</p>
     *
     * <pre>
     *   NumberHelper.toDouble(null, 1.1d)   = 1.1d
     *   NumberHelper.toDouble("", 1.1d)     = 1.1d
     *   NumberHelper.toDouble("1.5", 0.0d)  = 1.5d
     * </pre>
     *
     * @param str          the string to convert, may be <code>null</code>
     * @param defaultValue the default value
     * @return the double represented by the string, or defaultValue
     * if conversion fails
     * @since 2.1
     */
    public static double toDouble(final String str, final double defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>BigDecimal</code> to a <code>double</code>.</p>
     *
     * <p>If the <code>BigDecimal</code> <code>value</code> is
     * <code>null</code>, then the specified default value is returned.</p>
     *
     * <pre>
     *   NumberHelper.toDouble(null)                     = 0.0d
     *   NumberHelper.toDouble(BigDecimal.valudOf(8.5d)) = 8.5d
     * </pre>
     *
     * @param value the <code>BigDecimal</code> to convert, may be <code>null</code>.
     * @return the double represented by the <code>BigDecimal</code> or
     * <code>0.0d</code> if the <code>BigDecimal</code> is <code>null</code>.
     * @since 3.8
     */
    public static double toDouble(final BigDecimal value) {
        return toDouble(value, 0.0d);
    }

    /**
     * <pre>
     *   NumberHelper.toDouble(null, 1.1d)                     = 1.1d
     *   NumberHelper.toDouble(BigDecimal.valudOf(8.5d), 1.1d) = 8.5d
     * </pre>
     *
     * @param value        the <code>BigDecimal</code> to convert, may be <code>null</code>.
     * @param defaultValue the default value
     * @return the double represented by the <code>BigDecimal</code> or the
     * defaultValue if the <code>BigDecimal</code> is <code>null</code>.
     * @since 3.8
     */
    public static double toDouble(final BigDecimal value, final double defaultValue) {
        return value == null ? defaultValue : value.doubleValue();
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Convert a <code>String</code> to a <code>byte</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     *
     * <pre>
     *   NumberHelper.toByte(null) = 0
     *   NumberHelper.toByte("")   = 0
     *   NumberHelper.toByte("1")  = 1
     * </pre>
     *
     * @param str 原始数据
     * @return the byte represented by the string, or <code>zero</code> if
     * conversion fails
     * @since 2.5
     */
    public static byte toByte(final String str) {
        return toByte(str, (byte) 0);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>byte</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   NumberHelper.toByte(null, 1) = 1
     *   NumberHelper.toByte("", 1)   = 1
     *   NumberHelper.toByte("1", 0)  = 1
     * </pre>
     *
     * @param str          原始数据
     * @param defaultValue 默认值
     * @return the byte represented by the string, or the default if conversion fails
     * @since 2.5
     */
    public static byte toByte(final String str, final byte defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>short</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     *
     * <pre>
     *   NumberHelper.toShort(null) = 0
     *   NumberHelper.toShort("")   = 0
     *   NumberHelper.toShort("1")  = 1
     * </pre>
     *
     * @param str 原始数据
     * @return the short represented by the string, or <code>zero</code> if
     * conversion fails
     * @since 2.5
     */
    public static short toShort(final String str) {
        return toShort(str, (short) 0);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>short</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   NumberHelper.toShort(null, 1) = 1
     *   NumberHelper.toShort("", 1)   = 1
     *   NumberHelper.toShort("1", 0)  = 1
     * </pre>
     *
     * @param str          原始数据
     * @param defaultValue 默认值
     * @return the short represented by the string, or the default if conversion fails
     * @since 2.5
     */
    public static short toShort(final String str, final short defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Short.parseShort(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * @param value 原始数据
     * @since 3.8
     */
    public static BigDecimal toScaledBigDecimal(final BigDecimal value) {
        return toScaledBigDecimal(value, INTEGER_TWO, RoundingMode.HALF_EVEN);
    }

    /**
     * @param value        原始数据
     * @param scale        数据缩放
     * @param roundingMode
     * @since 3.8
     */
    public static BigDecimal toScaledBigDecimal(final BigDecimal value, final int scale, final RoundingMode roundingMode) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(
                scale,
                (roundingMode == null) ? RoundingMode.HALF_EVEN : roundingMode
        );
    }

    /**
     * @param value 原始数据
     * @since 3.8
     */
    public static BigDecimal toScaledBigDecimal(final Float value) {
        return toScaledBigDecimal(value, INTEGER_TWO, RoundingMode.HALF_EVEN);
    }

    /**
     * @param value        原始数据
     * @param scale        数据缩放
     * @param roundingMode
     * @since 3.8
     */
    public static BigDecimal toScaledBigDecimal(final Float value, final int scale, final RoundingMode roundingMode) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return toScaledBigDecimal(
                BigDecimal.valueOf(value),
                scale,
                roundingMode
        );
    }

    /**
     * @param value 原始数据
     * @since 3.8
     */
    public static BigDecimal toScaledBigDecimal(final Double value) {
        return toScaledBigDecimal(value, INTEGER_TWO, RoundingMode.HALF_EVEN);
    }

    /**
     * @param value        原始数据
     * @param scale        数据缩放
     * @param roundingMode
     * @since 3.8
     */
    public static BigDecimal toScaledBigDecimal(final Double value, final int scale, final RoundingMode roundingMode) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return toScaledBigDecimal(
                BigDecimal.valueOf(value),
                scale,
                roundingMode
        );
    }

    /**
     * @param value 原始数据
     * @since 3.8
     */
    public static BigDecimal toScaledBigDecimal(final String value) {
        return toScaledBigDecimal(value, INTEGER_TWO, RoundingMode.HALF_EVEN);
    }

    /**
     * @param value        原始数据
     * @param scale        数据缩放
     * @param roundingMode
     * @since 3.8
     */
    public static BigDecimal toScaledBigDecimal(final String value, final int scale, final RoundingMode roundingMode) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return toScaledBigDecimal(
                createBigDecimal(value),
                scale,
                roundingMode
        );
    }

    /**
     * @param source 原始数据
     * @throws NumberFormatException
     */
    public static BigDecimal createBigDecimal(final String source) {
        if (source == null) {
            return null;
        }
        if (Strings.isNullOrEmpty(source)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        if (source.trim().startsWith("--")) {
            throw new NumberFormatException(source + " is not a valid number.");
        }
        return new BigDecimal(source);
    }

    /**
     * is integer string.
     *
     * @param str
     * @return is integer
     */
    public static boolean isInteger(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return INT_PATTERN.matcher(str).matches();
    }

    /**
     * 是否是数字
     *
     * @param str 原始数据
     * @return
     */
    public static boolean isNumber(final String str) {
        if (Strings.isNullOrEmpty(str)) {
            return false;
        }

        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 随机整数
     * <p>
     * NumberHelper.randomInt(1) = (0, 1)
     * NumberHelper.randomInt(100) = (0, 100)
     * </p>
     *
     * @param max 最大数据
     * @return
     */
    public static double randomDouble(final double max) {
        if (max < 0D) {
            return 0D;
        }

        return Math.random() * max;
    }

    /**
     * 随机整数
     * <p>
     * NumberHelper.randomInt(1) = 0
     * NumberHelper.randomInt(100) = (0, 100)
     * </p>
     *
     * @param max 最大数据
     * @return
     */
    public static int randomInt(final int max) {
        if (max < 1) {
            return 0;
        }

        Double maxDouble = randomDouble(max);
        int maxInt = maxDouble.intValue();
        return maxInt;
    }

    /**
     * 栅格
     *
     * @param max 总大小
     * @param min 每块大小
     * @return
     */
    public static int fences(long max, long min) {
        final Long before = max / min;
        final Long after = max % min == 0L ? 0L : 1L;
        final Long sum = (before + after);
        return sum.intValue();
    }

    /**
     * 计算比率。计算结果四舍五入。
     *
     * @param numerator   分子
     * @param denominator 分母
     * @param scale       保留小数点后位数
     * @return 比率
     */
    public static double divide(long numerator, long denominator, int scale) {
        BigDecimal numeratorBd = new BigDecimal(numerator);
        BigDecimal denominatorBd = new BigDecimal(denominator);
        return numeratorBd.divide(denominatorBd, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算比率。计算结果四舍五入。保留小数点后两位。
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return 比率
     */
    public static double divide(long numerator, long denominator) {
        return divide(numerator, denominator, 2);
    }

    /**
     * 计算比率。计算结果四舍五入。
     *
     * @param numerator   分子
     * @param denominator 分母
     * @param scale       保留小数点后位数
     * @return 比率
     */
    public static double divide(double numerator, double denominator, int scale) {
        BigDecimal numeratorBd = BigDecimal.valueOf(numerator);
        BigDecimal denominatorBd = BigDecimal.valueOf(denominator);
        return numeratorBd.divide(denominatorBd, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算比率。计算结果四舍五入。保留小数点后两位。
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return 比率
     */
    public static double divide(double numerator, double denominator) {
        return divide(numerator, denominator, 2);
    }

    /**
     * 减法。计算结果四舍五入。
     *
     * @param minuend   被减数
     * @param reduction 减数
     * @param scale     计算结果保留位数。(注意包括整数部分)
     * @return 计算结果
     */
    public static double subtract(double minuend, double reduction, int scale) {
        BigDecimal minuendBd = BigDecimal.valueOf(minuend);
        BigDecimal reductionBd = BigDecimal.valueOf(reduction);
        MathContext mathContext = new MathContext(scale, RoundingMode.HALF_UP);
        return minuendBd.subtract(reductionBd, mathContext).doubleValue();
    }

    /**
     * 减法。
     *
     * @param minuend   被减数
     * @param reduction 减数
     * @return 计算结果
     */
    public static double subtract(double minuend, double reduction) {
        BigDecimal minuendBd = BigDecimal.valueOf(minuend);
        BigDecimal reductionBd = BigDecimal.valueOf(reduction);
        return minuendBd.subtract(reductionBd).doubleValue();
    }

    /**
     * 将int整数与小数相乘，计算结四舍五入保留整数位。
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 数字相乘计算结果
     */
    public static int multiply(int num1, double num2) {
        double num1D = num1;
        return multiply(num1D, num2);
    }

    /**
     * 将long整数与小数相乘，计算结四舍五入保留整数位。
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 数字相乘计算结果
     */
    public static int multiply(long num1, double num2) {
        double num1D = ((Long) num1).doubleValue();
        return multiply(num1D, num2);
    }

    /**
     * 将double与小数相乘，计算结四舍五入保留整数位。
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 数字相乘计算结果
     */
    public static int multiply(double num1, double num2) {
        BigDecimal num1Bd = BigDecimal.valueOf(num1);
        BigDecimal num2Bd = BigDecimal.valueOf(num2);
        MathContext mathContext = new MathContext(num1Bd.precision(), RoundingMode.HALF_UP);
        return num1Bd.multiply(num2Bd, mathContext).intValue();
    }

    /**
     * 数字转{@link BigDecimal}
     *
     * @param number 数字
     * @return {@link BigDecimal}
     * @since 4.0.9
     */
    public static BigDecimal toBigDecimal(Number number) {
        if (null == number) {
            return BigDecimal.ZERO;
        }

        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        } else if (number instanceof Long) {
            return new BigDecimal((Long) number);
        } else if (number instanceof Integer) {
            return new BigDecimal((Integer) number);
        } else if (number instanceof BigInteger) {
            return new BigDecimal((BigInteger) number);
        }

        return toBigDecimal(number.toString());
    }

    /**
     * 数字转{@link BigDecimal}
     *
     * @param number 数字
     * @return {@link BigDecimal}
     * @since 4.0.9
     */
    public static BigDecimal toBigDecimal(String number) {
        return (null == number) ? BigDecimal.ZERO : new BigDecimal(number);
    }


    /**
     * 数字转{@link BigInteger}
     *
     * @param number 数字
     * @return {@link BigInteger}
     * @since 5.4.5
     */
    public static BigInteger toBigInteger(Number number) {
        if (null == number) {
            return BigInteger.ZERO;
        }

        if (number instanceof BigInteger) {
            return (BigInteger) number;
        } else if (number instanceof Long) {
            return BigInteger.valueOf((Long) number);
        }

        return toBigInteger(number.longValue());
    }

    /**
     * 数字转{@link BigInteger}
     *
     * @param number 数字
     * @return {@link BigInteger}
     * @since 5.4.5
     */
    public static BigInteger toBigInteger(String number) {
        return (null == number) ? BigInteger.ZERO : new BigInteger(number);
    }
}
