package com.chua.utils.tools.prop.placeholder;

import com.chua.utils.tools.constant.PatternConstant;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * properties 占位符处理
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/28 18:20
 */
public class PropertiesPropertyPlaceholder implements PropertyPlaceholder {

    private static final String DEFAULT_BEFORE = "\\$\\{";
    private static final String DEFAULT_VALUE_SEPARATE = ":";
    private static final String DEFAULT_AFTER = "\\}";

    private final Map<String, Properties> cache = new HashMap<>();

    private String before = DEFAULT_BEFORE;
    private String valueSeparate = DEFAULT_VALUE_SEPARATE;
    private String after = DEFAULT_AFTER;
    private boolean ignoreUnknownFields = true;

    @Override
    public void before(String before) {
        this.before = before;
    }

    @Override
    public void after(String after) {
        this.after = after;
    }

    @Override
    public void valueSeparate(String valueSeparate) {
        this.valueSeparate = valueSeparate;
    }

    @Override
    public void ignoreUnknownFields(boolean ignoreUnknownFields) {
        this.ignoreUnknownFields = ignoreUnknownFields;
    }

    @Override
    public void addPropertySource(String name, Properties properties) {
        cache.put(name, properties);
    }

    @Override
    public Object placeholder(Object value) {
        if (null == value) {
            return null;
        }
        return analyse(value);
    }


    private Object analyse(Object value) {
        if (!(value instanceof String)) {
            return value;
        }
        String stringValue = value.toString();
        if (isPlaceholder(stringValue)) {
            return analysePlaceholder(stringValue);
        }
        return stringValue;
    }

    /**
     * 解析占位符
     * @param stringValue
     * @return
     */
    protected Object analysePlaceholder(String stringValue) {
        if (!isPlaceholder(stringValue)) {
            return stringValue;
        }
        PlaceholderSource placeholderSource = getPlaceholderSource(stringValue);

        Object o = get(placeholderSource.getNewKey());
        if (o == null) {
            return ignoreUnknownFields ? stringValue : placeholderSource.getDefaultValue();
        }
        Object analyse = analyse(o);
        if (null == analyse) {
            return placeholderSource.getDefaultValue();
        }
        return analyse;
    }

    /**
     * 解析占位符
     * @param stringValue
     * @return
     */
    protected PlaceholderSource getPlaceholderSource(String stringValue) {
        PlaceholderSource placeholderSource = new PlaceholderSource();
        if (!isPlaceholder(stringValue)) {
            placeholderSource.setNewKey(stringValue);
            return placeholderSource;
        }
        placeholderSource.setPlaceholder(true);
        Matcher matcher = pattern().matcher(stringValue);
        //开始匹配
        matcher.find();
        //占位数据
        String placeValue = matcher.group(1);
        //分割数据
        String[] split = placeValue.split(valueSeparate);
        String placeholderValue = split[0], defaultValue = null;
        if (split.length > 1) {
            defaultValue = split[1];
        }
        placeholderSource.setDefaultValue(defaultValue);
        placeholderSource.setNewKey(placeholderValue);
        return placeholderSource;
    }

    @Override
    public boolean isPlaceholder(String value) {
        return pattern().matcher(value).find();
    }

    @Override
    public Pattern pattern() {
        //占位符正则
        String compileString = this.before + PatternConstant.REGEXP_ANY + this.after;
        //占位符对象
        return Pattern.compile(compileString, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean containerKey(String key) {
        for (Map.Entry<String, Properties> entry : cache.entrySet()) {
            Properties properties = entry.getValue();
            if (!properties.containsKey(key)) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Override
    public Object get(String key) {
        Object value = null;
        for (Map.Entry<String, Properties> entry : cache.entrySet()) {
            Properties properties = entry.getValue();
            if (!properties.containsKey(key)) {
                continue;
            }
            value = properties.getProperty(key);
        }
        return value;
    }

    @Data
    protected static class PlaceholderSource {
        private String newKey;
        private String defaultValue;
        private boolean isPlaceholder;
    }
}
