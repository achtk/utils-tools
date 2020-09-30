package com.chua.utils.tools.example;

import com.chua.utils.tools.spring.expression.ExpressionHelper;
import org.testng.annotations.Test;

/**
 * 表达式工具类测试
 * @author CH
 * @date 2020-09-30
 */
public class ExpressionTest {


    @Test
    public void parser() {
        System.out.println(ExpressionHelper.parser("#{3}", Integer.class));
        System.out.println(ExpressionHelper.parser("#{1 + 3}", Integer.class));
        System.out.println(ExpressionHelper.parser("#{1}.#{2}", String.class));
        System.out.println(ExpressionHelper.parser("#{'test'}#{2}", String.class));
    }
}
