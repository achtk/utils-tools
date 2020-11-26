package com.chua.utils.tools.collects;

import bsh.EvalError;
import bsh.Interpreter;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.function.BiAppendable;
import com.chua.utils.tools.function.MapOperable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 扩展性Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class HashOperateMap extends HashMap<String, Object> implements MapOperable<String>, BiAppendable<String, Object> {

    private final Interpreter interpreter = new Interpreter();

    @Override
    public Object put(String key, Object value) {
        this.setKeyValue(key, value);
        return super.put(key, value);
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
        for (Entry<? extends String, ?> entry : m.entrySet()) {
            this.setKeyValue(entry.getKey(), entry.getValue());
        }
        super.putAll(m);
    }

    @Override
    public BiAppendable<String, Object> append(String v1, Object v2) {
        this.put(v1, v2);
        this.setKeyValue(v1, v2);
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
     * @param className 类称
     * @return Object
     */
    public Object getBean(final String className) {
        return getBean(ClassHelper.forName(className));
    }

    /**
     * 装配对象
     *
     * @param className 类称
     * @param tClass    类型
     * @param <T>       类型
     * @return Object
     */
    public <T> T getBean(final String className, final Class<T> tClass) {
        return getBean(ClassHelper.forObject(className, tClass));
    }

    /**
     * 装配对象
     *
     * @param tClass 类型
     * @param <T>    类型
     * @return T
     */
    public <T> T getBean(final Class<T> tClass) {
        return getBean(ClassHelper.safeForObject(tClass));
    }

    /**
     * 装配对象
     *
     * @param t1  类型
     * @param <T> 类型
     * @return T
     */
    public <T> T getBean(final T t1) {
        if (null == t1) {
            return null;
        }
        BeanCopy<T> beanCopy = BeanCopy.of(t1);
        beanCopy.with(this);
        return beanCopy.create();
    }
}
