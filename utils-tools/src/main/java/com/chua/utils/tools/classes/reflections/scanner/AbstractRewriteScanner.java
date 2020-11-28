package com.chua.utils.tools.classes.reflections.scanner;

import com.chua.utils.tools.classes.reflections.RewriteStore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.reflections.Configuration;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.AbstractScanner;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

import java.net.URL;
import java.util.function.Predicate;

/**
 * 抽象的扫描器(重写)
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
@Getter
@Setter
@EqualsAndHashCode
public abstract class AbstractRewriteScanner extends AbstractScanner {

    private Configuration configuration;
    private Predicate<String> resultFilter = s -> true;
    @Setter
    private URL url;

    @Override
    public Object scan(Vfs.File file, Object classObject, Store store) {
        if (classObject == null) {
            try {
                MetadataAdapter metadataAdapter = configuration.getMetadataAdapter();
                classObject = metadataAdapter.getOrCreateClassObject(file);
            } catch (Exception e) {
                throw new ReflectionsException("could not create class object from file " + file.getRelativePath(), e);
            }
        }
        this.scan(classObject, store);
        return classObject;
    }

    @Override
    protected MetadataAdapter getMetadataAdapter() {
        return configuration.getMetadataAdapter();
    }

    @Override
    protected void put(Store store, String key, String value) {
        if(store instanceof RewriteStore) {
            RewriteStore rewriteStore = (RewriteStore) store;
            rewriteStore.put(Utils.index(getClass()), key, value, url);
            return;
        }
        store.put(Utils.index(getClass()), key, value);
    }


}
