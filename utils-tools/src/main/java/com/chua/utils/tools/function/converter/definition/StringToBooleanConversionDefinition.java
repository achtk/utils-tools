package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * string -> Boolean
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToBooleanConversionDefinition implements TypeConversionDefinition<String, Boolean> {

    private static final Set<String> TRUE_VALUES = new HashSet<>(4);

    private static final Set<String> FALSE_VALUES = new HashSet<>(4);

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

    @Nullable
    @Override
    public Boolean convert(String source) {
        String value = source.trim();
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
        } catch (Exception ignore) {
        }
        return null;
    }
}
