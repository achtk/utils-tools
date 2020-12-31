package com.chua.utils.tools.function.converter;

import java.util.HashSet;
import java.util.Set;

/**
 * boolean类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class BooleanTypeConverter implements TypeConverter<Boolean> {

    private static final Set<String> TRUE_VALUES = new HashSet<String>(4);

    private static final Set<String> FALSE_VALUES = new HashSet<String>(4);

    static {
        TRUE_VALUES.add("true");
        TRUE_VALUES.add("on");
        TRUE_VALUES.add("yes");
        TRUE_VALUES.add("1");

        FALSE_VALUES.add("false");
        FALSE_VALUES.add("off");
        FALSE_VALUES.add("no");
        FALSE_VALUES.add("0");
    }

    @Override
    public Boolean convert(Object source) {
        if (null == source) {
            return null;
        }

        if (source instanceof Boolean) {
            return (Boolean) source;
        }

        if (source instanceof String) {
            String value = source.toString().trim();
            if ("".equals(value)) {
                return null;
            }
            value = value.toLowerCase();
            if (TRUE_VALUES.contains(value)) {
                return Boolean.TRUE;
            } else if (FALSE_VALUES.contains(value)) {
                return Boolean.FALSE;
            }

            try {
                return Boolean.valueOf(value);
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }
}
