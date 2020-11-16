package com.chua.utils.tools.classes.reflections.scanner;

import org.reflections.Store;
import org.reflections.vfs.Vfs;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class RewriteResourcesScanner extends AbstractRewriteScanner {
    public boolean acceptsInput(String file) {
        return !file.endsWith(".class"); //not a class
    }

    @Override
    public Object scan(Vfs.File file, Object classObject, Store store) {
        put(store, file.getName(), file.getRelativePath());
        return classObject;
    }

    public void scan(Object cls, Store store) {
        throw new UnsupportedOperationException(); //shouldn't get here
    }
}
