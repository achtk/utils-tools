package com.chua.utils.tools.bean.copy;

import com.chua.utils.tools.bean.config.BeanConfig;
import com.chua.utils.tools.bean.interpreter.NameInterpreter;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.callback.FieldCallback;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.BeansHelper;
import com.chua.utils.tools.function.converter.TypeConverter;
import net.sf.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        this(ClassHelper.forObject(tClass), new BeanConfig());
    }

    private StandardBeanCopy(T entity) {
        this(entity, new BeanConfig());
    }

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
//        if (!this.beanMap.containsKey(name)) {
//            return this;
//        }
        beanMap.put(name, value);
        withParams.put(name, value);
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
            ClassHelper.doWithFields(tClass, new FieldCallback() {
                @Override
                public void doWith(Field item) throws Throwable {
                    String name = item.getName();
                    if (!withParams.containsKey(name)) {
                        return;
                    }
                    Object value = withParams.get(name);
                    if (null == value) {
                        return;
                    }
                    ClassHelper.makeAccessible(item);

                    TypeConverter typeConverter = BeansHelper.getTypeConverter(item.getType());
                    try {
                        if (null == typeConverter) {
                            item.set(entity, value);
                        }
                        item.set(entity, typeConverter.convert(value));
                    } catch (Exception e) {
                    }

                }
            });
            return (T) entity;
        }
    }
}
