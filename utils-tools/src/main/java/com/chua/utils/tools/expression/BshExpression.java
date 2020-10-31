package com.chua.utils.tools.expression;

import bsh.Interpreter;

/**
 * bsh表达式引擎
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class BshExpression implements Expression {

    private Interpreter interpreter = new Interpreter();

    @Override
    public void set(String key, Object value) throws Exception {
        interpreter.set(key, value);
    }

    @Override
    public Object eval(String expression) throws Exception {
        return interpreter.eval(expression);
    }
}
