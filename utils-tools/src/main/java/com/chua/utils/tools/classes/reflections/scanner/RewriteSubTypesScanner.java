package com.chua.utils.tools.classes.reflections.scanner;

import org.reflections.Store;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.FilterBuilder;
import org.reflections.vfs.Vfs;

import java.util.List;

/**
 * 子类查找器(重写)
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class RewriteSubTypesScanner extends AbstractRewriteScanner {


    public RewriteSubTypesScanner() {
        this(true);
    }

    public RewriteSubTypesScanner(boolean excludeObjectClass) {
        if (excludeObjectClass) {
            super.setResultFilter(new FilterBuilder().exclude(Object.class.getName()));
        }
    }

    @Override
    public void scan(Object cls, Store store) {
        String className = getMetadataAdapter().getClassName(cls);
        String superclass = getMetadataAdapter().getSuperclassName(cls);

        if (acceptResult(superclass)) {
            put(store, superclass, className);
        }

        for (String anInterface : (List<String>) getMetadataAdapter().getInterfacesNames(cls)) {
            if (acceptResult(anInterface)) {
                put(store, anInterface, className);
            }
        }
    }
}
