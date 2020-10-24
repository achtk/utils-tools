package com.chua.utils.tools.classes.finder;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.factory.MemberFactory;
import com.chua.utils.tools.function.Filter;
import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;
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
public class ClassAnnotationFinder implements Supplier<MemberFactory> {

    private FinderProperties finderProperties;

    @Override
    public MemberFactory get() {
        Filter filter = finderProperties.getFilter();

        MemberFactory memberFactory = new MemberFactory();
        Class<?> aClass1 = finderProperties.getAClass();

        if (null == aClass1 || !aClass1.isAnnotation()) {
            return memberFactory;
        }

        Set<Class<?>> typesAnnotatedWith = ClassHelper.getTypesAnnotatedWith((Class<? extends Annotation>) aClass1);
        for (Class aClass : typesAnnotatedWith) {
            if (null != filter && !filter.matcher(aClass)) {
                continue;
            }
            memberFactory.addAnnotationType(aClass);
        }
        return memberFactory;
    }
}
