package com.chua.utils.tools.spring.mapping;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.spring.entity.MappingEntity;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.chua.utils.tools.constant.BeanConstant.BEAN_APPLICATION_CONTEXT;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_AT;

/**
 * 处理 @RequestMapping工具
 * @author CH
 * @version 1.0.0
 * @since 2020/03/17 09:18
 */
@AllArgsConstructor
public class RequestMappingHandlerMappingFactory {

    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    /**
     * 注册单个mapping
     *
     * @param mappingEntity
     */
    public synchronized void registerMapping(final MappingEntity mappingEntity) {
        if (null == mappingEntity) {
            throw new IllegalStateException("The bean mappingEntity not exist!");
        }

        Method method = mappingEntity.getMethod();
        if (null == method) {
            throw new IllegalStateException("The method not exist!");
        }

        Object obj = mappingEntity.getObj();
        if (null == obj) {
            throw new IllegalStateException("The obj not exist!");
        }

        //处理@Autowired|@Resource
        invokeFields(obj, mappingEntity.getApplicationContext());

        if (null == requestMappingHandlerMapping) {
            throw new IllegalStateException("The bean requestMappingHandlerMapping not exist!");
        }
        requestMappingHandlerMapping.registerMapping(requestMappingInfo(mappingEntity), obj, method);
    }


    /**
     * 注册单个mapping
     *
     * @param mappingEntity
     */
    public synchronized void registerMappings(final MappingEntity mappingEntity) {
        if (null == mappingEntity) {
            throw new IllegalStateException("The bean mappingEntity not exist!");
        }

        Method method = mappingEntity.getMethod();
        if (null != method) {
            registerMapping(mappingEntity);
            return;
        }

        Object obj = mappingEntity.getObj();
        if (null == obj) {
            throw new IllegalStateException("The obj not exist!");
        }

        if (null == requestMappingHandlerMapping) {
            throw new IllegalStateException("The bean requestMappingHandlerMapping not exist!");
        }

        //注入属性
        invokeFields(obj, mappingEntity.getApplicationContext());

        Class<?> aClass = requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass();
        if (!BooleanHelper.hasLength(mappingEntity.paths()) || SYMBOL_AT.equals(FinderHelper.firstElement(mappingEntity.paths()))) {
            try {
                Method declaredMethods = aClass.getDeclaredMethod("detectHandlerMethods", Object.class);
                declaredMethods.setAccessible(true);
                declaredMethods.invoke(requestMappingHandlerMapping, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            renderMethods(mappingEntity, requestMappingHandlerMapping);
        }
    }


    /**
     * 注入属性
     *
     * @param obj
     * @param applicationContext
     */
    private void invokeFields(Object obj, ApplicationContext applicationContext) {
        if (null == applicationContext) {
            return;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        if (!BooleanHelper.hasLength(fields)) {
            return;
        }
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class) || field.isAnnotationPresent(Resource.class)) {
                invokeSpringFields(obj, field, applicationContext);
            } else {

            }
        }
    }

    /**
     * 注入spring bean
     *
     * @param obj                实体类
     * @param field              字段
     * @param applicationContext 上下文
     */
    private void invokeSpringFields(Object obj, Field field, ApplicationContext applicationContext) {
        if (field.isAnnotationPresent(Autowired.class)) {
            invokeSpringAutowiredFields(obj, field, applicationContext);
        } else if (field.isAnnotationPresent(Resource.class)) {
            invokeSpringResourceFields(obj, field, applicationContext);
        }
    }

    /**
     * 注入spring bean
     *
     * @param obj                实体类
     * @param field              字段
     * @param applicationContext 上下文
     */
    private void invokeSpringResourceFields(Object obj, Field field, ApplicationContext applicationContext) {
        Object bean = null;
        if (field.isAnnotationPresent(Resource.class)) {
            Resource annotation = field.getAnnotation(Resource.class);
            String name = annotation.name();
            if (!Strings.isNullOrEmpty(name)) {
                if (applicationContext.containsBean(name)) {
                    bean = applicationContext.getBean(name);
                    renderField(obj, field, bean);
                }
            } else {
                renderFieldNameAndType(obj, field, applicationContext);
            }
        } else {
            renderFieldNameAndType(obj, field, applicationContext);
        }
    }

