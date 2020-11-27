package com.chua.utils.tools.bean.copy;

import com.chua.utils.tools.classes.ClassHelper;

/**
 * 标准的bean拷贝
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class ClassBeanCopy<T> extends StandardBeanCopy<T> {

    public ClassBeanCopy(String className) {
        super((Class<T>) ClassHelper.forName(className));
    }

}
