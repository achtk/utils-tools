package com.chua.utils.tools.common.loader;

import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.common.properties.AbstractPropertiesProducer;

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
    public PropertiesLoader set(final Properties properties) {
        if (null != properties) {
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
    public PropertiesLoader set(final String name, final Properties properties) {
        if (null != properties) {
            this.put(StringHelper.defaultIfBlank(name, StringHelper.uuid()), properties);
        }
        return this;
    }

}
