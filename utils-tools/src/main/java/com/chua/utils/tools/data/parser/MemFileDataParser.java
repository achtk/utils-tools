package com.chua.utils.tools.data.parser;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.enums.JavaType;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 内存解析器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
@Getter
@Setter
public class MemFileDataParser implements DataParser {

    private Object source;
    private HashOperateMap operate;
    private List<String> header;
    private Map<String, String> headers = new HashMap<>();
    private List<Map<String, Object>> copySource = new ArrayList<>();
    private Iterator<Map<String, Object>> iterator;

    @Override
    public void setSource(Object source) {
        this.source = source;
        this.analysis2Map();
        this.analysisHeader();
    }

    /**
     * 数据转为map
     */
    private void analysis2Map() {
        List<Object> data = (List<Object>) source;
        this.copySource = data.stream().map(item -> {
            if (item instanceof Map) {
                Map<String, Object> mapData = (Map<String, Object>) item;
                return mapData.keySet().stream().collect(Collectors.toMap(key -> key.toUpperCase(), key -> ((Map) item).get(key)));
            }
            Map<String, Object> items = new HashMap<>();
            ClassHelper.doWithFields(ClassHelper.getClass(item), field -> {
                items.put(field.getName().toUpperCase(), ClassHelper.getFieldValue(item, field));
            });
            return items;
        }).collect(Collectors.toList());
        this.iterator = copySource.iterator();
    }

    /**
     * 解析消息头
     */
    private void analysisHeader() {
        Map<String, Object> item = FinderHelper.firstElement(copySource);
        item.entrySet().forEach(entry -> {
            Object value = entry.getValue();
            headers.put(entry.getKey().toUpperCase(), JavaType.toJdbcType(value));
        });

        this.header = Lists.newArrayList(headers.keySet());
    }

    @Override
    public Map<String, String> getDataType() {
        if (!(source instanceof List)) {
            return null;
        }
        return headers;
    }

    @Override
    public Object[] getCurrent() {
        Map<String, Object> next = iterator.next();
        return ArraysHelper.toArray(next, header);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public void reset() {

    }
}
