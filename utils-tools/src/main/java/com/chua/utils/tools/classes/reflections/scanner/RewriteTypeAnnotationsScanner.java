package com.chua.utils.tools.classes.reflections.scanner;

import org.reflections.Store;

import java.lang.annotation.Inherited;
import java.util.List;

/**
 * 注解查询器(重写)
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class RewriteTypeAnnotationsScanner extends AbstractRewriteScanner {

    @Override
    public void scan(final Object cls, Store store) {
        final String className = getMetadataAdapter().getClassName(cls);
        for (String annotationType : (List<String>) getMetadataAdapter().getClassAnnotationNames(cls)) {

            if (acceptResult(annotationType) || annotationType.equals(Inherited.class.getName())) {
                put(store, annotationType, className);
            }
        }
    }
}
