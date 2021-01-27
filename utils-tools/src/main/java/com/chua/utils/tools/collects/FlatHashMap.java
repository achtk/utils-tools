package com.chua.utils.tools.collects;

import com.chua.utils.tools.function.impl.LevelsClose;
import com.chua.utils.tools.util.StringUtils;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * hash 单级 Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/27
 */
@NoArgsConstructor
public class FlatHashMap implements FlatMap {

    private transient Map<String, Object> flatMap = new HashMap<>();
    private final LevelsClose levelsClose = new LevelsClose();
    private Map<String, Object> source = new HashMap<>();

    public FlatHashMap(Map<String, Object> source) {
        this.source = source;
        this.flatMap = levelsClose.apply(source);
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public boolean isEmpty() {
        return null == source ? true : source.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return flatMap.containsKey(key) || source.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return flatMap.containsValue(value) || source.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        List<Object> values = new ArrayList<>();
        for (String s : flatMap.keySet()) {
            if (StringUtils.wildcardMatch(s, key.toString())) {
                values.add(flatMap.get(s));
            }
        }
        return values;
    }

    @Override
    public Object put(String key, Object value) {
        try {
            return source.put(key, value);
        } finally {
            this.flatMap = levelsClose.apply(source);
        }
    }

    @Override
    public Object remove(Object key) {
        try {
            return source.remove(key);
        } finally {
            this.flatMap = levelsClose.apply(source);
        }
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        try {
            source.putAll(m);
        } finally {
            this.flatMap = levelsClose.apply(source);
        }
    }

    @Override
    public void clear() {
        source.clear();
        flatMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return source.keySet();
    }

    @Override
    public Collection<Object> values() {
        return flatMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return source.entrySet();
    }
}
