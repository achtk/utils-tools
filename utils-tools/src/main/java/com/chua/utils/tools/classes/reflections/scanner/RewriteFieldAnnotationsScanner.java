package com.chua.utils.tools.classes.reflections.scanner;

import org.reflections.Store;

import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class RewriteFieldAnnotationsScanner extends AbstractRewriteScanner{
    @Override
    public void scan(Object cls, Store store) {
        final String className = getMetadataAdapter().getClassName(cls);
        List<Object> fields = getMetadataAdapter().getFields(cls);
        for (final Object field : fields) {
            List<String> fieldAnnotations = getMetadataAdapter().getFieldAnnotationNames(field);
            for (String fieldAnnotation : fieldAnnotations) {

                if (acceptResult(fieldAnnotation)) {
                    String fieldName = getMetadataAdapter().getFieldName(field);
                    put(store, fieldAnnotation, String.format("%s.%s", className, fieldName));
                }
            }
        }
    }
}
