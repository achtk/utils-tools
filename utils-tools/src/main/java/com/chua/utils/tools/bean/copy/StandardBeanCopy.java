package com.chua.utils.tools.bean.copy;

import com.chua.utils.tools.bean.config.BeanConfig;
import com.chua.utils.tools.bean.interpreter.NameInterpreter;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.function.Converter;
import com.chua.utils.tools.manager.parser.description.FieldDescription;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 标准的bean拷贝
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class StandardBeanCopy<T> implements BeanCopy<T> {
    /**
     * 基础配置
     */
    private BeanConfig beanConfig;
    /**
     * 类属性
     */
    private BeanMap beanMap;
    /**
     * 待处理类
     */
    private Class<T> tClass;
    /**
     * 待处理的对象
     */
    private T entity;
    /**
     * 待处理的数据缓存
     */
    private final Map<String, Object> withParams = new HashMap<>();

    private StandardBeanCopy() {
    }

    private StandardBeanCopy(Class<T> tClass) {
        this(ClassHelper.safeForObject(tClass), new BeanConfig());
    }

    private StandardBeanCopy(T entity) {
        this(entity, new BeanConfig());
    }

    @SuppressWarnings("all")
    private StandardBeanCopy(T entity, BeanConfig beanConfig) {
        this.tClass = (Class<T>) entity.getClass();
        this.entity = entity;
        this.beanConfig = beanConfig;
        this.beanMap = BeanMap.create(tClass);
    }

    /**
     * 初始化bean拷贝
     *
     * @param tClass 类
     * @param <T>    类型
     * @return this
     */
    public static <T> BeanCopy of(Class<T> tClass) {
        return new StandardBeanCopy<>(tClass);
    }

    /**
     * 初始化bean拷贝
     *
     * @param entity 对象
     * @param <T>    类型
     * @return this
     */
    public static <T> BeanCopy of(T entity) {
        return new StandardBeanCopy<>(entity);
    }

    @Override
    public BeanCopy with(String name, Object value) {
        name = transTo(name);
        beanMap.put(name, value);
        withParams.put(name, value);
        return this;
    }

    @Override
    public BeanCopy<T> with(Map<String, Object> param, String[] keys) {
        if (!MapOperableHelper.isEmpty(param)) {
            return this;
        }

        for (String key : keys) {
            if (!param.containsKey(key)) {
                continue;
            }
            with(key, param.get(key));
        }
        return this;
    }

    @Override
    public BeanCopy with(Map<String, Object> param) {
        if (!MapOperableHelper.isEmpty(param)) {
            return this;
        }
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            with(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public BeanCopy with(Object entity) {
        if (null == entity || entity instanceof Class) {
            return this;
        }
        return with(BeanMap.create(entity));
    }

    @Override
    public BeanCopy<T> with(Converter<String, Object> converter) {
        ClassHelper.doWithFields(tClass, field -> {
            Object convert = converter.convert(field.getName(), field.getType());
            with(field.getName(), convert);
        });
        return this;
    }

    @Override
    public T create() {
        if (MapOperableHelper.isEmpty(withParams)) {
            return entity;
        }
        ObjectCreate<T> objectCreate;
        if (isCglibable()) {
            objectCreate = new CglibObjectCreate<>();
        } else {
            objectCreate = new ReflectionObjectCreate<>();
        }
        return objectCreate.create();
    }

    @Override
    public HashOperateMap asMap() {
        HashOperateMap hashOperateMap = new HashOperateMap();
        BeanMap beanMap = null;
        try {
            beanMap = BeanMap.create(entity);
            hashOperateMap.putAll(beanMap);
        } catch (Exception e) {
            ClassHelper.doWithFields(tClass, field -> {
                hashOperateMap.put(field.getName(), ClassHelper.getFieldValue(entity, field));
            });
        }
        return hashOperateMap;
    }


    /**
     * 是否可以被cglib处理
     *
     * @return 能被cglib处理返回true
     */
    private boolean isCglibable() {
        Map.Entry<String, Object> entry = MapOperableHelper.getFirst(this.withParams);

        if (this.beanMap.containsKey(entry.getKey())) {
            this.beanMap.put(entry.getKey(), entry.getValue());
            return entry.getValue().equals(this.beanMap.get(entry.getKey()));
        }
        return false;
    }

    /**
     * 命名转化
     *
     * @param name 命名
     * @return 转化后的命名
     */
    private String transTo(String name) {
        Set<NameInterpreter> interpreters = beanConfig.getInterpreters();
        for (NameInterpreter interpreter : interpreters) {
            if (interpreter.isMatcher(name)) {
                return interpreter.resolve(name);
            }
        }
        return name;
    }

    /**
     * 实体创建
     *
     * @param <T> 类型
     */
    interface ObjectCreate<T> {

        /**
         * 创建实体
         *
         * @return 实体
         */
        T create();
    }

    /**
     * cglib对象
     *
     * @param <T> 类型
     */
    class CglibObjectCreate<T> implements ObjectCreate<T> {

        @Override
        public T create() {
            beanMap.putAll(withParams);
            return (T) beanMap.getBean();
        }
    }

    /**
     * 反射对象
     *
     * @param <T> 类型
     */
    class ReflectionObjectCreate<T> implements ObjectCreate<T> {


        @Override
        public T create() {
            ClassHelper.doWithFields(tClass, item -> {
                FieldDescription fieldDescription = new FieldDescription();
                fieldDescription.setEntity(entity);
                fieldDescription.setField(item);

                String name = fieldDescription.getName();

                if (!withParams.containsKey(name)) {
                    return;
                }

                fieldDescription.set(withParams.get(name));
            });
            return (T) entity;
        }
    }
}
