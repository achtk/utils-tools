package com.chua.utils.tools.collects;

import bsh.EvalError;
import bsh.Interpreter;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.function.converter.ClassTypeConverter;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.chua.utils.tools.util.CollectionUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.chua.utils.tools.constant.NumberConstant.THIRD;

/**
 * 扩展性Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class OperateHashMap extends HashMap<String, Object> implements OperateMap {

    private final Interpreter interpreter = new Interpreter();

    private final static OperateHashMap INSTANCE = OperateHashMap.create();

    private Class<?> valueClass = Object.class;

    public OperateHashMap() {
    }

    public OperateHashMap(Class<?> valueClass) {
        this.valueClass = valueClass;
    }

    /**
     * 创建 OperateHashMap
     *
     * @return OperateHashMap
     */
    public static OperateHashMap create() {
        return new OperateHashMap();
    }

    /**
     * 创建 OperateHashMap
     *
     * @return OperateHashMap
     */
    public static OperateHashMap create(Consumer<OperateHashMap> consumer) {
        OperateHashMap operateMap = new OperateHashMap();
        consumer.accept(operateMap);
        return operateMap;
    }

    /**
     * 创建 OperateHashMap
     *
     * @return OperateHashMap
     */
    public static OperateHashMap emptyMap() {
        return INSTANCE;
    }

    /**
     * 创建 OperateHashMap
     *
     * @return OperateHashMap
     */
    public static OperateHashMap create(Class<?> valueClass) {
        return new OperateHashMap(valueClass);
    }

    /**
     * 创建 OperateHashMap
     *
     * @return OperateHashMap
     */
    public static OperateHashMap create(String key, Object value) {
        return new OperateHashMap().append(key, value);
    }

    /**
     * 创建 OperateHashMap
     *
     * @return OperateHashMap
     */
    public static OperateHashMap create(Map<String, Object> params) {
        return new OperateHashMap().append(params);
    }

    /**
     * 创建 OperateHashMap
     *
     * @param params 数据
     * @return OperateHashMap
     */
    public static OperateHashMap create(String params) {
        return create(params, ",", ":");
    }

    /**
     * 创建 OperateHashMap
     *
     * @param params            数据
     * @param lineSeparator     数据分隔符
     * @param keyValueSeparator 键值对分隔符
     * @return OperateHashMap
     */
    public static OperateHashMap create(String params, String lineSeparator, String keyValueSeparator) {
        OperateHashMap operateHashMap = new OperateHashMap();
        int size = 2;
        Splitter.on(lineSeparator)
                .trimResults()
                .omitEmptyStrings()
                .splitToStream(params)
                .forEach(item -> {
                    List<String> strings = Splitter.on(keyValueSeparator).trimResults().omitEmptyStrings().splitToList(item);
                    if (strings.size() == size) {
                        operateHashMap.put(strings.get(0), strings.get(1));
                    } else if (strings.size() > size) {
                        operateHashMap.put(strings.get(0), Joiner.on(keyValueSeparator).join(strings.subList(1, strings.size())));
                    }
                });
        return operateHashMap;
    }

    @Override
    public Object put(String key, Object value) {
        this.setKeyValue(key, value);
        if (null != value && null != valueClass && !valueClass.isAssignableFrom(value.getClass())) {
            return null;
        }
        return super.put(key, value);
    }

    /**
     * 添加实体类属性到集合
     *
     * @param object 实体类
     */
    public void putComputeIfEntity(Object object) {
        if (null == object || object instanceof Class) {
            return;
        }
        BeanCopy<Object> beanCopy = BeanCopy.of(object);
        OperateHashMap operateMap = beanCopy.asMap();
        this.putAll(operateMap);
    }

    /**
     * 保存时计算结果是否满足
     * <p>满足表示已有数据满足条件不进行插入</p>
     * <p>e.g. putComputeIfAbsent(null, "demo", item -> item != null) = null </p>
     * <p>e.g. putComputeIfAbsent("demo", "demo", item -> item != null) = 原始值 || demo </p>
     * <p>e.g. putComputeIfAbsent("demo", null, item -> item != null) = 原始值 </p>
     * <p>条件函数{@link Predicate}</p>
     *
     * @param key      索引
     * @param newValue 值
     * @param filter   条件
     * @return <ul>
     * <li>当前{key}为空返回null</li>
     * <li>当前{key}不在集合中直接存储当前数据返回{newValue}</li>
     * <li>当前{key}在集合中并且存储的数据满足条件{filter}直接存储进行覆盖当前数据返回{newValue}</li>
     * </ul>
     */
    public Object putComputeIfAbsent(String key, Object newValue, Predicate<Object> filter) {
        if (null == key) {
            return null;
        }

        if (!containsKey(key)) {
            return put(key, newValue);
        }

        Object oldValue = get(key);
        if (filter.test(oldValue)) {
            return put(key, newValue);
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        if (CollectionUtils.isEmpty(m)) {
            return;
        }
        for (Entry<? extends String, ?> entry : m.entrySet()) {
            this.setKeyValue(entry.getKey(), entry.getValue());
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public OperateHashMap append(String v1, Object v2) {
        this.setKeyValue(v1, v2);
        this.put(v1, v2);
        return this;
    }

    /**
     * 添加
     *
     * @param params 数据
     * @return this
     */
    public OperateHashMap append(Map<String, Object> params) {
        this.putAll(params);
        return this;
    }

    @Override
    public Map<String, Object> getMap() {
        return this;
    }

    /**
     * 设置参数
     *
     * @param key   索引
     * @param value 值
     */
    private void setKeyValue(String key, Object value) {
        try {
            interpreter.set(key, value);
        } catch (EvalError ignored) {
        }
    }

    /**
     * 处理表达式
     *
     * @param expression 表达式
     * @return 表达式
     */
    @Override
    public Object expression(String expression) {
        try {
            return interpreter.eval(expression);
        } catch (EvalError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 装配对象
     *
     * @param t1  类型
     * @param <T> 类型
     * @return T
     */
    @Override
    public <T> T assemblyBean(final T t1) {
        if (null == t1) {
            return null;
        }
        BeanCopy<T> beanCopy = BeanCopy.of(t1);
        beanCopy.with(this);
        return beanCopy.create();
    }

    /**
     * 获取实体
     *
     * @param key    索引
     * @param tClass 类型
     * @param <T>    类型
     * @return 实体
     */
    @Override
    public <T> T getEntity(String key, Class<T> tClass) {
        Object object = getObject(key);
        return null == object || (null != tClass && tClass.isAssignableFrom(object.getClass())) ? (T) object : null;
    }


    @Override
    @SuppressWarnings("ALL")
    public <T> Class<? extends T> getClass(String key, final Class<T> tClass) {
        TypeConverter<Class> typeConverter = new ClassTypeConverter();
        Object object = getObject(key);
        Class aClass = typeConverter.convert(object);
        if (null == aClass || null == tClass) {
            return null;
        }
        return tClass.isAssignableFrom(aClass) ? aClass : null;
    }

    /**
     * 字段是否有效(存在/非空)
     *
     * @param key 索引
     * @return 有效返回true
     */
    public boolean isValid(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return false;
        }
        return null != getObject(key);
    }

    /**
     * 字段是否都有效(存在/非空)
     *
     * @param keys 索引
     * @return 有效返回true
     */
    public boolean isAllValid(String... keys) {
        if (null == keys) {
            return false;
        }

        boolean isAllValid = true;
        for (String key : keys) {
            boolean valid = isValid(key);
            if (!valid) {
                isAllValid = false;
                break;
            }
        }
        return isAllValid;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("{\r\n");
        for (Entry<String, Object> entry : this.entrySet()) {
            stringBuffer.append("\t").append(entry.getKey()).append(":").append(entry.getValue()).append("(").append(entry.getValue().getClass().getSimpleName()).append("),\r\n");
        }
        if (stringBuffer.length() > THIRD) {
            stringBuffer.delete(stringBuffer.length() - 3, stringBuffer.length());
        }
        stringBuffer.append("\r\n}");
        return stringBuffer.toString();
    }
}
