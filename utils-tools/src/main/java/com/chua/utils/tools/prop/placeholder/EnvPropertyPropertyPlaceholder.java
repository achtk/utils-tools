package com.chua.utils.tools.prop.placeholder;

import lombok.EqualsAndHashCode;

/**
 * 环境变量
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/28 18:45
 */
@EqualsAndHashCode(callSuper = false)
public class EnvPropertyPropertyPlaceholder extends PropertiesPropertyPlaceholder {

    @Override
    public Object placeholder(Object value) {
        if (!(value instanceof String)) {
            return super.placeholder(value);
        }
        PlaceholderSource placeholderSource = super.getPlaceholderSource(value.toString());
        String getenv = System.getProperty(placeholderSource.getNewKey());
        return null == getenv ? placeholderSource.getDefaultValue() : getenv;
    }
}
