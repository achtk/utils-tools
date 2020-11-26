package com.chua.utils.tools.spring.converter;

import com.chua.utils.tools.function.converter.TypeConverter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * bean转化器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class BeanTypeConverter implements ApplicationContextAware, TypeConverter<Object> {

    @Setter
    private ApplicationContext applicationContext;

    @Override
    public Object convert(Object value) {
        if (null == value || null == applicationContext) {
            return null;
        }
        return applicationContext.getBean(value.getClass());
    }

    @Override
    public Class<Object> getType() {
        return Object.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
