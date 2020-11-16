package com.chua.utils.tools.classes.reflections.scanner;

import org.reflections.Store;
import org.reflections.adapters.MetadataAdapter;

import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class RewriteMethodParameterScanner extends AbstractRewriteScanner{
    @Override
    public void scan(Object cls, Store store) {
        final MetadataAdapter md = getMetadataAdapter();

        for (Object method : md.getMethods(cls)) {

            String signature = md.getParameterNames(method).toString();
            if (acceptResult(signature)) {
                put(store, signature, md.getMethodFullKey(cls, method));
            }

            String returnTypeName = md.getReturnTypeName(method);
            if (acceptResult(returnTypeName)) {
                put(store, returnTypeName, md.getMethodFullKey(cls, method));
            }

            List<String> parameterNames = md.getParameterNames(method);
            for (int i = 0; i < parameterNames.size(); i++) {
                for (Object paramAnnotation : md.getParameterAnnotationNames(method, i)) {
                    if (acceptResult((String) paramAnnotation)) {
                        put(store, (String) paramAnnotation, md.getMethodFullKey(cls, method));
                    }
                }
            }
        }
    }
}
