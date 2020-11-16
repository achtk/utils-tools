package com.chua.utils.tools.collects;

import bsh.EvalError;
import bsh.Interpreter;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.function.BiAppendable;
import com.chua.utils.tools.function.MapOperable;

import java.util.HashMap;
import java.util.Map;

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
