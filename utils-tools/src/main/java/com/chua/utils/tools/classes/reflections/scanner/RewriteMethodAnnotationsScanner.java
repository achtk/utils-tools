package com.chua.utils.tools.classes.reflections.scanner;

import org.reflections.Store;

import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class RewriteMethodAnnotationsScanner extends AbstractRewriteScanner {
    @Override
    public void scan(Object cls, Store store) {
        for (Object method : getMetadataAdapter().getMethods(cls)) {
            for (String methodAnnotation : (List<String>) getMetadataAdapter().getMethodAnnotationNames(method)) {
                if (acceptResult(methodAnnotation)) {
                    put(store, methodAnnotation, getMetadataAdapter().getMethodFullKey(cls, method));
                }
            }
        }
    }
}
