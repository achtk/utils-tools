package com.chua.utils.tools.bean.map;

import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.tuple.Pair;
import com.chua.utils.tools.util.StringUtils;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 标准的Bean处理
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/2
 */
public class StandardBeanMap extends HashMap<String, Object> implements BeanMapper {
    private final String[] mapperStr;
    /**
     * 对象
     */
    private Object obj;
    /**
     * 映射
     */
    private Map<String, String> mapper;

    public StandardBeanMap(Object obj, String[] mapperStr) {
        this.obj = obj;
        this.mapperStr = mapperStr;
        this.str2Mapper();
        this.analysisAndStorage();
    }

    /**
     * 映射
     */
    private void str2Mapper() {
        if (null == mapperStr) {
            return;
        }
        for (String s : mapperStr) {
            Pair<String, String> unpack = StringUtils.unpack(s, StringConstant.MAPPER);
            if (null == mapper) {
                mapper = new HashMap<>(mapperStr.length);
            }
            mapper.put(unpack.getLeft(), unpack.getRight());
        }
    }

    /**
     * 解析入库
     */
    private void analysisAndStorage() {
        if (null == obj) {
            return;
        }
        BeanMap beanMap = BeanMap.create(obj);
        if (null != mapper) {
            for (Object key : beanMap.keySet()) {
                if (mapper.containsKey(key.toString())) {
                    this.put(mapper.get(key.toString()), beanMap.get(key));
                } else {
                    this.put(key.toString(), beanMap.get(key));
                }
            }
            return;
        }
        this.putAll(beanMap);
    }

    @Override
    public Map<String, Object> toMap() {
        return this;
    }
}
