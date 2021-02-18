package com.chua.utils.tools.prop.placeholder;

/**
 * 环境变量
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/28 18:45
 */
public class EnvPropertyPlaceholder extends PropertiesPropertyPlaceholder {

    @Override
    public Object placeholder(Object value) {
        if (!(value instanceof String)) {
            return super.placeholder(value);
        }
        PlaceholderSource placeholderSource = super.getPlaceholderSource(value.toString());
        String getenv = null;
        if (placeholderSource.isPlaceholder()) {
            getenv = System.getenv(placeholderSource.getNewKey());
        } else {
            getenv = placeholderSource.getNewKey();
        }
        return null == getenv ? placeholderSource.getDefaultValue() : getenv;
    }
}
