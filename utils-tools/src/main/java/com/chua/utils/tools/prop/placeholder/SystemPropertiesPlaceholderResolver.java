package com.chua.utils.tools.prop.placeholder;

import com.chua.utils.tools.prop.placeholder.mapper.PlaceholderMapper;

import java.util.regex.Matcher;

/**
 * 默认占位符
 * @author CH
 */
public class SystemPropertiesPlaceholderResolver extends AbstractPropertiesPlaceholderResolver {

    @Override
    public String before() {
        return "#\\{";
    }

    @Override
    public boolean isMatcher(String value) {
        if(null == value) {
            return false;
        }
        return getCompile().matcher(value).find();
    }

    @Override
    public PlaceholderMapper analyze(String value) {
        Matcher matcher = getCompile().matcher(value);
        //开始匹配
        matcher.find();
        //占位数据
        String placeValue = matcher.group(1);
        //分割数据
        String[] split = placeValue.split(valueSeparate());
        String placeholderValue = split[0], defaultValue = null;
        if(split.length > 1) {
            defaultValue = split[1];
        }
        String property = System.getProperty(placeholderValue);
        if(null == property) {
            property = System.getenv(placeholderValue);
        }
        return new PlaceholderMapper(property, defaultValue, false);
    }
}
