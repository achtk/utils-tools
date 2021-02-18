package com.chua.utils.tools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Getter and Setter数据分析对象
 *
 * @author CH
 * @date 2020-09-26
 */
@NoArgsConstructor(staticName = "of")
public class GetterSetterProperties {

    private static final ConcurrentHashMap<String, GetterSetterStatus> CACHE = new ConcurrentHashMap<>();
    private static final LongAdder LONG_ADDER = new LongAdder();
    private Class<?> aClass;

    /**
     * 创建状态
     *
     * @return
     */
    public static GetterSetterStatus newStatus() {
        return GetterSetterStatus.newStatus(true, true);
    }

    public boolean isEmpty() {
        return LONG_ADDER.intValue() == 0;
    }

    /**
     * 存储状态
     *
     * @param name      属性名称
     * @param hasGetter 是否包含get
     * @param hasSetter 是否包含set
     * @return
     */
    public GetterSetterProperties put(String name, boolean hasGetter, boolean hasSetter) {
        if (!hasGetter || !hasSetter) {
            LONG_ADDER.increment();
        }
        CACHE.put(name, GetterSetterStatus.newStatus(hasGetter, hasSetter));
        return this;
    }

    /**
     * 存储状态
     *
     * @param name   属性名称
     * @param status 状态
     * @return
     */
    public GetterSetterProperties put(String name, GetterSetterStatus status) {
        return put(name, status.isGetter(), status.isSetter());
    }

    /**
     * 记录一个类
     *
     * @param aClass
     */
    public void record(Class<?> aClass) {
        this.aClass = aClass;
    }

    /**
     * 获取记录的类
     *
     * @return
     */
    public Class<?> getRecordClass() {
        return this.aClass;
    }

    @Data
    @NoArgsConstructor(staticName = "newStatus")
    @AllArgsConstructor(staticName = "newStatus")
    @EqualsAndHashCode
    public static class GetterSetterStatus {
        /**
         * 是否包含get
         */
        private boolean getter;
        /**
         * 是否包含set
         */
        private boolean setter;
    }
}
