package com.chua.utils.tools.common.loader;

import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.common.properties.AbstractPropertiesProducer;
import com.google.common.base.Strings;

import java.util.Map;
import java.util.Properties;

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
            this.put(StringHelper.uuid(), properties);
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
            Properties properties = new Properties();
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                if(Strings.isNullOrEmpty(entry.getKey()) || null == entry.getValue()) {
                    continue;
                }
                properties.put(entry.getKey(), entry.getValue());
            }
            this.put(StringHelper.uuid(), properties);
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
            this.put(StringHelper.defaultIfBlank(name, StringHelper.uuid()), properties);
        }
        return this;
    }

}
