package com.chua.utils.tools.prop.placeholder.resolver;

import com.chua.utils.tools.constant.PatternConstant;

import java.util.regex.Pattern;

/**
 * 抽象占位符
 *
 * @author CH
 */
public abstract class AbstractPropertiesPlaceholderResolver implements PropertiesPlaceholderResolver {

    private static final String DEFAULT_BEFORE = "\\$\\{";
    private static final String DEFAULT_VALUE_SEPARATE = ":";
    private static final String DEFAULT_AFTER = "\\}";
    private String after = DEFAULT_AFTER;
    private String valueSeparate = DEFAULT_VALUE_SEPARATE;
    private String before = DEFAULT_BEFORE;

    protected AbstractPropertiesPlaceholderResolver() {
        this(DEFAULT_BEFORE, DEFAULT_VALUE_SEPARATE, DEFAULT_AFTER);
    }

    protected AbstractPropertiesPlaceholderResolver(String before, String valueSeparate, String after) {
        this.before = before;
        this.valueSeparate = valueSeparate;
        this.after = after;
    }

    public Pattern getCompile() {
        //占位符正则
        String compileString = this.before() + PatternConstant.REGEXP_ANY + this.after();
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
