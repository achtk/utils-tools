package com.chua.utils.tools.bean.part;

import com.chua.utils.tools.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * bean part
 * <p>bean各个部分数据</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/8
 */
public class StandardBeanPart<T> implements BeanPart<T> {
    /**
     * 对象
     */
    private final T object;

    public StandardBeanPart(T object) {
        this.object = object;
    }

    @Override
    public Class<T> toClass() {
        return null == object ? null : (Class<T>) ClassUtils.getClass(object);
    }

    @Override
    public Object getFieldValue(Field field) {
        if (null == object) {
            return null;
        }

        return ClassUtils.getFieldValue(object, field);
    }

    @Override
    public void setFieldValue(Field field, Object value) {
        if (null == object) {
            return;
        }
        ClassUtils.doWithSetFieldValue(field, value, object);
    }

    @Override
    public Object invoke(Method method, Object... params) {
        if (null == object || null == method) {
            return null;
        }

        return ClassUtils.getMethodValue(object, method, params);
    }

}
