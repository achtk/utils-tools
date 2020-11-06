package com.chua.utils.tools.common.loader;

import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.common.properties.AbstractPropertiesProducer;
import com.chua.utils.tools.text.IdHelper;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * property处理器
 *
 * @author CH
 * @date 2020-09-29
 */
public class PropertiesLoader extends AbstractPropertiesProducer {

    private PropertiesLoader() {
    }

    public PropertiesLoader(String name, Properties properties) {
        this.put(name, properties);
    }

    public static synchronized PropertiesLoader newLoader() {
        return new PropertiesLoader();
    }

    /**
     * @param properties
     * @return
     */
    public static synchronized PropertiesLoader newLoader(final Properties properties) {
        return new PropertiesLoader(null, properties);
    }

    /**
     * @param properties
     * @return
     */
    public static synchronized PropertiesLoader newLoader(final String name, final Properties properties) {
        return new PropertiesLoader(name, properties);
    }

    /**
     * 吸收数据
     *
     * @param properties 数据
     * @return
     */
    public PropertiesLoader add(final Properties properties) {
        if (null != properties) {
            this.put(IdHelper.createUuid(), properties);
        }
        return this;
    }

    /**
     * 吸收数据
     *
     * @param propertiesLoader 数据
     * @return
     */
    public PropertiesLoader add(final PropertiesLoader propertiesLoader) {
        if (null != propertiesLoader) {
            ConcurrentHashMap<String, Properties> concurrentHashMap = propertiesLoader.allMap();
            for (Map.Entry<String, Properties> entry : concurrentHashMap.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    /**
     * 吸收数据
     *
     * @param objectMap 数据
     * @return
     */
    public PropertiesLoader add(final Map<String, Object> objectMap) {
        if (null != objectMap) {
            Properties properties = MapOperableHelper.toProp(objectMap);
            this.put(IdHelper.createUuid(), properties);
        }
        return this;
    }

    /**
     * 吸收数据
     *
     * @param properties 数据
     * @param name       名称
     * @return
     */
    public PropertiesLoader add(final String name, final Properties properties) {
        if (null != properties) {
            this.put(StringHelper.getStringOrDefault(name, IdHelper.createUuid()), properties);
        }
        return this;
    }

    /**
     * 获取map
     *
     * @param name 名称
     * @return
     */
    public Properties asMap(final String name) {
        ConcurrentHashMap<String, Properties> concurrentHashMap = allMap();
        return concurrentHashMap.get(name);
    }

    /**
     * 获取map
     *
     * @return
     */
    public Properties asMapIfOnly() {
        ConcurrentHashMap<String, Properties> concurrentHashMap = allMap();
        return concurrentHashMap.size() == 1 ? FinderHelper.firstElement(concurrentHashMap) : null;
    }

}
