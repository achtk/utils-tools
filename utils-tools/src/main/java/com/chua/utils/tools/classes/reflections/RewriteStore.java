package com.chua.utils.tools.classes.reflections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.scanners.Scanner;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
    private static final ConcurrentMap<URL, Map<String, HashMultimap<String, String>>> REFLECT_CACHE = new ConcurrentHashMap<>();

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
     * @param url url
     * @return 包含返回true
     */
    public boolean container(URL url) {
        Set<String> strings = keySet();
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
            return REFLECT_CACHE.computeIfAbsent(url, item -> new HashMap<>())
                    .computeIfAbsent(index, item -> HashMultimap.create()).put(key, value);
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
        Multimap<String, String> multimap = get(index(scannerClass));
        List<String> workKeys = new ArrayList<>(keys);

        Set<String> result = new HashSet<>();
        keys.stream().parallel().forEach(key -> {
            try {
                if (result.add(key)) {
                    Collection<String> values = multimap.get(key);
                    if (values != null) {
                        workKeys.addAll(values);
                    }
                }
            } catch (Exception ignore) {
            }
        });
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
        Multimap<String, String> mmap = get(index);
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
    private Multimap<String, String> get(String index) {
        Multimap<String, String> result = HashMultimap.create();

        Collection<Map<String, HashMultimap<String, String>>> mapCollection = REFLECT_CACHE.values();
        for (Map<String, HashMultimap<String, String>> stringHashMultimapMap : mapCollection) {
            Multimap<String, String> multimap = stringHashMultimapMap.get(index);
            if(null == multimap) {
                continue;
            }
            result.putAll(multimap);
        }
        return result;
    }
}
