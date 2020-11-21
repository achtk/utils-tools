package com.chua.tools.spider.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * css选择器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD})
@Documented
public @interface CssQuery {

    /**
     * CSS-like query, like "#body"
     * <p>
     * CSS选择器, 如 "#body"
     *
     * @return String
     */
    String value() default "";


    /**
     * jquery data-extraction-type，like ".html()/.text()/.val()/.attr() ..."
     * <p>
     * jquery 数据抽取方式，如 ".html()/.text()/.val()/.attr() ..."等
     *
     * @return SelectType
     */
    SelectType selectType() default SelectType.TEXT;

    /**
     * jquery data-extraction-value, effect when SelectType=ATTR/HAS_CLASS, like ".attr("abs:src")"
     * <p>
     * jquery 数据抽取参数，SelectType=ATTR/HAS_CLASS 时有效，如 ".attr("abs:src")"
     *
     * @return String
     */
    String selectVal() default "";

    /**
     * data patttern, valid when date data
     * <p>
     * 时间格式化，日期类型数据有效
     *
     * @return String
     */
    String datePattern() default "yyyy-MM-dd HH:mm:ss";

    public enum SelectType {
        // .html()
        HTML,
        // .val()
        VAL,
        // .text()
        TEXT,
        // .toString()
        TOSTRING,
        // .attr("attributeKey")
        ATTR,
        // .hasClass("className")
        HAS_CLASS;
    }

}
