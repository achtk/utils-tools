package com.chua.utils.tools.common;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.regex.Pattern;

/**
 * 数字处理
 *
 * @author Administrator
 */
public class NumberHelper {
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

        if (value.startsWith("-")) {
            negative = true;
            index++;
        }

        if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        } else if (value.startsWith("#", index)) {
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
        if (StringHelper.isBlank(source)) {
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
        if (StringHelper.isBlank(str)) {
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
     *     NumberHelper.randomInt(1) = (0, 1)
     *     NumberHelper.randomInt(100) = (0, 100)
     * </p>
     * @param max 最大数据
     * @return
     */
    public static double randomDouble(final double max) {
        if(max < 0D) {
            return 0D;
        }

        return Math.random() * max;
    }
    /**
     * 随机整数
     * <p>
     *     NumberHelper.randomInt(1) = 0
     *     NumberHelper.randomInt(100) = (0, 100)
     * </p>
     * @param max 最大数据
     * @return
     */
    public static int randomInt(final int max) {
        if(max < 1) {
            return 0;
        }

        Double maxDouble = randomDouble(max);
        int maxInt = maxDouble.intValue();
        return maxInt;
    }

    /**
     * 栅格
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
        BigDecimal reductionBd =  BigDecimal.valueOf(reduction);
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
        BigDecimal num1Bd =  BigDecimal.valueOf(num1);
        BigDecimal num2Bd =  BigDecimal.valueOf(num2);
        MathContext mathContext = new MathContext(num1Bd.precision(), RoundingMode.HALF_UP);
        return num1Bd.multiply(num2Bd, mathContext).intValue();
    }

}
