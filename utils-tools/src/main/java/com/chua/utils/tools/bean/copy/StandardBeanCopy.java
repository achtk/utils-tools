package com.chua.utils.tools.bean.copy;

import com.chua.utils.tools.annotations.BinderMapper;
import com.chua.utils.tools.bean.config.BeanConfig;
import com.chua.utils.tools.bean.interpreter.NameInterpreter;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.function.Converter;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.chua.utils.tools.util.AnnotationUtils;
import com.chua.utils.tools.util.ClassUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.sf.cglib.beans.BeanMap;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * 标准的bean拷贝
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class StandardBeanCopy<T> implements BeanCopy<T> {
    /**
     * 字段信息
     */
    private Map<String, Class<?>> fieldInfo;
    /**
     * 字段映射
     */
    private Multimap<String, String> mapper = HashMultimap.create();
    /**
     * 基础配置
     */
    private BeanConfig beanConfig = new BeanConfig();
    /**
     * 类属性
     */
    protected BeanMap beanMap;
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
    protected Map<String, Object> withParams = new HashMap<>();

    private StandardBeanCopy() {
    }

    protected StandardBeanCopy(Class<T> tClass) {
        this(ClassHelper.safeForObject(tClass), new BeanConfig());
        this.tClass = tClass;
    }

    private StandardBeanCopy(T entity) {
        this(entity, new BeanConfig());
    }

    @SuppressWarnings("all")
    private StandardBeanCopy(T entity, BeanConfig beanConfig) {
        if (null != entity) {
            this.tClass = (Class<T>) entity.getClass();
            this.beanMap = BeanMap.create(entity);
        }
        this.analyse();
        this.entity = entity;
        this.beanConfig = beanConfig;
    }

    /**
     * 解析字段信息
     *
     * @return 字段信息
     */
    private void analyse() {
        if (null == tClass) {
            return;
        }
        this.fieldInfo = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        ClassUtils.doWithFields(tClass, field -> {
            fieldInfo.put(field.getName(), field.getType());
            BinderMapper binderMapper = field.getDeclaredAnnotation(BinderMapper.class);
            if (null != binderMapper) {
                mapper.put(binderMapper.value(), field.getName());
            }
        });
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
        if (null != entity && entity instanceof String) {
            return null;
        }
        return new StandardBeanCopy<>(entity);
    }

    @Override
    public BeanCopy with(String name, Object value) {
        name = transTo(name);
        this.mapper(name, value);
        return this;
    }

    /**
     * 名称映射
     *
     * @param name  名称
     * @param value 值
     */
    protected void mapper(String name, Object value) {
        value = transform(name, value);

        if (null != beanMap) {
            beanMap.put(name, value);
        }
        withParams.put(name, value);
        if (mapper.containsKey(name)) {
            Collection<String> realNames = mapper.get(name);
            for (String realName : realNames) {
                if (null != beanMap) {
                    beanMap.put(realName, value);
                }
                withParams.put(realName, value);
            }
        }
    }

    /**
     * 转化
     *
     * @param name  索引
     * @param value 值
     * @return 值
     */
    private Object transform(String name, Object value) {
        if (null != fieldInfo && fieldInfo.containsKey(name)) {
            Class<?> aClass = fieldInfo.get(name);
            TypeConverter typeConverter = com.chua.utils.tools.function.converter.Converter.getTypeConverter(aClass);
            if (null != typeConverter) {
                value = typeConverter.convert(value);
            }
        }
        return value;
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
        if (MapOperableHelper.isEmpty(param)) {
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
        if (entity instanceof List) {
            AtomicInteger atomicInteger = new AtomicInteger();
            ClassHelper.doWithFields(tClass, field -> {
                with(field.getName(), ((List) entity).get(atomicInteger.getAndIncrement()));
            });
            return this;
        }

        if (entity instanceof Map) {
            with((Map<String, Object>) entity);
            return this;
        }
        BeanMap beanMap = BeanMap.create(entity);
        ClassUtils.doWithFields(entity.getClass(), fields -> {
            String name = fields.getName();
            BinderMapper binderMapper = AnnotationUtils.get(fields.getClass(), BinderMapper.class);
            this.with(name, beanMap.get(name));
            if (null != binderMapper) {
                this.with(binderMapper.value(), beanMap.get(name));
            }
        });
        return this;
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
        if (MapOperableHelper.isEmpty(withParams) || null == entity) {
            if (Map.class.isAssignableFrom(tClass)) {
                return (T) withParams;
            }
            return entity;
        }
        ObjectCreate<T> objectCreate = null;
        if (isCglibable()) {
            objectCreate = new CglibObjectCreate<>();
        } else {
            objectCreate = new ReflectionObjectCreate<>();
        }
        return objectCreate.create();
    }

    @Override
    public T create(String... mapper) {
        if (null == mapper) {
            return create();
        }
        for (String s : mapper) {
            List<String> strings = Splitter.on("->").trimResults().omitEmptyStrings().splitToList(s);
            if (null == strings || strings.size() != 2) {
                continue;
            }
            String mapperName = strings.get(0);
            String realName = strings.get(1);
            this.mapper.put(mapperName, realName);
        }
        this.with(withParams);
        return create();
    }

    @Override
    public OperateHashMap asMap() {
        OperateHashMap operateHashMap = new OperateHashMap();
        BeanMap beanMap = null;
        try {
            beanMap = BeanMap.create(entity);
            operateHashMap.putAll(beanMap);
        } catch (Exception e) {
            ClassHelper.doWithFields(tClass, field -> {
                operateHashMap.put(field.getName(), ClassHelper.getFieldValue(entity, field));
            });
        }
        return operateHashMap;
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
        entry = MapOperableHelper.getIndex(this.withParams, 1);
        if (null != entry && this.beanMap.containsKey(entry.getKey())) {
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
    protected String transTo(String name) {
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
            ClassUtils.doWithSetFieldValue(entity, withParams);
            return (T) entity;
        }
    }
}
