package com.chua.utils.tools.prop.placeholder;

import java.util.regex.Pattern;

import static com.chua.utils.tools.constant.StringConstant.REGEXP_ALL;

/**
 * 抽象占位符
 * @author CH
 */
public abstract class AbstractPropertiesPlaceholderResolver implements PropertiesPlaceholderResolver {

    private static final String DEFAULT_BEFORE = "\\$\\{";
    private static final String DEFAULT_VALUE_SEPARATE = ":";
    private static final String DEFAULT_AFTER = "\\}";
    private String after = DEFAULT_AFTER;
    private String valueSeparate = DEFAULT_VALUE_SEPARATE;
    private String before = DEFAULT_BEFORE;

    public AbstractPropertiesPlaceholderResolver() {
        this(DEFAULT_BEFORE, DEFAULT_VALUE_SEPARATE, DEFAULT_AFTER);
    }

    public AbstractPropertiesPlaceholderResolver(String before, String valueSeparate, String after) {
        this.before = before;
        this.valueSeparate = valueSeparate;
        this.after = after;
    }

    public Pattern getCompile() {
        //占位符正则
        String compileString = this.before() + REGEXP_ALL + this.after();
        //占位符对象
        return Pattern.compile(compileString, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public String before() {
        return before;
    }

    @Override
    public String valueSeparate() {
        return valueSeparate;
    }

    @Override
    public String after() {
        return after;
    }


}
