package com.chua.utils.tools.classes.finder;

import com.chua.utils.tools.function.Filter;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Delegate;

import java.util.HashSet;
import java.util.Set;

/**
 * 查找器
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
@Data
public class FinderProperties {

    private Object obj;
    private Class<?> aClass;

    /**
     * 静态
     */
    private boolean isStatic;
    /**
     * 过滤器
     */
    private Filter filter;
}
