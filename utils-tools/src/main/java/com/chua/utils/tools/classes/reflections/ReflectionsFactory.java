package com.chua.utils.tools.classes.reflections;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BeansHelper;
import javassist.bytecode.ClassFile;
import lombok.Getter;
import org.reflections.*;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.vfs.Vfs;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
    private static final ConcurrentHashMap<String, URL> URL_CACHE = new ConcurrentHashMap<>();
    @Getter
    private Map<String, Throwable> throwableMap = new HashMap<>();

    public ReflectionsFactory(Configuration configuration) {
        super.store = STORE;
        this.configuration = configuration;
        this.scanUrl();
    }

    /**
     * 通过类名获取类所在URL
     *
     * @param className 类名
     * @return  URL
     */
    public URL getClassFromUrl(String className) {
        return URL_CACHE.get(className);
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
                    longAdder.increment();
                }
            }
            if (longAdder.intValue() != 0) {
                this.scan();
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

    @Override
    protected void scan() {
        if (configuration.getUrls() == null || configuration.getUrls().isEmpty()) {
            if (log != null) {
                log.warn("given scan urls are empty. set urls in the configuration");
            }
            return;
        }

        if (log != null && log.isDebugEnabled()) {
            log.debug("going to scan these urls: {}", configuration.getUrls());
        }

        long time = System.currentTimeMillis();
        int scannedUrls = 0;
        ExecutorService executorService = configuration.getExecutorService();
        List<Future<?>> futures = new ArrayList<>();

        for (final URL url : configuration.getUrls()) {
            try {
                if (executorService != null) {
                    futures.add(executorService.submit(() -> {
                        if (log != null) {
                            log.debug("[{}] scanning {}", Thread.currentThread().toString(), url);
                        }
                        scan(url);
                    }));
                } else {
                    scan(url);
                }
                scannedUrls++;
            } catch (ReflectionsException e) {
                if (log != null) {
                    log.warn("could not create Vfs.Dir from url. ignoring the exception and continuing", e);
                }
            }
        }

        //todo use CompletionService
        if (executorService != null) {
            for (Future future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //gracefully shutdown the parallel scanner executor service.
        if (executorService != null) {
            executorService.shutdown();
        }

        if (log != null) {
            log.info(format("ReflectionsFactory took %d ms to scan %d urls, producing %s %s",
                    System.currentTimeMillis() - time, scannedUrls, producingDescription(store),
                    executorService instanceof ThreadPoolExecutor ?
                            format("[using %d cores]", ((ThreadPoolExecutor) executorService).getMaximumPoolSize()) : ""));
        }
    }

    private static String producingDescription(Store store) {
        int keys = 0;
        int values = 0;
        for (String index : store.keySet()) {
            keys += store.keys(index).size();
            values += store.values(index).size();
        }
        return String.format("%d keys and %d values", keys, values);
    }

    @Override
    protected void scan(URL url) {
        Vfs.Dir dir = Vfs.fromURL(url);

        try {
            for (final Vfs.File file : dir.getFiles()) {
                // scan if inputs filter accepts file relative path or fqn
                Predicate<String> inputsFilter = configuration.getInputsFilter();
                String path = file.getRelativePath();
                String fqn = path.replace('/', '.');
                if (inputsFilter == null || inputsFilter.test(path) || inputsFilter.test(fqn)) {
                    Object classObject = null;
                    for (Scanner scanner : configuration.getScanners()) {
                        try {
                            if (scanner.acceptsInput(path) || scanner.acceptsInput(fqn)) {
                                classObject = scanner.scan(file, classObject, store);
                                if (classObject instanceof ClassFile) {
                                    URL_CACHE.put(((ClassFile) classObject).getName(), url);
                                }
                            }
                        } catch (Exception e) {
                            if (log != null) {
                                throwableMap.put(file.getRelativePath(), e);
                            }
                        }
                    }
                }
            }
        } finally {
            dir.close();
        }
    }
}
