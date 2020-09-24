package com.chua.utils.tools.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;

/**
 * mapping对象
 * @author CH
 */
@Getter
@Setter
public class MappingEntity {
    /**
     * 文件路径
     */
    @Accessors(fluent = true)
    private String[] paths;
    /**
     * 方法
     */
    @Accessors(fluent = true)
    private RequestMethod[] methods = new RequestMethod[] {RequestMethod.GET, RequestMethod.POST};

    @Accessors(fluent = true)
    private String[] produces;

    @Accessors(fluent = true)
    private String[] consumer;
    /**
     * 对象
     */
    private Object obj;
    /**
     * 方法
     */
    private Method method;
    /**
     * 上下文
     */
    private ApplicationContext applicationContext;

}
