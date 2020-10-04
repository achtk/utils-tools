package com.chua.utils.tools.spring.finder;

import java.util.Set;

/**
 * 对象查找器
 * @author CH
 */
public interface IBeanFinder {
    /**
     * 查找bean
     * @param tClass 类
     * @return
     */
    Set<Class> findBean(Class<?> tClass);

}
