package com.chua.utils.tools.example;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class BshExample {

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        String s = "return \"hello\"";
        try {
            Object object = interpreter.eval(s);
            System.out.println(object.toString());
        } catch (EvalError e) {
            e.printStackTrace();
        }
    }
}