    /**
     * 注入spring bean
     *
     * @param obj                实体类
     * @param field              字段
     * @param applicationContext 上下文
     */
    private void invokeSpringAutowiredFields(Object obj, Field field, ApplicationContext applicationContext) {
        Object bean = null;
        if (field.isAnnotationPresent(Qualifier.class)) {
            Qualifier annotation = field.getAnnotation(Qualifier.class);
            String value = annotation.value();
            if (!Strings.isNullOrEmpty(value)) {
                if (applicationContext.containsBean(value)) {
                    bean = applicationContext.getBean(value);
                    renderField(obj, field, bean);
                }
            } else {
                renderFieldNameAndType(obj, field, applicationContext);
            }
        } else {
            renderFieldNameAndType(obj, field, applicationContext);
        }
    }

    /**
     * @param object
     * @param field
     * @param applicationContext
     */
    private void renderFieldNameAndType(Object object, Field field, ApplicationContext applicationContext) {
        Object bean = null;
        String name = field.getName();
        if (applicationContext.containsBean(name)) {
            bean = applicationContext.getBean(name);
            renderField(object, field, bean);
        } else {
            Class<?> type = field.getType();
            try {
                bean = applicationContext.getBean(type);
            } catch (Throwable e) {
                bean = throwableBean(type, applicationContext);
            }
            if (null != bean) {
                renderField(object, field, bean);
            }
        }
    }

    /**
     *
     * @return
     * @param type
     * @param applicationContext
     */
    private Object throwableBean(Class<?> type, ApplicationContext applicationContext) {
        if(type.getName().indexOf(BEAN_APPLICATION_CONTEXT) > -1) {
            return applicationContext;
        }
        return null;
    }
    /**
     * 渲染字段
     *
     * @param obj    实体类
     * @param field  字段
     * @param bean bean
     */
    private void renderField(Object obj, Field field, Object bean) {
        field.setAccessible(true);
        try {
            field.set(obj, bean);
        } catch (IllegalAccessException e) {
            try {
                field.set(null, bean);
            } catch (IllegalAccessException ex) {}
        }
    }

    /**
     * 渲染每一个方法
     *
     * @param mappingEntity
     * @param requestMappingHandlerMapping
     */
    private void renderMethods(final MappingEntity mappingEntity, final RequestMappingHandlerMapping requestMappingHandlerMapping) {
        String[] paths = mappingEntity.paths();
        String path = StringHelper.getStringOrDefault(FinderHelper.firstElement(paths));
        Object obj = mappingEntity.getObj();

        Method[] declaredMethods = obj.getClass().getDeclaredMethods();
        if (!BooleanHelper.hasLength(declaredMethods)) {
            return;
        }
        for (Method method : declaredMethods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            String[] value = requestMapping.value();
            if (!BooleanHelper.hasLength(value)) {
                continue;
            }

            String[] newValues = new String[value.length];
            int cnt = 0;
            for (String s : value) {
                newValues[cnt++] = path + s;
            }

            RequestMappingInfo.Builder builder =
                    RequestMappingInfo.paths(newValues).methods(requestMapping.method());

            if (!StringUtils.isEmpty(requestMapping.produces())) {
                builder.produces(requestMapping.produces());
            }
            if (!StringUtils.isEmpty(requestMapping.consumes())) {
                builder.consumes(requestMapping.consumes());
            }
            if (!StringUtils.isEmpty(requestMapping.params())) {
                builder.params(requestMapping.params());
            }
            if (!StringUtils.isEmpty(requestMapping.headers())) {
                builder.headers(requestMapping.headers());
            }
            RequestMappingInfo requestMappingInfo = builder.build();

            try {
                requestMappingHandlerMapping.registerMapping(requestMappingInfo, obj, method);
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * 注册单个mapping
     *
     * @param requestMappingHandlerMapping
     * @param mappingEntity
     */
    public void unregisterMapping(final MappingEntity mappingEntity, final RequestMappingHandlerMapping requestMappingHandlerMapping) {
        if (null == mappingEntity) {
            throw new IllegalStateException("The bean mappingEntity not exist!");
        }

        if (null == requestMappingHandlerMapping) {
            throw new IllegalStateException("The bean requestMappingHandlerMapping not exist!");
        }
        requestMappingHandlerMapping.unregisterMapping(requestMappingInfo(mappingEntity));
    }

    /**
     * 构建Mapping
     *
     * @param mappingEntity
     * @return
     */
    private RequestMappingInfo requestMappingInfo(final MappingEntity mappingEntity) {
        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(mappingEntity.paths()).methods(mappingEntity.methods());
        if (!StringUtils.isEmpty(mappingEntity.produces())) {
            builder.produces(mappingEntity.produces());
        }
        if (!StringUtils.isEmpty(mappingEntity.consumer())) {
            builder.consumes(mappingEntity.consumer());
        }

        return builder.build();
    }
}
