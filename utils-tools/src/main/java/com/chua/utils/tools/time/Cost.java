package com.chua.utils.tools.time;

/**
 * 计算耗时<br>
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public interface Cost {

    /**
     * 毫秒
     *
     * @return
     */
    static Cost mill() {
        return new MillisecondCost();
    }

    /**
     * 毫秒
     *
     * @return
     */
    static Cost nano() {
        return new NanosecondCost();
    }

    /**
     * 停止计时，返回计时结果
     *
     * @return 返回计时结果
     */
    long stop();

    /**
     * 停止计时，并打印计时结果
     * <p>例如:  name cost: 1000 ms, 使用System.out.println
     */
    void stopAndPrint();

    /**
     * 停止计时，并返回组装计时结果
     * <p>例如:  name cost: 1000 ms
     *
     * @return 返回组装计时结果
     */
    String stopAndFormat();

    /**
     * 停止计时，返回精确计时，带3小数的结果，使用ROUND_DOWN 舍弃超过3位的小数部分。
     * <p>例如：1000.500
     *
     * @return 返回带小数的结果
     */
    String stopAccurate();

    /**
     * 停止计时，并打印精确计时结果
     * <p>例如:  name cost: 1000.500 ms, 使用System.out.println
     */
    void stopAccurateAndPrint();

    /**
     * 停止计时，并返回组装精确计时结果
     * <p>例如:  name cost: 1000.500 ms
     *
     * @return 返回组装精确计时结果
     */
    String stopAccurateAndFormat();
}
