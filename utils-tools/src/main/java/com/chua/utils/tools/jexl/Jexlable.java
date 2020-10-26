package com.chua.utils.tools.jexl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.With;
import org.apache.commons.jexl3.*;

import java.util.concurrent.Callable;

/**
 * jexl
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/26
 */
public class Jexlable {

    private final String expression;
    private JexlEngine jexl = new JexlBuilder().create();
    private JexlExpression jexlExpression;
    private JexlContext jexlContext;

    public Jexlable(String expression, JexlContext jexlContext) {
        this.expression = expression;
        this.jexlContext = jexlContext;
        this.jexlExpression = jexl.createExpression(expression);
    }

    public static Jexlable.Builder newBuilder(final String expression) {
        return new Jexlable.Builder(expression);
    }

    public <T> T get(Class<T> tClass) {
        return (T) jexlExpression.evaluate(jexlContext);
    }

    public <T>Callable<T> callable(Class<T> tClass) {
        return (Callable<T>) jexlExpression.callable(jexlContext);
    }

    public Object get() {
        return jexlExpression.evaluate(jexlContext);
    }

    public Callable<Object> callable() {
        return jexlExpression.callable(jexlContext);
    }

    @With
    @AllArgsConstructor
    public static class Builder {

        private String expression;

        private final JexlContext jexlContext = new MapContext();

        public Builder environment(String key, Object value) {
            jexlContext.set(key, value);
            return this;
        }

        public Jexlable build() {
            return new Jexlable(expression, jexlContext);
        }
    }
}
