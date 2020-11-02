package com.chua.utils.tools.transform;

import com.chua.utils.tools.properties.OperatorProperties;
import lombok.AllArgsConstructor;

/**
 * 操作转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public interface OperatorTransform<T> {

    /**
     * 转化
     *
     * @param operatorProperties 操作属性
     * @return T
     */
    T transform(OperatorProperties operatorProperties);
}
