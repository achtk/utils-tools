package com.chua.utils.tools.predicate;

import com.google.common.base.Predicate;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * 返回true Predicate
 *
 * @author CH
 */
public class TruePredicate implements Predicate<Object> {

    public static final Predicate<Object> INSTANCE = new TruePredicate();

    @Override
    public boolean apply(@Nullable Object input) {
        return true;
    }
}
