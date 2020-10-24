package com.chua.utils.tools.classes.finder;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.ReflectionsFactory;
import com.chua.utils.tools.factory.MemberFactory;
import com.chua.utils.tools.function.Filter;
import lombok.AllArgsConstructor;
import org.reflections.Reflections;

import java.util.Set;
import java.util.function.Supplier;

/**
 * 子类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
@AllArgsConstructor
public class ClassSubTypeFinder implements Supplier<MemberFactory> {

    private FinderProperties finderProperties;

    @Override
    public MemberFactory get() {
        Filter filter = finderProperties.getFilter();

        MemberFactory memberFactory = new MemberFactory();
        if (null != finderProperties.getClass()) {
            Set<? extends Class> subTypesOf = ClassHelper.getSubTypesOf(finderProperties.getClass());
            for (Class aClass : subTypesOf) {
                if (null != filter && !filter.matcher(aClass)) {
                    continue;
                }
                memberFactory.addSubType(aClass);
            }
        }
        return memberFactory;
    }
}
