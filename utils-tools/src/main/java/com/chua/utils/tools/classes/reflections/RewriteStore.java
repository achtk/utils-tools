package com.chua.utils.tools.classes.reflections;

import com.chua.utils.tools.classes.reflections.scan.RewriteScan;
import com.chua.utils.tools.classes.reflections.scanner.AbstractRewriteScanner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.scanners.Scanner;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

import static org.reflections.util.Utils.index;

/**
 * 内存存储
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class RewriteStore extends Store {
    /**
     * <pre>
     * <code>
     *  {"URL": {
     *      "SubTypeScanner": {
     *          "Object": []
     *          }
     *      }
     *  }
     *  </code>
     *  </pre>
     */
    private static final ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<String>>>> REFLECT_CACHE = new ConcurrentHashMap<>();
    /**
     * 计数器
     */
    private static final LongAdder COUNT = new LongAdder();

    /**
     * 是否已检索
     *
     * @param scanner 扫描器
     * @return 包含返回true
     */
    public boolean container(Scanner scanner) {
        Set<String> strings = keySet();
        return strings.contains(scanner.getClass().getSimpleName());
    }

    /**
     * 是否已检索
     *
     * @param scanner 扫描器
     * @return 包含返回true
     */
    public boolean container(Class<? extends AbstractRewriteScanner> scanner) {
        Collection<ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<String>>>> values = REFLECT_CACHE.values();
        for (ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<String>>> value : values) {
            if (value.containsKey(scanner.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否已检索
     *
     * @param url url
     * @return 包含返回true
     */
    public boolean container(URL url) {
        Set<String> strings = REFLECT_CACHE.keySet();
        return strings.contains(url.getClass().getSimpleName());
    }

    @Override
    public boolean put(Class<?> scannerClass, String key, String value) {
        return super.put(scannerClass, key, value);
    }

    @Override
    public boolean put(String index, String key, String value) {
        try {
            return put(index, key, value, new URL("."));
        } catch (MalformedURLException e) {
        }
        return false;
    }

    public boolean put(String index, String key, String value, URL url) {
        synchronized (REFLECT_CACHE) {
            COUNT.increment();
            return REFLECT_CACHE.computeIfAbsent(url.toExternalForm(), item -> new ConcurrentHashMap<>())
                    .computeIfAbsent(index, item -> new ConcurrentHashMap<>())
                    .computeIfAbsent(key, item -> new CopyOnWriteArrayList<>())
                    .add(value);
        }
    }

    @Override
    public Set<String> get(Class<?> scannerClass, String key) {
        return this.get(Utils.index(scannerClass), Collections.singletonList(key));
    }

    /**
     * 获取所有包含的数据
     *
     * @param scannerClass
     * @param keys
     * @return
     */
    @Override
    public Set<String> getAllIncluding(Class<?> scannerClass, Collection<String> keys) {
        Map<String, List<String>> multimap = get(index(scannerClass));
        List<String> workKeys = new ArrayList<>(keys);

        Set<String> result = new HashSet<>();
        for (int i = 0; i < workKeys.size(); i++) {
            String key = workKeys.get(i);
            try {
                if (result.add(key)) {
                    Collection<String> values = multimap.get(key);
                    if (values != null) {
                        workKeys.addAll(values);
                    }
                }
            } catch (Exception ignore) {
            }
        }
        ;
        return result;
    }

    /**
     * 获取数据
     *
     * @param index 索引
     * @param keys  key
     * @return
     */
    private Set<String> get(String index, Collection<String> keys) {
        Map<String, List<String>> mmap = get(index);
        Set<String> result = new LinkedHashSet<>();
        for (String key : keys) {
            Collection<String> values = mmap.get(key);
            if (values != null) {
                result.addAll(values);
            }
        }
        return result;
    }

    /**
     * 获取数据
     *
     * @param index
     * @return
     */
    private Map<String, List<String>> get(String index) {
        Map<String, List<String>> result = new HashMap<>();

        Collection<ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<String>>>> mapCollection = REFLECT_CACHE.values();
        for (ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<String>>> map : mapCollection) {
            synchronized (result) {
                ConcurrentMap<String, CopyOnWriteArrayList<String>> multimap = map.get(index);
                if (null == multimap) {
                    continue;
                }
                for (Map.Entry<String, CopyOnWriteArrayList<String>> entry : multimap.entrySet()) {
                    result.computeIfAbsent(entry.getKey(), item -> new ArrayList<>()).addAll(entry.getValue());
                }
            }
        }

        return result;
    }

    @Override
    public Set<String> keys(String index) {
        Set<String> result = new HashSet<>();

        Collection<ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<String>>>> mapCollection = REFLECT_CACHE.values();
        for (ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<String>>> stringHashMultimapMap : mapCollection) {
            ConcurrentMap<String, CopyOnWriteArrayList<String>> multimap = stringHashMultimapMap.get(index);
            if (null == multimap) {
                continue;
            }
            result.addAll(multimap.keySet());
        }
        return result;

    }

    @Override
    public Set<String> get(Class<?> scannerClass, Collection<String> keys) {
        return this.getRewrite(index(scannerClass), keys);
    }

    public HashMultimap<String, String> getMap(Class<?> scannerClass, String key) {
        HashMultimap<String, String> result = HashMultimap.create();

        for (String url : REFLECT_CACHE.keySet()) {
            ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<String>>> mapCollection = REFLECT_CACHE.get(url);
            ConcurrentMap<String, CopyOnWriteArrayList<String>> multimap = mapCollection.get(index(scannerClass));
            if (null == multimap) {
                continue;
            }
            for (String value : multimap.get(key)) {
                result.put(url, value);
            }
        }
        return result;
    }

    public Map<String, String> getMap(Class<?> scannerClass, Collection<String> keys) {
        return this.getRewriteMap(index(scannerClass), keys);
    }

    private Set<String> getRewrite(String index, Collection<String> keys) {
        Map<String, List<String>> mmap = get(index);
        Set<String> result = new LinkedHashSet<>();
        for (String key : keys) {
            Collection<String> values = mmap.get(key);
            if (values != null) {
                result.addAll(values);
            }
        }
        return result;
    }

    private Map<String, String> getRewriteMap(String index, Collection<String> keys) {
        Map<String, String> result = new HashMap<>();

        for (String url : REFLECT_CACHE.keySet()) {
            ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<String>>> mapCollection = REFLECT_CACHE.get(url);
            ConcurrentMap<String, CopyOnWriteArrayList<String>> multimap = mapCollection.get(index);
            if (null == multimap) {
                continue;
            }
            for (String key : keys) {
                for (String value : multimap.get(key)) {
                    result.put(url, value);
                }
            }
        }
        return result;
    }
}
