package com.chua.utils.tools.time;

/**
 * 计算耗时，单位纳秒<br>
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class NanosecondCost implements Cost {

    private final String name;

    private final long nanosecond;

    public NanosecondCost() {
        super();
        this.name = "";
        this.nanosecond = System.nanoTime();
    }

    public NanosecondCost(String name) {
        super();
        this.name = name;
        this.nanosecond = System.nanoTime();
    }

    @Override
    public long stop() {
        return System.nanoTime() - nanosecond;
    }

    @Override
    public void stopAndPrint() {
        System.out.println(stopAndFormat());
    }

    @Override
    public String stopAndFormat() {
        return (this.name + " cost: " + stop() + " ns");
    }

    @Override
    public String stopAccurate() {
        return String.valueOf(stop());
    }

    @Override
    public void stopAccurateAndPrint() {
        System.out.println(stopAccurateAndFormat());
    }

    @Override
    public String stopAccurateAndFormat() {
        return stopAndFormat();
    }
}
