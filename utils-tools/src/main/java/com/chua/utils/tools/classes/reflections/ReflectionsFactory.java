package com.chua.utils.tools.classes.reflections;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BeansHelper;
import org.reflections.*;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

import static java.lang.String.format;
import static org.reflections.util.Utils.index;

/**
 * 反射处理工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class ReflectionsFactory extends Reflections {

    private transient Configuration configuration;
    private static StoreFactory STORE = new StoreFactory();

    public ReflectionsFactory(Configuration configuration) {
        this.configuration = configuration;
        super.store = STORE;
        this.scanUrl();
    }

    /**
     * 扫描文件
     */
    private void scanUrl() {
        LongAdder longAdder = new LongAdder();
        if (configuration.getScanners() != null && !configuration.getScanners().isEmpty()) {
            for (Scanner scanner : configuration.getScanners()) {
                if (!STORE.container(scanner)) {
                    scanner.setConfiguration(configuration);
                }
                longAdder.increment();
            }
            if (longAdder.intValue() != 0) {
                BeansHelper.copierIfEffective(configuration, super.configuration);
                super.scan();
                if (configuration.shouldExpandSuperTypes()) {
                    expandSuperTypes();
                }
            }
        }
    }

    @Override
    public void expandSuperTypes() {
        String index = index(SubTypesScanner.class);
        Set<String> keys = STORE.keys(index);
        keys.removeAll(STORE.values(index));
        keys.parallelStream().forEach(new Consumer<String>() {
            @Override
            public void accept(String key) {
                final Class<?> type = ClassHelper.forName(key, configuration.getClassLoaders());
                if (type != null) {
                    expandSupertypes(STORE, key, type);
                }
            }
        });
    }

    private void expandSupertypes(Store store, String key, Class<?> type) {
        for (Class<?> supertype : ReflectionUtils.getSuperTypes(type)) {
            if (store.put(SubTypesScanner.class, supertype.getName(), key)) {
                if (log != null) {
                    log.debug("expanded subtype {} -> {}", supertype.getName(), key);
                }
                expandSupertypes(store, supertype.getName(), supertype);
            }
        }
    }
}
