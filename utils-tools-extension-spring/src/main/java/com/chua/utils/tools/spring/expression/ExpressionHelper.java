package com.chua.utils.tools.spring.expression;

import com.chua.utils.tools.common.BooleanHelper;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * EL表达式工具类
 *
 * @author CH
 * @date 2020-09-30
 */
public class ExpressionHelper {
    /**
     * 解析el表达式
     * <p>#{testConstant}输出testConstant的值</p>
     * <p>#{testConstant.STR}输出testConstant中的STR常量/变量值</p>
     * <p>#{5}输出num的值, 当返回值为整数输出5，否则输出对应值</p>
     * <p>#{testConstant.showProperty('Hello')} 调用testConstant的showProperty方法并传递参数</p>
     * <p>#{testConstant.showProperty().toUpperCase()}希望方法返回的String为大写</p>
     * <p>
     * #{testConstant.showProperty()?.toUpperCase}若然showProperty返回为null,
     * 将会抛出NullPointerException,可以使用以下方式避免
     * </p>
     * <p>#{T(java.lang.Math).PI 当前实体T()中的PI产量</p>
     * <p>#{T(java.lang.Math).random()}调用random方法获取返回值</p>
     * <p>#{testConstant.nickname + ' ' + testConstant.name} 拼接字符串</p>
     * <p>
     * #{ 3 * T(java.lang.Math).PI + testConstant.num} 对数字类型进行运算,testConstant拥有num属性
     * </p>
     * <p>#{testConstant.num > 100 and testConstant.num <= 200} 进行逻辑运算</p>
     * <p>#{testConstant.num > 100 or  testConstant.num <= 200} 进行或非逻辑操作</p>
     * <p>#{testConstant.num > 100 ? testConstant.num : testConstant.num + 100} 使用三元运算符</p>
     * <p>
     * #{testConstant.STR match '\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+'}
     * 验证是否邮箱地址正则表达式
     * </p>
     * <p>#{testConstant.testList[0]} 获取下标为0的元素</p>
     * <p>#{testConstant.testList[0]?.toUpperCase()} 获取下标为0元素的大写形式</p>
     * <p>#{testConstant.testMap['hello']} 获取map中key为hello的value</p>
     * <p>#{testConstant.testMap[testConstant.testList[0]]} 根据testList下标为0元素作为key获取testMap的value</p>
     * <p>
     * #{testConstant.cityList.?[population > 1000]}
     * 过滤testConstant中cityList集合population属性大于1000的全部数据注入到本属性
     * </p>
     * <p>
     * #{testConstant.cityList.^[population == 1000]}
     * 过滤testConstant中cityList集合population属性等于1000的第一条数据注入到本属性
     * </p>
     * <p>
     * #{testConstant.cityList.$[population == 1000]}
     * 过滤testConstant中cityList集合population属性等于1000的最后一条数据注入到本属性
     * </p>
     * <p>
     * 假如我们在过滤城市集合后只想保留城市的名称 可以使用如下方式
     * #{testConstant.cityList.?[population > 1000].![name]}
     * </p>
     *
     * @param expression el表达式
     * @param params     变量
     * @param <T>
     * @return
     */
    public static <T> T parser(final String expression, final Class<T> tClass, final Map<String, Object> params) {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        if (BooleanHelper.hasLength(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }

        return parser.parseExpression(expression, new TemplateParserContext()).getValue(context, tClass);
    }

    /**
     * 解析el表达式
     *
     * @param expression el表达式
     * @param tClass     类型
     * @param <T>
     * @return
     */
    public static <T> T parser(final String expression, final Class<T> tClass) {
        return parser(expression, tClass, null);
    }

    /**
     * 解析el表达式
     *
     * @param expression el表达式
     * @return
     */
    public static String parserString(final String expression) {
        return parser(expression, String.class, null);
    }

    /**
     * 解析el表达式
     *
     * @param expression el表达式
     * @param params     变量
     * @return
     */
    public static String parserString(final String expression, final Map<String, Object> params) {
        return parser(expression, String.class, params);
    }
    /**
     * 解析el表达式
     *
     * @param expression el表达式
     * @return
     */
    public static Integer parserInteger(final String expression) {
        return parser(expression, Integer.class, null);
    }

    /**
     * 解析el表达式
     *
     * @param expression el表达式
     * @param params     变量
     * @return
     */
    public static Integer parserInteger(final String expression, final Map<String, Object> params) {
        return parser(expression, Integer.class, params);
    }
    /**
     * 解析el表达式
     *
     * @param expression el表达式
     * @return
     */
    public static Boolean parserBoolean(final String expression) {
        return parser(expression, Boolean.class, null);
    }

    /**
     * 解析el表达式
     *
     * @param expression el表达式
     * @param params     变量
     * @return
     */
    public static Boolean parserBoolean(final String expression, final Map<String, Object> params) {
        return parser(expression, Boolean.class, params);
    }
}
