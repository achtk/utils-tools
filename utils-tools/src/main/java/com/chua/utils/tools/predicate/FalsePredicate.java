package com.chua.utils.tools.predicate;

import com.google.common.base.Predicate;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * 返回false Predicate
 * @author CH
 */
public class FalsePredicate implements Predicate {
    public static final Predicate INSTANCE = new FalsePredicate();

    @Override
    public boolean apply(@Nullable Object input) {
        return false;
    }
}
