package com.chua.utils.tools.judge;

import com.chua.utils.tools.common.CollectionHelper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 可判断
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/26
 */
@NoArgsConstructor
@RequiredArgsConstructor
public class Judgeable<T> {

    private static final Judgeable<?> EMPTY = new Judgeable<>();
    @NonNull
    private T value;

    public static <T> Judgeable<T> ofNullable(T value) {
        return value == null ? (Judgeable<T>) EMPTY : new Judgeable<T>(value);
    }

    /**
     * 获取数据
     *
     * @return
     */
    public T getIfPresent() {
        return value;
    }
    /**
     * 尝试获取List
     * @return
     */
    public List<T> getListIfFeasible() {
        return null == value ? Collections.emptyList() : (CollectionHelper.getListIfFeasible(value));
    }

    /**
     * 获取数据
     *
     * @param defaultValue 默认值
     * @return
     */
    public T getIfPresent(T defaultValue) {
        return null == value ? defaultValue : value;
    }
}
