package com.chua.utils.tools.spring.factorybean;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * 扩展factoryBean
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
@Data
public abstract class EnableInnerClassInvokeFactoryBean<T> implements FactoryBean<T> {

    protected Class<Object> type;
}
